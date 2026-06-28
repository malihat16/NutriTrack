package com.fit2081.a1_maliha33473692.viewmodel

import androidx.lifecycle.*
import com.fit2081.a1_maliha33473692.data.entity.PatientEntity
import com.fit2081.a1_maliha33473692.data.repository.PatientRepository
import kotlinx.coroutines.launch
import java.security.MessageDigest

class AuthViewModel(
    private val repo: PatientRepository
) : ViewModel() {

    private val _user  = MutableLiveData<PatientEntity?>(null)
    val user: LiveData<PatientEntity?> = _user

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    val userIds = repo.getAllUserIdsLive()

    fun clearError() {
        _error.value = null
    }

    fun claimAccount(userId: Int, phone: String, name: String, password: String) {
        viewModelScope.launch {
            _error.value = null

            // does this clinician‐provided ID exist?
            val patient = repo.findByIdOnce(userId)
            if (patient == null) {
                _error.value = "That ID isn’t valid."
                return@launch
            }

            // have they already claimed?
            if (!patient.passwordHash.isNullOrBlank()) {
                _error.value = "You have already registered, please proceed to login."
                return@launch
            }

            // does the phone match
            if (patient.phoneNumber != phone.trim()) {
                _error.value = "Phone number is wrong."
                return@launch
            }

            // OK → hash + save
            val hash = password.sha256()
            repo.claimAccount(userId, name.trim(), hash)

            // notify UI
            _user.value = patient.copy(name = name.trim(), passwordHash = hash)
        }
    }

    private fun String.sha256(): String {
        val md = MessageDigest.getInstance("SHA-256")
        return md.digest(toByteArray()).joinToString("") { "%02x".format(it) }
    }
}
