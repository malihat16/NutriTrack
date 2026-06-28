package com.fit2081.a1_maliha33473692

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.fit2081.a1_maliha33473692.data.ThemePreferences
import com.fit2081.a1_maliha33473692.data.api.RetrofitInstance
import com.fit2081.a1_maliha33473692.data.database.NutriTrackDatabase
import com.fit2081.a1_maliha33473692.data.repository.NutriCoachRepository
import com.fit2081.a1_maliha33473692.data.repository.PatientRepository
import com.fit2081.a1_maliha33473692.ui.screens.ClinicianScreen
import com.fit2081.a1_maliha33473692.ui.screens.FoodIntakeQuestionnaireScreen
import com.fit2081.a1_maliha33473692.ui.screens.HomeScreen
import com.fit2081.a1_maliha33473692.ui.screens.InsightsScreen
import com.fit2081.a1_maliha33473692.ui.screens.LoginScreen
import com.fit2081.a1_maliha33473692.ui.screens.NutriCoachScreen
import com.fit2081.a1_maliha33473692.ui.screens.RegisterScreen
import com.fit2081.a1_maliha33473692.ui.screens.SettingsScreen
import com.fit2081.a1_maliha33473692.ui.screens.WelcomeScreen
import com.fit2081.a1_maliha33473692.ui.theme.A1_maliha33473692Theme
import com.fit2081.a1_maliha33473692.viewmodel.ClinicianViewModel
import com.fit2081.a1_maliha33473692.viewmodel.ClinicianViewModelFactory
import com.fit2081.a1_maliha33473692.viewmodel.NutriCoachViewModel
import com.fit2081.a1_maliha33473692.viewmodel.NutriCoachViewModelFactory
import com.fit2081.a1_maliha33473692.viewmodel.ThemeViewModel
import com.fit2081.a1_maliha33473692.viewmodel.ThemeViewModelFactory

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object Register : Screen("register")
    object Questionnaire : Screen("questionnaire/{userId}") {
        fun createRoute(id: Int) = "questionnaire/$id"
    }

    object Home : Screen("home/{userId}") {
        fun createRoute(id: Int) = "home/$id"
    }

    object Insights : Screen("insights/{userId}") {
        fun createRoute(id: Int) = "insights/$id"
    }

    object NutriCoach : Screen("nutricoach/{userId}") {
        fun createRoute(id: Int) = "nutricoach/$id"
    }

    object Settings : Screen("settings/{userId}") {
        fun createRoute(id: Int) = "settings/$id"
    }

    object ClinicianLogin : Screen("clinician_login/{userId}") {
        fun createRoute(id: Int) = "clinician_login/$id"
    }
}

class MainActivity : ComponentActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContent {
                val ctx = LocalContext.current

                // Theme
                val themeVm: ThemeViewModel = viewModel(
                    factory = ThemeViewModelFactory(ctx)
                )
                val isDarkMode by ThemePreferences
                    .darkModeFlow(ctx)
                    .collectAsState(initial = false)

                A1_maliha33473692Theme(darkTheme = isDarkMode) {
                    val navController = rememberNavController()
                    val db = remember { NutriTrackDatabase.getInstance(ctx) }

                    NavHost(
                        navController = navController,
                        startDestination = Screen.Welcome.route
                    ) {
                        // 1) Welcome
                        composable(Screen.Welcome.route) {
                            WelcomeScreen(onLoginClicked = {
                                navController.navigate(Screen.Login.route)
                            })
                        }

                        // 2) Login
                        composable(Screen.Login.route) {
                            LoginScreen(
                                onLoginSuccess = { patient ->
                                    navController.navigate(
                                        Screen.Questionnaire.createRoute(patient.userId)
                                    ) {
                                        popUpTo(Screen.Login.route) { inclusive = true }
                                    }
                                },
                                onRegisterClicked = {
                                    navController.navigate(Screen.Register.route)
                                }
                            )
                        }

                        // 3) Register
                        composable(Screen.Register.route) {
                            RegisterScreen(navController)
                        }

                        // 4) Questionnaire
                        composable(
                            Screen.Questionnaire.route,
                            arguments = listOf(navArgument("userId") { type = NavType.IntType })
                        ) { backStack ->
                            val userId = backStack.arguments!!.getInt("userId")
                            FoodIntakeQuestionnaireScreen(
                                patientId   = userId,
                                onSaveClicked = {
                                    navController.navigate(Screen.Home.createRoute(userId)) {
                                        popUpTo(Screen.Questionnaire.route) { inclusive = true }
                                    }
                                },
                                // instead of popBackStack(), do:
                                onBackClicked = {
                                    navController.navigate(Screen.Welcome.route) {
                                        // clear everything so this becomes the new root
                                        popUpTo(0)
                                    }
                                },
                                onSkipClicked = {
                                    navController.navigate(Screen.Home.createRoute(userId)) {
                                        popUpTo(Screen.Questionnaire.route) { inclusive = true }
                                    }
                                }
                            )
                        }


                        // 5) Home
                        composable(
                            Screen.Home.route,
                            arguments = listOf(navArgument("userId") {
                                type = NavType.IntType
                            })
                        ) { backStack ->
                            val userId = backStack.arguments!!.getInt("userId")
                            HomeScreen(
                                userId = userId,
                                onEditClicked = {
                                    navController.navigate(Screen.Questionnaire.createRoute(userId))
                                },
                                onInsightsClicked = {
                                    navController.navigate(Screen.Insights.createRoute(userId))
                                },
                                onNutriCoachClicked = {
                                    navController.navigate(Screen.NutriCoach.createRoute(userId))
                                },
                                onSettingsClicked = {
                                    navController.navigate(Screen.Settings.createRoute(userId))
                                }
                            )
                        }

                        // 6) Insights
                        composable(
                            Screen.Insights.route,
                            arguments = listOf(navArgument("userId") {
                                type = NavType.IntType
                            })
                        ) { backStack ->
                            val userId = backStack.arguments!!.getInt("userId")
                            InsightsScreen(
                                userId = userId,
                                onHomeClicked = {
                                    navController.navigate(Screen.Home.createRoute(userId))
                                },
                                onNutriCoachClicked = {
                                    navController.navigate(Screen.NutriCoach.createRoute(userId))
                                },
                                onSettingsClicked = {
                                    navController.navigate(Screen.Settings.createRoute(userId))
                                }
                            )
                        }

                        // 7) NutriCoach
                        composable(
                            Screen.NutriCoach.route,
                            arguments = listOf(navArgument("userId") {
                                type = NavType.IntType
                            })
                        ) { backStack ->
                            val userId = backStack.arguments!!.getInt("userId")
                            val pRepo = remember { PatientRepository(db.patientDao()) }
                            val tipRepo = remember {
                                NutriCoachRepository(
                                    dao = db.nutriCoachTipDao(),
                                    fruityViceApi = RetrofitInstance.api
                                )
                            }
                            val vm: NutriCoachViewModel = viewModel(
                                factory = NutriCoachViewModelFactory(
                                    tipRepo = tipRepo,
                                    patientRepo = pRepo,
                                    userId = userId
                                )
                            )
                            NutriCoachScreen(
                                viewModel = vm,
                                onHomeClicked = {
                                    navController.navigate(
                                        Screen.Home.createRoute(
                                            userId
                                        )
                                    )
                                },
                                onInsightsClicked = {
                                    navController.navigate(
                                        Screen.Insights.createRoute(
                                            userId
                                        )
                                    )
                                },
                                onNutriCoachClicked = { /* no-op */ },
                                onSettingsClicked = {
                                    navController.navigate(
                                        Screen.Settings.createRoute(
                                            userId
                                        )
                                    )
                                }
                            )
                        }

                        // 8) Settings
                        composable(
                            Screen.Settings.route,
                            arguments = listOf(navArgument("userId") {
                                type = NavType.IntType
                            })
                        ) { backStack ->
                            val userId = backStack.arguments!!.getInt("userId")
                            SettingsScreen(
                                userId = userId,
                                onLogoutClicked = {
                                    navController.navigate(Screen.Login.route) {
                                        popUpTo(Screen.Welcome.route) { inclusive = true }
                                    }
                                },
                                onClinicianLogin = {
                                    navController.navigate(Screen.ClinicianLogin.createRoute(userId))
                                },
                                onHomeClicked = {
                                    navController.navigate(Screen.Home.createRoute(userId))
                                },
                                onNutriCoachClicked = {
                                    navController.navigate(Screen.NutriCoach.createRoute(userId))
                                },
                                onInsightsClicked = {
                                    navController.navigate(Screen.Insights.createRoute(userId))
                                }
                            )
                        }

                        // 9) Clinician login
                        composable(
                            Screen.ClinicianLogin.route,
                            arguments = listOf(navArgument("userId") { type = NavType.IntType })
                        ) { backStack ->
                            val userId = backStack.arguments!!.getInt("userId")

                            val patientRepo = remember { PatientRepository(db.patientDao()) }
                            val genAiRepo = remember {
                                NutriCoachRepository(
                                    dao = db.nutriCoachTipDao(),
                                    fruityViceApi = RetrofitInstance.api
                                )
                            }
                            val vm: ClinicianViewModel = viewModel(
                                factory = ClinicianViewModelFactory(patientRepo, genAiRepo)
                            )

                            ClinicianScreen(
                                viewModel = vm,
                                onBack = { navController.popBackStack() },
                                onHomeClicked = {
                                    navController.navigate(
                                        Screen.Home.createRoute(
                                            userId
                                        )
                                    )
                                },
                                onInsightsClicked = {
                                    navController.navigate(
                                        Screen.Insights.createRoute(
                                            userId
                                        )
                                    )
                                },
                                onNutriCoachClicked = {
                                    navController.navigate(
                                        Screen.NutriCoach.createRoute(
                                            userId
                                        )
                                    )
                                },
                                onSettingsClicked = {
                                    navController.navigate(
                                        Screen.Settings.createRoute(
                                            userId
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
