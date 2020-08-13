package com.psplog.whereisraspberry.dto.device

data class NoiseDTO(
    val currentPageNumber: Int,
    val data: Data,
    val first: Boolean,
    val last: Boolean,
    val totalPageNumber: Int
) {
    data class Data(
        val noises: List<Noise>
    ) {
        data class Noise(
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
}