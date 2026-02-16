package com.staroscky.transferencia.service.steps

import com.staroscky.transferencia.client.PaasFeignClient
import com.staroscky.transferencia.domain.TransferenciaContext
import com.staroscky.transferencia.service.mapper.CheckinToPaasMapper
import com.staroscky.transferencia.service.pipeline.PipelineStep
import org.springframework.stereotype.Component
import java.time.Instant

/**
 * Step que atualiza transferência no PAAS (PATCH)
 * 
 * 1. Identifica o que mudou (data e/ou valor)
 * 2. Monta request PATCH com apenas campos alterados
 * 3. Chama PAAS para atualizar transferência
 * 4. Atualiza timestamp
 */
@Component
class AtualizarTransferenciaStep(
    private val paasClient: PaasFeignClient,
    private val mapper: CheckinToPaasMapper
) : PipelineStep {
    
    override fun executar(context: TransferenciaContext) {
        val cache = context.dadosCache
            ?: throw IllegalStateException("Cache deve existir para atualizar")
        
        // Monta request PATCH com apenas os campos que mudaram
        val patchRequest = mapper.mapUpdate(
            context = context,
            cacheAtual = cache
        )
        
        // Atualiza no PAAS
        paasClient.atualizar(cache.paasId, patchRequest)
        
        // Atualiza timestamp
        context.updatedAt = Instant.now()
    }
}
