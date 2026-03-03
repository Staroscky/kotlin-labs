package com.staroscky.transferencia.service.pipeline.strategy

import com.staroscky.transferencia.domain.TransferenciaContext
import com.staroscky.transferencia.service.pipeline.PipelineStep
import com.staroscky.transferencia.service.steps.AtualizarTransferenciaStep
import com.staroscky.transferencia.service.steps.SalvarCacheStep
import org.springframework.stereotype.Component

/**
 * Strategy para atualizar transferência no PAAS
 * 
 * Aplica quando:
 * - Cache existe
 * - Hash mudou (valor ou data diferente)
 * - Motor validou com sucesso (não lançou exceção)
 * 
 * Faz PATCH no PAAS com apenas os campos alterados e atualiza cache.
 */
@Component
class AtualizarTransferenciaStrategy(
    private val atualizarTransferenciaStep: AtualizarTransferenciaStep,
    private val salvarCacheStep: SalvarCacheStep
) : FluxoStrategy {
    
    override fun aplica(context: TransferenciaContext): Boolean {
        val cache = context.dadosCache ?: return false
        
        // Só atualiza se cache existe E hash mudou
        // (se motor reprovou, já lançou exceção antes de chegar aqui)
        return cache.hashRequest != context.hashRequest
    }
    
    override fun obterSteps(): List<PipelineStep> = listOf(
        atualizarTransferenciaStep,
        salvarCacheStep
    )
    
    override fun prioridade(): Int = 30
}
