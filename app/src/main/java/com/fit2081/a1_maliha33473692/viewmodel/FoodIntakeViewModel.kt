package com.fit2081.a1_maliha33473692.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.*
import com.fit2081.a1_maliha33473692.data.entity.FoodIntakeEntity
import com.fit2081.a1_maliha33473692.data.repository.FoodIntakeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FoodIntakeViewModel(
    private val repo: FoodIntakeRepository,
    private val userId: Int
) : ViewModel() {

    /** LiveData of saved responses (or null if never saved) */
    val intake: LiveData<FoodIntakeEntity?> =
        repo.getIntakeForPatientLive(userId)

    /** Local UI state for each checkbox & timing field */
    /** Local UI state for each checkbox & timing field */
    var fruitsChecked       by mutableStateOf(false)
        private set
    var vegetablesChecked   by mutableStateOf(false)
        private set
    var grainsChecked       by mutableStateOf(false)
        private set
    var redMeatChecked      by mutableStateOf(false)
        private set
    var seafoodChecked      by mutableStateOf(false)
        private set
    var poultryChecked      by mutableStateOf(false)
        private set
    var eggsChecked         by mutableStateOf(false)
        private set
    var fishChecked         by mutableStateOf(false)
        private set
    var nutsSeedsChecked    by mutableStateOf(false)
        private set

    var selectedPersona     by mutableStateOf("")
        private set
    var biggestMealTime     by mutableStateOf("Not Set")
        private set
    var sleepTime           by mutableStateOf("Not Set")
        private set
    var wakeTime            by mutableStateOf("Not Set")
        private set


    init {
        // When the DB emits a saved entity, populate our local UI state
        intake.observeForever { saved ->
            saved?.let {
                fruitsChecked    = it.fruitsChecked
                vegetablesChecked= it.vegetablesChecked
                grainsChecked    = it.grainsChecked
                redMeatChecked   = it.redMeatChecked
                seafoodChecked   = it.seafoodChecked
                poultryChecked   = it.poultryChecked
                eggsChecked      = it.eggsChecked
                fishChecked      = it.fishChecked
                nutsSeedsChecked = it.nutsSeedsChecked
                selectedPersona  = it.selectedPersona
                biggestMealTime  = it.biggestMealTime
                sleepTime        = it.sleepTime
                wakeTime         = it.wakeTime
            }
        }
    }

    fun setFruits(yes: Boolean)       { fruitsChecked = yes }
    fun setVegetables(yes: Boolean)   { vegetablesChecked = yes }
    fun setGrains(yes: Boolean)       { grainsChecked = yes }
    fun setRedMeat(yes: Boolean)      { redMeatChecked = yes }
    fun setSeafood(yes: Boolean)      { seafoodChecked = yes }
    fun setPoultry(yes: Boolean)      { poultryChecked = yes }
    fun setEggs(yes: Boolean)         { eggsChecked = yes }
    fun setFish(yes: Boolean)         { fishChecked = yes }
    fun setNutsSeeds(yes: Boolean)    { nutsSeedsChecked = yes }
    fun setPersona(p: String)         { selectedPersona = p }
    fun updateBiggestMealTime(t: String) { biggestMealTime = t }
    fun updateSleepTime(t: String)       { sleepTime = t }
    fun updateWakeTime(t: String)        { wakeTime = t }

    /** Call when the user taps “Save” */
    fun saveResponses() {
        viewModelScope.launch(Dispatchers.IO) {
            val entity = FoodIntakeEntity(
                patientUserId    = userId,
                fruitsChecked    = fruitsChecked,
                vegetablesChecked= vegetablesChecked,
                grainsChecked    = grainsChecked,
                redMeatChecked   = redMeatChecked,
                seafoodChecked   = seafoodChecked,
                poultryChecked   = poultryChecked,
                eggsChecked      = eggsChecked,
                fishChecked      = fishChecked,
                nutsSeedsChecked = nutsSeedsChecked,
                selectedPersona  = selectedPersona,
                biggestMealTime  = biggestMealTime,
                sleepTime        = sleepTime,
                wakeTime         = wakeTime
            )
            repo.saveIntake(entity)
        }
    }
}

