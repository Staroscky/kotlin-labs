package com.staroscky.transferencia.service.pipeline.strategy

import com.staroscky.transferencia.domain.TransferenciaContext
import com.staroscky.transferencia.service.pipeline.PipelineStep
import com.staroscky.transferencia.service.steps.BuscarCheckinStep
import org.springframework.stereotype.Component

@Component
class BuscarCheckinStrategy(
    private val buscarCheckinStep: BuscarCheckinStep
) : FluxoStrategy {
    
    override fun aplica(context: TransferenciaContext): Boolean {
        // Só busca checkin se não tem cache
        return context.dadosCache == null
    }
    
    override fun obterSteps(): List<PipelineStep> = listOf(
        buscarCheckinStep
    )
    
    override fun prioridade(): Int = 10
}
