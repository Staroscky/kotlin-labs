package com.staroscky.paas.model

import java.math.BigDecimal
import java.time.LocalDate

data class PaasRequest(
    val checkinId: String,
    val valor: BigDecimal,
    val data: LocalDate
)
