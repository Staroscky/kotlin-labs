package com.staroscky.transferencia.controller

import com.staroscky.transferencia.TransferenciaApi
import com.staroscky.transferencia.domain.TransferenciaRequest
import com.staroscky.transferencia.domain.TransferenciaResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Controller REST para expor API de transferências
 *
 * Endpoint: POST /api/v1/transferencias
 */
@RestController
@RequestMapping("/v1/transferencias")
class TransferenciaController(
    private val transferenciaApi: TransferenciaApi
) {

    /**
     * Cria ou atualiza uma transferência
     *
     * Se for a primeira vez (cache miss), cria no PAAS.
     * Se já existe (cache hit) com dados diferentes, atualiza no PAAS.
     * Se já existe com mesmos dados, retorna do cache.
     *
     * @param request dados da transferência
     * @return response com dados do motor e ID do PAAS
     */
    @PostMapping
    fun criarOuAtualizar(
        @Valid @RequestBody request: TransferenciaRequest
    ): ResponseEntity<TransferenciaResponse> {
        val response = transferenciaApi.criarOuAtualizarTransferencia(request)
        return ResponseEntity.ok(response)
    }
}