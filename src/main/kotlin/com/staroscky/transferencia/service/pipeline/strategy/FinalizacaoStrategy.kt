package com.staroscky.transferencia.service.pipeline.strategy

import com.staroscky.transferencia.domain.TransferenciaContext
import com.staroscky.transferencia.service.pipeline.PipelineStep
import com.staroscky.transferencia.service.steps.MontarResponseStep
import org.springframework.stereotype.Component

/**
 * Strategy de finalização
 * 
 * Sempre executa no final do pipeline para montar a response.
 */
@Component
class FinalizacaoStrategy(
    private val montarResponseStep: MontarResponseStep
) : FluxoStrategy {
    
    override fun aplica(context: TransferenciaContext): Boolean {
        // Sempre aplica - é a finalização obrigatória
        return true
    }
    
    override fun obterSteps(): List<PipelineStep> = listOf(
        montarResponseStep
    )
    
    override fun prioridade(): Int = 999  // Último sempre
}
