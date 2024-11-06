package com.example.listgamedata.network

import com.example.listgamedata.ui.model.GameDataResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("AdvertiseNewApplications/17/com.hd.camera.apps.high.quality")
    suspend fun getData() : Response<GameDataResponse>

}