package com.staroscky.transferencia.domain

import com.staroscky.checkin.domain.response.CheckinResponse
import com.staroscky.motor.domain.MotorDecisaoResponse
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.util.Objects

/**
 * Modelo de dados armazenado no Redis
 * TTL: 15 minutos
 */
data class TransferenciaCache(
    val checkinId: String,
    val paasId: String,
    val dataTransferencia: LocalDate,
    val valorTransferencia: BigDecimal,
    val hashRequest: String,
    val dadosCheckinMap: String,
    val dadosCheckin: CheckinResponse? = null,
    val resultadoMotor: MotorDecisaoResponse,
    val createdAt: Instant,
    val updatedAt: Instant
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TransferenciaCache) return false

        return checkinId == other.checkinId &&
                paasId == other.paasId &&
                dataTransferencia == other.dataTransferencia &&
                valorTransferencia.compareTo(other.valorTransferencia) == 0 &&
                hashRequest == other.hashRequest &&
                dadosCheckinMap == other.dadosCheckinMap &&
                dadosCheckin == other.dadosCheckin &&
                resultadoMotor == other.resultadoMotor &&
                createdAt == other.createdAt &&
                updatedAt == other.updatedAt
    }

    override fun hashCode(): Int = Objects.hash(
        checkinId,
        paasId,
        dataTransferencia,
        valorTransferencia.stripTrailingZeros(),
        hashRequest,
        dadosCheckinMap,
        dadosCheckin,
        resultadoMotor,
        createdAt,
        updatedAt
    )
}
