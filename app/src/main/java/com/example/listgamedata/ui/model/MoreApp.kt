package com.example.listgamedata.ui.model

data class MoreApp(
    val catgeory: String,
    val id: Int,
    val is_active: Int,
    val name: String,
    val position: Int,
    val sub_category: List<SubCategoryXX>
)