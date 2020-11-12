package com.example.assemblyapp.common;

import android.view.View;
import android.view.ViewGroup;

import com.example.assemblyapp.R;
import com.google.android.material.snackbar.Snackbar;

public class Utils {
    public static final String MY_REF = "my_pref";
    public static void showSnackbar(String message, ViewGroup layout) {
        Snackbar snackbar = Snackbar.make(layout, message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        sbView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();
    }
}
