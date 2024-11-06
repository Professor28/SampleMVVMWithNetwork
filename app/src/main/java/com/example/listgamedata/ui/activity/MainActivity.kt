package com.example.listgamedata.ui.activity

import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.listgamedata.R
import com.example.listgamedata.common.ResponseHandler
import com.example.listgamedata.databinding.ActivityMainBinding
import com.example.listgamedata.ui.adapter.GameListAdapter
import com.example.listgamedata.ui.model.GameDataResponse
import com.example.listgamedata.ui.model.SubCategory
import com.example.listgamedata.ui.viewmodel.MainViewModel
import com.google.gson.Gson


class MainActivity : AppCompatActivity(), GameListAdapter.ClickListener {

    private lateinit var binding : ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var gameDataResponse :  GameDataResponse
    private lateinit var gameListAdapter: GameListAdapter
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        init()
        initListener()
        getGameListData()
    }

    private fun init() {
        sharedPreferences = getSharedPreferences(packageName, MODE_PRIVATE)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        gameListAdapter = GameListAdapter(this, this)
        binding.rvGameList.adapter = gameListAdapter
    }

    private fun initListener() {
        mainViewModel.gameListLiveData.observe(this) { response ->

            when(response) {
                is ResponseHandler.Loading -> {
                    Toast.makeText(this, getString(R.string.strLoading), Toast.LENGTH_LONG).show()
                }

                is ResponseHandler.Success -> {
                    Toast.makeText(this, getString(R.string.strSuccess), Toast.LENGTH_LONG).show()
                    response.value?.let {
                        gameDataResponse = response.value
                        setGameData(gameDataResponse)
                    }
                }

                is ResponseHandler.Fail -> {
                    Toast.makeText(this, response.msg, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun getGameListData() {
        if (!isConnected()) {
            Toast.makeText(this, getString(R.string.strNoInternet), Toast.LENGTH_SHORT).show()
            val gameDataResponse = getGameDataFromSharedPref()
            if (gameDataResponse != null) {
                this.gameDataResponse = gameDataResponse
                setGameData(gameDataResponse)
            } else {
                Toast.makeText(this, getString(R.string.strGameDataListNotAvailable), Toast.LENGTH_SHORT).show()
            }
            return
        }
        mainViewModel.getGameListData()

    }

    private fun setGameData(gameDataResponse: GameDataResponse) {
        saveToSharedPref(gameDataResponse)
        val popularGames = gameDataResponse.app_center.find { it.name == getString(R.string.strPOPULARGAMES) }

        if (popularGames != null && popularGames.sub_category.isNotEmpty()) {
            gameListAdapter.addGameList(popularGames.sub_category as ArrayList<SubCategory>)
        } else {
            Toast.makeText(this, "Popular game list is empty", Toast.LENGTH_LONG).show()
        }
    }

    private fun saveToSharedPref(gameDataResponse: GameDataResponse) {
        val editor = sharedPreferences.edit()
        editor.putString("gameData", Gson().toJson(gameDataResponse))
        editor.apply()
    }

    private fun getGameDataFromSharedPref() : GameDataResponse? {
        if (sharedPreferences.contains("gameData") && sharedPreferences.getString("gameData", "").toString().isNotEmpty()) {
            val stringValue = sharedPreferences.getString("gameData", "")
            return Gson().fromJson(stringValue, GameDataResponse::class.java)
        } else {
            return null
        }
    }

    private fun isConnected() : Boolean {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val active = connectivityManager.activeNetwork ?: return false
        val capability = connectivityManager.getNetworkCapabilities(active) ?: return false

        return when {
            capability.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capability.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }

    override fun onClickDownload(subCategory: SubCategory) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW, Uri.parse(
                    "https://play.google.com/store/apps/details?id=${subCategory.app_link}"
                )
            )
        )
    }
}