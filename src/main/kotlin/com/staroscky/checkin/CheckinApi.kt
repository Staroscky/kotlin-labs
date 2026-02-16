package com.staroscky.checkin

import com.staroscky.checkin.domain.response.CheckinResponse

interface CheckinApi {
    fun getCheckin(id: String): CheckinResponse

    fun convert(checkinId: String, payload: Map<String, Any>): CheckinResponse
}