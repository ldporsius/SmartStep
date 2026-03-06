package nl.codingwithlinda.smartstep

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import nl.codingwithlinda.smartstep.application.SmartStepApplication
import nl.codingwithlinda.smartstep.application.di.AndroidDispatcherProvider
import nl.codingwithlinda.smartstep.application.di.AppContainerImpl
import nl.codingwithlinda.smartstep.core.domain.step_tracker_finite_state.SmartStepStateControllerImpl
import nl.codingwithlinda.smartstep.core.domain.step_tracker_finite_state.StepTrackerFiniteState
import nl.codingwithlinda.smartstep.core.domain.util.ObserveAsEvents
import nl.codingwithlinda.smartstep.design_system.ui.theme.SmartStepTheme
import nl.codingwithlinda.smartstep.features.ai_integration.data.finite_state.AIStateController
import nl.codingwithlinda.smartstep.features.ai_integration.data.gemini.core.GeminiAIMessenger
import nl.codingwithlinda.smartstep.features.ai_integration.data.gemini.passive.Gemini_2_5_Config
import nl.codingwithlinda.smartstep.navigation.MainNavGraph


class MainActivity : ComponentActivity(), StepTrackerFiniteState {

    lateinit var smartStepStateController: SmartStepStateControllerImpl

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

        val appContainer = AppContainerImpl(this.application)
        smartStepStateController = SmartStepStateControllerImpl(
            this, appContainer
        )

        val aiMessenger =
            GeminiAIMessenger(
                geminiGonfig = Gemini_2_5_Config(),
                dispatcherProvider = AndroidDispatcherProvider()
            )

        setContent {
            val viewModel = SmartStepApplication.viewModelServiceLocator.createShouldShowSettingsViewModel(this)
            ObserveAsEvents(viewModel.isChecking) {
                isChecking = it
            }

            SmartStepTheme {
                MainNavGraph(
                    appContainer = appContainer,
                    smartStepStateController = smartStepStateController,
                )
            }
        }
    }

    override fun setState() {
        smartStepStateController.onResult()
    }

}
