package com.staroscky.transferencia.service.pipeline

import com.staroscky.transferencia.domain.TransferenciaContext

interface PipelineStep {

    fun executar(context: TransferenciaContext)
}