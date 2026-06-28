package com.fit2081.a1_maliha33473692

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fit2081.a1_maliha33473692.utils.PersonaInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonaInfoDialog(
    personaInfo: PersonaInfo,
    onDismiss:    () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape            = RoundedCornerShape(4.dp),
        title            = {
            Text(
                text      = personaInfo.name,
                style     = MaterialTheme.typography.titleMedium
                    .copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
                modifier  = Modifier.fillMaxWidth()
            )
        },
        text = {
            // wrap in a Box -> Column with max height + scroll
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp)                  // cap the height
                    .verticalScroll(rememberScrollState())   // make it scrollable
                    .padding(top = 8.dp)
            ) {
                Column(
                    modifier            = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter           = painterResource(id = personaInfo.imageRes),
                        contentDescription = personaInfo.name,
                        modifier           = Modifier
                            .size(200.dp)
                            .clip(RoundedCornerShape(0.dp)),
                        contentScale       = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text      = personaInfo.description,
                        textAlign = TextAlign.Justify
                    )
                }
            }
        },
        confirmButton = { /* no primary button */ },
        dismissButton = {
            TextButton(
                onClick  = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Dismiss")
            }
        }
    )
}
