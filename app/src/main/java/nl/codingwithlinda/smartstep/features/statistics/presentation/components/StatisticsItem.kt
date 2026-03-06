package nl.codingwithlinda.smartstep.features.statistics.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import nl.codingwithlinda.core.domain.util.UiText
import nl.codingwithlinda.smartstep.core.presentation.util.asString

@Composable
fun StatisticsItem(
    icon: Int,
    value: UiText,
    modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(4.dp)
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