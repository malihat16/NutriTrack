package com.fit2081.a1_maliha33473692.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.fit2081.a1_maliha33473692.data.dao.PatientDao
import com.fit2081.a1_maliha33473692.data.entity.PatientEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class PatientRepository(private val dao: PatientDao) {
    fun getAllUserIdsLive(): LiveData<List<Int>> =
        dao.getAllUserIds()
            .asLiveData(Dispatchers.IO)

    /** Observe a single patient as LiveData */
    fun getPatientByIdLive(userId: Int): LiveData<PatientEntity?> =
        dao.getPatientById(userId)
            .asLiveData(Dispatchers.IO)

    fun getAverageHeifaMale(): Flow<Double?>   = dao.getAverageHeifaMale()
    fun getAverageHeifaFemale(): Flow<Double?> = dao.getAverageHeifaFemale()


    suspend fun findByIdOnce(userId: Int): PatientEntity? =
        dao.findByIdOnce(userId)

    /** Called when a user “claims” their seeded account */
    suspend fun claimAccount(
        userId: Int,
        name: String,
        passwordHash: String
    ) {
        dao.updateNameAndPassword(userId, name, passwordHash)
    }

    fun getAverageVegetablesScore(): Flow<Double?> = dao.getAverageVegetablesScore()
    fun getAverageFruitScore(): Flow<Double?> = dao.getAverageFruitScore()
    fun getAverageFruitVariation(): Flow<Double?> = dao.getAverageFruitVariation()

}
