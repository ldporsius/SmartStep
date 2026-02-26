package nl.codingwithlinda.smartstep

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import nl.codingwithlinda.smartstep.application.SmartStepApplication.Companion.userSettingsRepo
import nl.codingwithlinda.smartstep.core.domain.step_tracker_finite_state.StepTrackerFiniteState
import nl.codingwithlinda.smartstep.core.domain.util.ObserveAsEvents
import nl.codingwithlinda.smartstep.design_system.ui.theme.SmartStepTheme
import nl.codingwithlinda.smartstep.core.domain.step_tracker_finite_state.SmartStepStateController
import nl.codingwithlinda.smartstep.features.onboarding.presentation.ShouldShowSettingsViewModel
import nl.codingwithlinda.smartstep.navigation.MainNavGraph


class MainActivity : ComponentActivity(), StepTrackerFiniteState {

    lateinit var smartStepStateController: SmartStepStateController
    var isChecking = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
            .apply {
                setKeepOnScreenCondition {
                    isChecking
                }
            }

        enableEdgeToEdge()

        smartStepStateController = SmartStepStateController(this)

        setContent {
            val viewModel = viewModel<ShouldShowSettingsViewModel>(
                factory = viewModelFactory {
                    initializer {
                        ShouldShowSettingsViewModel(
                            userSettingsRepo = userSettingsRepo
                        )
                    }
                }
            )
            ObserveAsEvents(viewModel.isChecking) {
                isChecking = it
            }


            SmartStepTheme {
                MainNavGraph(
                    smartStepStateController = smartStepStateController
                )
            }

            ObserveAsEvents(smartStepStateController.startTracking) {
                it.startTracking()
            }
        }
    }

    override fun setState() {
        smartStepStateController.onResult()
    }

    override fun onResume() {
        println("--- MAIN ACTIVITY --- On resume")
        super.onResume()
        setState()
    }

}
