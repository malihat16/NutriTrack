package com.fit2081.a1_maliha33473692

import androidx.compose.foundation.layout.height
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Psychology

@Composable
fun MyBottomBar(
    onHomeClicked: () -> Unit,
    onInsightsClicked: () -> Unit,
    onNutriCoachClicked: () -> Unit,
    onSettingsClicked: () -> Unit
) {
    NavigationBar(
        modifier = Modifier.height(80.dp)
    ) {
        NavigationBarItem(
            selected = false,
            onClick = onHomeClicked,
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = false,
            onClick = onInsightsClicked,
            icon = { Icon(Icons.Default.BarChart, contentDescription = "Insights") },
            label = { Text("Insights") }
        )
        NavigationBarItem(
            selected = false,
            onClick = onNutriCoachClicked,
            icon = { Icon(Icons.Default.Psychology, contentDescription = "NutriCoach") },
            label = { Text("NutriCoach") }
        )
        NavigationBarItem(
            selected = false,
            onClick = onSettingsClicked,
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            label = { Text("Settings") }
        )
    }
}
