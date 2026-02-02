package com.staroscky.checkin.domain.response

sealed class CheckinResponse {
    abstract val checkinId: String
    abstract val uuid: String
}