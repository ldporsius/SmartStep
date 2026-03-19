package nl.codingwithlinda.smartstep

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import nl.codingwithlinda.smartstep.application.SmartStepApplication
import nl.codingwithlinda.smartstep.core.data.step_tracker_finite_state.SmartStepStateControllerImpl
import nl.codingwithlinda.smartstep.core.domain.step_tracker_finite_state.SmartStepStateController
import nl.codingwithlinda.smartstep.core.presentation.util.ObserveAsEvents
import nl.codingwithlinda.smartstep.design_system.ui.theme.SmartStepTheme
import nl.codingwithlinda.smartstep.navigation.MainNavGraph


class MainActivity : ComponentActivity(){

    val appContainer = SmartStepApplication.appContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
            .apply {
                setKeepOnScreenCondition {
                   false
                }
            }

        enableEdgeToEdge()

        val smartStepStateController: SmartStepStateController = SmartStepStateControllerImpl.getInstance(
            this, appContainer.stepTracker
        )


        setContent {
            SmartStepTheme {
                MainNavGraph(
                    appContainer = appContainer,
                    smartStepStateController = smartStepStateController,
                )
            }
        }
    }
}
