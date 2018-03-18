package co.early.password123.ui.passwordchooser;

import android.content.res.Resources;
import android.support.v4.content.ContextCompat;

import java.text.NumberFormat;
import java.util.Locale;

import co.early.password123.CustomApp;
import co.early.password123.R;
import co.early.pwned.feature.PwnedResult;


public class ResultFormatter {

    public static int getResovledColourForSeverity(PwnedResult pwnedResult){
        int colourRes;

        switch (pwnedResult.severity){
            case HELL_NO:
                colourRes = R.color.colorPW123Severity3;
                break;
            case HIGHLY_DISCOURAGE:
                colourRes = R.color.colorPW123Severity2;
                break;
            case BEST_IF_U_DONT:
                colourRes = R.color.colorPW123Severity1;
                break;
            default:
                colourRes = R.color.colorPW123Severity0;
        }

        if (pwnedResult.pwnedState == PwnedResult.IsPwned.TOO_SHORT){
            colourRes = R.color.colorPW123Severity3;
        }

        return ContextCompat.getColor(CustomApp.getInst(), colourRes);
    }

    public static String formatWarningMessage(PwnedResult pwnedResult){

        Resources resources = CustomApp.getInst().getResources();

        if (pwnedResult.pwnedState == PwnedResult.IsPwned.PWNED) {

            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append(resources.getString(pwnedResult.pwnedState.messageResourceId));
            stringBuilder.append(" ");
            stringBuilder.append(resources.getString(
                    pwnedResult.incidenceCount>1 ?
                            R.string.pwned_state_pwned_suffix_pl : R.string.pwned_state_pwned_suffix_sl,
                    NumberFormat.getNumberInstance(Locale.getDefault()).format(pwnedResult.incidenceCount)));

            return stringBuilder.toString();

        }else{
            return resources.getString(pwnedResult.pwnedState.messageResourceId);
        }
    }

    public static String formatErrorMessage(PwnedResult.Error error){

        int maxErrorMessageLength = 100;

        if (error == null){
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("HTTP-");
        stringBuilder.append(error.httpCode);
        stringBuilder.append(" ");
        if (error.message != null) {
            if (error.message.length() > maxErrorMessageLength) {
                stringBuilder.append(error.message.substring(0, maxErrorMessageLength));
                stringBuilder.append("...");
            } else {
                stringBuilder.append(error.message);
            }
        }

        return stringBuilder.toString();
    }

}
