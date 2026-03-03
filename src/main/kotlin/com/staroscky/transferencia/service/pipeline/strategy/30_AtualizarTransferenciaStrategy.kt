package com.staroscky.transferencia.service.pipeline.strategy

import com.staroscky.transferencia.domain.TransferenciaContext
import com.staroscky.transferencia.service.pipeline.PipelineStep
import com.staroscky.transferencia.service.steps.AtualizarTransferenciaStep
import com.staroscky.transferencia.service.steps.SalvarCacheStep
import org.springframework.stereotype.Component


@Component
class AtualizarTransferenciaStrategy(
    private val atualizarTransferenciaStep: AtualizarTransferenciaStep,
    private val salvarCacheStep: SalvarCacheStep
) : FluxoStrategy {
    
    override fun aplica(context: TransferenciaContext): Boolean {
        val cache = context.dadosCache ?: return false

        return cache.hashRequest != context.hashRequest
    }
    
    override fun obterSteps(): List<PipelineStep> = listOf(
        atualizarTransferenciaStep,
        salvarCacheStep
    )
    
    override fun prioridade(): Int = 30
}
