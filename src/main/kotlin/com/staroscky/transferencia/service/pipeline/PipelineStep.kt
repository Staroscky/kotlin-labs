package com.staroscky.transferencia.service.pipeline

import com.staroscky.transferencia.domain.TransferenciaContext

/**
 * Interface base para steps do pipeline
 * Cada step executa uma operação específica e popula o contexto
 */
interface PipelineStep {
    /**
     * Executa a lógica do step, mutando o context conforme necessário
     *
     * @param context contexto compartilhado entre todos os steps
     * @throws Exception se houver erro na execução
     */
    fun executar(context: TransferenciaContext)
}