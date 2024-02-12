package com.example.eventfinder.Util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class EventSpinner extends DialogFragment {

    private ProgressDialog progressDialog;
    //private EntwineTracker entwineTracker;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //entwineTracker = EntwineTracker.getInstance(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        try {
            progressDialog = new ProgressDialog(getActivity());
            this.setStyle(STYLE_NO_TITLE, getTheme());
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setIndeterminate(true);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        } catch (Exception e) {
            //entwineTracker.sendException(e);
        }
        return progressDialog;
    }
}
