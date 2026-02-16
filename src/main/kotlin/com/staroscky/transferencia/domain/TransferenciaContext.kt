package com.staroscky.transferencia.domain

import com.staroscky.checkin.domain.CheckinId
import com.staroscky.checkin.domain.response.CheckinResponse
import com.staroscky.motor.domain.MotorDecisaoResponse
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate

/**
 * Contexto mutável compartilhado entre os steps do pipeline
 * Contém dados de entrada e dados populados durante a execução
 */
data class TransferenciaContext(
    // ========== INPUT (imutável) ==========
    val checkinId: String,
    val dataTransferencia: LocalDate,
    val valorTransferencia: BigDecimal,
    
    // ========== DADOS POPULADOS PELOS STEPS (mutável) ==========
    
    // Calculado por CalcularHashStep
    var hashRequest: String? = null,
    
    // Populado por VerificarCacheStep
    var dadosCache: TransferenciaCache? = null,
    
    // Populado por BuscarCheckinStep
    var dadosCheckin: CheckinResponse? = null,
    var checkinIdParsed: CheckinId? = null,
    
    // Populado por ValidarMotorStep
    var resultadoMotor: MotorDecisaoResponse? = null,
    
    // Populado por CriarTransferenciaStep ou recuperado do cache
    var paasId: String? = null,
    
    // Timestamps
    var createdAt: Instant? = null,
    var updatedAt: Instant? = null
)
