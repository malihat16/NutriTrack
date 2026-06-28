package com.fit2081.a1_maliha33473692.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.fit2081.a1_maliha33473692.MyBottomBar
import com.fit2081.a1_maliha33473692.data.database.NutriTrackDatabase
import com.fit2081.a1_maliha33473692.data.repository.FoodIntakeRepository
import com.fit2081.a1_maliha33473692.data.repository.PatientRepository
import com.fit2081.a1_maliha33473692.viewmodel.HomeViewModel
import com.fit2081.a1_maliha33473692.viewmodel.HomeViewModelFactory
import com.fit2081.a1_maliha33473692.viewmodel.ThemeViewModel
import com.fit2081.a1_maliha33473692.viewmodel.ThemeViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    userId: Int,
    onLogoutClicked:     () -> Unit,
    onClinicianLogin:   () -> Unit,
    onHomeClicked:      () -> Unit,
    onNutriCoachClicked:() -> Unit,
    onInsightsClicked:  () -> Unit
) {
    val context = LocalContext.current
    val db      = remember { NutriTrackDatabase.getInstance(context) }
    val pRepo   = remember { PatientRepository(db.patientDao()) }
    val iRepo   = remember { FoodIntakeRepository(db.foodIntakeDao()) }
    val vm: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(pRepo, iRepo, userId)
    )
    val patient by vm.patient.observeAsState()

    // sheet state
    var isSheetOpen by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    // dark-mode VM
    val themeVm: ThemeViewModel = viewModel(
        factory = ThemeViewModelFactory(LocalContext.current)
    )
    val isDarkEnabled by themeVm.isDarkMode.collectAsState()

    // passphrase
    var passInput by rememberSaveable { mutableStateOf("") }
    var passError by rememberSaveable { mutableStateOf<String?>(null) }
    val sheetScroll = rememberScrollState()
    var passVisible   by rememberSaveable { mutableStateOf(false) }


    // the clinician-login sheet
    if (isSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch { sheetState.hide() }
                isSheetOpen = false
            },
            sheetState = sheetState
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(sheetScroll)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Clinician Login",
                    style = MaterialTheme.typography.headlineSmall
                        .copy(fontWeight = FontWeight.Bold)
                )
                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = passInput,
                    onValueChange = {
                        passInput = it
                        passError = null
                    },
                    label = { Text("Enter clinician key") },
                    isError = passError != null,
                    singleLine = true,
                    visualTransformation = if (passVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passVisible)
                            Icons.Default.VisibilityOff
                        else
                            Icons.Default.Visibility

                        IconButton(onClick = { passVisible = !passVisible }) {
                            Icon(
                                imageVector = image,
                                contentDescription =
                                if (passVisible) "Hide passphrase" else "Show passphrase"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )




                passError?.let {
                    Text(
                        it,
                        color    = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (passInput == "dollar-entry-apples") {
                            scope.launch {
                                sheetState.hide()
                                isSheetOpen = false
                            }
                            onClinicianLogin()
                        } else {
                            passError = "Incorrect passphrase"
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Login")
                }
            }
        }
    }

    // the main settings screen
    Scaffold(
        bottomBar = {
            MyBottomBar(
                onHomeClicked       = onHomeClicked,
                onInsightsClicked   = onInsightsClicked,
                onNutriCoachClicked = onNutriCoachClicked,
                onSettingsClicked   = { /* no-op */ }
            )
        }
    ) { padding ->
        val scrollState = rememberScrollState()
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // header
            Text(
                "Settings",
                style = MaterialTheme.typography.headlineMedium
                    .copy(fontWeight = FontWeight.Bold)
            )
            Spacer(Modifier.height(24.dp))

            // account
            Text("ACCOUNT", style = MaterialTheme.typography.labelLarge)
            patient?.let { user ->
                ListItem(
                    leadingContent    = { Icon(Icons.Default.AccountCircle, null) },
                    headlineContent   = { Text(user.name ?: "–") },
                    supportingContent = { Text("User ID: ${user.userId}") }
                )
                ListItem(
                    leadingContent  = { Icon(Icons.Default.Call, null) },
                    headlineContent = { Text(user.phoneNumber) }
                )
                ListItem(
                    leadingContent  = { Icon(Icons.Default.Badge, null) },
                    headlineContent = { Text(user.sex) }
                )
            } ?: Box(
                Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }

            Divider(Modifier.padding(vertical = 16.dp))

            // other settings
            Text("OTHER SETTINGS", style = MaterialTheme.typography.labelLarge)

            // Dark mode toggle
            ListItem(
                leadingContent = {
                    Icon(
                        imageVector = Icons.Default.DarkMode,
                        contentDescription = "Dark mode icon"
                    )
                },
                headlineContent = {
                    Text("Dark Mode")
                },
                trailingContent = {
                    Switch(
                        checked = isDarkEnabled,
                        onCheckedChange = { themeVm.setDarkMode(it) }
                    )
                },
                modifier = Modifier.clickable {
                    // letting click the whole row toggle it too
                    themeVm.setDarkMode(!isDarkEnabled)
                }
            )
            // Logout
            ListItem(
                modifier        = Modifier.clickable { onLogoutClicked() },
                leadingContent  = { Icon(Icons.Default.Logout, null) },
                headlineContent = { Text("Logout") }
            )

            // Clinician Login
            ListItem(
                modifier        = Modifier.clickable {
                    isSheetOpen = true
                    scope.launch { sheetState.show() }
                },
                leadingContent  = { Icon(Icons.Default.AdminPanelSettings, null) },
                headlineContent = { Text("Clinician Login") }
            )

        }
    }
}