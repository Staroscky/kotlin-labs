package com.staroscky.checkin.domain

@JvmInline
value class CheckinId(val value: String) {
    init {
        require(value.split(":").size == 4) {
            "CheckinId deve estar no formato {checkinId:entrada:versao:uuid}"
        }
    }

    val checkinId: String get() = value.split(":")[0]
    val tipoEntrada: TipoEntrada get() = TipoEntrada.valueOf(value.split(":")[1])
    val versao: Int get() = value.split(":")[2].toInt()
    val uuid: String get() = value.split(":")[3]
}

