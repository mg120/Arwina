package com.tamiuz.arwina.utils;

import android.app.ProgressDialog;
import android.content.Context;


/**
 * Created by Mahmoud on 10/16/17.
 */

public class DialogUtil {

    public DialogUtil() {
    }

    public static ProgressDialog showProgressDialog(Context context, String message, boolean cancelable) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage(message);
        dialog.setCancelable(cancelable);
        dialog.show();
        return dialog;
    }
}
