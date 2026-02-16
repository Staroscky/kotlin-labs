package com.staroscky.checkin.domain.response

data class ContaTransacionalV1Response(
    val dadosBancarios: DadosBancarios
) : CheckinResponse {

    data class DadosBancarios(
        val ispb: String,
        val agencia: String,
        val conta: String,
        val dac: String,
        val tipoConta: String
    )
}