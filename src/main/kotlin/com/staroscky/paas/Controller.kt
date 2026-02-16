package com.staroscky.paas

import com.staroscky.checkin.domain.response.CheckinResponse
import com.staroscky.paas.model.PaasRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller(
    private val service: Service
) {

    @PostMapping("/paas")
    fun hello(
        @RequestBody body: PaasRequest
    ): CheckinResponse {
        return service.getCheckinInfo(body.checkinId)
    }
}