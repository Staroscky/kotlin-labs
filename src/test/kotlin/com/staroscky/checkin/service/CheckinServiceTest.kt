package com.staroscky.checkin.service

import com.staroscky.checkin.client.CheckinFeignClient
import com.staroscky.checkin.domain.TipoEntrada
import com.staroscky.checkin.domain.response.ChavePixV1Response
import com.staroscky.checkin.service.strategy.CheckinResponseStrategyFactory
import com.staroscky.checkin.service.strategy.impl.ChavePixV1Strategy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.util.UUID
import kotlin.test.assertEquals

class CheckinServiceTest {

    private var feignClient: CheckinFeignClient = mock()
    private var strategyFactory: CheckinResponseStrategyFactory = mock()
    private lateinit var checkinService: CheckinService

    @BeforeEach
    fun setup() {
        checkinService = CheckinService(feignClient, strategyFactory)
    }


    @Test
    fun `deve retornar ChavePixV1Response quando tipo CHAVE_PIX versao 1`() {
        // Given
        val id = "123:CHAVE_PIX:1:${UUID.randomUUID()}"
        val rawResponse = mapOf(
            "checkinId" to "123",
            "uuid" to "uuid-test",
            "chavePix" to "chave@teste.com",
            "banco" to "001"
        )
        val strategy = mock<ChavePixV1Strategy>()
        val expectedResponse = mock<ChavePixV1Response>()

        Mockito.`when`(feignClient.getCheckin(id)).thenReturn(rawResponse)
        Mockito.`when`(strategyFactory.getStrategy(TipoEntrada.CHAVE_PIX, 1)).thenReturn(strategy)
        Mockito.`when`(strategy.convert(rawResponse, any())).thenReturn(expectedResponse)

        val result = checkinService.getCheckin(id)

        // Then
        //assertEquals(expectedResponse, result) // TODO: Ajustar posteriormente para comparar campos específicos
        verify(feignClient).getCheckin(id)
        verify(strategy).convert(eq(rawResponse), any())
    }

    @Test
    fun `deve lancar excecao quando strategy nao encontrada`() {
        // Given
        val id = "123:CHAVE_PIX:99:${UUID.randomUUID()}"

        Mockito.`when`(feignClient.getCheckin(id)).thenReturn(emptyMap())
        Mockito.`when`(strategyFactory.getStrategy(TipoEntrada.CHAVE_PIX, 99) )
            .thenThrow(IllegalStateException("Strategy não encontrada"))

        val exception = assertThrows<IllegalStateException> {
            checkinService.getCheckin(id)
        }
        assertEquals("Strategy não encontrada", exception.message)
    }
}