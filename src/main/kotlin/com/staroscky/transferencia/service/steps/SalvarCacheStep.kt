package com.staroscky.transferencia.service.steps

import com.fasterxml.jackson.databind.ObjectMapper
import com.staroscky.transferencia.cache.TransferenciaCacheRepository
import com.staroscky.transferencia.domain.TransferenciaCache
import com.staroscky.transferencia.domain.TransferenciaContext
import com.staroscky.transferencia.service.pipeline.PipelineStep
import org.springframework.stereotype.Component
import java.time.Instant

/**
 * Step que salva ou atualiza transferência no cache (Redis)
 * 
 * Persiste todos os dados relevantes com TTL de 15 minutos.
 */
@Component
class SalvarCacheStep(
    private val objectMapper: ObjectMapper,
    private val cacheRepository: TransferenciaCacheRepository
) : PipelineStep {
    
    override fun executar(context: TransferenciaContext) {
        val cache = TransferenciaCache(
            checkinId = context.checkinId,
            paasId = context.paasId
                ?: throw IllegalStateException("paasId deve estar populado"),
            dataTransferencia = context.dataTransferencia,
            valorTransferencia = context.valorTransferencia,
            hashRequest = context.hashRequest
                ?: throw IllegalStateException("hashRequest deve estar populado"),
            dadosCheckinMap = context.dadosCheckin?.let { objectMapper.writeValueAsString(it) }
                ?: throw IllegalStateException("dadosCheckin deve estar populado"),
            resultadoMotor = context.resultadoMotor
                ?: throw IllegalStateException("resultadoMotor deve estar populado"),
            createdAt = context.createdAt ?: Instant.now(),
            updatedAt = context.updatedAt ?: Instant.now()
        )
        
        cacheRepository.salvar(cache)
        
        // Atualiza context com cache salvo
        context.dadosCache = cache
    }
}
