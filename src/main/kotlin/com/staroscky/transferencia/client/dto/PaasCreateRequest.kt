package com.staroscky.transferencia.client.dto

import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID

/**
 * Request para criar transferência no PAAS (POST)
 */
data class PaasCreateRequest(
    val comandosJornadas: ComandosJornadas
)

data class ComandosJornadas(
    val origem: OrigemPaas,
    val destinos: List<DestinoPaas>
)

data class OrigemPaas(
    val dadosCliente: DadosClientePaas,
    val dadosConta: DadosContaPaas
)

data class DestinoPaas(
    val transferencia: TransferenciaPaas,
    val dadosCliente: DadosClientePaas,
    val dadosConta: DadosContaPaas
)

data class TransferenciaPaas(
    val valor: BigDecimal,
    val dataTransferencia: OffsetDateTime,
    val descricao: String,
    val endToEndId: String? = null,
    val idTransferencia: String? = null,
    val dadosPix: DadosPixPaas? = null
)

data class DadosPixPaas(
    val tipoChave: String? = null,
    val chave: String? = null,
    val tipoQRCode: String? = null,
    val idQRCode: String? = null,
    val troco: TrocoPaas? = null
)

data class TrocoPaas(
    val valorOriginal: BigDecimal,
    val valorTroco: BigDecimal,
    val valorFinal: BigDecimal
)

data class DadosClientePaas(
    val idCliente: UUID
)

data class DadosContaPaas(
    val idConta: UUID,
    val tipoConta: String,
    val agencia: String,
    val conta: String,
    val ispb: String,
    val nomeBanco: String
)
