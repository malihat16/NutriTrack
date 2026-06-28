package com.fit2081.a1_maliha33473692.data.model

data class Nutrition(
    val calories: Int,
    val fat: Double,
    val sugar: Double,
    val carbohydrates: Double,
    val protein: Double
)

data class FruitInfoResponse(
    val name: String,
    val family: String,
    val genus: String,
    val order: String,
    val nutritions: Nutrition
)
