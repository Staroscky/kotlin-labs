package com.staroscky.motor

import com.staroscky.motor.domain.MotorDecisaoRequest
import com.staroscky.motor.domain.MotorDecisaoResponse

interface MotorDecisaoAPI {
    fun validar(request: MotorDecisaoRequest): MotorDecisaoResponse
}