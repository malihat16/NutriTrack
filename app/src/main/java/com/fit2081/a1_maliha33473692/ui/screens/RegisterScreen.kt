package com.fit2081.a1_maliha33473692.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.fit2081.a1_maliha33473692.data.database.NutriTrackDatabase
import com.fit2081.a1_maliha33473692.data.repository.PatientRepository
import com.fit2081.a1_maliha33473692.viewmodel.AuthViewModel
import com.fit2081.a1_maliha33473692.viewmodel.AuthViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {
    val context = LocalContext.current
    val repo    = remember { PatientRepository(NutriTrackDatabase.getInstance(context).patientDao()) }
    val vm: AuthViewModel = viewModel(factory = AuthViewModelFactory(repo))

    val user    by vm.user.observeAsState()
    val error   by vm.error.observeAsState()
    var localError by rememberSaveable { mutableStateOf<String?>(null) }

    var phoneText by rememberSaveable { mutableStateOf("") }
    var nameText  by rememberSaveable { mutableStateOf("") }
    var pwText    by rememberSaveable { mutableStateOf("") }
    var pw2Text   by rememberSaveable { mutableStateOf("") }

    // visibility toggles
    var pwVisible1 by rememberSaveable { mutableStateOf(false) }
    var pwVisible2 by rememberSaveable { mutableStateOf(false) }

    val ids by vm.userIds.observeAsState(emptyList())
    var expanded   by rememberSaveable { mutableStateOf(false) }
    var selectedId by rememberSaveable { mutableStateOf<Int?>(null) }

    // Snackbar + navigate back after success
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(user) {
        if (user != null) {
            snackbarHostState.showSnackbar("Registration successful! Please log in.")
            navController.popBackStack()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Register",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))

            // ID dropdown
            Text("My ID (Clinician-provided)")
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedId?.toString() ?: "",
                    onValueChange = {},
                    label = { Text("Select your ID") },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    ids.forEach { id ->
                        DropdownMenuItem(
                            text = { Text(id.toString()) },
                            onClick = {
                                selectedId = id
                                expanded   = false
                            }
                        )
                    }
                }
            }
            Spacer(Modifier.height(9.dp))

            // Phone (digits only)
            OutlinedTextField(
                value = phoneText,
                onValueChange = { if (it.all(Char::isDigit)) phoneText = it },
                label = { Text("Phone Number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            // Name (letters and spaces only)
            OutlinedTextField(
                value = nameText,
                onValueChange = { input ->
                    // only update if every character is a letter or a space
                    if (input.all { ch -> ch.isLetter() || ch == ' ' }) {
                        nameText = input
                    }
                },
                label = { Text("Your Name") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            // Password field with toggle
            OutlinedTextField(
                value = pwText,
                onValueChange = { pwText = it },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = if (pwVisible1) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { pwVisible1 = !pwVisible1 }) {
                        Icon(
                            imageVector = if (pwVisible1) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (pwVisible1) "Hide password" else "Show password"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            // Confirm password with toggle
            OutlinedTextField(
                value = pw2Text,
                onValueChange = { pw2Text = it },
                label = { Text("Confirm Password") },
                singleLine = true,
                visualTransformation = if (pwVisible2) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { pwVisible2 = !pwVisible2 }) {
                        Icon(
                            imageVector = if (pwVisible2) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (pwVisible2) "Hide password" else "Show password"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            // error messages
            localError?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(8.dp))
            }
            error?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(8.dp))
            }

            Text(
                "This app is only for pre-registered users. Please enter your ID, phone number and password to claim your account.",
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    localError = when {
                        selectedId == null -> "Please select your ID"
                        phoneText.isBlank() || nameText.isBlank() || pwText.isBlank() || pw2Text.isBlank() ->
                            "Please fill in all fields"
                        pwText != pw2Text -> "Passwords do not match"
                        else -> null
                    }
                    if (localError == null) {
                        vm.claimAccount(
                            selectedId!!,
                            phoneText.trim(),
                            nameText.trim(),
                            pwText
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Register")
            }

            Spacer(Modifier.height(8.dp))

            TextButton(onClick = { navController.popBackStack() }) {
                Text("Back to Login")
            }
        }
    }
}
