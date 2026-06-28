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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fit2081.a1_maliha33473692.data.database.NutriTrackDatabase
import com.fit2081.a1_maliha33473692.data.entity.PatientEntity
import com.fit2081.a1_maliha33473692.data.repository.PatientRepository
import com.fit2081.a1_maliha33473692.viewmodel.LoginState
import com.fit2081.a1_maliha33473692.viewmodel.LoginViewModel
import com.fit2081.a1_maliha33473692.viewmodel.LoginViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: (PatientEntity) -> Unit,
    onRegisterClicked: () -> Unit
) {
    val context = LocalContext.current
    val repo    = remember {
        PatientRepository(
            NutriTrackDatabase
                .getInstance(context)
                .patientDao()
        )
    }
    val vm: LoginViewModel = viewModel(factory = LoginViewModelFactory(repo))

    val ids by vm.userIds.observeAsState(emptyList())
    var expanded       by rememberSaveable { mutableStateOf(false) }
    var selectedId     by rememberSaveable { mutableStateOf<Int?>(null) }
    var passwordInput  by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val state = vm.loginState

    // react to a successful login:
    LaunchedEffect(state) {
        if (state is LoginState.Success) {
            onLoginSuccess(state.patient)
            vm.reset()
        }
    }

    val scrollState = rememberScrollState()

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(24.dp),
        verticalArrangement   = Arrangement.Center,
        horizontalAlignment   = Alignment.CenterHorizontally
    ) {
        Text(
            "Log In",
            style = MaterialTheme.typography.headlineMedium
                .copy(fontWeight = FontWeight.Bold)
        )
        Spacer(Modifier.height(24.dp))

        // User ID DROPDOWN
        Text("My ID (Provided by your Clinician)")
        ExposedDropdownMenuBox(
            expanded         = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value         = selectedId?.toString() ?: "",
                onValueChange = {},
                label         = { Text("Select your ID") },
                readOnly      = true,
                trailingIcon  = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier      = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded        = expanded,
                onDismissRequest = { expanded = false }
            ) {
                ids.forEach { id ->
                    DropdownMenuItem(
                        text    = { Text(id.toString()) },
                        onClick = {
                            selectedId = id
                            expanded   = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Password FIELD with visibility toggle
        OutlinedTextField(
            value               = passwordInput,
            onValueChange       = { passwordInput = it },
            label               = { Text("Password") },
            visualTransformation =
            if (passwordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            keyboardOptions     = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine          = true,
            trailingIcon        = {
                val image = if (passwordVisible)
                    Icons.Default.VisibilityOff
                else
                    Icons.Default.Visibility

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = image,
                        contentDescription =
                        if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            },
            modifier            = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        if (state is LoginState.Error) {
            Text(
                text  = state.message,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(Modifier.height(24.dp))

        // Login Button
        Button(
            onClick = {
                val id = selectedId ?: return@Button
                vm.login(id, passwordInput)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("Login")
        }

        Spacer(Modifier.height(16.dp))

        // Register Button
        Button(
            onClick  = onRegisterClicked,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("Register")
        }
    }
}
