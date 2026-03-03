package com.staroscky.transferencia

import com.staroscky.transferencia.domain.TransferenciaRequest
import com.staroscky.transferencia.domain.TransferenciaResponse

interface TransferenciaApi {

    fun criarOuAtualizarTransferencia(request: TransferenciaRequest): TransferenciaResponse
}
