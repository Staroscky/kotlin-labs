package com.staroscky.transferencia.service.pipeline.strategy

import com.staroscky.transferencia.domain.TransferenciaContext
import com.staroscky.transferencia.service.pipeline.PipelineStep
import org.springframework.stereotype.Component

/**
 * Strategy para retornar dados do cache
 * 
 * Aplica quando:
 * - Cache existe
 * - Hash da request é igual ao hash do cache (nada mudou)
 * 
 * Quando aplica, não adiciona nenhum step adicional pois
 * já tem todos os dados necessários no cache.
 */
@Component
class RetornarCacheStrategy : FluxoStrategy {
    
    override fun aplica(context: TransferenciaContext): Boolean {
        val cache = context.dadosCache ?: return false
        return cache.hashRequest == context.hashRequest
    }
    
    override fun obterSteps(): List<PipelineStep> {
        // Não precisa executar nada - usa dados do cache
        return emptyList()
    }
    
    override fun prioridade(): Int = 5  // Logo após verificar cache
}
