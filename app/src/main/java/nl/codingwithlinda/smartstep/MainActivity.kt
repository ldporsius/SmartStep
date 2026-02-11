package nl.codingwithlinda.smartstep

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.DisposableEffect
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.rememberLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.application.SmartStepApplication.Companion.userSettingsRepo
import nl.codingwithlinda.smartstep.design.ui.theme.SmartStepTheme
import nl.codingwithlinda.smartstep.features.onboarding.presentation.ShouldShowSettingsViewModel
import nl.codingwithlinda.smartstep.navigation.MainNavGraph

class MainActivity : ComponentActivity() {

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

            val lifecycleOwner = rememberLifecycleOwner()
            DisposableEffect(lifecycleOwner) {
                val observer = LifecycleEventObserver{_, event ->
                    if(event == Lifecycle.Event.ON_START){
                        lifecycleScope.launch {
                            viewModel.isChecking.collect {
                                isChecking = it
                            }
                        }
                    }
                }
                lifecycleOwner.lifecycle.addObserver(observer)
                onDispose {
                    lifecycleOwner.lifecycle.removeObserver(observer)
                }
            }

            SmartStepTheme {
                MainNavGraph()
            }
        }

    }
}
