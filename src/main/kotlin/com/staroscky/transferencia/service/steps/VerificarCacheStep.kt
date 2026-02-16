package com.staroscky.transferencia.service.steps

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.staroscky.checkin.CheckinApi
import com.staroscky.checkin.domain.CheckinId
import com.staroscky.transferencia.cache.TransferenciaCacheRepository
import com.staroscky.transferencia.domain.TransferenciaContext
import com.staroscky.transferencia.service.pipeline.PipelineStep
import org.springframework.stereotype.Component

/**
 * Step que verifica se existe transferência no cache (Redis)
 * 
 * Popula context.dadosCache se encontrar.
 * Se cache existe, reutiliza dadosCheckin e resultadoMotor salvos.
 */
@Component
class VerificarCacheStep(
    private val objectMapper: ObjectMapper,
    private val cacheRepository: TransferenciaCacheRepository,
    private val checkinApi: CheckinApi
) : PipelineStep {
    
    override fun executar(context: TransferenciaContext) {
        val cache = cacheRepository.buscar(context.checkinId)

        if (cache != null) {
            context.dadosCache = cache
            context.checkinIdParsed = CheckinId(cache.checkinId)
            context.dadosCheckin = checkinApi.convert(cache.checkinId, objectMapper.readValue(cache.dadosCheckinMap))
            context.resultadoMotor = cache.resultadoMotor
            context.paasId = cache.paasId
        }
    }
}
