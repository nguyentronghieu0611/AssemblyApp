package com.example.giaothongapp.common;

import android.view.View;
import android.view.ViewGroup;

import com.example.giaothongapp.R;
import com.google.android.material.snackbar.Snackbar;

public class Utils {
    public static void showSnackbar(String message, Snackbar snackbar, ViewGroup layout) {
        snackbar = Snackbar.make(layout, message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        sbView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();
    }
}
