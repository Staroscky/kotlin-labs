package com.staroscky.checkin.service

import com.staroscky.checkin.CheckinApi
import com.staroscky.checkin.client.CheckinFeignClient
import com.staroscky.checkin.domain.CheckinId
import com.staroscky.checkin.domain.response.CheckinResponse
import com.staroscky.checkin.service.strategy.CheckinResponseStrategyFactory
import org.springframework.stereotype.Service

@Service
class CheckinService(
    private val feignClient: CheckinFeignClient,
    private val strategyFactory: CheckinResponseStrategyFactory
): CheckinApi {

    override fun getCheckin(id: String): CheckinResponse {
        val checkinId = CheckinId(id)

        val rawResponse = feignClient.getCheckin(id)

        val strategy = strategyFactory.getStrategy(
            checkinId.tipoEntrada,
            checkinId.versao
        )

        return strategy.convert(rawResponse, checkinId)
    }
}