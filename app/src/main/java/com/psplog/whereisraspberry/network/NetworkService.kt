package com.psplog.whereisraspberry.network

import com.psplog.whereisraspberry.dto.device.DeviceDTO
import com.psplog.whereisraspberry.dto.device.NoiseDTO
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NetworkService {
    @GET("/api/v1/noise")
    fun getNoiseListResponse(
        @Header("Content-Type") content_type: String,
        @Query("device") device: String?=null,
        @Query("decibel") decibel: String?=null,
        @Query("temperature") temperature: String?=null,
        @Query("tag") tag: String?=null,
        @Query("page") page: String?=null,
        @Query("size") size: Int?=50
    ): Call<NoiseDTO>

    @GET("/api/v1/noise/recent")
    fun getDeviceListResponse(
        @Header("Content-Type") content_type: String
    ): Call<DeviceDTO>

}



