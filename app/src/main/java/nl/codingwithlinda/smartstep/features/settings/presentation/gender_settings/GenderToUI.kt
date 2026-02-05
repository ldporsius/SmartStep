package nl.codingwithlinda.smartstep.features.settings.presentation.gender_settings

import nl.codingwithlinda.smartstep.R
import nl.codingwithlinda.smartstep.core.domain.model.settings.Gender
import nl.codingwithlinda.smartstep.core.domain.util.UiText

fun Gender.toUi(): UiText{
    return when(this){
        Gender.MALE -> UiText.StringResourceText(R.string.male)
        Gender.FEMALE -> UiText.StringResourceText(R.string.female)
    }
}