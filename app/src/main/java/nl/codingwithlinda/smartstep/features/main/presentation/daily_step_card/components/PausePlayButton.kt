package nl.codingwithlinda.smartstep.features.main.presentation.daily_step_card.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import nl.codingwithlinda.smartstep.R

@Composable
fun PausePlayButton(
    isPaused: Boolean,
    actionPause: () -> Unit,
    actionPlay: () -> Unit,
    iconModifier: Modifier = Modifier) {


    when(isPaused){
        true -> {
            Icon(painter = painterResource(R.drawable.polygon_1),
                contentDescription = "play",
                modifier = iconModifier
                    .then(
                        Modifier.clickable(){
                            actionPlay()
                        }
                    )
            )
        }
        false -> {
            Icon(painter = painterResource(R.drawable.pause),
                contentDescription = "pause",
                modifier = iconModifier
                    .then(
                        Modifier.clickable(){
                            actionPause()
                        }
                    )
            )
        }
    }

}