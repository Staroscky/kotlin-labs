package com.staroscky.transferencia.service

import com.staroscky.transferencia.TransferenciaApi
import com.staroscky.transferencia.domain.TransferenciaRequest
import com.staroscky.transferencia.domain.TransferenciaResponse
import com.staroscky.transferencia.domain.TransferenciaData
import com.staroscky.transferencia.domain.PagamentoData
import com.staroscky.transferencia.service.handler.CacheHandler
import com.staroscky.transferencia.service.handler.CheckinHandler
import com.staroscky.transferencia.service.handler.MotorHandler
import com.staroscky.transferencia.service.handler.PaasHandler
import org.springframework.stereotype.Service

@Service
class TransferenciaService(
    private val cacheHandler: CacheHandler,
    private val checkinHandler: CheckinHandler,
    private val motorHandler: MotorHandler,
    private val paasHandler: PaasHandler
) : TransferenciaApi {

    override fun criarOuAtualizarTransferencia(request: TransferenciaRequest): TransferenciaResponse {
        return when (val cacheResult = cacheHandler.verificar(request)) {
            is CacheHandler.CacheResult.Ausente -> criarNova(request)
            is CacheHandler.CacheResult.Valido -> cacheResult.toResponse()
            is CacheHandler.CacheResult.Desatualizado -> atualizar(request, cacheResult.novoHash, cacheResult.cache)
        }
    }

    private fun criarNova(request: TransferenciaRequest): TransferenciaResponse {
        val dadosCheckin = checkinHandler.buscar(request.checkinId)
        val motorResponse = motorHandler.validar(request, dadosCheckin)
        val paasId = paasHandler.criar(request, dadosCheckin)

        cacheHandler.salvar(request, dadosCheckin, motorResponse, paasId)

        return TransferenciaResponse(
            data = TransferenciaData(
                motor = motorResponse,
                pagamento = PagamentoData(idPaas = paasId)
            )
        )
    }

    private fun atualizar(
        request: TransferenciaRequest,
        hash: String,
        cache: com.staroscky.transferencia.domain.TransferenciaCache
    ): TransferenciaResponse {
        val motorResponse = motorHandler.validar(
            request = request,
            dadosCheckin = cache.dadosCheckin!!)

        paasHandler.atualizar(cache.paasId, request)
        cacheHandler.atualizar(request, cache, hash, motorResponse)

        return TransferenciaResponse(
            data = TransferenciaData(
                motor = motorResponse,
                pagamento = PagamentoData(idPaas = cache.paasId)
            )
        )
    }

    private fun CacheHandler.CacheResult.Valido.toResponse(): TransferenciaResponse {
        return TransferenciaResponse(
            data = TransferenciaData(
                motor = this.cache.resultadoMotor,
                pagamento = PagamentoData(idPaas = this.cache.paasId)
            )
        )
    }
}