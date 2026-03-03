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

@RestController
@RequestMapping("/v1/transferencias")
class TransferenciaController(
    private val transferenciaApi: TransferenciaApi
) {

    @PostMapping
    fun criarOuAtualizar(
        @Valid @RequestBody request: TransferenciaRequest
    ): ResponseEntity<TransferenciaResponse> {
        val response = transferenciaApi.criarOuAtualizarTransferencia(request)
        return ResponseEntity.ok(response)
    }
}