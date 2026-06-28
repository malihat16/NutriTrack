package com.fit2081.a1_maliha33473692.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fit2081.a1_maliha33473692.data.repository.NutriCoachRepository
import com.fit2081.a1_maliha33473692.data.repository.PatientRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull

class ClinicianViewModel(
    private val patientRepo: PatientRepository,
    private val genAiRepo:   NutriCoachRepository
) : ViewModel() {

    // Averages
    private val _avgMale   = MutableLiveData<Double?>(null)
    val avgMale: LiveData<Double?> = _avgMale

    private val _avgFemale = MutableLiveData<Double?>(null)
    val avgFemale: LiveData<Double?> = _avgFemale

    // GenAI insights
    private val _insights          = MutableLiveData<List<String>>(emptyList())
    val insights: LiveData<List<String>> = _insights

    private val _loadingInsights   = MutableLiveData(false)
    val loadingInsights: LiveData<Boolean> = _loadingInsights

    private val _errorInsights     = MutableLiveData<String?>(null)
    val errorInsights: LiveData<String?> = _errorInsights


    init {
        viewModelScope.launch(Dispatchers.IO) {
            loadAverages()
            // auto-run insights once on start:
            generateInsights()
        }
    }

    fun refreshInsights() {
        viewModelScope.launch(Dispatchers.IO) {
            generateInsights()
        }
    }

    private suspend fun loadAverages() {
        // grab them and stash in LiveData
        val m = patientRepo.getAverageHeifaMale().firstOrNull()   ?: 0.0
        val f = patientRepo.getAverageHeifaFemale().firstOrNull() ?: 0.0
        _avgMale.postValue(m)
        _avgFemale.postValue(f)
    }

    private suspend fun generateInsights() {
        _loadingInsights.postValue(true)
        _errorInsights.postValue(null)

        try {
            // fetch all the averages
            val maleAvg       = patientRepo.getAverageHeifaMale().firstOrNull()        ?: 0.0
            val femaleAvg     = patientRepo.getAverageHeifaFemale().firstOrNull()      ?: 0.0
            val vegAvg        = patientRepo.getAverageVegetablesScore().firstOrNull()  ?: 0.0
            val fruitAvg      = patientRepo.getAverageFruitScore().firstOrNull()       ?: 0.0
            val fruitVarAvg   = patientRepo.getAverageFruitVariation().firstOrNull()   ?: 0.0

            // build the prompt with all those numbers
            val prompt = """
          Here’s a quick summary of our entire user base’s median scores:

          • Total HEIFA (male):     ${"%.1f".format(maleAvg)}  
          • Total HEIFA (female):   ${"%.1f".format(femaleAvg)}  
          • Vegetables HEIFA avg:   ${"%.1f".format(vegAvg)}  
          • Fruits HEIFA avg:       ${"%.1f".format(fruitAvg)}  
          • Fruit‐variation avg:    ${"%.1f".format(fruitVarAvg)}  

          Using _only_ these numbers, please generate 3 bullet‐point observations
          highlighting interesting patterns in the data.  
          Just as an example: something like this, but even better and unique
          - “Most users who scored above average on vegetables also had higher‐than‐average fruit scores.”  
          - “Female users showed 10% more variation in fruit intake than male users.”  
        """.trimIndent()

            // ask existing GenAI repo to send it to Gemini
            val response = genAiRepo.generateTip(prompt)

            // split & post back
            _insights.postValue(
                response
                    .lines()
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }
            )
        }
        catch(e: Exception) {
            _errorInsights.postValue(e.localizedMessage)
        }
        finally {
            _loadingInsights.postValue(false)
        }
    }

}
