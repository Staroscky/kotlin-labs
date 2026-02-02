package com.staroscky.checkin.service.strategy

import com.staroscky.checkin.domain.CheckinId
import com.staroscky.checkin.domain.response.CheckinResponse
import com.staroscky.checkin.domain.TipoEntrada

interface CheckinResponseStrategy {
    fun getSupportedVersion(): Pair<TipoEntrada, Int>
    fun convert(rawResponse: Map<String, Any>, checkinId: CheckinId): CheckinResponse
}