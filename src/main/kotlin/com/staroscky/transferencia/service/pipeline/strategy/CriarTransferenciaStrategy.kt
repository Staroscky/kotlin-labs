package com.staroscky.transferencia.service.pipeline.strategy

import com.staroscky.transferencia.domain.TransferenciaContext
import com.staroscky.transferencia.service.pipeline.PipelineStep
import com.staroscky.transferencia.service.steps.CriarTransferenciaStep
import com.staroscky.transferencia.service.steps.SalvarCacheStep
import org.springframework.stereotype.Component

/**
 * Strategy para criar transferência no PAAS
 * 
 * Aplica quando:
 * - Cache não existe (primeira vez)
 * - Motor validou com sucesso (não lançou exceção)
 * 
 * Cria no PAAS e salva resultado no cache.
 */
@Component
class CriarTransferenciaStrategy(
    private val criarTransferenciaStep: CriarTransferenciaStep,
    private val salvarCacheStep: SalvarCacheStep
) : FluxoStrategy {
    
    override fun aplica(context: TransferenciaContext): Boolean {
        // Só cria se cache não existe
        // (se motor reprovou, já lançou exceção antes de chegar aqui)
        return context.dadosCache == null
    }
    
    override fun obterSteps(): List<PipelineStep> = listOf(
        criarTransferenciaStep,
        salvarCacheStep
    )
    
    override fun prioridade(): Int = 30
}
