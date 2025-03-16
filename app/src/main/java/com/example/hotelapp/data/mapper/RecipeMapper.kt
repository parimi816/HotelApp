package com.example.hotelapp.data.mapper

import com.example.hotelapp.data.remote.dto.RecipeDto
import com.example.hotelapp.domain.model.Recipe

fun RecipeDto.toDomain(): Recipe = Recipe(calories, carbos,description,difficulty,fats,headline,id,image,name,proteins,thumb,time)
fun Recipe.toDto(): RecipeDto = RecipeDto(calories, carbos,description,difficulty,fats,headline,id,image,name,proteins,thumb,time)
