package com.staroscky.transferencia.service.pipeline.strategy

import com.staroscky.transferencia.domain.TransferenciaContext
import com.staroscky.transferencia.service.pipeline.PipelineStep
import com.staroscky.transferencia.service.steps.ValidarMotorStep
import org.springframework.stereotype.Component

/**
 * Strategy para validar no Motor de Decisão
 * 
 * Aplica quando:
 * - Cache não existe (primeira vez), OU
 * - Hash mudou (valor ou data diferente)
 * 
 * Sempre que precisa validar, chama o motor.
 * O motor já lança exceção se nenhum instrumento for válido.
 */
@Component
class ValidacaoMotorStrategy(
    private val validarMotorStep: ValidarMotorStep
) : FluxoStrategy {
    
    override fun aplica(context: TransferenciaContext): Boolean {
        val cache = context.dadosCache
        
        return when {
            // Cache não existe → validar
            cache == null -> true
            
            // Hash mudou → revalidar
            cache.hashRequest != context.hashRequest -> true
            
            // Hash igual → não precisa validar
            else -> false
        }
    }
    
    override fun obterSteps(): List<PipelineStep> = listOf(
        validarMotorStep
    )
    
    override fun prioridade(): Int = 20
}
