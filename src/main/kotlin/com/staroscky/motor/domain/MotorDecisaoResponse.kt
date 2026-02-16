package com.staroscky.motor.domain

data class MotorDecisaoResponse(
    val decisao: Decisao,
){
    enum class Decisao {
        APROVADO,
        REPROVADO,
        PENDENTE
    }
}
