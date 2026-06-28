package com.fit2081.a1_maliha33473692.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fit2081.a1_maliha33473692.data.entity.PatientEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientDao {
    @Query("SELECT userId FROM patient")
    fun getAllUserIds(): Flow<List<Int>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(patients: List<PatientEntity>)

    @Query("SELECT * FROM patient WHERE userId = :id")
    fun getPatientById(id: Int): Flow<PatientEntity?>

    @Query("SELECT * FROM patient WHERE userId = :id")
    suspend fun findByIdOnce(id: Int): PatientEntity?

    @Query("UPDATE patient SET name = :name, passwordHash = :passwordHash WHERE userId = :userId")
    suspend fun updateNameAndPassword(userId: Int, name: String, passwordHash: String)

    @Query("SELECT AVG(vegetablesScoreMale) FROM patient")
    fun getAverageVegetablesScore(): Flow<Double?>

    @Query("SELECT AVG(fruitVariationScore) FROM patient")
    fun getAverageFruitVariation(): Flow<Double?>

    @Query("SELECT AVG(totalscoreMale) FROM patient")
    fun getAverageHeifaMale(): Flow<Double?>

    // average HEIFA score for female users
    @Query("SELECT AVG(totalscoreFemale) FROM patient")
    fun getAverageHeifaFemale(): Flow<Double?>

    @Query("SELECT AVG(fruitScoreMale) FROM patient")
    fun getAverageFruitScore(): Flow<Double?>


}
