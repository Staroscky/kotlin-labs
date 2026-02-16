# ✅ Checklist de Implementação

## 📦 Estrutura Criada

### ✅ Domain Models
- [x] TransferenciaRequest
- [x] TransferenciaResponse
- [x] TransferenciaContext
- [x] TransferenciaCache

### ✅ Client Layer
- [x] PaasFeignClient (HTTP/2)
- [x] PaasCreateRequest
- [x] PaasUpdateRequest
- [x] PaasResponse

### ✅ Cache Layer
- [x] TransferenciaCacheRepository (Redis)
- [x] RedisTemplate Configuration

### ✅ Pipeline Core
- [x] PipelineStep (interface)
- [x] FluxoStrategy (interface)
- [x] TransferenciaPipeline (executor)

### ✅ Strategies (7 total)
- [x] PreparacaoInicialStrategy
- [x] RetornarCacheStrategy
- [x] BuscarCheckinStrategy
- [x] ValidacaoMotorStrategy
- [x] CriarTransferenciaStrategy
- [x] AtualizarTransferenciaStrategy
- [x] FinalizacaoStrategy

### ✅ Steps (8 total)
- [x] CalcularHashStep
- [x] VerificarCacheStep
- [x] BuscarCheckinStep
- [x] ValidarMotorStep
- [x] CriarTransferenciaStep
- [x] AtualizarTransferenciaStep
- [x] SalvarCacheStep
- [x] MontarResponseStep

### ✅ Mappers
- [x] CheckinToMotorMapper
- [x] CheckinToPaasMapper

### ✅ Service Layer
- [x] TransferenciaApi (interface)
- [x] TransferenciaService (implementação)

### ✅ Controller
- [x] TransferenciaController (REST)

### ✅ Configuration
- [x] TransferenciaConfig (Redis)
- [x] application.yml (exemplo)

### ✅ Exception Handling
- [x] TransferenciaException (base)
- [x] NenhumInstrumentoValidoException
- [x] CacheNaoEncontradoException
- [x] CheckinNaoEncontradoException
- [x] PaasIntegracaoException

### ✅ Documentation
- [x] README.md
- [x] DIAGRAMS.md (Mermaid)
- [x] ValidacaoMotorStrategyTest.kt (exemplo)
- [x] NEXT_STEPS.md (este arquivo)

---

## 🔧 TODOs Críticos Antes de Rodar

### 1. ⚠️ Motor de Decisão - Interface Real
**Arquivo**: `service/steps/ValidarMotorStep.kt`

```kotlin
// TROCAR ISSO:
private val motorDecisao: Any,  // TODO

// POR ISSO:
private val motorDecisao: MotorDecisao,  // interface real do módulo

// E DESCOMENTAR:
val motorResponse = motorDecisao.validar(motorRequest)
context.resultadoMotor = motorResponse
```

**Ação**: Importar interface do módulo do Motor de Decisão.

---

### 2. ⚠️ Dados de Origem (Usuário Logado)
**Arquivos**: 
- `service/mapper/CheckinToMotorMapper.kt` (linha 18)
- `service/mapper/CheckinToPaasMapper.kt` (linha 17)

```kotlin
// TROCAR os mocks:
private val ORIGEM_MOCK = ...

// POR:
// 1. Extrair do JWT token
// 2. Buscar de cache de sessão
// 3. Injetar serviço de contexto do usuário
```

**Ação**: Implementar extração de dados do usuário autenticado.

---

### 3. ⚠️ Tipagem do MotorDecisaoResponse
**Arquivos**:
- `domain/TransferenciaContext.kt` (linha 31)
- `domain/TransferenciaCache.kt` (linha 17)
- `domain/TransferenciaResponse.kt` (linha 11)

```kotlin
// TROCAR:
var resultadoMotor: Any? = null

// POR:
var resultadoMotor: MotorDecisaoResponse? = null
```

**Ação**: Importar DTO correto do módulo do Motor.

---

### 4. ⚠️ Ajustar ContaTransacionalV1Response
**Arquivo**: `checkin/domain/response/ContaTransacionalV1Response.kt`

Segundo o JSON de exemplo (`manual.json`), a estrutura correta é:
```kotlin
data class ContaTransacionalV1Response(
    val conta: Conta,
    val titular: Titular
) : CheckinResponse

// NÃO:
data class ContaTransacionalV1Response(
    val dadosBancarios: DadosBancarios  // ❌ nome incorreto
)
```

**Ação**: Ajustar modelo para bater com JSON real.

---

### 5. ⚠️ Ajustar QRCodePixV1Response
**Arquivo**: `checkin/domain/response/QRCodePixV1Response.kt`

Mapear nomes dos campos corretamente:
- `txid` → `txId` (@JsonProperty)
- `expiracao` → `expiracaoEmSegundos`
- `modalidadeAlteracao` → `permiteAlteracao`
- Etc.

**Ação**: Adicionar `@JsonProperty` nos campos que diferem do JSON.

---

## 🚀 Como Integrar no Projeto Existente

### 1. Copiar Arquivos
```bash
# Copiar estrutura para o projeto
cp -r transferencia-structure/* src/main/kotlin/com/staroscky/
```

### 2. Adicionar Dependências
**build.gradle.kts**:
```kotlin
dependencies {
    // Já deve ter:
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation("io.github.openfeign:feign-http2client")
    implementation("org.apache.commons:commons-pool2")  // ← Para Redis pool
    
    // Validação
    implementation("org.springframework.boot:spring-boot-starter-validation")
    
    // Testes
    testImplementation("io.mockk:mockk:1.13.8")
}
```

### 3. Configurar application.yml
Copiar seções relevantes do `config/application.yml` de exemplo.

### 4. Habilitar Feign Clients
**Application.kt**:
```kotlin
@SpringBootApplication
@EnableFeignClients  // ← Adicionar se não tiver
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
```

### 5. Testar
```bash
# Subir dependências
docker-compose up -d redis

# Rodar aplicação
./gradlew bootRun

# Testar endpoint
curl -X POST http://localhost:8080/api/v1/transferencias \
  -H "Content-Type: application/json" \
  -d '{
    "checkinId": "checkin:chave_pix:1:550e8400-e29b-41d4-a716-446655440000",
    "dataTransferencia": "2026-02-20",
    "valorTransferencia": 150.00
  }'
```

---

## 📊 Testes Recomendados

### Unit Tests
- [x] ValidacaoMotorStrategyTest (exemplo criado)
- [ ] Outras strategies
- [ ] Cada step isoladamente
- [ ] Mappers (cenários por tipo de entrada)

### Integration Tests
- [ ] Pipeline completo (end-to-end)
- [ ] Redis (cache hit/miss)
- [ ] Feign Clients (mocked)

### Performance Tests
- [ ] Load test com 800 RPS
- [ ] Cache hit rate
- [ ] Connection pool usage

---

## 🎯 Evoluções Futuras (Pós-MVP)

### Curto Prazo
- [ ] Global Exception Handler (@ControllerAdvice)
- [ ] Retry com backoff exponencial (Resilience4j)
- [ ] Circuit breaker para PAAS
- [ ] Métricas customizadas (Micrometer)
- [ ] Logs estruturados (JSON)

### Médio Prazo
- [ ] Auditoria de operações
- [ ] Validação de limite de valor
- [ ] Validação de horário comercial
- [ ] Notificações assíncronas
- [ ] Webhooks de status

### Longo Prazo
- [ ] Suporte a múltiplos destinos (array)
- [ ] Agendamento recorrente
- [ ] Cancelamento de transferências
- [ ] Histórico completo (Event Sourcing)
- [ ] Dashboard de monitoramento

---

## 📚 Referências Úteis

### Spring Boot
- [Virtual Threads](https://spring.io/blog/2023/09/09/all-together-now-spring-boot-3-2-graalvm-native-images-java-21-and-virtual)
- [Data Redis](https://docs.spring.io/spring-data/redis/docs/current/reference/html/)

### Feign
- [HTTP/2 Client](https://github.com/OpenFeign/feign/tree/master/http2client)
- [Spring Cloud OpenFeign](https://docs.spring.io/spring-cloud-openfeign/docs/current/reference/html/)

### Patterns
- [Pipeline Pattern](https://www.martinfowler.com/articles/collection-pipeline/)
- [Strategy Pattern](https://refactoring.guru/design-patterns/strategy)
- [Vertical Slice Architecture](https://www.jimmybogard.com/vertical-slice-architecture/)

---

## ✅ Checklist Final de Deploy

- [ ] TODOs críticos resolvidos (Motor, Origem, Tipagens)
- [ ] Testes unitários passando
- [ ] Testes de integração passando
- [ ] Load test validado (800 RPS)
- [ ] Configurações de produção ajustadas
- [ ] Métricas configuradas
- [ ] Logs estruturados
- [ ] Exception handling global
- [ ] Documentação atualizada
- [ ] Code review aprovado
- [ ] Deploy em staging validado

---

**Estrutura completa criada! 🎉**

Total de arquivos: **30+**
Linhas de código: **~2500+**

Pronto para integração no projeto!
