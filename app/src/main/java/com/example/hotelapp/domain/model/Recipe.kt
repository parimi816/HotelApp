package com.example.hotelapp.domain.model

import com.google.gson.annotations.SerializedName

data class Recipe(
    val calories: String,
    val carbos: String,
    val description: String,
    val difficulty: String,
    val fats: String,
    val headline: String,
    val id: String,
    val image: String,
    val name: String,
    val proteins: String,
    val thumb: String,
    val time: String,
)