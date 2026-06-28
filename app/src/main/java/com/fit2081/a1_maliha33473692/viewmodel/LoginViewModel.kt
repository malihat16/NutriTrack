package com.fit2081.a1_maliha33473692.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fit2081.a1_maliha33473692.data.entity.PatientEntity
import com.fit2081.a1_maliha33473692.data.repository.PatientRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import java.security.MessageDigest



sealed class LoginState {
    object Idle : LoginState()
    data class ClaimSuccess(val patient: PatientEntity) : LoginState()
    data class Success(val patient: PatientEntity)     : LoginState()
    data class Error(val message: String)              : LoginState()
}

class LoginViewModel(
    private val repo: PatientRepository
) : ViewModel() {

    // Expose the “currently loaded” patient as LiveData
    private val _patient = MediatorLiveData<PatientEntity?>().apply {
        // optionally start with null or load in init
        addSource(repo.getPatientByIdLive(0)) { value = it }
    }
    val patient: LiveData<PatientEntity?> = _patient

    // Expose the login/claim state as Compose-friendly state
    var loginState by mutableStateOf<LoginState>(LoginState.Idle)
        private set
    val userIds: LiveData<List<Int>> = repo.getAllUserIdsLive()

    /** Call this before showing your Claim or Login UI, to start streaming the DB row */
    fun loadPatient(userId: Int) {
        // swap to the correct source
        _patient.addSource(repo.getPatientByIdLive(userId)) { _patient.value = it }
    }

    fun login(userId: Int, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val hash = hashPassword(password)
            val patient = repo.findByIdOnce(userId)
            when {
                patient == null                      -> loginState = LoginState.Error("Invalid ID")
                patient.passwordHash != hash         -> loginState = LoginState.Error("Wrong password")
                else                                  -> loginState = LoginState.Success(patient)
            }
        }
    }

    private fun hashPassword(pw: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        return md.digest(pw.toByteArray())
            .joinToString("") { "%02x".format(it) }
    }

    fun reset() {
        loginState = LoginState.Idle
    }

}


