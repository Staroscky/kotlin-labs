package com.staroscky.transferencia.service.pipeline.strategy

import com.staroscky.transferencia.domain.TransferenciaContext
import com.staroscky.transferencia.service.pipeline.PipelineStep
import com.staroscky.transferencia.service.steps.MontarResponseStep
import org.springframework.stereotype.Component

@Component
class FinalizacaoStrategy(
    private val montarResponseStep: MontarResponseStep
) : FluxoStrategy {
    
    override fun aplica(context: TransferenciaContext): Boolean {
        return true
    }
    
    override fun obterSteps(): List<PipelineStep> = listOf(
        montarResponseStep
    )
    
    override fun prioridade(): Int = 999
}
