package com.fit2081.a1_maliha33473692.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fit2081.a1_maliha33473692.data.repository.NutriCoachRepository
import com.fit2081.a1_maliha33473692.data.repository.PatientRepository


class NutriCoachViewModelFactory(
    private val tipRepo: NutriCoachRepository,
    private val patientRepo: PatientRepository,
    private val userId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return NutriCoachViewModel(tipRepo, patientRepo, userId) as T
    }
}
