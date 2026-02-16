package com.staroscky.transferencia.client.dto

import java.math.BigDecimal
import java.time.OffsetDateTime

/**
 * Request para atualizar transferência no PAAS (PATCH)
 * Envia APENAS os campos que foram alterados
 */
data class PaasUpdateRequest(
    val comandosJornadas: ComandosJornadasUpdate
)

data class ComandosJornadasUpdate(
    val destinos: List<DestinoUpdatePaas>
)

data class DestinoUpdatePaas(
    val transferencia: TransferenciaUpdatePaas
)

data class TransferenciaUpdatePaas(
    val valor: BigDecimal? = null,
    val dataTransferencia: OffsetDateTime? = null
)
