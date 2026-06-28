package com.fit2081.a1_maliha33473692.utils

import android.app.TimePickerDialog
import android.content.Context
import androidx.annotation.DrawableRes
import com.fit2081.a1_maliha33473692.R
import com.fit2081.a1_maliha33473692.data.entity.PatientEntity
import java.util.*

data class PatientData(
    val phoneNumber: String,
    val userId: Int,
    val sex: String,
    val totalScoreMale: Double,
    val totalScoreFemale: Double,
    // below scores out of 10
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
    // Scores out of 5
    val alcoholScoreMale: Double,
    val alcoholScoreFemale: Double,
    val waterScoreMale: Double,
    val waterScoreFemale: Double,
    // Additional scores out of 10
    val sugarScoreMale: Double,
    val sugarScoreFemale: Double,
    val saturatedFatScoreMale: Double,
    val saturatedFatScoreFemale: Double,
    val unsaturatedFatScoreMale: Double,
    val unsaturatedFatScoreFemale: Double,
    val fruitServeSize: Double,
    val fruitVariationScore: Double
)

data class PersonaInfo(
    val name: String,
    val description: String,
    @DrawableRes val imageRes: Int
)

data class CategoryScore(
    val name: String,
    val fraction: Float,     // for the progress bar
    val rawScore: Double,   // actual double from CSV
    val maxScore: Double
)


val personaList = listOf(
    PersonaInfo(
        name = "Health Devotee",
        description = "I’m passionate about healthy eating & health plays a big part in my life. I use social media to follow active lifestyle personalities or get new recipes/exercise ideas. I may even buy superfoods or follow a particular type of diet. I like to think I am super healthy.",
        imageRes = R.drawable.persona_1
    ),
    PersonaInfo(
        name = "Mindful Eater",
        description = "I’m health-conscious and being healthy and eating healthy is important to me. Although health means different things to different people, I make conscious lifestyle decisions about eating based on what I believe healthy means. I look for new recipes and healthy eating information on social media.",
        imageRes = R.drawable.persona_2
    ),
    PersonaInfo(
        name = "Wellness Striver",
        description = "I aspire to be healthy (but struggle sometimes). Healthy eating is hard work! I’ve tried to improve my diet, but always find things that make it difficult to stick with the changes. Sometimes I notice recipe ideas or healthy eating hacks, and if it seems easy enough, I’ll give it a go.",
        imageRes = R.drawable.persona_3
    ),
    PersonaInfo(
        name = "Balance Seeker",
        description = "I try and live a balanced lifestyle, and I think that all foods are okay in moderation. I shouldn’t have to feel guilty about eating a piece of cake now and again. I get all sorts of inspiration from social media like finding out about new restaurants, fun recipes and sometimes healthy eating tips.",
        imageRes = R.drawable.persona_4
    ),
    PersonaInfo(
        name = "Health Procrastinator",
        description = "I’m contemplating healthy eating but it’s not a priority for me right now. I know the basics about what it means to be healthy, but it doesn’t seem relevant to me right now. I have taken a few steps to be healthier but I am not motivated to make it a high priority because I have too many other things going on in my life.",
        imageRes = R.drawable.persona_5
    ),
    PersonaInfo(
        name = "Food Carefree",
        description = "I’m not bothered about healthy eating. I don’t really see the point and I don’t think about it. I don’t really notice healthy eating tips or recipes and I don’t care what I eat.",
        imageRes = R.drawable.persona_6
    )
)

// CSV Data Reader
fun readCsvData(
    context: Context,
    fileName: String = "patients.csv"
): List<PatientData> {
    return context.assets
        .open(fileName)
        .bufferedReader()
        .useLines { lines ->
            lines
                .drop(1) // skip header
                .mapNotNull { line ->
                    val cols = line.split(",")
                    // must have at least 62 columns (0..61)
                    if (cols.size < 62) return@mapNotNull null

                    // parse each field, defaulting to 0.0 on bad data
                    val userId = cols[1].toIntOrNull() ?: return@mapNotNull null
                    PatientData(
                        userId                   = userId,
                        phoneNumber              = cols[0].trim(),
                        sex                      = cols[2].trim(),
                        totalScoreMale           = cols[3].toDoubleOrNull() ?: 0.0,
                        totalScoreFemale         = cols[4].toDoubleOrNull() ?: 0.0,

                        // Scores out of 10:
                        discretionaryScoreMale   = cols[5].toDoubleOrNull() ?: 0.0,
                        discretionaryScoreFemale = cols[6].toDoubleOrNull() ?: 0.0,

                        vegetablesScoreMale      = cols[8].toDoubleOrNull() ?: 0.0,
                        vegetablesScoreFemale    = cols[9].toDoubleOrNull() ?: 0.0,

                        fruitScoreMale           = cols[19].toDoubleOrNull() ?: 0.0,
                        fruitScoreFemale         = cols[20].toDoubleOrNull() ?: 0.0,

                        grainsScoreMale          = cols[29].toDoubleOrNull() ?: 0.0,
                        grainsScoreFemale        = cols[30].toDoubleOrNull() ?: 0.0,

                        wholeGrainsScoreMale     = cols[33].toDoubleOrNull() ?: 0.0,
                        wholeGrainsScoreFemale   = cols[34].toDoubleOrNull() ?: 0.0,

                        meatScoreMale            = cols[36].toDoubleOrNull() ?: 0.0,
                        meatScoreFemale          = cols[37].toDoubleOrNull() ?: 0.0,

                        dairyScoreMale           = cols[40].toDoubleOrNull() ?: 0.0,
                        dairyScoreFemale         = cols[41].toDoubleOrNull() ?: 0.0,

                        sodiumScoreMale          = cols[43].toDoubleOrNull() ?: 0.0,
                        sodiumScoreFemale        = cols[44].toDoubleOrNull() ?: 0.0,

                        sugarScoreMale           = cols[54].toDoubleOrNull() ?: 0.0,
                        sugarScoreFemale         = cols[55].toDoubleOrNull() ?: 0.0,

                        saturatedFatScoreMale    = cols[57].toDoubleOrNull() ?: 0.0,
                        saturatedFatScoreFemale  = cols[58].toDoubleOrNull() ?: 0.0,

                        unsaturatedFatScoreMale  = cols[60].toDoubleOrNull() ?: 0.0,
                        unsaturatedFatScoreFemale= cols[61].toDoubleOrNull() ?: 0.0,

                        // Scores out of 5:
                        alcoholScoreMale         = cols[46].toDoubleOrNull() ?: 0.0,
                        alcoholScoreFemale       = cols[47].toDoubleOrNull() ?: 0.0,
                        waterScoreMale           = cols[49].toDoubleOrNull() ?: 0.0,
                        waterScoreFemale         = cols[50].toDoubleOrNull() ?: 0.0,

                        fruitServeSize           = cols[21].toDoubleOrNull() ?: 0.0,
                        fruitVariationScore      = cols[22].toDoubleOrNull() ?: 0.0
                    )
                }
                .toList()
        }
}


// Time Picker

fun showTimePicker(
    context: Context,
    onTimeSelected: (String) -> Unit
) {
    val now = Calendar.getInstance()
    TimePickerDialog(
        context,
        { _, selectedHour, selectedMinute ->
            // <-- NOW we keep 24h formatting:
            val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            onTimeSelected(formattedTime)
        },
        now.get(Calendar.HOUR_OF_DAY),
        now.get(Calendar.MINUTE),
        true    // 24-hour view
    ).show()
}

// Computes Discretionary Foods score (out of 10)
fun computeDiscretionaryScore(patient: PatientEntity): CategoryScore {
    val rawScore = if (patient.sex.equals("Male", ignoreCase = true))
        patient.discretionaryScoreMale
    else
        patient.discretionaryScoreFemale
    val fraction = (rawScore / 10.0).coerceIn(0.0, 1.0).toFloat()
    return CategoryScore("Discretionary", fraction, rawScore, 10.0)
}

// Computes Vegetables score (out of 10)
fun computeVegetablesScore(patient: PatientEntity): CategoryScore {
    val rawScore = if (patient.sex.equals("Male", ignoreCase = true))
        patient.vegetablesScoreMale
    else
        patient.vegetablesScoreFemale
    val fraction = (rawScore / 10.0).coerceIn(0.0, 1.0).toFloat()
    return CategoryScore("Vegetables", fraction, rawScore, 10.0)
}

// Computes Fruits score (out of 10)
fun computeFruitsScore(patient: PatientEntity): CategoryScore {
    val rawScore = if (patient.sex.equals("Male", ignoreCase = true))
        patient.fruitScoreMale
    else
        patient.fruitScoreFemale
    val fraction = (rawScore / 10.0).coerceIn(0.0, 1.0).toFloat()
    return CategoryScore("Fruits", fraction, rawScore, 10.0)
}

// Computes Grains & Cereals score (out of 10)
fun computeGrainsScore(patient: PatientEntity): CategoryScore {
    val rawScore = if (patient.sex.equals("Male", ignoreCase = true))
        patient.grainsScoreMale
    else
        patient.grainsScoreFemale
    val fraction = (rawScore / 10.0).coerceIn(0.0, 1.0).toFloat()
    return CategoryScore("Grains & Cereals", fraction, rawScore, 5.0)
}

// Computes Whole Grains score (out of 10)
fun computeWholeGrainsScore(patient: PatientEntity): CategoryScore {
    val rawScore = if (patient.sex.equals("Male", ignoreCase = true))
        patient.wholeGrainsScoreMale
    else
        patient.wholeGrainsScoreFemale
    val fraction = (rawScore / 10.0).coerceIn(0.0, 1.0).toFloat()
    return CategoryScore("Whole Grains", fraction, rawScore, 5.0)
}

// Computes Meat & Alternatives score (out of 10)
fun computeMeatScore(patient: PatientEntity): CategoryScore {
    val rawScore = if (patient.sex.equals("Male", ignoreCase = true))
        patient.meatScoreMale
    else
        patient.meatScoreFemale
    val fraction = (rawScore / 10.0).coerceIn(0.0, 1.0).toFloat()
    return CategoryScore("Meat & Alternatives", fraction, rawScore, 10.0)
}

// Computes Dairy score (out of 10)
fun computeDairyScore(patient: PatientEntity): CategoryScore {
    val rawScore = if (patient.sex.equals("Male", ignoreCase = true))
        patient.dairyScoreMale
    else
        patient.dairyScoreFemale
    val fraction = (rawScore / 10.0).coerceIn(0.0, 1.0).toFloat()
    return CategoryScore("Dairy", fraction, rawScore, 10.0)
}

// Computes Sodium score (out of 10)
fun computeSodiumScore(patient: PatientEntity): CategoryScore {
    val rawScore = if (patient.sex.equals("Male", ignoreCase = true))
        patient.sodiumScoreMale
    else
        patient.sodiumScoreFemale
    val fraction = (rawScore / 10.0).coerceIn(0.0, 1.0).toFloat()
    return CategoryScore("Sodium", fraction, rawScore, 10.0)
}

// Computes Sugar score (out of 10)
fun computeSugarScore(patient: PatientEntity): CategoryScore {
    val rawScore = if (patient.sex.equals("Male", ignoreCase = true))
        patient.sugarScoreMale
    else
        patient.sugarScoreFemale
    val fraction = (rawScore / 10.0).coerceIn(0.0, 1.0).toFloat()
    return CategoryScore("Sugar", fraction, rawScore, 10.0)
}

// Computes Saturated Fat score (out of 10)
fun computeSaturatedFatScore(patient: PatientEntity): CategoryScore {
    val rawScore = if (patient.sex.equals("Male", ignoreCase = true))
        patient.saturatedFatScoreMale
    else
        patient.saturatedFatScoreFemale
    val fraction = (rawScore / 10.0).coerceIn(0.0, 1.0).toFloat()
    return CategoryScore("Saturated Fat", fraction, rawScore, 5.0)
}

// Computes Unsaturated Fat score (out of 10)
fun computeUnsaturatedFatScore(patient: PatientEntity): CategoryScore {
    val rawScore = if (patient.sex.equals("Male", ignoreCase = true))
        patient.unsaturatedFatScoreMale
    else
        patient.unsaturatedFatScoreFemale
    val fraction = (rawScore / 10.0).coerceIn(0.0, 1.0).toFloat()
    return CategoryScore("Unsaturated Fat", fraction, rawScore, 5.0)
}

// Computes Water score (out of 5)
fun computeWaterScore(patient: PatientEntity): CategoryScore {
    val rawScore = if (patient.sex.equals("Male", ignoreCase = true))
        patient.waterScoreMale
    else
        patient.waterScoreFemale
    val fraction = (rawScore / 5.0).coerceIn(0.0, 1.0).toFloat()
    return CategoryScore("Water", fraction, rawScore, 5.0)
}

// Computes Alcohol score (out of 5)
fun computeAlcoholScore(patient: PatientEntity): CategoryScore {
    val rawScore = if (patient.sex.equals("Male", ignoreCase = true))
        patient.alcoholScoreMale
    else
        patient.alcoholScoreFemale
    val fraction = (rawScore / 5.0).coerceIn(0.0, 1.0).toFloat()
    return CategoryScore("Alcohol", fraction, rawScore, 5.0)
}
