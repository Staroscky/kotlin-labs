package com.staroscky.transferencia.service.steps

import com.staroscky.transferencia.domain.TransferenciaContext
import com.staroscky.transferencia.service.pipeline.PipelineStep
import org.springframework.stereotype.Component

/**
 * Step que monta a response final
 * 
 * Usa dados do context (ou cache) para montar TransferenciaResponse.
 * Este step sempre é o último a ser executado.
 */
@Component
class MontarResponseStep : PipelineStep {
    
    override fun executar(context: TransferenciaContext) {
        // Validação: deve ter resultadoMotor e paasId
        require(context.resultadoMotor != null) {
            "resultadoMotor deve estar populado para montar response"
        }
        require(context.paasId != null) {
            "paasId deve estar populado para montar response"
        }
        
        // Response será montada pelo pipeline usando esses dados
        // Este step apenas valida que tudo está pronto
    }
}
