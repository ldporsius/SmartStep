package nl.codingwithlinda.smartstep.features.settings.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingBoxComponent(
    modifier: Modifier = Modifier,
    label: String = "",
    action: () -> Unit = {},
    content: @Composable () -> Unit,
) {

    Box(modifier = modifier.
    clickable(){
        action()
    }.background(color = MaterialTheme.colorScheme.secondaryContainer, shape = MaterialTheme.shapes.medium)


    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(label)
                content()
            }

            Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = null)

        }
    }
}