package com.staroscky.transferencia.service.pipeline

import com.staroscky.transferencia.domain.TransferenciaContext
import com.staroscky.transferencia.domain.TransferenciaResponse
import com.staroscky.transferencia.service.pipeline.strategy.FluxoStrategy
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * Pipeline executor que orquestra a execução dos steps
 *
 * Usa strategies para decidir quais steps executar baseado no estado do contexto.
 * Permite decisões dinâmicas após execução de steps críticos.
 */
@Component
class TransferenciaPipeline(
    private val strategies: List<FluxoStrategy>
) {
    private val logger = LoggerFactory.getLogger(TransferenciaPipeline::class.java)
    private val strategiesOrdened = strategies.sortedBy { it.prioridade() }
    /**
     * Executa o pipeline completo usando Chain of Responsibility
     *
     * Fluxo:
     * 1. Itera sobre strategies ordenadas por prioridade
     * 2. Para cada strategy: avalia SE aplica (com context atualizado)
     * 3. Se aplicar: executa seus steps
     * 4. Próxima strategy avalia com context JÁ ATUALIZADO
     *
     * Isso garante que decisões são tomadas APÓS steps críticos executarem.
     * Exemplo: AtualizarTransferenciaStrategy só é avaliada DEPOIS que
     * VerificarCacheStep já populou context.dadosCache.
     *
     * @param context contexto com dados da request
     * @return response com dados do motor e PAAS
     */
    fun executar(context: TransferenciaContext): TransferenciaResponse {
        val iterator = strategiesOrdened.iterator()
        while (iterator.hasNext()) {
            val strategy = iterator.next()

            if (strategy.aplica(context)) {
                strategy.obterSteps().forEach { step ->
                    logger.debug("Executando step ${step::class.simpleName} para strategy ${strategy::class.simpleName}")
                    step.executar(context)
                }
            }
        }

        return montarResponseFinal(context)
    }

    private fun montarResponseFinal(context: TransferenciaContext): TransferenciaResponse {
        // Se chegou aqui sem response no context, algo deu errado
        return context.dadosCache?.let { cache ->
            // Retorna do cache
            TransferenciaResponse(
                data = com.staroscky.transferencia.domain.TransferenciaData(
                    motor = cache.resultadoMotor,
                    pagamento = com.staroscky.transferencia.domain.PagamentoData(
                        idPaas = cache.paasId
                    )
                )
            )
        } ?: TransferenciaResponse(
            data = com.staroscky.transferencia.domain.TransferenciaData(
                motor = context.resultadoMotor!!,
                pagamento = com.staroscky.transferencia.domain.PagamentoData(
                    idPaas = context.paasId!!
                )
            )
        )
    }
}