package com.example.findmykidappparents.sessions;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class AlertDialogManager {
    public AlertDialogManager() {
    }

    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = (new Builder(context)).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        if (status != null) {
            alertDialog.setIcon(status ? 2130837507 : 2130837504);
        }

        alertDialog.setButton("OK", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();
    }
}