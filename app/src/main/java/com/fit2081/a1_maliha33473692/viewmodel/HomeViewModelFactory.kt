package com.fit2081.a1_maliha33473692.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fit2081.a1_maliha33473692.data.repository.FoodIntakeRepository
import com.fit2081.a1_maliha33473692.data.repository.PatientRepository

class HomeViewModelFactory(
    private val patientRepo: PatientRepository,
    private val intakeRepo: FoodIntakeRepository,
    private val userId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(patientRepo, intakeRepo, userId) as T
        }
        throw IllegalArgumentException("Unknown VM class")
    }
}
