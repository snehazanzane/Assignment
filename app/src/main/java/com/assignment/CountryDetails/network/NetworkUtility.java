package com.assignment.CountryDetails.network;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Button;

import androidx.core.content.ContextCompat;

import com.assignment.CountryDetails.R;

/**
 * The type Network utility.
 */
public class NetworkUtility {
    /**
     * Instantiates a new Network utility.
     */
    NetworkUtility() {
    }

    /**
     * Is connected boolean.
     *
     * @param context the context
     * @return the boolean
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return activeNetwork.isConnected();
            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return activeNetwork.isConnected();
        }
        return false;
    }

    /**
     * Show alert.
     *
     * @param context the context
     */
    public static void showAlert(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("");
        builder.setMessage(context.getString(R.string.msg_No_internet_connection))
                .setCancelable(false)
                .setPositiveButton(context.getString(R.string.str_OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
    }
}
