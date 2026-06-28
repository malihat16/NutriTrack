// src/main/java/com/fit2081/a1_maliha33473692/viewmodel/NutriCoachViewModel.kt
package com.fit2081.a1_maliha33473692.viewmodel

import androidx.lifecycle.*
import com.fit2081.a1_maliha33473692.data.entity.PatientEntity
import com.fit2081.a1_maliha33473692.data.entity.NutriCoachTipEntity
import com.fit2081.a1_maliha33473692.data.model.FruitInfoResponse
import com.fit2081.a1_maliha33473692.data.repository.NutriCoachRepository
import com.fit2081.a1_maliha33473692.data.repository.PatientRepository
import kotlinx.coroutines.launch

class NutriCoachViewModel(
    private val tipRepo: NutriCoachRepository,
    private val patientRepo: PatientRepository,
    private val userId: Int
) : ViewModel() {

    /** 1) Publicly expose the patient row */
    val patient: LiveData<PatientEntity?> =
        patientRepo.getPatientByIdLive(userId)

    /** 2) Loading / error for all ops */
    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    /** 3) Fruit‐info from external API */
    private val _fruitInfo = MutableLiveData<FruitInfoResponse?>(null)
    val fruitInfo: LiveData<FruitInfoResponse?> = _fruitInfo

    /** 4) All saved AI tips for this user */
    val tipsHistory: LiveData<List<NutriCoachTipEntity>> =
        tipRepo.getTipsForPatientLive(userId)

    /** 5) The one “live” AI tip you just generated */
    private val _aiTip = MutableLiveData<String?>(null)
    val aiTip: LiveData<String?> = _aiTip

    /** Fetch nutritional info for a fruit */
    fun fetchFruitInfo(name: String) = viewModelScope.launch {
        _loading.value = true
        _error.value = null
        try {
            _fruitInfo.value = tipRepo.fetchFruitInfo(name)
        } catch (t: Throwable) {
            _error.value = t.message
        } finally {
            _loading.value = false
        }
    }

    /** Save a generated tip into the DB (if new) */
    fun saveTip(tip: String) = viewModelScope.launch {
        tipRepo.saveTipIfNew(userId, tip)
    }

    /**
     * Build your prompt here (you have access to `patient.value`)
     * and then call into your repo’s `generateTip(...)`.
     */
    fun generateFruitTip(fruitName: String) = viewModelScope.launch {
        _loading.value = true
        _error.value = null
        try {
            val p = patient.value
                ?: throw IllegalStateException("Patient not loaded")

            // e.g. pick their current total score:
            val total = if (p.sex.equals("Male", true))
                p.totalScoreMale else p.totalScoreFemale

            val prompt = """
        Below is a summary of the most recent food‐intake:
        • Total food quality: ${"%.1f".format(total)} / 100  
        • You currently average ${"%.1f".format(
                if (p.sex.equals("Male", true)) p.fruitScoreMale else p.fruitScoreFemale
            )} servings of fruit.
        Analyze it and generate a short detailed message to encourage improving fruit intake. 
        Mention “$fruitName” too.
      """.trimIndent()

            val response = tipRepo.generateTip(prompt)
            _aiTip.value = response.trim()
        } catch (t: Throwable) {
            _error.value = t.message
        } finally {
            _loading.value = false
        }
    }
}

