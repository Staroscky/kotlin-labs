package com.staroscky.transferencia.domain

import com.staroscky.checkin.domain.response.CheckinResponse
import com.staroscky.motor.domain.MotorDecisaoResponse
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate

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
)
