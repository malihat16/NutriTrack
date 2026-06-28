package com.fit2081.a1_maliha33473692.data.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.fit2081.a1_maliha33473692.data.dao.FoodIntakeDao
import com.fit2081.a1_maliha33473692.data.dao.NutriCoachTipDao
import com.fit2081.a1_maliha33473692.data.dao.PatientDao
import com.fit2081.a1_maliha33473692.data.entity.FoodIntakeEntity
import com.fit2081.a1_maliha33473692.data.entity.NutriCoachTipEntity
import com.fit2081.a1_maliha33473692.data.entity.PatientEntity
import com.fit2081.a1_maliha33473692.utils.readCsvData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        PatientEntity::class,
        FoodIntakeEntity::class,
        NutriCoachTipEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class NutriTrackDatabase : RoomDatabase() {
    abstract fun patientDao(): PatientDao
    abstract fun foodIntakeDao(): FoodIntakeDao
    abstract fun nutriCoachTipDao(): NutriCoachTipDao

    companion object {
        @Volatile private var INSTANCE: NutriTrackDatabase? = null

        fun getInstance(ctx: Context): NutriTrackDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    ctx.applicationContext,
                    NutriTrackDatabase::class.java,
                    "nutritrack_db"
                )

                    .addCallback(SeedCallback(ctx))
                    .build()
                    .also { INSTANCE = it }
            }
    }


    private class SeedCallback(val ctx: Context) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            Log.d("NutriTrackDB", ">>> onCreate seed callback firing!")

            // this coroutine will launch only once, DB file is created
            CoroutineScope(Dispatchers.IO).launch {
                // readCsvData opens “assets/patients.csv”
                val patientsFromCsv = readCsvData(ctx)
                // gets every row into Room table
                Log.d("NutriTrackDB", ">>> readCsvData returned ${patientsFromCsv.size} rows")

                getInstance(ctx).patientDao().insertAll(
                    patientsFromCsv.map { dto ->
                        PatientEntity(
                            userId               = dto.userId,
                            phoneNumber          = dto.phoneNumber,
                            sex                  = dto.sex,
                            totalScoreMale       = dto.totalScoreMale,
                            totalScoreFemale     = dto.totalScoreFemale,
                            discretionaryScoreMale   = dto.discretionaryScoreMale,
                            discretionaryScoreFemale = dto.discretionaryScoreFemale,
                            vegetablesScoreMale      = dto.vegetablesScoreMale,
                            vegetablesScoreFemale    = dto.vegetablesScoreFemale,

                            fruitScoreMale             = dto.fruitScoreMale,
                            fruitScoreFemale           = dto.fruitScoreFemale,
                            fruitServeSize           = dto.fruitServeSize,
                            fruitVariationScore      = dto.fruitVariationScore,
                            grainsScoreMale            = dto.grainsScoreMale,
                            grainsScoreFemale          = dto.grainsScoreFemale,
                            wholeGrainsScoreMale       = dto.wholeGrainsScoreMale,
                            wholeGrainsScoreFemale     = dto.wholeGrainsScoreFemale,
                            meatScoreMale              = dto.meatScoreMale,
                            meatScoreFemale            = dto.meatScoreFemale,
                            dairyScoreMale             = dto.dairyScoreMale,
                            dairyScoreFemale           = dto.dairyScoreFemale,
                            sodiumScoreMale            = dto.sodiumScoreMale,
                            sodiumScoreFemale          = dto.sodiumScoreFemale,
                            sugarScoreMale             = dto.sugarScoreMale,
                            sugarScoreFemale           = dto.sugarScoreFemale,
                            saturatedFatScoreMale      = dto.saturatedFatScoreMale,
                            saturatedFatScoreFemale    = dto.saturatedFatScoreFemale,
                            unsaturatedFatScoreMale    = dto.unsaturatedFatScoreMale,
                            unsaturatedFatScoreFemale  = dto.unsaturatedFatScoreFemale,
                            alcoholScoreMale           = dto.alcoholScoreMale,
                            alcoholScoreFemale         = dto.alcoholScoreFemale,
                            waterScoreMale             = dto.waterScoreMale,
                            waterScoreFemale           = dto.waterScoreFemale,
                        )
                    }
                )
            }
        }
    }
}
