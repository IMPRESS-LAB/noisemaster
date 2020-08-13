package com.psplog.whereisraspberry.dto.device

data class DeviceDTO(
    val devices: List<Device>
) {
    data class Device(
        val createdTime: String,
        val decibel: Int,
        val device: String,
        val placeDTO: PlaceDTO,
        val temperature: String
    ) {
        data class PlaceDTO(
            val gridX: String,
            val gridY: String,
            val placeId: Int,
            val tag: String
        )
    }
}