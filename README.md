# Módulo de Transferência

## 📋 Visão Geral

Módulo responsável por criar e atualizar transferências utilizando:
- **CheckinCore**: busca dados do destino
- **Motor de Decisão**: valida instrumentos disponíveis
- **PAAS**: persiste a transferência
- **Redis**: cache para otimizar chamadas

---

## 🏗️ Arquitetura

### Vertical Slice Architecture
Seguindo o padrão já utilizado no projeto, todo código relacionado a transferências está em `com.staroscky.transferencia/`.

### Pipeline Pattern com Strategies
- **Pipeline**: orquestra a execução dos steps
- **Strategies**: decidem quais steps executar baseado no contexto
- **Steps**: executam ações específicas (buscar checkin, validar motor, etc)

---

## 📂 Estrutura de Pastas

```
transferencia/
├── TransferenciaApi.kt                    # Interface pública
├── domain/                                # Modelos de domínio
│   ├── TransferenciaRequest.kt
│   ├── TransferenciaResponse.kt
│   ├── TransferenciaContext.kt           # Contexto do pipeline
│   └── TransferenciaCache.kt             # Model do Redis
├── client/                                # Integrações externas
│   ├── PaasFeignClient.kt                # Cliente HTTP/2 para PAAS
│   └── dto/                              # DTOs de request/response
├── cache/                                 # Camada de cache
│   └── TransferenciaCacheRepository.kt
├── service/
│   ├── TransferenciaService.kt           # Implementação da API
│   ├── pipeline/
│   │   ├── PipelineStep.kt               # Interface base
│   │   ├── TransferenciaPipeline.kt      # Executor
│   │   └── strategy/                     # Strategies de decisão
│   ├── steps/                            # Steps individuais
│   │   ├── CalcularHashStep.kt
│   │   ├── VerificarCacheStep.kt
│   │   ├── BuscarCheckinStep.kt
│   │   ├── ValidarMotorStep.kt
│   │   ├── CriarTransferenciaStep.kt
│   │   ├── AtualizarTransferenciaStep.kt
│   │   ├── SalvarCacheStep.kt
│   │   └── MontarResponseStep.kt
│   └── mapper/                           # Conversores de dados
│       ├── CheckinToMotorMapper.kt
│       └── CheckinToPaasMapper.kt
├── exception/                            # Exceções customizadas
│   └── TransferenciaException.kt
└── config/                               # Configurações
    └── TransferenciaConfig.kt
```

---

## 🔄 Fluxo de Execução

### Cenário 1: Primeira Chamada (Cache Miss)
```
1. CalcularHashStep         → calcula hash(data + valor)
2. VerificarCacheStep       → busca no Redis → não encontra
3. BuscarCheckinStep        → busca dados do destino no CheckinCore
4. ValidarMotorStep         → valida no Motor de Decisão
5. CriarTransferenciaStep   → POST no PAAS
6. SalvarCacheStep          → salva no Redis (TTL 15min)
7. MontarResponseStep       → monta response final
```

### Cenário 2: Chamada Repetida (Cache Hit + Hash Igual)
```
1. CalcularHashStep         → calcula hash(data + valor)
2. VerificarCacheStep       → busca no Redis → encontra
3. [compara hash]           → hash IGUAL
4. MontarResponseStep       → retorna dados do cache
```

### Cenário 3: Atualização (Cache Hit + Hash Diferente)
```
1. CalcularHashStep         → calcula hash(data + valor)
2. VerificarCacheStep       → busca no Redis → encontra
3. [compara hash]           → hash DIFERENTE
4. ValidarMotorStep         → revalida (usa dadosCheckin do cache)
5. AtualizarTransferenciaStep → PATCH no PAAS (só campos alterados)
6. SalvarCacheStep          → atualiza cache
7. MontarResponseStep       → monta response final
```

---

## 🎯 Como Adicionar Novas Regras

### Exemplo: Validar Fraude para Valores Altos

```kotlin
@Component
class ValidacaoFraudeStrategy(
    private val validarFraudeStep: ValidarFraudeStep
) : FluxoStrategy {
    
    override fun aplica(context: TransferenciaContext): Boolean {
        return context.valorTransferencia > BigDecimal(1000)
    }
    
    override fun obterSteps() = listOf(validarFraudeStep)
    
    override fun prioridade() = 15  // Entre buscar checkin e validar motor
}
```

**Pronto!** Spring injeta automaticamente e o pipeline passa a executar esta validação.

---

## 🔧 Configurações Importantes

### Redis
- **TTL**: 15 minutos (padrão)
- **Pool**: 50 conexões ativas (otimizado para Virtual Threads)
- **Chave**: `transferencia:{checkinId}`

### Feign
- **HTTP/2**: Habilitado
- **Connections**: 50 globais, 10 por rota
- **Timeouts**: Connect 5s, Read 10s

### Virtual Threads
- **Habilitado**: Java 21+ com Spring Boot 3.2+
- **Sem limite de threads**: permite alto throughput

---

## ⚠️ TODOs Importantes

### 1. Motor de Decisão
```kotlin
// ValidarMotorStep.kt - linha 20
private val motorDecisao: Any,  // TODO: substituir por interface real MotorDecisao
```

**Ação**: Substituir `Any` pela interface correta do módulo do Motor.

### 2. Dados de Origem
```kotlin
// CheckinToMotorMapper.kt e CheckinToPaasMapper.kt
private val ORIGEM_MOCK = ...  // TODO: buscar do token/cache do usuário
```

**Ação**: Implementar extração de dados do usuário logado (token JWT ou cache).

### 3. Tipagem do Motor
```kotlin
// TransferenciaContext.kt - linha 31
var resultadoMotor: Any? = null,  // TODO: usar MotorDecisaoResponse
```

**Ação**: Substituir `Any` pelo tipo correto quando tiver acesso ao DTO.

---

## 🧪 Testando

### Request de Exemplo
```json
POST /transferencias
{
  "checkinId": "checkin:chave_pix:1:550e8400-e29b-41d4-a716-446655440000",
  "dataTransferencia": "2026-02-20",
  "valorTransferencia": 150.00
}
```

### Response Esperada
```json
{
  "data": {
    "motor": {
      "instrumentos": [
        {
          "id": "1",
          "nome": "PIX",
          "valido": true,
          "validoAgendamento": true,
          "ordemMelhorInstrumento": 1
        }
      ]
    },
    "pagamento": {
      "idPaas": "uuid-gerado-pelo-paas"
    }
  }
}
```

---

## 📊 Métricas Disponíveis

Via `/actuator/metrics`:
- `lettuce.command.duration` - Latência do Redis
- `lettuce.command.pool.active` - Conexões ativas Redis
- `http.client.requests` - Chamadas Feign (PAAS)

---

## 🚀 Próximas Evoluções

1. Implementar retry com backoff exponencial
2. Adicionar circuit breaker (Resilience4j)
3. Implementar fallback para cache expirado
4. Adicionar validação de limite de valor
5. Implementar auditoria de operações
6. Adicionar eventos assíncronos (Kafka/SQS)

---

## 📝 Notas de Implementação

- **Thread Safety**: Context é mutável mas cada request tem seu próprio context
- **Exceções**: Motor já lança exceção se inválido, não precisa validar no step
- **PATCH**: Envia apenas campos alterados para otimizar payload
- **Cache**: Usa CheckinId como chave única
- **Idempotência**: Hash garante que requests iguais retornam cache
