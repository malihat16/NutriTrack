package com.fit2081.a1_maliha33473692.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fit2081.a1_maliha33473692.MyBottomBar
import com.fit2081.a1_maliha33473692.R
import com.fit2081.a1_maliha33473692.data.database.NutriTrackDatabase
import com.fit2081.a1_maliha33473692.data.repository.FoodIntakeRepository
import com.fit2081.a1_maliha33473692.data.repository.PatientRepository
import com.fit2081.a1_maliha33473692.viewmodel.HomeViewModel
import com.fit2081.a1_maliha33473692.viewmodel.HomeViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userId: Int,
    onEditClicked: () -> Unit,
    onInsightsClicked: () -> Unit,
    onNutriCoachClicked: () -> Unit,
    onSettingsClicked: () -> Unit
) {
    val context = LocalContext.current

    // build DB and repos once
    val db = remember { NutriTrackDatabase.getInstance(context) }
    val patientRepo = remember { PatientRepository(db.patientDao()) }
    val intakeRepo  = remember { FoodIntakeRepository(db.foodIntakeDao()) }

    // create (or retrieve) the ViewModel
    val vm: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(patientRepo, intakeRepo, userId)
    )

    //observe the patient LiveData
    val patient = vm.patient.observeAsState().value

    Scaffold(
        bottomBar = {
            MyBottomBar(
                onHomeClicked      = { /* already here */ },
                onInsightsClicked = onInsightsClicked,
                onNutriCoachClicked = onNutriCoachClicked,
                onSettingsClicked = onSettingsClicked
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (patient == null) {
                // still loading from the database
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                // we have our patient, compute the score and render in existing UI
                val p = patient!!
                val actualScore = if (p.sex.equals("Male", ignoreCase = true))
                    p.totalScoreMale
                else
                    p.totalScoreFemale

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    // HEADLINE GREETING
                    Text(
                        text = "Hello,",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = p.name,
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Spacer(Modifier.height(14.dp))

                    // INFO + EDIT ROW
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Edit your responses for the Food Intake Questionnaire at any time!",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(Modifier.width(8.dp))
                        Button(onClick = onEditClicked) {
                            Text("Edit")
                        }
                    }
                    Spacer(Modifier.height(24.dp))

                    // FOOD BOWL IMAGE
                    Image(
                        painter = painterResource(id = R.drawable.food_bowl),
                        contentDescription = "Food Representation",
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 200.dp),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(Modifier.height(24.dp))

                    // SEE ALL SCORES
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("My Score", style = MaterialTheme.typography.titleLarge)
                        TextButton(onClick = onInsightsClicked) {
                            Text("See all scores >")
                        }
                    }

                    // SHOW THE USER’S ACTUAL SCORE
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.ArrowUpward,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "${"%.1f".format(actualScore)}/100",
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(Modifier.height(16.dp))

                    // EXPLANATION TEXT
                    Text(
                        text = "What is the Food Quality Score?",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = """
                          Your Food Quality Score provides a snapshot of how well your eating patterns align with established food guidelines, helping you identify both strengths and opportunities for improvement in your diet.
                          
                          This personalized measurement considers various food groups including vegetables, fruits, whole grains, and proteins to give you practical insights for making healthier food choices.
                      """.trimIndent(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(Modifier.height(24.dp))
                }
            }
        }
    }
}
