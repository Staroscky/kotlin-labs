package com.staroscky.transferencia.domain

/**
 * Response da transferência com dados do motor e PAAS
 */
data class TransferenciaResponse(
    val data: TransferenciaData
)

data class TransferenciaData(
    val motor: Any,  // MotorDecisaoResponse - tipagem genérica por enquanto
    val pagamento: PagamentoData
)

data class PagamentoData(
    val idPaas: String
)
