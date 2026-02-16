package com.staroscky.transferencia.service.steps

import com.staroscky.motor.MotorDecisaoAPI
import com.staroscky.transferencia.domain.TransferenciaContext
import com.staroscky.transferencia.service.mapper.CheckinToMotorMapper
import com.staroscky.transferencia.service.pipeline.PipelineStep
import org.springframework.stereotype.Component

/**
 * Step que valida transferência no Motor de Decisão
 * 
 * 1. Monta request do motor usando mapper
 * 2. Chama motor de decisão (módulo interno)
 * 3. Motor já lança exceção se nenhum instrumento for válido
 * 4. Popula context.resultadoMotor
 * 
 * Nota: MotorDecisao é injetado do módulo existente no projeto
 */
@Component
class ValidarMotorStep(
    private val motorDecisao: MotorDecisaoAPI,
    private val mapper: CheckinToMotorMapper
) : PipelineStep {
    
    override fun executar(context: TransferenciaContext) {
        val motorRequest = mapper.map(context)

        val motorResponse = motorDecisao.validar(motorRequest)
        context.resultadoMotor = motorResponse
    }
}
