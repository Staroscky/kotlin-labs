package com.staroscky.transferencia.service.pipeline.strategy

import com.staroscky.transferencia.domain.TransferenciaContext
import com.staroscky.transferencia.service.pipeline.PipelineStep
import com.staroscky.transferencia.service.steps.ValidarMotorStep
import org.springframework.stereotype.Component


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
