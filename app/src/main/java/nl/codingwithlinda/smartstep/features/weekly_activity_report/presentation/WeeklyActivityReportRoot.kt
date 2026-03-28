package nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import nl.codingwithlinda.smartstep.R
import nl.codingwithlinda.smartstep.application.SmartStepApplication.Companion.appContainer
import nl.codingwithlinda.smartstep.design_system.form_factors.ScreenOrientation
import nl.codingwithlinda.smartstep.design_system.form_factors.screenFormHelper
import nl.codingwithlinda.smartstep.design_system.ui.theme.SmartStepTheme
import nl.codingwithlinda.smartstep.features.weekly_activity_report.data.WeeklyStatisticsManager
import nl.codingwithlinda.smartstep.features.weekly_activity_report.domain.ReportTarget
import nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.components.TopSummaryCard
import nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.components.WeekPicker
import nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.components.WeeklyBreakdownList
import nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.interaction.ReportTargetAction
import nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.interaction.ReportTargetUiState
import nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.interaction.WeekPickerAction
import nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.interaction.WeekPickerUiState
import nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.model.TopSummaryUi
import nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.model.WeeklyBreakdownUi
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
                        userStatisticsRepo = appContainer.userStatisticsRepo,
                        dailyStepRepo = appContainer.dailyStepRepo,
                        walkDurationRepo = appContainer.walkDurationRepo
                    ),
                    userSettingsRepo = appContainer.userSettingsRepo
                )
            }
        }
    )

    WeeklyActivityAdaptiveScreen(
        targetUiState = reportViewModel.targetUiState.collectAsStateWithLifecycle().value,
        onTargetAction = {
            reportViewModel.onTargetAction(it)
        },
        onNavBack = onNavBack
    ) {
        WeeklyActivityReportScreen(
            topSummaryUi = reportViewModel.topSummaryUi.collectAsStateWithLifecycle().value,
            weekPickerUi = reportViewModel.weekPickerUiState.collectAsStateWithLifecycle().value,
            onWeekPickerAction = reportViewModel::onWeekAction,
            weeklyBreakdownItems = reportViewModel.weekItems.collectAsStateWithLifecycle().value,

        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeklyActivityAdaptiveScreen(
    modifier: Modifier = Modifier,
    targetUiState: ReportTargetUiState,
    onTargetAction: (ReportTargetAction) -> Unit,
    onNavBack:() -> Unit,
    content: @Composable () -> Unit
) {

    val selectedTabIndex = remember(targetUiState.selectedTarget) {
        ReportTarget.entries.indexOf(targetUiState.selectedTarget)
    }
    val screen = screenFormHelper()
    val bottomBarPosition = when (screen.orientation) {
        ScreenOrientation.PORTRAIT,ScreenOrientation.NA
             -> 0
        ScreenOrientation.LANDSCAPE -> 1
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
                        Icon(
                            painter = painterResource(R.drawable.arrow_left),
                            contentDescription = "back"
                        )
                    }
                }
            )
        },
    ) { paddingValues ->
        if (bottomBarPosition == 0) {
            Column(
                modifier = modifier.padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    content()
                }

                WeekReportTargetTabRow(
                    selectedTabIndex = selectedTabIndex,
                    targetUiState = targetUiState,
                    onTargetAction = onTargetAction
                )

            }
        } else Column(modifier = modifier) {
            content()
        }
    }
}

@Composable
fun WeekReportTargetTabRow(
    selectedTabIndex: Int,
    targetUiState: ReportTargetUiState,
    onTargetAction: (ReportTargetAction) -> Unit) {
    PrimaryTabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = Modifier.width(480.dp)
            .clip(RoundedCornerShape(16.dp)),


    ) {
        ReportTarget.entries.forEach { target ->
            with(target.toUi()) {
                Tab(
                    selected = target == targetUiState.selectedTarget,
                    onClick = {
                        onTargetAction(
                            ReportTargetAction.SetTargetAction(
                                target
                            )
                        )
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeklyActivityReportScreen(
    topSummaryUi: TopSummaryUi,
    weekPickerUi: WeekPickerUiState,
    onWeekPickerAction: (WeekPickerAction) -> Unit,
    weeklyBreakdownItems: List<WeeklyBreakdownUi>,
) {


        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopSummaryCard(
                modifier = Modifier
                    .width(480.dp)
                    .padding(16.dp),
                topSummaryUi = topSummaryUi
            )
            WeekPicker(
                modifier = Modifier.width(480.dp),
                uiState = weekPickerUi,
                onAction = {
                    onWeekPickerAction(it)
                }
            )
            WeeklyBreakdownList(
                modifier = Modifier.fillMaxWidth(),
                weekItems = weeklyBreakdownItems
            )
        }
    }


@PreviewScreenSizes
@Composable
private fun PreviewWeeklyReport() {
    SmartStepTheme() {

        WeeklyActivityAdaptiveScreen(
            modifier = Modifier,
            targetUiState = ReportTargetUiState(ReportTarget.STEPS),
            onTargetAction = {},
            onNavBack = {},
            content = {
                WeeklyActivityReportScreen(

                    topSummaryUi = TopSummaryUi(
                    ),
                    weekPickerUi = WeekPickerUiState(),
                    onWeekPickerAction = {},
                    weeklyBreakdownItems = listOf(
                        WeeklyBreakdownUi(),
                        WeeklyBreakdownUi(),
                        WeeklyBreakdownUi(),
                        WeeklyBreakdownUi(),
                        WeeklyBreakdownUi(),
                    ),
                )
            }
        )

    }
}
