package com.staroscky.transferencia.service.mapper

import com.staroscky.checkin.domain.TipoEntrada
import com.staroscky.checkin.domain.response.ChavePixV1Response
import com.staroscky.checkin.domain.response.ContaTransacionalV1Response
import com.staroscky.checkin.domain.response.QrCodePixV1Response
import com.staroscky.transferencia.client.dto.*
import com.staroscky.transferencia.domain.TransferenciaCache
import com.staroscky.transferencia.domain.TransferenciaContext
import org.springframework.stereotype.Component
import java.time.ZoneOffset
import java.util.UUID

/**
 * Mapper para converter CheckinResponse em requests do PAAS
 * 
 * Possui dois métodos:
 * - mapCreate: monta request completo para POST (criar)
 * - mapUpdate: monta request parcial para PATCH (atualizar apenas campos alterados)
 */
@Component
class CheckinToPaasMapper {
    
    companion object {
        // Dados de origem mockados (futuramente virão do token/cache)
        private val ID_CLIENTE_ORIGEM_MOCK = UUID.fromString("00000000-0000-0000-0000-000000000000")
        private val ID_CONTA_ORIGEM_MOCK = UUID.fromString("11111111-1111-1111-1111-111111111111")
        
        private val ORIGEM_MOCK = OrigemPaas(
            dadosCliente = DadosClientePaas(idCliente = ID_CLIENTE_ORIGEM_MOCK),
            dadosConta = DadosContaPaas(
                idConta = ID_CONTA_ORIGEM_MOCK,
                tipoConta = "CACC",
                agencia = "0001",
                conta = "123456",
                ispb = "00000000",
                nomeBanco = "Banco Mock"
            )
        )
    }
    
    /**
     * Mapeia para request de criação (POST) completo
     */
    fun mapCreate(context: TransferenciaContext): PaasCreateRequest {
        val checkinResponse = context.dadosCheckin
            ?: throw IllegalStateException("dadosCheckin deve estar populado")
        
        val checkinId = context.checkinIdParsed
            ?: throw IllegalStateException("checkinIdParsed deve estar populado")
        
        val destino = when (checkinId.tipoEntrada) {
            TipoEntrada.CHAVE_PIX -> mapDestinoChavePix(context, checkinResponse as ChavePixV1Response)
            TipoEntrada.QRCODE_PIX -> mapDestinoQRCodePix(context, checkinResponse as QrCodePixV1Response)
            TipoEntrada.MANUAL -> mapDestinoManual(context, checkinResponse as ContaTransacionalV1Response)
        }
        
        return PaasCreateRequest(
            comandosJornadas = ComandosJornadas(
                origem = ORIGEM_MOCK,
                destinos = listOf(destino)
            )
        )
    }
    
    /**
     * Mapeia para request de atualização (PATCH) parcial
     * Envia APENAS os campos que mudaram
     */
    fun mapUpdate(context: TransferenciaContext, cacheAtual: TransferenciaCache): PaasUpdateRequest {
        val transferencia = mutableMapOf<String, Any>()
        
        // Verifica o que mudou
        if (context.valorTransferencia != cacheAtual.valorTransferencia) {
            transferencia["valor"] = context.valorTransferencia
        }
        
        if (context.dataTransferencia != cacheAtual.dataTransferencia) {
            transferencia["dataTransferencia"] = context.dataTransferencia
                .atStartOfDay()
                .atOffset(ZoneOffset.UTC)
        }
        
        return PaasUpdateRequest(
            comandosJornadas = ComandosJornadasUpdate(
                destinos = listOf(
                    DestinoUpdatePaas(
                        transferencia = TransferenciaUpdatePaas(
                            valor = transferencia["valor"] as? java.math.BigDecimal,
                            dataTransferencia = transferencia["dataTransferencia"] as? java.time.OffsetDateTime
                        )
                    )
                )
            )
        )
    }
    
    private fun mapDestinoChavePix(
        context: TransferenciaContext,
        response: ChavePixV1Response
    ): DestinoPaas {
        return DestinoPaas(
            transferencia = TransferenciaPaas(
                valor = context.valorTransferencia,
                dataTransferencia = context.dataTransferencia.atStartOfDay().atOffset(ZoneOffset.UTC),
                descricao = "Transferência via CheckinId",
                endToEndId = response.endToEndId,
                idTransferencia = null,
                dadosPix = DadosPixPaas(
                    tipoChave = response.tipoChave.name,
                    chave = response.chave
                )
            ),
            dadosCliente = DadosClientePaas(idCliente = response.titular.idCliente),
            dadosConta = DadosContaPaas(
                idConta = response.conta.idConta,
                tipoConta = response.conta.tipoConta.name,
                agencia = response.conta.agencia,
                conta = response.conta.numero,
                ispb = response.conta.ispb,
                nomeBanco = response.conta.nomeBanco
            )
        )
    }
    
    private fun mapDestinoQRCodePix(
        context: TransferenciaContext,
        response: QrCodePixV1Response
    ): DestinoPaas {
        val (tipoQRCode, troco) = when (response) {
            is QrCodePixV1Response.Imediato -> {
                "ESTATICO" to null
            }
            is QrCodePixV1Response.ComVencimento -> {
                "DINAMICO_COBV" to null
            }
        }
        
        // QRCode não tem dados completos do destino, usar dados básicos
        return DestinoPaas(
            transferencia = TransferenciaPaas(
                valor = context.valorTransferencia,
                dataTransferencia = context.dataTransferencia.atStartOfDay().atOffset(ZoneOffset.UTC),
                descricao = "Transferência via CheckinId",
                endToEndId = null,
                idTransferencia = null,
                dadosPix = DadosPixPaas(
                    tipoChave = null,
                    chave = response.chave.value,
                    tipoQRCode = tipoQRCode,
                    idQRCode = response.txId.value,
                    troco = troco
                )
            ),
            dadosCliente = DadosClientePaas(idCliente = UUID.randomUUID()),  // Mock
            dadosConta = DadosContaPaas(
                idConta = UUID.randomUUID(),  // Mock
                tipoConta = "CACC",
                agencia = "0000",
                conta = "000000",
                ispb = "00000000",
                nomeBanco = "Desconhecido"
            )
        )
    }
    
    private fun mapDestinoManual(
        context: TransferenciaContext,
        response: ContaTransacionalV1Response
    ): DestinoPaas {
        return DestinoPaas(
            transferencia = TransferenciaPaas(
                valor = context.valorTransferencia,
                dataTransferencia = context.dataTransferencia.atStartOfDay().atOffset(ZoneOffset.UTC),
                descricao = "Transferência via CheckinId",
                endToEndId = null,
                idTransferencia = null,
                dadosPix = null
            ),
            dadosCliente = DadosClientePaas(idCliente = UUID.randomUUID()),  // Mock - não tem no response
            dadosConta = DadosContaPaas(
                idConta = UUID.randomUUID(),  // Mock - não tem no response
                tipoConta = response.dadosBancarios.tipoConta,
                agencia = response.dadosBancarios.agencia,
                conta = response.dadosBancarios.conta,
                ispb = response.dadosBancarios.ispb,
                nomeBanco = "Banco Destino"  // Mock - não tem no response
            )
        )
    }
}
