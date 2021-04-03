package com.example.recify;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
// loader from stevdsu-san youtube
public class Loader {

    private Activity activity;
    AlertDialog dialog;

    Loader(Activity myActivity){
        activity = myActivity;
    }

    void loadStart() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_loader, null));
        builder.setCancelable(true);

        dialog = builder.create();

        dialog.show();
    }

    void loadEnd() {
        dialog.dismiss();

    }
}
