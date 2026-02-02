package com.staroscky.checkin.service.strategy

import com.staroscky.checkin.domain.TipoEntrada
import org.springframework.stereotype.Component

@Component
class CheckinResponseStrategyFactory(
    strategies: List<CheckinResponseStrategy>
) {
    private val strategyMap: Map<Pair<TipoEntrada, Int>, CheckinResponseStrategy> =
        strategies.associateBy { strategy -> strategy.getSupportedVersion() }

    fun getStrategy(tipoEntrada: TipoEntrada, versao: Int): CheckinResponseStrategy {
        return strategyMap[tipoEntrada to versao]
            ?: throw IllegalStateException(
                "Strategy não encontrada para entrada=$tipoEntrada versão=$versao"
            )
    }
}