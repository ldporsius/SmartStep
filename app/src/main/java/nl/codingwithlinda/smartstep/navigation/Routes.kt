package nl.codingwithlinda.smartstep.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable


@Serializable
data object StartRoute: NavKey

@Serializable
data object UserSettingsOnboardingRoute: NavKey

@Serializable
data object UserSettingsRoute: NavKey

@Serializable
data object MainRoute: NavKey


@Serializable
data object AIChatRoute: NavKey

@Serializable
data object WeeklyActivityReportRoute: NavKey
