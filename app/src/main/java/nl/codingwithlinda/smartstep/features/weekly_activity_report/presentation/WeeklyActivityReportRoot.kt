package nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import nl.codingwithlinda.smartstep.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeklyActivityReportRoot(
    onNavBack:() -> Unit
) {

    Scaffold(
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
        }
    ) {paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)

        ) {



        }
    }
}