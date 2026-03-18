package nl.codingwithlinda.smartstep.features.main.statistics.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import nl.codingwithlinda.smartstep.core.presentation.util.UiText

@Composable
fun StatisticsItem(
    icon: Int,
    value: UiText,
    modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = modifier
        )
        Text(value.asString(),
            style = MaterialTheme.typography.labelSmall
        )


    }
}