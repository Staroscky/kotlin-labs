package com.staroscky.transferencia.service.pipeline.strategy

import com.staroscky.transferencia.domain.TransferenciaContext
import com.staroscky.transferencia.service.pipeline.PipelineStep
import com.staroscky.transferencia.service.steps.CalcularHashStep
import com.staroscky.transferencia.service.steps.VerificarCacheStep
import org.springframework.stereotype.Component

/**
 * Strategy de preparação inicial
 * 
 * Sempre executa no início do pipeline para:
 * - Calcular hash da request
 * - Verificar se existe cache
 */
@Component
class PreparacaoInicialStrategy(
    private val calcularHashStep: CalcularHashStep,
    private val verificarCacheStep: VerificarCacheStep
) : FluxoStrategy {
    
    override fun aplica(context: TransferenciaContext): Boolean {
        // Sempre aplica - é a preparação obrigatória
        return true
    }
    
    override fun obterSteps(): List<PipelineStep> = listOf(
        calcularHashStep,
        verificarCacheStep
    )
    
    override fun prioridade(): Int = 0  // Primeiro sempre
}
