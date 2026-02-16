package com.staroscky.transferencia.service.steps

import com.staroscky.checkin.CheckinApi
import com.staroscky.checkin.domain.CheckinId
import com.staroscky.transferencia.domain.TransferenciaContext
import com.staroscky.transferencia.service.pipeline.PipelineStep
import org.springframework.stereotype.Component

/**
 * Step que busca dados do checkin no CheckinCore
 * 
 * Usa o CheckinApi (módulo existente) para buscar dados do destino.
 * Popula context.dadosCheckin e context.checkinIdParsed.
 */
@Component
class BuscarCheckinStep(
    private val checkinApi: CheckinApi
) : PipelineStep {
    
    override fun executar(context: TransferenciaContext) {
        // Parse do checkinId
        val checkinIdParsed = CheckinId(context.checkinId)
        context.checkinIdParsed = checkinIdParsed
        
        // Busca dados do checkin
        val checkinResponse = checkinApi.getCheckin(context.checkinId)
        context.dadosCheckin = checkinResponse
    }
}
