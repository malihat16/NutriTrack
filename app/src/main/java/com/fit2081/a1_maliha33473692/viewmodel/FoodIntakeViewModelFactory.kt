package com.fit2081.a1_maliha33473692.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fit2081.a1_maliha33473692.data.repository.FoodIntakeRepository

/** Factory so we can pass userId & repo into our ViewModel */
class FoodIntakeViewModelFactory(
    private val repo: FoodIntakeRepository,
    private val userId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FoodIntakeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FoodIntakeViewModel(repo, userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
