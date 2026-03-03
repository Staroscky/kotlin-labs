package com.staroscky.transferencia.service.handler

import com.staroscky.checkin.domain.response.CheckinResponse
import com.staroscky.motor.MotorDecisaoAPI
import com.staroscky.motor.domain.MotorDecisaoResponse
import com.staroscky.transferencia.domain.TransferenciaRequest
import com.staroscky.transferencia.service.mapper.CheckinToMotorMapper
import org.springframework.stereotype.Component


@Component
class MotorHandler(
    private val motorDecisao: MotorDecisaoAPI,
    private val mapper: CheckinToMotorMapper
) {

    fun validar(
        request: TransferenciaRequest,
        dadosCheckin: CheckinResponse
    ): MotorDecisaoResponse {
        val motorRequest = mapper.map(request, dadosCheckin)
        return motorDecisao.validar(motorRequest)
    }
}
