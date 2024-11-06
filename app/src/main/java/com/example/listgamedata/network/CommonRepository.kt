package com.example.listgamedata.network

class CommonRepository {
    suspend fun getGameData() = RetrofitClient.api.getData()
}