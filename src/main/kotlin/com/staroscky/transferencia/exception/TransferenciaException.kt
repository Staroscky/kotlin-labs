package com.staroscky.transferencia.exception

/**
 * Exceção base para erros de transferência
 */
open class TransferenciaException(
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)

/**
 * Exceção lançada quando nenhum instrumento é válido
 * 
 * Nota: Essa exceção pode não ser necessária se o Motor de Decisão
 * já lança sua própria exceção quando não há instrumentos válidos.
 */
class NenhumInstrumentoValidoException(
    val motorResponse: Any  // MotorDecisaoResponse
) : TransferenciaException(
    message = "Nenhum instrumento válido disponível para esta transferência"
)

/**
 * Exceção lançada quando cache deveria existir mas não existe
 */
class CacheNaoEncontradoException(
    val checkinId: String
) : TransferenciaException(
    message = "Cache não encontrado para checkinId: $checkinId"
)

/**
 * Exceção lançada quando há erro ao buscar dados do checkin
 */
class CheckinNaoEncontradoException(
    val checkinId: String,
    cause: Throwable? = null
) : TransferenciaException(
    message = "Checkin não encontrado: $checkinId",
    cause = cause
)

/**
 * Exceção lançada quando há erro na comunicação com o PAAS
 */
class PaasIntegracaoException(
    message: String,
    cause: Throwable? = null
) : TransferenciaException(
    message = "Erro na integração com PAAS: $message",
    cause = cause
)
