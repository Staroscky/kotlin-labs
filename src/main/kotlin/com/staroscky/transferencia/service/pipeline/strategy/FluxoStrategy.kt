package com.staroscky.transferencia.service.pipeline.strategy

import com.staroscky.transferencia.domain.TransferenciaContext
import com.staroscky.transferencia.service.pipeline.PipelineStep

/**
 * Strategy que define quando e quais steps devem ser executados
 * 
 * Cada strategy avalia o contexto atual e decide se deve adicionar seus steps ao pipeline.
 * Isso permite decisões baseadas em resultados de steps anteriores.
 */
interface FluxoStrategy {
    
    /**
     * Verifica se esta strategy se aplica ao contexto atual
     * 
     * Pode ser avaliada ANTES ou DEPOIS de executar outros steps,
     * permitindo decisões baseadas em dados populados no contexto.
     * 
     * @param context contexto atual do pipeline
     * @return true se a strategy deve adicionar seus steps
     */
    fun aplica(context: TransferenciaContext): Boolean
    
    /**
     * Retorna a lista de steps que devem ser executados
     * 
     * @return lista de steps (pode ser vazia)
     */
    fun obterSteps(): List<PipelineStep>
    
    /**
     * Prioridade de execução
     * 
     * Strategies com menor prioridade são avaliadas primeiro.
     * Útil para controlar a ordem de execução dos steps.
     * 
     * @return valor de prioridade (menor = primeiro)
     */
    fun prioridade(): Int = 100
}
