package com.staroscky.checkin.domain.response

data class ChavePixV1Response(
    override val checkinId: String,
    override val uuid: String,
    val chavePix: String,
    val banco: String
) : CheckinResponse()