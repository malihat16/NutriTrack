package com.fit2081.a1_maliha33473692.data.repository

import androidx.lifecycle.asLiveData
import com.fit2081.a1_maliha33473692.data.api.FruityViceApi
import com.fit2081.a1_maliha33473692.data.dao.NutriCoachTipDao
import com.fit2081.a1_maliha33473692.data.entity.NutriCoachTipEntity
import com.fit2081.a1_maliha33473692.data.model.FruitInfoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import com.fit2081.a1_maliha33473692.BuildConfig
import java.io.IOException

class NutriCoachRepository(
    private val dao: NutriCoachTipDao,
    private val fruityViceApi: FruityViceApi

) {
    private val http   = OkHttpClient()
    private val apiKey = BuildConfig.GEMINI_API_KEY


    /** Stream all saved tips for a patient */
    fun getTipsForPatientLive(userId: Int) =
        dao.getTipsForPatient(userId).asLiveData(Dispatchers.IO)

    /** Fetch nutrition details for a fruit name */
    suspend fun fetchFruitInfo(fruitName: String): FruitInfoResponse =
        fruityViceApi.getFruitInfo(fruitName.lowercase())

    /** Insert a new tip if it’s not a duplicate of the latest one */
    suspend fun saveTipIfNew(patientUserId: Int, tip: String) = withContext(Dispatchers.IO) {
        val existing = dao.getTipsForPatient(patientUserId)
            .map { it.firstOrNull()?.tip }
            .firstOrNull()
        if (existing != tip) {
            dao.insertTip(NutriCoachTipEntity(patientUserId = patientUserId, tip = tip))
        }
    }

//    Calls Gemini with a custom prompt.
    suspend fun generateTip(prompt: String): String = withContext(Dispatchers.IO) {
        // wrap prompt in the expected JSON shape
        val escaped = JSONObject.quote(prompt)
        val jsonBody = """
          {
            "contents": [
              { "parts":[{ "text": $escaped }] }
            ],
            "generationConfig": {
              "temperature": 0.7,
              "candidateCount": 1
            }
          }
          """.trimIndent()

        // HTTP request
        val body = RequestBody.create(
            "application/json".toMediaTypeOrNull(),
            jsonBody
        )
        val request = Request.Builder()
            .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=$apiKey")
            .post(body)
            .build()

        http.newCall(request).execute().use { resp ->
            if (!resp.isSuccessful) {
                throw IOException("GenAI error ${resp.code}: ${resp.body?.string()}")
            }
            val root = JSONObject(resp.body!!.string())
            root.getJSONArray("candidates")
                .getJSONObject(0)
                .getJSONObject("content")
                .getJSONArray("parts")
                .getJSONObject(0)
                .getString("text")
        }
    }

}



