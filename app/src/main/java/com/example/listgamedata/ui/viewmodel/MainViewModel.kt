package com.example.listgamedata.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listgamedata.common.ResponseHandler
import com.example.listgamedata.network.CommonRepository
import com.example.listgamedata.ui.model.GameDataResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    val gameListLiveData : MutableLiveData<ResponseHandler<GameDataResponse>> = MutableLiveData()
    private val commonRepository = CommonRepository()

    fun getGameListData() = viewModelScope.launch {
        gameListLiveData.postValue(ResponseHandler.Loading())
        try {

            val gameResponse = commonRepository.getGameData()
            Log.e("ViewModel", Gson().toJson(gameResponse.body()))

            if (gameResponse.isSuccessful) {

                if (gameResponse.body() != null) {
                    gameListLiveData.postValue(ResponseHandler.Success(gameResponse.body()))
                } else {
                    gameListLiveData.postValue(ResponseHandler.Fail("Something Went Wrong"))
                }


            } else {
                gameListLiveData.postValue(ResponseHandler.Fail(gameResponse.message()))
            }

        } catch (e: Exception) {
            gameListLiveData.postValue(ResponseHandler.Fail(e.message.toString()))
        }
    }
}