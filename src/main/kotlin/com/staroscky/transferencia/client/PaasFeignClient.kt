package com.staroscky.transferencia.client

import com.staroscky.transferencia.client.dto.PaasCreateRequest
import com.staroscky.transferencia.client.dto.PaasResponse
import com.staroscky.transferencia.client.dto.PaasUpdateRequest
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

/**
 * Cliente Feign para integração com PAAS
 * Usa HTTP/2 Client configurado globalmente
 */
@FeignClient(
    name = "PaasApi"
)
interface PaasFeignClient {

    /**
     * Cria uma nova transferência no PAAS
     */
    @PostMapping("/transferencias")
    fun criar(@RequestBody request: PaasCreateRequest): PaasResponse

    /**
     * Atualiza uma transferência existente (PATCH parcial)
     */
    @PatchMapping("/transferencias/{paasId}")
    fun atualizar(
        @PathVariable paasId: String,
        @RequestBody request: PaasUpdateRequest
    ): PaasResponse

    /**
     * Busca transferência por ID (para futuras funcionalidades)
     */
    @GetMapping("/transferencias/{paasId}")
    fun buscar(@PathVariable paasId: String): PaasResponse
}