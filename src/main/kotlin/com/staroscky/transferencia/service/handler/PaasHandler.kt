package com.staroscky.transferencia.service.handler

import com.staroscky.checkin.domain.response.CheckinResponse
import com.staroscky.transferencia.client.PaasFeignClient
import com.staroscky.transferencia.domain.TransferenciaCache
import com.staroscky.transferencia.domain.TransferenciaRequest
import com.staroscky.transferencia.service.mapper.CheckinToPaasMapper
import org.springframework.stereotype.Component

@Component
class PaasHandler(
    private val paasClient: PaasFeignClient,
    private val mapper: CheckinToPaasMapper
) {

    fun criar(
        request: TransferenciaRequest,
        dadosCheckin: CheckinResponse
    ): String {
        val paasRequest = mapper.mapCreate(request, dadosCheckin)
        val response = paasClient.criar(paasRequest)
        return response.data.idPaas
    }

    fun atualizar(
        paasId: String,
        request: TransferenciaRequest
    ) {
        val patchRequest = mapper.mapUpdate(request)
        paasClient.atualizar(paasId, patchRequest)
    }
}
