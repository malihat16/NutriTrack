package com.fit2081.a1_maliha33473692.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "patient")
data class PatientEntity(
    @PrimaryKey val userId: Int,
    val phoneNumber: String,
    val name: String = "",          // default empty
    val passwordHash: String = "",     // default empty
    val sex: String,
    // total HEIFA score
    val totalScoreMale: Double,
    val totalScoreFemale: Double,
    // 13 category scores (male / female)
    val discretionaryScoreMale: Double,
    val discretionaryScoreFemale: Double,
    val vegetablesScoreMale: Double,
    val vegetablesScoreFemale: Double,
    val fruitScoreMale: Double,
    val fruitScoreFemale: Double,
    val grainsScoreMale: Double,
    val grainsScoreFemale: Double,
    val wholeGrainsScoreMale: Double,
    val wholeGrainsScoreFemale: Double,
    val meatScoreMale: Double,
    val meatScoreFemale: Double,
    val dairyScoreMale: Double,
    val dairyScoreFemale: Double,
    val sodiumScoreMale: Double,
    val sodiumScoreFemale: Double,
    val sugarScoreMale: Double,
    val sugarScoreFemale: Double,
    val saturatedFatScoreMale: Double,
    val saturatedFatScoreFemale: Double,
    val unsaturatedFatScoreMale: Double,
    val unsaturatedFatScoreFemale: Double,
    val alcoholScoreMale: Double,
    val alcoholScoreFemale: Double,
    val waterScoreMale: Double,
    val waterScoreFemale: Double,
    val fruitVariationScore: Double,
    val fruitServeSize: Double
)
