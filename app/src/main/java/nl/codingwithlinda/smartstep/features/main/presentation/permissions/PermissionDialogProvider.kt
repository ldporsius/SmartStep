package nl.codingwithlinda.smartstep.features.main.presentation.permissions

interface PermissionDialogProvider {
    fun description(isPermanentlyDeclined: Boolean): String
}