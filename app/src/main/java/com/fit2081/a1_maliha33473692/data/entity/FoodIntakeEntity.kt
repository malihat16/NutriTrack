package com.fit2081.a1_maliha33473692.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "food_intake",
    foreignKeys = [
        ForeignKey(
            entity = PatientEntity::class,
            parentColumns = ["userId"],
            childColumns = ["patientUserId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [ Index("patientUserId") ]
)
data class FoodIntakeEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val patientUserId: Int,
    // questionnaire booleans
    val fruitsChecked: Boolean,
    val vegetablesChecked: Boolean,
    val grainsChecked: Boolean,
    val redMeatChecked: Boolean,
    val seafoodChecked: Boolean,
    val poultryChecked: Boolean,
    val eggsChecked: Boolean,
    val fishChecked: Boolean,
    val nutsSeedsChecked: Boolean,
    // persona & timings
    val selectedPersona: String,
    val biggestMealTime: String,
    val sleepTime: String,
    val wakeTime: String
)
