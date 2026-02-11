package nl.codingwithlinda.smartstep

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.application.SmartStepApplication.Companion.userSettingsRepo
import nl.codingwithlinda.smartstep.core.data.step_tracker.StepTrackerService
import nl.codingwithlinda.smartstep.design.ui.theme.SmartStepTheme
import nl.codingwithlinda.smartstep.features.onboarding.presentation.ShouldShowSettingsViewModel
import nl.codingwithlinda.smartstep.navigation.MainNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var isChecking = true

        val viewModel = ShouldShowSettingsViewModel(userSettingsRepo)

        installSplashScreen()
            .apply {
                setKeepOnScreenCondition {
                    isChecking
                }
            }
        enableEdgeToEdge()


        lifecycleScope.launch {
            viewModel.isChecking.collect {
                isChecking = it
            }
        }

        setContent {
            SmartStepTheme {
                MainNavGraph()
            }
        }


    }
}
