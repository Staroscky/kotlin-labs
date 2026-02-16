package com.staroscky.transferencia.service.steps

import com.staroscky.transferencia.client.PaasFeignClient
import com.staroscky.transferencia.domain.TransferenciaContext
import com.staroscky.transferencia.service.mapper.CheckinToPaasMapper
import com.staroscky.transferencia.service.pipeline.PipelineStep
import org.springframework.stereotype.Component
import java.time.Instant

/**
 * Step que cria transferência no PAAS (POST)
 * 
 * 1. Monta request completo do PAAS usando mapper
 * 2. Chama PAAS para criar transferência
 * 3. Popula context.paasId com ID retornado
 * 4. Seta timestamp de criação
 */
@Component
class CriarTransferenciaStep(
    private val paasClient: PaasFeignClient,
    private val mapper: CheckinToPaasMapper
) : PipelineStep {
    
    override fun executar(context: TransferenciaContext) {
        // Monta request completo para criar no PAAS
        val paasRequest = mapper.mapCreate(context)
        
        // Cria transferência no PAAS
        val paasResponse = paasClient.criar(paasRequest)
        
        // Popula contexto
        context.paasId = paasResponse.data.idPaas
        context.createdAt = Instant.now()
        context.updatedAt = context.createdAt
    }
}
