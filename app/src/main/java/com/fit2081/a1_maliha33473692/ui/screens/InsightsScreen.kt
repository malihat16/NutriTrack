package com.fit2081.a1_maliha33473692.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fit2081.a1_maliha33473692.MyBottomBar
import com.fit2081.a1_maliha33473692.data.database.NutriTrackDatabase
import com.fit2081.a1_maliha33473692.data.entity.PatientEntity
import com.fit2081.a1_maliha33473692.data.repository.FoodIntakeRepository
import com.fit2081.a1_maliha33473692.data.repository.PatientRepository
import com.fit2081.a1_maliha33473692.viewmodel.HomeViewModel
import com.fit2081.a1_maliha33473692.viewmodel.HomeViewModelFactory
import com.fit2081.a1_maliha33473692.utils.CategoryScore
import com.fit2081.a1_maliha33473692.utils.computeAlcoholScore
import com.fit2081.a1_maliha33473692.utils.computeDairyScore
import com.fit2081.a1_maliha33473692.utils.computeDiscretionaryScore
import com.fit2081.a1_maliha33473692.utils.computeFruitsScore
import com.fit2081.a1_maliha33473692.utils.computeGrainsScore
import com.fit2081.a1_maliha33473692.utils.computeMeatScore
import com.fit2081.a1_maliha33473692.utils.computeSaturatedFatScore
import com.fit2081.a1_maliha33473692.utils.computeSodiumScore
import com.fit2081.a1_maliha33473692.utils.computeSugarScore
import com.fit2081.a1_maliha33473692.utils.computeUnsaturatedFatScore
import com.fit2081.a1_maliha33473692.utils.computeVegetablesScore
import com.fit2081.a1_maliha33473692.utils.computeWaterScore
import com.fit2081.a1_maliha33473692.utils.computeWholeGrainsScore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsightsScreen(
    userId: Int,
    onHomeClicked:       () -> Unit,
    onNutriCoachClicked: () -> Unit,
    onSettingsClicked:   () -> Unit
) {
    val context = LocalContext.current
    val db      = remember { NutriTrackDatabase.getInstance(context) }
    val pRepo   = remember { PatientRepository(db.patientDao()) }
    val iRepo   = remember { FoodIntakeRepository(db.foodIntakeDao()) }

    // same VM as HomeScreen
    val vm: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(pRepo, iRepo, userId)
    )
    val patientEntity by vm.patient.observeAsState()

    Scaffold(
        bottomBar = {
            MyBottomBar(
                onHomeClicked       = onHomeClicked,
                onInsightsClicked   = { /* already here */ },
                onNutriCoachClicked = onNutriCoachClicked,
                onSettingsClicked   = onSettingsClicked
            )
        }
    ) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (patientEntity == null) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            } else {
                // once loaded, render the full content
                RenderInsightsContent(
                    patient               = patientEntity!!,
                    onImproveDietClicked  = { onNutriCoachClicked() }, // or a separate callback
                )
            }
        }
    }
}

@Composable
private fun RenderInsightsContent(
    patient: PatientEntity,
    onImproveDietClicked: () -> Unit
) {
    val context = LocalContext.current

    // compute all 13 category scores
    val discretionary  = computeDiscretionaryScore(patient)
    val vegetables     = computeVegetablesScore(patient)
    val fruits         = computeFruitsScore(patient)
    val grains         = computeGrainsScore(patient)
    val wholeGrains    = computeWholeGrainsScore(patient)
    val meat           = computeMeatScore(patient)
    val dairy          = computeDairyScore(patient)
    val sodium         = computeSodiumScore(patient)
    val sugar          = computeSugarScore(patient)
    val saturatedFat   = computeSaturatedFatScore(patient)
    val unsaturatedFat = computeUnsaturatedFatScore(patient)
    val water          = computeWaterScore(patient)
    val alcohol        = computeAlcoholScore(patient)

    val categories = listOf(
        discretionary, vegetables, fruits,
        grains, wholeGrains, meat,
        dairy, sodium, sugar,
        saturatedFat, unsaturatedFat,
        water, alcohol
    )

    // total 0–100 by sex
    val totalScore = if (patient.sex.equals("Male", true))
        patient.totalScoreMale
    else
        patient.totalScoreFemale

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Insights: Food Score",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
        )
        Spacer(Modifier.height(16.dp))

        // sliders
        categories.forEach { cat ->
            CategorySliderRow(cat)
            Spacer(Modifier.height(16.dp))
        }

        Spacer(Modifier.height(24.dp))
        Text("Total Food Quality Score", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        Row(
            Modifier
                .fillMaxWidth()
                .height(40.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment   = Alignment.CenterVertically
        ) {
            val frac = (totalScore / 100.0).coerceIn(0.0,1.0).toFloat()
            LinearProgressIndicator(
                progress = frac,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(6.dp))
            )
            Text("${"%.1f".format(totalScore)}/100")
        }

        Spacer(Modifier.height(24.dp))

        // share & improve
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {
                    val shareText = buildString {
                        append("My Food Quality Score is ${"%.1f".format(totalScore)}/100.\n")
                        append("Category breakdown:\n")
                        categories.forEach { c ->
                            append("• ${c.name}: ${"%.1f".format(c.rawScore)}/${"%.1f".format(c.maxScore)}\n")
                        }
                        append("\nCheck out NutriTrack!")
                    }
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, shareText)
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share via"))
                },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Share, contentDescription = "Share")
                Spacer(Modifier.width(4.dp))
                Text("Share")
            }

            Button(
                onClick = onImproveDietClicked,
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Favorite, contentDescription = "Improve")
                Spacer(Modifier.width(4.dp))
                Text("Improve my diet!")
            }
        }
    }
}

@Composable
private fun CategorySliderRow(category: CategoryScore) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)     // a bit more breathing room
    ) {
        // label on its own line
        Text(
            text = category.name,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.height(4.dp))

        // slider + value on a second line
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LinearProgressIndicator(
                progress = category.fraction.toFloat(),
                modifier = Modifier
                    .weight(1f)
                    .height(8.dp)             // thinner bar
                    .clip(RoundedCornerShape(4.dp))
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "${"%.1f".format(category.rawScore)}/${"%.1f".format(category.maxScore)}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
