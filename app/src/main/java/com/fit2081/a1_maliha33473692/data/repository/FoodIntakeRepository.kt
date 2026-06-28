package com.fit2081.a1_maliha33473692.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.fit2081.a1_maliha33473692.data.dao.FoodIntakeDao
import com.fit2081.a1_maliha33473692.data.entity.FoodIntakeEntity
import kotlinx.coroutines.Dispatchers

class FoodIntakeRepository(private val dao: FoodIntakeDao) {
    /** Stream the saved questionnaire for a patient */
    fun getIntakeForPatientLive(userId: Int): LiveData<FoodIntakeEntity?> =
        dao.getIntakeForPatient(userId)
            .asLiveData(Dispatchers.IO)

    /** Insert or update the patient’s responses */
    suspend fun saveIntake(intake: FoodIntakeEntity) {
        dao.insertIntake(intake)
    }
}
