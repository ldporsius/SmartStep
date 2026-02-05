package nl.codingwithlinda.smartstep.features.settings.presentation.gender_settings.state

import nl.codingwithlinda.smartstep.core.domain.model.settings.Gender

interface ActionGender {
    data class ChangeGender(val gender: Gender): ActionGender
}

