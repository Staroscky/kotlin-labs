package com.staroscky.checkin.domain.response

import java.math.BigDecimal
import java.time.LocalDate
import java.time.OffsetDateTime

sealed interface QrCodePixV1Response : CheckinResponse {
    val txId: TxId
    val revisao: Int
    val status: StatusQrCodePix
    val chave: ChavePix
    val solicitacaoPagador: String?
    val infoAdicionais: List<InfoAdicional>

    data class Imediato(
        override val txId: TxId,
        override val revisao: Int,
        override val status: StatusQrCodePix,
        override val chave: ChavePix,
        override val solicitacaoPagador: String?,
        override val infoAdicionais: List<InfoAdicional>,
        val calendario: CalendarioImediato,
        val valor: ValorImediato
    ) : QrCodePixV1Response

    data class ComVencimento(
        override val txId: TxId,
        override val revisao: Int,
        override val status: StatusQrCodePix,
        override val chave: ChavePix,
        override val solicitacaoPagador: String?,
        override val infoAdicionais: List<InfoAdicional>,
        val calendario: CalendarioComVencimento,
        val valor: ValorComVencimento,
        val devedor: Devedor?,
        val recebedor: Recebedor
    ) : QrCodePixV1Response

    @JvmInline
    value class TxId(val value: String)

    @JvmInline
    value class ChavePix(val value: String)

    data class InfoAdicional(
        val nome: String,
        val valor: String
    )


    data class CalendarioImediato(
        val criacao: OffsetDateTime,
        val apresentacao: OffsetDateTime?,
        val expiracaoEmSegundos: Int
    )

    data class CalendarioComVencimento(
        val criacao: OffsetDateTime,
        val apresentacao: OffsetDateTime?,
        val dataVencimento: LocalDate,
        val validadeAposVencimentoEmDias: Int?
    )

    data class ValorImediato(
        val original: BigDecimal,
        val permiteAlteracao: Boolean
    )

    data class ValorComVencimento(
        val original: BigDecimal,
        val multa: BigDecimal?,
        val juros: BigDecimal?,
        val valorFinal: BigDecimal?
    )

    data class Devedor(
        val documento: Documento,
        val nome: String
    )

    data class Recebedor(
        val documento: Documento,
        val nome: String,
        val endereco: Endereco
    )

    data class Documento(
        val numero: String,
        val tipo: TipoDocumento
    )

    enum class TipoDocumento {
        CPF,
        CNPJ
    }

    data class Endereco(
        val logradouro: String,
        val cidade: String,
        val uf: String,
        val cep: String
    )

    enum class StatusQrCodePix {
        ATIVA,
        CONCLUIDA,
        REMOVIDA_PELO_USUARIO_RECEBEDOR,
        REMOVIDA_PELO_PSP,
        EXPIRADA
    }

}
