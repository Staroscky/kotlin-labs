package com.staroscky.transferencia.service.mapper

import com.staroscky.checkin.domain.TipoEntrada
import com.staroscky.checkin.domain.response.ChavePixV1Response
import com.staroscky.checkin.domain.response.ContaTransacionalV1Response
import com.staroscky.checkin.domain.response.QrCodePixV1Response
import com.staroscky.motor.domain.MotorDecisaoRequest
import com.staroscky.transferencia.domain.TransferenciaContext
import org.springframework.stereotype.Component
import java.time.ZoneOffset

@Component
class CheckinToMotorMapper {

    companion object {
        private val ORIGEM_MOCK = MotorDecisaoRequest.Conta(
            tipoConta = "CACC",
            agencia = "0001",
            conta = "123456",
            ispb = "00000000",
            nomeBanco = "Banco Mock"
        )
    }

    fun map(context: TransferenciaContext): MotorDecisaoRequest {
        val checkinResponse = context.dadosCheckin
            ?: throw IllegalStateException("dadosCheckin deve estar populado")

        val checkinId = context.checkinIdParsed
            ?: throw IllegalStateException("checkinIdParsed deve estar populado")

        return when (checkinId.tipoEntrada) {
            TipoEntrada.CHAVE_PIX ->
                mapChavePix(context, checkinResponse as ChavePixV1Response)

            TipoEntrada.QRCODE_PIX ->
                mapQRCodePix(context, checkinResponse as QrCodePixV1Response)

            TipoEntrada.MANUAL ->
                mapManual(context, checkinResponse as ContaTransacionalV1Response)
        }
    }

    private fun mapChavePix(
        context: TransferenciaContext,
        response: ChavePixV1Response
    ): MotorDecisaoRequest {

        val operacao = MotorDecisaoRequest.Operacao(
            valor = context.valorTransferencia,
            dataTransferencia = context.dataTransferencia
                .atStartOfDay()
                .atOffset(ZoneOffset.UTC),
            descricao = "Transferência via CheckinId",
            endToEndId = response.endToEndId,
            idTransferencia = null,
            tipoChave = response.tipoChave.name,
            chavePix = response.chave
        )

        val destino = MotorDecisaoRequest.Conta(
            tipoConta = response.conta.tipoConta.name,
            agencia = response.conta.agencia,
            conta = response.conta.numero,
            ispb = response.conta.ispb,
            nomeBanco = response.conta.nomeBanco
        )

        return MotorDecisaoRequest(
            transferencia = MotorDecisaoRequest.Transferencia(
                operacao = operacao,
                origem = ORIGEM_MOCK,
                destino = destino
            )
        )
    }

    private fun mapQRCodePix(
        context: TransferenciaContext,
        response: QrCodePixV1Response
    ): MotorDecisaoRequest {

        val qrcode = when (response) {
            is QrCodePixV1Response.Imediato ->
                MotorDecisaoRequest.QRCode(
                    tipoQRCode = "ESTATICO",
                    idQRCode = response.txId.value
                )

            is QrCodePixV1Response.ComVencimento ->
                MotorDecisaoRequest.QRCode(
                    tipoQRCode = "DINAMICO_COBV",
                    idQRCode = response.txId.value
                )
        }

        val operacao = MotorDecisaoRequest.Operacao(
            valor = context.valorTransferencia,
            dataTransferencia = context.dataTransferencia
                .atStartOfDay()
                .atOffset(ZoneOffset.UTC),
            descricao = "Transferência via CheckinId",
            endToEndId = null,
            idTransferencia = null,
            qrcode = qrcode
        )

        val destino = MotorDecisaoRequest.Conta(
            tipoConta = "CACC",
            agencia = "0000",
            conta = "000000",
            ispb = "00000000",
            nomeBanco = "Desconhecido"
        )

        return MotorDecisaoRequest(
            transferencia = MotorDecisaoRequest.Transferencia(
                operacao = operacao,
                origem = ORIGEM_MOCK,
                destino = destino
            )
        )
    }

    private fun mapManual(
        context: TransferenciaContext,
        response: ContaTransacionalV1Response
    ): MotorDecisaoRequest {

        val operacao = MotorDecisaoRequest.Operacao(
            valor = context.valorTransferencia,
            dataTransferencia = context.dataTransferencia
                .atStartOfDay()
                .atOffset(ZoneOffset.UTC),
            descricao = "Transferência via CheckinId",
            endToEndId = null,
            idTransferencia = null
        )

        val destino = MotorDecisaoRequest.Conta(
            tipoConta = response.dadosBancarios.tipoConta,
            agencia = response.dadosBancarios.agencia,
            conta = response.dadosBancarios.conta,
            ispb = response.dadosBancarios.ispb,
            nomeBanco = "Banco Destino"
        )

        return MotorDecisaoRequest(
            transferencia = MotorDecisaoRequest.Transferencia(
                operacao = operacao,
                origem = ORIGEM_MOCK,
                destino = destino
            )
        )
    }
}
