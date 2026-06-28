package com.fit2081.a1_maliha33473692.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fit2081.a1_maliha33473692.data.entity.NutriCoachTipEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NutriCoachTipDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTip(tip: NutriCoachTipEntity)

    @Query("""
    SELECT * FROM nutricoach_tips
    WHERE patientUserId = :userId
    ORDER BY timestamp DESC
  """)
    fun getTipsForPatient(userId: Int): Flow<List<NutriCoachTipEntity>>
}
