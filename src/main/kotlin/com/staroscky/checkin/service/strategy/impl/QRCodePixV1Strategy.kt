package com.staroscky.checkin.service.strategy.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.staroscky.checkin.domain.CheckinId
import com.staroscky.checkin.domain.response.CheckinResponse
import com.staroscky.checkin.domain.TipoEntrada
import com.staroscky.checkin.domain.response.QrCodePixV1Response
import com.staroscky.checkin.service.strategy.CheckinResponseStrategy
import org.springframework.stereotype.Component

@Component
class QRCodePixV1Strategy(
    private val objectMapper: ObjectMapper
) : CheckinResponseStrategy {
    override fun getSupportedVersion(): Pair<TipoEntrada, Int> =
        Pair(TipoEntrada.QRCODE_PIX, 1)

    override fun convert(rawResponse: Any, checkinId: CheckinId): CheckinResponse {
        return objectMapper.convertValue(rawResponse, QrCodePixV1Response::class.java)
    }
}
