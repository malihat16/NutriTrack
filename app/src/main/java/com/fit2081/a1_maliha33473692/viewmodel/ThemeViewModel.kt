package com.fit2081.a1_maliha33473692.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fit2081.a1_maliha33473692.data.ThemePreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ThemeViewModel(private val context: Context) : ViewModel() {
    // a StateFlow that emits the current preference
    val isDarkMode: StateFlow<Boolean> =
        ThemePreferences.darkModeFlow(context)
            .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            ThemePreferences.setDarkMode(context, enabled)
        }
    }
}
