package nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.model

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import nl.codingwithlinda.smartstep.R
import nl.codingwithlinda.smartstep.core.presentation.util.UiText

data class WeeklyBreakdownUi(
    val dayName: String = "Monday",
    val value: UiText = UiText.DynamicText("100"),
    val unit: UiText = UiText.DynamicText("steps"),
    val status: WeeklyBreakdownStatus = WeeklyBreakdownStatus.NOT_STARTED
)

enum class WeeklyBreakdownStatus{
    FINISHED, IN_PROGRESS, NOT_STARTED
}

@Composable
fun WeeklyBreakdownStatus.WeeklyBreakdownIcon(
    modifier: Modifier = Modifier
){
    val bgColor = when(this){
        WeeklyBreakdownStatus.FINISHED -> MaterialTheme.colorScheme.secondary
        WeeklyBreakdownStatus.IN_PROGRESS -> MaterialTheme.colorScheme.primary.copy(.5f)
        WeeklyBreakdownStatus.NOT_STARTED -> MaterialTheme.colorScheme.surfaceDim
    }
    @Composable
    fun container(icon: Int) = Box(
        contentAlignment = Alignment.Center,
        modifier =
            Modifier.size(48.dp)
            .background(color = bgColor, shape = CircleShape)
                .then(modifier)
    ){
        Image(painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.fillMaxSize().padding(4.dp),
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(
                color = MaterialTheme.colorScheme.primary,
                blendMode = BlendMode.SrcIn
            )
        )
    }
    when(this){
        WeeklyBreakdownStatus.FINISHED -> {
            container(R.drawable.selected_icon)
        }
        WeeklyBreakdownStatus.IN_PROGRESS -> {
            container(R.drawable.circular_arrow)
        }
        WeeklyBreakdownStatus.NOT_STARTED -> {
            container(R.drawable.minus)
        }
    }
}