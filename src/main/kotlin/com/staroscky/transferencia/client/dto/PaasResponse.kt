package com.staroscky.transferencia.client.dto

/**
 * Response do PAAS (tanto para CREATE quanto UPDATE)
 */
data class PaasResponse(
    val data: PaasResponseData
)

data class PaasResponseData(
    val idPaas: String
)
