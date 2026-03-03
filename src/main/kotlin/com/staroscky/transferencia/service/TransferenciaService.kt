package com.staroscky.transferencia.service

import com.staroscky.transferencia.TransferenciaApi
import com.staroscky.transferencia.domain.TransferenciaContext
import com.staroscky.transferencia.domain.TransferenciaRequest
import com.staroscky.transferencia.domain.TransferenciaResponse
import com.staroscky.transferencia.service.pipeline.TransferenciaPipeline
import org.springframework.stereotype.Service

@Service
class TransferenciaService(
    private val pipeline: TransferenciaPipeline
) : TransferenciaApi {

    override fun criarOuAtualizarTransferencia(request: TransferenciaRequest): TransferenciaResponse {
        val context = TransferenciaContext(
            checkinId = request.checkinId,
            dataTransferencia = request.data,
            valorTransferencia = request.valor
        )

        return pipeline.executar(context)
    }
}