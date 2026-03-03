package com.staroscky.transferencia.service.handler

import com.staroscky.checkin.CheckinApi
import com.staroscky.checkin.domain.response.CheckinResponse
import org.springframework.stereotype.Component

@Component
class CheckinHandler(
    private val checkinApi: CheckinApi
) {

    fun buscar(checkinId: String): CheckinResponse {
        return checkinApi.getCheckin(checkinId)
    }
}
