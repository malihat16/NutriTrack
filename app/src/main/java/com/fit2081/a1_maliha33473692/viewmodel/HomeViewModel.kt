package com.fit2081.a1_maliha33473692.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.fit2081.a1_maliha33473692.data.entity.FoodIntakeEntity
import com.fit2081.a1_maliha33473692.data.entity.PatientEntity
import com.fit2081.a1_maliha33473692.data.repository.FoodIntakeRepository
import com.fit2081.a1_maliha33473692.data.repository.PatientRepository

class HomeViewModel(
    private val patientRepo: PatientRepository,
    private val intakeRepo: FoodIntakeRepository,
    private val userId: Int
) : ViewModel() {

    /** LiveData streams */
    val patient: LiveData<PatientEntity?> =
        patientRepo.getPatientByIdLive(userId)

    val intake: LiveData<FoodIntakeEntity?> =
        intakeRepo.getIntakeForPatientLive(userId)
}
