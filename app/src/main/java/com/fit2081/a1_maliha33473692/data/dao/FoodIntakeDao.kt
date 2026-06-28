package com.fit2081.a1_maliha33473692.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fit2081.a1_maliha33473692.data.entity.FoodIntakeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodIntakeDao {
    @Query("SELECT * FROM food_intake WHERE patientUserId = :userId")
    fun getIntakeForPatient(userId: Int): Flow<FoodIntakeEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIntake(entry: FoodIntakeEntity)
}
