package com.fit2081.a1_maliha33473692.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "nutricoach_tips",
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

data class NutriCoachTipEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val patientUserId: Int,
    val tip: String,
    val timestamp: Long = System.currentTimeMillis()
)
