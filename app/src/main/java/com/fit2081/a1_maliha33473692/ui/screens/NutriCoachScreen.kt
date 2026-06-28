package com.fit2081.a1_maliha33473692.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.fit2081.a1_maliha33473692.MyBottomBar
import com.fit2081.a1_maliha33473692.data.model.FruitInfoResponse
import com.fit2081.a1_maliha33473692.viewmodel.NutriCoachViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutriCoachScreen(
    viewModel:           NutriCoachViewModel,
    onHomeClicked:       () -> Unit,
    onInsightsClicked:   () -> Unit,
    onNutriCoachClicked: () -> Unit,
    onSettingsClicked:   () -> Unit
) {
    // observe the patient LiveData
    val patient by viewModel.patient.observeAsState()

    // local UI state
    var fruitName      by rememberSaveable { mutableStateOf("") }
    var showTipsDialog by remember { mutableStateOf(false) }

    // VM state
    val loading by viewModel.loading.observeAsState(false)
    val error   by viewModel.error.observeAsState()
    val info    by viewModel.fruitInfo.observeAsState()
    val tips    by viewModel.tipsHistory.observeAsState(emptyList())
    val aiTip   by viewModel.aiTip.observeAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope     = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },

        bottomBar = {
            MyBottomBar(
                onHomeClicked       = onHomeClicked,
                onInsightsClicked   = onInsightsClicked,
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
            //  Patient loading
            if (patient == null) {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
                return@Box
            }

            //  Main scrollable content
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Text(
                    "NutriCoach",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                )
                Spacer(Modifier.height(16.dp))

                // Fruit Details Retrieval
                val p          = patient!!
                val serveThreshold  = 2.0
                val fruitServes     = p.fruitServeSize

                if (fruitServes < serveThreshold) {
                    OutlinedTextField(
                        value = fruitName,
                        onValueChange = { fruitName = it },
                        label = { Text("Enter Fruit Name here") },
                        trailingIcon = {
                            IconButton(
                                onClick = { viewModel.fetchFruitInfo(fruitName.trim()) }
                            ) {
                                Icon(
                                    imageVector   = Icons.Default.Search,
                                    contentDescription = "Details"
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(16.dp))

                    info?.let { showFruitInfo(it) }
                    Spacer(Modifier.height(24.dp))
                } else {
                    Image(
                        painter            = rememberAsyncImagePainter("https://picsum.photos/400"),
                        contentDescription = null,
                        modifier           = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(MaterialTheme.shapes.medium)
                    )
                    Spacer(Modifier.height(24.dp))
                }

                // Motivational Message (AI) with chat icon
                Button(
                    onClick = { viewModel.generateFruitTip(fruitName.trim()) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.ChatBubble,
                        contentDescription = "Chat Icon"
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Motivational Message (AI)")
                }
                Spacer(Modifier.height(16.dp))

                aiTip?.let { tip ->
                    Text(tip, style = MaterialTheme.typography.bodyLarge)
                    Spacer(Modifier.height(8.dp))

                    Button(
                        onClick = {
                            // 3) save + show snackbar
                            viewModel.saveTip(tip)
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Tip saved!")
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Save Tip")
                    }
                }

                Spacer(Modifier.height(80.dp))
            }

            //   “Show All Tips” pinned bottom-right
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomEnd
            ) {
                Button(
                    onClick = { showTipsDialog = true },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Show All Tips")
                }
            }

            // Loading overlay
            if (loading) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            // Error banner
            error?.let { msg ->
                Text(
                    text = msg,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(8.dp)
                        .align(Alignment.TopCenter)
                )
            }

            //  Saved Tips Dialog
            if (showTipsDialog) {
                AlertDialog(
                    onDismissRequest = { showTipsDialog = false },
                    title = { Text(
                        "AI Tips",
                        style = MaterialTheme.typography.headlineSmall
                            .copy(fontWeight = FontWeight.Bold)) },
                    text = {
                        if (tips.isEmpty()) {
                            Text("(No saved tips yet)")
                        } else {
                            // wrap in a fixed-height Box so the dialog doesn’t grow forever
                            Box(Modifier.heightIn(max = 400.dp)) {
                                LazyColumn(
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    contentPadding = PaddingValues(vertical = 8.dp)
                                ) {
                                    items(tips) { tipEntity ->
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 4.dp),
                                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                            shape = MaterialTheme.shapes.small
                                        ) {
                                            Column(Modifier.padding(12.dp)) {
                                                Text(
                                                    text = tipEntity.tip,
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = { showTipsDialog = false }) {
                            Text("Done")
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun showFruitInfo(info: FruitInfoResponse) {
    Card(
        Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            InfoRow("Family",   info.family)
            InfoRow("Calories", info.nutritions.calories.toString())
            InfoRow("Fat",      info.nutritions.fat.toString())
            InfoRow("Sugar",    info.nutritions.sugar.toString())
            InfoRow("Carbs",    info.nutritions.carbohydrates.toString())
            InfoRow("Protein",  info.nutritions.protein.toString())
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}
