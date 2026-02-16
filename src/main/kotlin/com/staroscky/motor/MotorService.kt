package com.staroscky.motor

import com.staroscky.motor.domain.MotorDecisaoRequest
import com.staroscky.motor.domain.MotorDecisaoResponse
import com.staroscky.motor.domain.MotorDecisaoResponse.Decisao
import org.springframework.stereotype.Service

@Service
class MotorService(): MotorDecisaoAPI {

        override fun validar(request: MotorDecisaoRequest): MotorDecisaoResponse {
            return MotorDecisaoResponse(
                decisao = Decisao.APROVADO
            )
        }
}