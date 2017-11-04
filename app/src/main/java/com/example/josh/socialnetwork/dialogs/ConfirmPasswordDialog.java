package com.example.josh.socialnetwork.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.josh.socialnetwork.R;

/**
 * Created by jbghostman on 04/11/17.
 */

public class ConfirmPasswordDialog extends DialogFragment {
    private static final String TAG = "ConfirmPasswordDialog";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_confirm_password, container, false);
        Log.d(TAG, "onCreateView: started");

        return view;
    }
}
