package nl.codingwithlinda.smartstep.features.settings.presentation.gender_settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import nl.codingwithlinda.smartstep.core.domain.model.Gender
import nl.codingwithlinda.smartstep.core.presentation.util.asString
import nl.codingwithlinda.smartstep.features.settings.presentation.common.SettingBoxComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderComponent(
    currentGender: Gender,
    action: (Gender) -> Unit,
    modifier: Modifier = Modifier) {

    var isGenderExpanded by rememberSaveable { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = isGenderExpanded,
        onExpandedChange = {
            isGenderExpanded = it
        },
        modifier = modifier
        ) {

        SettingBoxComponent(
            label = "Gender",
            action = { isGenderExpanded = true },
            modifier = Modifier
                .semantics(){
                    contentDescription = "Gender"
                }
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
        ) {
            Text(text = currentGender.toUi().asString(),
                modifier = Modifier
            )
        }

        ExposedDropdownMenu(
            expanded = isGenderExpanded,
            onDismissRequest = { isGenderExpanded = false }
        ) {
            Gender.entries.forEach {
                val isSelectedItem = it == currentGender
                DropdownMenuItem(
                    text = { Text(text = it.toUi().asString()) },
                    onClick = {
                        action(it)
                        isGenderExpanded = false
                    },
                    trailingIcon = {
                        if (isSelectedItem) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null)
                        }
                    }
                )
            }
        }
    }
}