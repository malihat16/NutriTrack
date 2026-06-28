package com.fit2081.a1_maliha33473692.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fit2081.a1_maliha33473692.PersonaInfoDialog
import com.fit2081.a1_maliha33473692.utils.PersonaInfo
import com.fit2081.a1_maliha33473692.utils.personaList
import com.fit2081.a1_maliha33473692.utils.showTimePicker
import com.fit2081.a1_maliha33473692.data.database.NutriTrackDatabase
import com.fit2081.a1_maliha33473692.data.repository.FoodIntakeRepository
import com.fit2081.a1_maliha33473692.viewmodel.FoodIntakeViewModel
import com.fit2081.a1_maliha33473692.viewmodel.FoodIntakeViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodIntakeQuestionnaireScreen(
    patientId: Int,
    onSaveClicked:   () -> Unit,
    onBackClicked:   () -> Unit,
    onSkipClicked:   () -> Unit
) {
    val context = LocalContext.current

    // state & VM
    var validationError by remember { mutableStateOf<String?>(null) }
    val db               = remember { NutriTrackDatabase.getInstance(context) }
    val repo             = remember { FoodIntakeRepository(db.foodIntakeDao()) }
    val vm: FoodIntakeViewModel = viewModel(
        factory = FoodIntakeViewModelFactory(repo, patientId)
    )

    // pull VM state
    val fruitsChecked     = vm.fruitsChecked
    val vegetablesChecked = vm.vegetablesChecked
    val grainsChecked     = vm.grainsChecked
    val redMeatChecked    = vm.redMeatChecked
    val seafoodChecked    = vm.seafoodChecked
    val poultryChecked    = vm.poultryChecked
    val eggsChecked       = vm.eggsChecked
    val fishChecked       = vm.fishChecked
    val nutsSeedsChecked  = vm.nutsSeedsChecked

    val selectedPersona   = vm.selectedPersona
    val biggestMealTime   = vm.biggestMealTime
    val sleepTime         = vm.sleepTime
    val wakeTime          = vm.wakeTime

    // persona dialog UI
    var personaDropdownExpanded by remember { mutableStateOf(false) }
    var showPersonaDialog       by remember { mutableStateOf(false) }
    var dialogInfo              by remember { mutableStateOf<PersonaInfo?>(null) }

    //helper to parse "HH:mm" → total minutes
    fun parseMinutes(t: String): Int? {
        val parts = t.split(":").mapNotNull { it.toIntOrNull() }
        return if (parts.size == 2) parts[0] * 60 + parts[1] else null
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                title = {
                    Text(
                        "Food Intake Questionnaire",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // FOOD CHECKBOXES
            Text(
                "Tick all the food categories you can eat:",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(Modifier.height(8.dp))

// build a list of all 9 items (label, checkedState, setter)
            val categories = listOf(
                Triple("Fruits",     fruitsChecked,     vm::setFruits),
                Triple("Red Meat",   redMeatChecked,    vm::setRedMeat),
                Triple("Eggs",       eggsChecked,       vm::setEggs),
                Triple("Vegetable", vegetablesChecked, vm::setVegetables),
                Triple("Seafood",    seafoodChecked,    vm::setSeafood),
                Triple("Fish",       fishChecked,       vm::setFish),
                Triple("Grains",     grainsChecked,     vm::setGrains),
                Triple("Poultry",    poultryChecked,    vm::setPoultry),
                Triple("Nuts/Seeds", nutsSeedsChecked,  vm::setNutsSeeds)
            )

// chunk into three rows
            categories.chunked(3).forEach { rowItems ->
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    rowItems.forEach { (label, checked, setter) ->
                        FoodCheckbox(
                            label     = label,
                            checked   = checked,
                            onChange  = setter,
                            modifier  = Modifier.weight(1f)
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
            }

            Spacer(Modifier.height(20.dp))

            //  PERSONA GRID & PICKER
            Text("Your Persona", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
            Spacer(Modifier.height(4.dp))
            Text(
                "Click on each button to learn more, then select your persona.",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                ),
                textAlign = TextAlign.Justify,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            @Composable
            fun rowOfPersonas(i1: Int, i2: Int) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    personaList.getOrNull(i1)?.let { info ->
                        PersonaButton(info, onClick = {
                            dialogInfo = info; showPersonaDialog = true
                        }, modifier = Modifier.weight(1f))
                    }
                    personaList.getOrNull(i2)?.let { info ->
                        PersonaButton(info, onClick = {
                            dialogInfo = info; showPersonaDialog = true
                        }, modifier = Modifier.weight(1f))
                    }
                }
            }
            rowOfPersonas(0,1); Spacer(Modifier.height(8.dp))
            rowOfPersonas(2,3); Spacer(Modifier.height(8.dp))
            rowOfPersonas(4,5); Spacer(Modifier.height(16.dp))

            ExposedDropdownMenuBox(
                expanded = personaDropdownExpanded,
                onExpandedChange = { personaDropdownExpanded = !personaDropdownExpanded }
            ) {
                OutlinedTextField(
                    value = selectedPersona,
                    onValueChange = {},
                    label = { Text("Which persona best fits you?") },
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(personaDropdownExpanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = personaDropdownExpanded,
                    onDismissRequest = { personaDropdownExpanded = false }
                ) {
                    personaList.forEach { p ->
                        DropdownMenuItem(
                            text = { Text(p.name) },
                            onClick = {
                                vm.setPersona(p.name)           // just setting VM state
                                personaDropdownExpanded = false // close the menu
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            //  TIMINGS
            Text("Timings", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
            Spacer(Modifier.height(8.dp))
            TimeRow(
                question  = "When do you eat your biggest meal?",
                timeValue = biggestMealTime
            ) { showTimePicker(context) { vm.updateBiggestMealTime(it) } }
            Spacer(Modifier.height(8.dp))
            TimeRow(
                question  = "What time do you go to sleep?",
                timeValue = sleepTime
            ) { showTimePicker(context) { vm.updateSleepTime(it) } }
            Spacer(Modifier.height(8.dp))
            TimeRow(
                question  = "What time do you wake up?",
                timeValue = wakeTime
            ) { showTimePicker(context) { vm.updateWakeTime(it) } }

            Spacer(Modifier.height(32.dp))

            // VALIDATION ERROR
            validationError?.let { err ->
                Text(err, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(bottom = 8.dp))
            }

            // SAVE & SKIP
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                // — Save with full validation —
                Button(
                    onClick = {
                        validationError = null

                        // must have at least one food ticked —
                        val anyFoodChecked = listOf(
                            fruitsChecked,
                            vegetablesChecked,
                            grainsChecked,
                            redMeatChecked,
                            seafoodChecked,
                            poultryChecked,
                            eggsChecked,
                            fishChecked,
                            nutsSeedsChecked
                        ).any { it }
                        if (!anyFoodChecked) {
                            validationError = "Please tick at least one food category"
                        }
                        //  persona selected
                        else if (selectedPersona.isBlank()) {
                            validationError = "Please select a persona"
                        }
                        // times must differ
                        else if (biggestMealTime == sleepTime ||
                            sleepTime      == wakeTime ||
                            biggestMealTime== wakeTime) {
                            validationError = "Meal, sleep, and wake times must all differ"
                        }
                        // parse minutes format & range checks
                        else {
                            val meal  = parseMinutes(biggestMealTime)
                            val wake  = parseMinutes(wakeTime)
                            val sleep = parseMinutes(sleepTime)

                            if (meal == null || wake == null || sleep == null) {
                                validationError = "Invalid time selection"
                            }
                            else if (meal < wake || meal > sleep) {
                                validationError = "Meal time must fall between wake and sleep"
                            }
                        }

                        //  if still no error, save & navigate
                        if (validationError == null) {
                            vm.saveResponses()
                            onSaveClicked()
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Save")
                }


                // Skip (no checks)
                OutlinedButton(
                    onClick = onSkipClicked,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Skip")
                }
            }
        }
    }

    // PERSONA DIALOG
    if (showPersonaDialog && dialogInfo != null) {
        PersonaInfoDialog(
            personaInfo = dialogInfo!!,
            onDismiss   = { showPersonaDialog = false },
        )
    }
}

// FOOD CHECKBOX COMPOSABLE
@Composable
private fun FoodCheckbox(
    label: String,
    checked: Boolean,
    onChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = checked, onCheckedChange = onChange)
        Spacer(Modifier.width(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// PERSONA BUTTON COMPOSABLE
@Composable
private fun PersonaButton(
    info: PersonaInfo,
    onClick: (PersonaInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { onClick(info) },
        modifier = modifier.height(40.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
            contentColor   = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Text(
            text = info.name,
            style = MaterialTheme.typography.labelMedium,
            fontSize = 13.sp
        )
    }
}

//  TIME ROW COMPOSABLE
@Composable
private fun TimeRow(
    question: String,
    timeValue: String,
    onPick: () -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        Text(question, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(4.dp))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment   = Alignment.CenterVertically
        ) {
            Text(timeValue, style = MaterialTheme.typography.labelLarge)
            Button(
                onClick = onPick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF73BCEF),
                    contentColor   = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = "Pick time",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text("Pick time", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}
