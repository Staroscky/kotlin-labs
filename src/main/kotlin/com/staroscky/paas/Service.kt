package com.staroscky.paas

import com.staroscky.checkin.CheckinApi
import org.springframework.stereotype.Service

@Service
class Service(
    private val checkinApi: CheckinApi
) {

    fun getCheckinInfo(id: String) = checkinApi.getCheckin(id)
}