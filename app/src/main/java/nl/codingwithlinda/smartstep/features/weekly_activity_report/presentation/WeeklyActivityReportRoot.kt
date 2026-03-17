package nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import nl.codingwithlinda.smartstep.R
import nl.codingwithlinda.smartstep.application.SmartStepApplication.Companion.appContainer
import nl.codingwithlinda.smartstep.features.weekly_activity_report.data.WeeklyStatisticsManager
import nl.codingwithlinda.smartstep.features.weekly_activity_report.domain.ReportTarget
import nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.components.TopSummaryCard
import nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.interaction.ReportTargetAction
import nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.util.toUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeklyActivityReportRoot(
    onNavBack:() -> Unit
) {
    val reportViewModel = viewModel<ReportViewModel>(
        factory = viewModelFactory {
            initializer {
                ReportViewModel(
                    weeklyStatisticsManager = WeeklyStatisticsManager(
                        userSettingsRepo = appContainer.userSettingsRepo,
                        dailyStepRepo = appContainer.dailyStepRepo
                    )
                )
            }
        }
    )

    val uiState = reportViewModel.uiState.collectAsStateWithLifecycle().value

    val selectedTabIndex = remember(uiState.selectedTarget) {
        ReportTarget.entries.indexOf(uiState.selectedTarget)
    }

    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Report")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onNavBack()
                    }) {
                        Icon(painter = painterResource(R.drawable.arrow), contentDescription = "back")
                    }
                }
            )
        },
        bottomBar = {
            PrimaryTabRow(
                selectedTabIndex = selectedTabIndex
            ) {
                ReportTarget.entries.forEach {target ->
                    with(target.toUi()) {
                        Tab(
                            selected = target == uiState.selectedTarget,
                            onClick = {
                                reportViewModel.onAction(ReportTargetAction.SetTargetAction(target))
                            },
                            text = { Text(text) },
                            icon = {
                                Icon(painter = painterResource(icon), contentDescription = null)
                            }
                        )
                    }
                }

            }
        }
    ) {paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            TopSummaryCard(
                modifier = Modifier.
                    width(480.dp)
                    .padding(16.dp),
                topSummaryUi = reportViewModel.topSummaryUi.collectAsStateWithLifecycle().value
            )


        }
    }
}