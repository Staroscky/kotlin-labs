package com.staroscky.transferencia.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.staroscky.transferencia.domain.TransferenciaCache
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

/**
 * Configuração do módulo de Transferência
 */
@Configuration
class TransferenciaConfig {

    /**
     * Configura RedisTemplate para TransferenciaCache
     *
     * Usa Jackson para serialização JSON do cache.
     */
    @Bean
    fun transferenciaCacheRedisTemplate(
        objectMapper: ObjectMapper,
        connectionFactory: RedisConnectionFactory
    ): RedisTemplate<String, TransferenciaCache> {
        val template = RedisTemplate<String, TransferenciaCache>()
        template.connectionFactory = connectionFactory

        // Serializer para chave (String)
        val stringSerializer = StringRedisSerializer()
        template.keySerializer = stringSerializer
        template.hashKeySerializer = stringSerializer

        // Serializer para valor (JSON usando Jackson)
        val jsonSerializer = Jackson2JsonRedisSerializer(objectMapper, TransferenciaCache::class.java)
        template.valueSerializer = jsonSerializer
        template.hashValueSerializer = jsonSerializer

        template.afterPropertiesSet()
        return template
    }
}