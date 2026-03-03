package com.staroscky.transferencia.service.pipeline.strategy

import com.staroscky.transferencia.domain.TransferenciaContext
import com.staroscky.transferencia.service.pipeline.PipelineStep
import com.staroscky.transferencia.service.steps.CalcularHashStep
import com.staroscky.transferencia.service.steps.VerificarCacheStep
import org.springframework.stereotype.Component


@Component
class PreparacaoInicialStrategy(
    private val calcularHashStep: CalcularHashStep,
    private val verificarCacheStep: VerificarCacheStep
) : FluxoStrategy {
    
    override fun aplica(context: TransferenciaContext): Boolean {
        return true
    }
    
    override fun obterSteps(): List<PipelineStep> = listOf(
        calcularHashStep,
        verificarCacheStep
    )
    
    override fun prioridade(): Int = 0
}
