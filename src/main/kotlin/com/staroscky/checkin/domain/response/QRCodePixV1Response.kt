package com.staroscky.checkin.domain.response

import java.math.BigDecimal

data class QRCodePixV1Response(
    override val checkinId: String,
    override val uuid: String,
    val qrCodeData: String,
    val valor: BigDecimal
) : CheckinResponse()