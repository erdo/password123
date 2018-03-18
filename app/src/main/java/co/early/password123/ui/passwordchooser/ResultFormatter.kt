package co.early.password123.ui.passwordchooser

import co.early.password123.CustomApp
import co.early.password123.R
import co.early.pwned.feature.PwnedResult
import java.text.NumberFormat
import java.util.*


object ResultFormatter {

    fun getResovledColourForSeverity(pwnedResult: PwnedResult): Int {
        var colourRes: Int

        when (pwnedResult.severity) {
            PwnedResult.Severity.HELL_NO -> colourRes = R.color.colorPW123Severity3
            PwnedResult.Severity.HIGHLY_DISCOURAGE -> colourRes = R.color.colorPW123Severity2
            PwnedResult.Severity.BEST_IF_U_DONT -> colourRes = R.color.colorPW123Severity1
            else -> colourRes = R.color.colorPW123Severity0
        }

        if (pwnedResult.pwnedState == PwnedResult.IsPwned.TOO_SHORT) {
            colourRes = R.color.colorPW123Severity3
        }

        return CustomApp.inst!!.getResources().getColor(colourRes)
    }

    fun formatWarningMessage(pwnedResult: PwnedResult): String {

        val resources = CustomApp.inst!!.getResources()

        if (pwnedResult.pwnedState == PwnedResult.IsPwned.PWNED) {

            val stringBuilder = StringBuilder()

            stringBuilder.append(resources.getString(pwnedResult.pwnedState.messageResourceId))
            stringBuilder.append(" ")
            stringBuilder.append(resources.getString(
                    if (pwnedResult.incidenceCount > 1)
                        R.string.pwned_state_pwned_suffix_pl
                    else
                        R.string.pwned_state_pwned_suffix_sl,
                    NumberFormat.getNumberInstance(Locale.getDefault()).format(pwnedResult.incidenceCount.toLong())))

            return stringBuilder.toString()

        } else {
            return resources.getString(pwnedResult.pwnedState.messageResourceId)
        }
    }

    fun formatErrorMessage(error: PwnedResult.Error?): String? {

        val maxErrorMessageLength = 100

        if (error == null) {
            return null
        }

        val stringBuilder = StringBuilder()
        stringBuilder.append("HTTP-")
        stringBuilder.append(error.httpCode)
        stringBuilder.append(" ")
        if (error.message != null) {
            if (error.message.length > maxErrorMessageLength) {
                stringBuilder.append(error.message.substring(0, maxErrorMessageLength))
                stringBuilder.append("...")
            } else {
                stringBuilder.append(error.message)
            }
        }

        return stringBuilder.toString()
    }

}
