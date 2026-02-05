package nl.codingwithlinda.smartstep.features.main.presentation.permissions

class BodySensorsPermissionDialogProvider: PermissionDialogProvider {
    override fun description(isPermanentlyDeclined: Boolean): String {
        if (isPermanentlyDeclined) {
            return "It seems like you permanently declined Body Sensors permission. " +
                    "You can go to the app settings to grant it again."
        }
        return "This app needs access to your body sensors to track your steps."
    }
}