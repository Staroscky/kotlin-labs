package com.staroscky.transferencia

import com.staroscky.transferencia.domain.TransferenciaRequest
import com.staroscky.transferencia.domain.TransferenciaResponse

/**
 * Interface pública para criação e atualização de transferências
 */
interface TransferenciaApi {
    /**
     * Cria ou atualiza uma transferência baseada no checkinId
     * 
     * @param request dados da transferência (checkinId, data, valor)
     * @return resposta com dados do motor e ID do PAAS
     */
    fun criarOuAtualizarTransferencia(request: TransferenciaRequest): TransferenciaResponse
}
