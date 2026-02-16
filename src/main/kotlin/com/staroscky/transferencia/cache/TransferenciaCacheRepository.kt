package com.staroscky.transferencia.cache

import com.staroscky.transferencia.domain.TransferenciaCache
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.time.Duration

/**
 * Repositório para gerenciar cache de transferências no Redis
 * TTL padrão: 15 minutos
 */
@Repository
class TransferenciaCacheRepository(
    private val redisTemplate: RedisTemplate<String, TransferenciaCache>
) {

    companion object {
        private const val KEY_PREFIX = "transferencia:"
        private val DEFAULT_TTL = Duration.ofMinutes(15)
    }

    /**
     * Busca transferência no cache pelo checkinId
     */
    fun buscar(checkinId: String): TransferenciaCache? {
        val key = buildKey(checkinId)
        return redisTemplate.opsForValue().get(key)
    }

    /**
     * Salva ou atualiza transferência no cache
     */
    fun salvar(transferencia: TransferenciaCache) {
        val key = buildKey(transferencia.checkinId)
        redisTemplate.opsForValue().set(key, transferencia, DEFAULT_TTL)
    }

    /**
     * Verifica se existe no cache
     */
    fun existe(checkinId: String): Boolean {
        val key = buildKey(checkinId)
        return redisTemplate.hasKey(key)
    }

    private fun buildKey(checkinId: String): String = "$KEY_PREFIX$checkinId"
}