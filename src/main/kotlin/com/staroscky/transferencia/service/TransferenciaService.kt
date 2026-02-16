package com.staroscky.transferencia.service

import com.staroscky.transferencia.TransferenciaApi
import com.staroscky.transferencia.domain.TransferenciaContext
import com.staroscky.transferencia.domain.TransferenciaRequest
import com.staroscky.transferencia.domain.TransferenciaResponse
import com.staroscky.transferencia.service.pipeline.TransferenciaPipeline
import org.springframework.stereotype.Service

/**
 * Implementação do TransferenciaApi
 *
 * Orquestra a criação/atualização de transferências usando pipeline de steps.
 */
@Service
class TransferenciaService(
    private val pipeline: TransferenciaPipeline
) : TransferenciaApi {

    override fun criarOuAtualizarTransferencia(request: TransferenciaRequest): TransferenciaResponse {
        // Cria contexto inicial com dados da request
        val context = TransferenciaContext(
            checkinId = request.checkinId,
            dataTransferencia = request.data,
            valorTransferencia = request.valor
        )

        // Executa pipeline
        return pipeline.executar(context)
    }
}