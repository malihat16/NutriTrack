package com.fit2081.a1_maliha33473692.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fit2081.a1_maliha33473692.data.repository.PatientRepository

class LoginViewModelFactory(
    private val repo: PatientRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown VM class")
    }
}
