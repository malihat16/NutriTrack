package com.fit2081.a1_maliha33473692.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.fit2081.a1_maliha33473692.viewmodel.ClinicianViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.fit2081.a1_maliha33473692.MyBottomBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClinicianScreen(
    viewModel: ClinicianViewModel,
    onHomeClicked:     () -> Unit,
    onInsightsClicked: () -> Unit,
    onNutriCoachClicked: () -> Unit,
    onSettingsClicked: () -> Unit, // we call this when the user is done
    onBack:            () -> Unit
) {
    // VM state
    val avgMale by viewModel.avgMale.observeAsState(0.0)
    val avgFemale by viewModel.avgFemale.observeAsState(0.0)
    val insights by viewModel.insights.observeAsState(emptyList())
    val loading by viewModel.loadingInsights.observeAsState(false)
    val errorMessage by viewModel.errorInsights.observeAsState()

    // local state: have we tapped "Find Data Patterns"?
    var fetched by remember { mutableStateOf(false) }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Clinician Dashboard",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },

//                navigationIcon = {
//                    IconButton(onClick = onBack) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
//                    }
//                }
            )
        },
        bottomBar = {
            MyBottomBar(
                onHomeClicked = onHomeClicked,
                onInsightsClicked = onInsightsClicked,
                onNutriCoachClicked = onNutriCoachClicked,
                onSettingsClicked = onSettingsClicked
            )
        }
    ) { innerPadding ->
        // scrollable container
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                "Average HEIFA Scores",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(Modifier.height(8.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                avgMale?.let { StatisticCard("Male", it) }
                avgFemale?.let { StatisticCard("Female", it) }
            }

            Spacer(Modifier.height(24.dp))

            // Find Data Patterns button
            Button(
                onClick = {
                    viewModel.refreshInsights()
                    fetched = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Find Data Patterns")
            }

            Spacer(Modifier.height(16.dp))

            // Loading indicator
            if (loading) {
                LinearProgressIndicator(Modifier.fillMaxWidth())
                Spacer(Modifier.height(16.dp))
            }

            // Error banner
            errorMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(16.dp))
            }

            if (fetched) {
                insights.forEach { insight ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Text(insight, Modifier.padding(16.dp))
                    }
                    Spacer(Modifier.height(8.dp))
                }

                Spacer(Modifier.height(24.dp))


            }
            // Done button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onSettingsClicked
                ) {
                    Text("Back to Settings")
                }
            }
        }
    }
}

@Composable
private fun StatisticCard(label: String, value: Double) {
    Card(elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
        Column(
            Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(label, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(4.dp))
            Text(String.format("%.1f", value), style = MaterialTheme.typography.headlineMedium)
        }
    }
}
