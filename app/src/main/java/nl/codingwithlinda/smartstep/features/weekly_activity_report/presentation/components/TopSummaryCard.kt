package nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import nl.codingwithlinda.smartstep.core.presentation.util.asString
import nl.codingwithlinda.smartstep.design_system.ui.theme.SmartStepTheme
import nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.model.TopSummaryUi

@Composable
fun TopSummaryCard(
    modifier: Modifier = Modifier,
    topSummaryUi: TopSummaryUi
) {

        Surface(
            modifier = modifier,
            color = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            shape = RoundedCornerShape(16.dp)
         ) {
            ConstraintLayout(
                modifier = Modifier.padding(16.dp)
            ) {
                val (
                    title,
                    value,
                    subtitle,
                    timeSpan
                ) = createRefs()


                Text(
                    text = topSummaryUi.title,
                    modifier = Modifier.constrainAs(title) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    },
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = topSummaryUi.value.toString(),
                    modifier = Modifier.constrainAs(value) {
                        top.linkTo(title.bottom, margin = 4.dp)
                        start.linkTo(parent.start, margin = 4.dp)
                    },
                    style = MaterialTheme.typography.headlineLarge

                )
                Text(
                    text = topSummaryUi.subtitle.asString(),
                    modifier = Modifier.constrainAs(subtitle) {
                        top.linkTo(value.bottom, margin = 16.dp)
                        start.linkTo(parent.start)
                    },
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = topSummaryUi.timeSpan,
                    modifier = Modifier.constrainAs(timeSpan) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                    }
                )
            }
        }
}

@Preview
@Composable
private fun PreviewTopSummaryCard() {
    SmartStepTheme() {
        TopSummaryCard(
            modifier = Modifier.width(480.dp),
            topSummaryUi = TopSummaryUi(

            )
        )
    }
}