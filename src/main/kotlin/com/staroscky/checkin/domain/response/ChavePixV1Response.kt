package com.staroscky.checkin.domain.response

import java.util.UUID

data class ChavePixV1Response(
    val endToEndId: String,
    val chave: String,
    val tipoChave: TipoChave,
    val conta: Conta,
    val titular: Titular,
    val status: StatusChavePix
) : CheckinResponse {
    data class Conta(
        val idConta: UUID,
        val tipoConta: TipoConta,
        val agencia: String,
        val numero: String,
        val ispb: String,
        val nomeBanco: String
    )

    data class Titular(
        val idCliente: UUID,
        val nome: String,
        val cpfCnpj: String,
        val tipoPessoa: TipoPessoa
    )


    enum class TipoChave {
        EMAIL,
        CPF,
        CNPJ,
        TELEFONE,
        ALEATORIA
    }

    enum class TipoConta {
        CACC, // Conta Corrente
        SVGS  // Conta Poupança
    }

    enum class StatusChavePix {
        ATIVA,
        INATIVA,
        BLOQUEADA,
        CANCELADA
    }

    enum class TipoPessoa {
        FISICA,
        JURIDICA
    }
}


