package nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nl.codingwithlinda.smartstep.R
import nl.codingwithlinda.smartstep.design_system.ui.theme.SmartStepTheme

@Composable
fun WeekPicker(
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        IconButton(
            onClick = { /*TODO*/ }
        ) {
            Icon(painter = painterResource(R.drawable.arrow_left), contentDescription = null)
        }
        IconButton(
            onClick = { /*TODO*/ }
        ) {
            Icon(painter = painterResource(R.drawable.arrow_right), contentDescription = null)
        }
    }
}

@Preview
@Composable
private fun PreviewWeekPicker() {
    SmartStepTheme() {
        WeekPicker(
            modifier = Modifier.width(480.dp)
        )
    }
}