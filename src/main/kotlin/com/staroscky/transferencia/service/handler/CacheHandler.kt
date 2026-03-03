package com.staroscky.transferencia.service.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.staroscky.checkin.domain.response.CheckinResponse
import com.staroscky.motor.domain.MotorDecisaoResponse
import com.staroscky.transferencia.cache.TransferenciaCacheRepository
import com.staroscky.transferencia.domain.TransferenciaCache
import com.staroscky.transferencia.domain.TransferenciaRequest
import org.springframework.stereotype.Component
import java.security.MessageDigest
import java.time.Instant

@Component
class CacheHandler(
    private val objectMapper: ObjectMapper,
    private val repository: TransferenciaCacheRepository
) {

    sealed class CacheResult {
        object Ausente : CacheResult()

        data class Valido(val cache: TransferenciaCache) : CacheResult()

        data class Desatualizado(
            val cache: TransferenciaCache,
            val novoHash: String
        ) : CacheResult()
    }

    fun verificar(request: TransferenciaRequest): CacheResult {
        val hash = calcularHash(request)
        val cache = repository.buscar(request.checkinId) ?: return CacheResult.Ausente

        return if (cache.hashRequest == hash) {
            CacheResult.Valido(cache)
        } else {
            CacheResult.Desatualizado(cache = cache, novoHash = hash)
        }
    }

    fun salvar(
        request: TransferenciaRequest,
        dadosCheckin: CheckinResponse,
        motorResponse: MotorDecisaoResponse,
        paasId: String
    ) {
        val now = Instant.now()

        TransferenciaCache(
            checkinId = request.checkinId,
            paasId = paasId,
            dataTransferencia = request.data,
            valorTransferencia = request.valor,
            hashRequest = calcularHash(request),
            dadosCheckinMap = objectMapper.writeValueAsString(dadosCheckin),
            dadosCheckin = null,
            resultadoMotor = motorResponse,
            createdAt = now,
            updatedAt = now
        ).also { repository.salvar(it) }
    }

    fun atualizar(
        request: TransferenciaRequest,
        cacheAtual: TransferenciaCache,
        novoHash: String,
        motorResponse: MotorDecisaoResponse
    ) {
        val dadosCheckinMap = requireNotNull(cacheAtual.dadosCheckin) {
            "dadosCheckin não pode ser null ao atualizar cache do checkinId=${cacheAtual.checkinId}"
        }.let { objectMapper.writeValueAsString(it) }

        cacheAtual.copy(
            dataTransferencia = request.data,
            valorTransferencia = request.valor,
            hashRequest = novoHash,
            dadosCheckinMap = dadosCheckinMap,
            resultadoMotor = motorResponse,
            updatedAt = Instant.now()
        ).also { repository.salvar(it) }
    }

    private fun calcularHash(request: TransferenciaRequest): String {
        val conteudo = "${request.data}|${request.valor.toPlainString()}"

        return MessageDigest
            .getInstance("SHA-256")
            .digest(conteudo.toByteArray())
            .joinToString("") { "%02x".format(it) }
    }
}