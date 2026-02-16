package com.staroscky.transferencia.service.steps

import com.staroscky.transferencia.domain.TransferenciaContext
import com.staroscky.transferencia.service.pipeline.PipelineStep
import org.springframework.stereotype.Component
import java.security.MessageDigest

/**
 * Step que calcula o hash SHA-256 da request
 * 
 * Hash = SHA256(dataTransferencia + valorTransferencia)
 * 
 * Usado para detectar mudanças nos dados da transferência.
 */
@Component
class CalcularHashStep : PipelineStep {
    
    override fun executar(context: TransferenciaContext) {
        val conteudo = "${context.dataTransferencia}${context.valorTransferencia}"
        val hash = calcularSHA256(conteudo)
        context.hashRequest = hash
    }
    
    private fun calcularSHA256(input: String): String {
        val bytes = MessageDigest
            .getInstance("SHA-256")
            .digest(input.toByteArray())
        
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
