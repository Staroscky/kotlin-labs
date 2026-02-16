package com.staroscky.motor.domain

import java.math.BigDecimal
import java.time.OffsetDateTime

data class MotorDecisaoRequest(
    val transferencia: Transferencia
) {

    data class Transferencia(
        val operacao: Operacao,
        val origem: Conta,
        val destino: Conta
    )

    data class Operacao(
        val valor: BigDecimal,
        val dataTransferencia: OffsetDateTime,
        val descricao: String,
        val endToEndId: String? = null,
        val idTransferencia: String? = null,

        // PIX por chave
        val tipoChave: String? = null,
        val chavePix: String? = null,

        // PIX por QRCode
        val qrcode: QRCode? = null
    )

    data class QRCode(
        val tipoQRCode: String,
        val idQRCode: String
    )

    data class Conta(
        val tipoConta: String,
        val agencia: String,
        val conta: String,
        val ispb: String,
        val nomeBanco: String
    )
}
