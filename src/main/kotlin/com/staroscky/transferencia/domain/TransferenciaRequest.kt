package com.staroscky.transferencia.domain

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.LocalDate

/**
 * Request de entrada para criar/atualizar transferência
 */
data class TransferenciaRequest(
    @field:NotBlank(message = "checkinId é obrigatório")
    val checkinId: String,

    @field:NotNull(message = "data é obrigatória")
    val data: LocalDate,

    @field:NotNull(message = "valor é obrigatório")
    @field:DecimalMin(value = "0.01", message = "valorTransferencia deve ser maior que zero")
    val valor: BigDecimal
)
