package com.fit2081.a1_maliha33473692.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fit2081.a1_maliha33473692.data.repository.NutriCoachRepository
import com.fit2081.a1_maliha33473692.data.repository.PatientRepository

class ClinicianViewModelFactory(
    private val patientRepo: PatientRepository,
    private val genAiRepo:   NutriCoachRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClinicianViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ClinicianViewModel(patientRepo, genAiRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
