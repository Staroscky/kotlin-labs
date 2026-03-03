package com.staroscky.transferencia.service.pipeline.strategy

import com.staroscky.transferencia.domain.TransferenciaContext
import com.staroscky.transferencia.service.pipeline.PipelineStep
import com.staroscky.transferencia.service.steps.CriarTransferenciaStep
import com.staroscky.transferencia.service.steps.SalvarCacheStep
import org.springframework.stereotype.Component

@Component
class CriarTransferenciaStrategy(
    private val criarTransferenciaStep: CriarTransferenciaStep,
    private val salvarCacheStep: SalvarCacheStep
) : FluxoStrategy {
    
    override fun aplica(context: TransferenciaContext): Boolean {
        return context.dadosCache == null
    }
    
    override fun obterSteps(): List<PipelineStep> = listOf(
        criarTransferenciaStep,
        salvarCacheStep
    )
    
    override fun prioridade(): Int = 30
}
