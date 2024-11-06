package com.example.listgamedata.ui.model

data class GameDataResponse(
    val app_center: List<AppCenter>,
    val `data`: List<Data>,
    val home: List<Home>,
    val message: String,
    val more_apps: List<MoreApp>,
    val native_add: NativeAdd,
    val status: Int,
    val translator_ads_id: TranslatorAdsId
)