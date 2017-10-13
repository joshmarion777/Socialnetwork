package com.example.josh.socialnetwork.Login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.josh.socialnetwork.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by JOSH on 12-10-2017.
 */

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";

    private Context mContext;
    private String email,password,username;
    private EditText mEmail,mPassword,mUsername;
    private TextView loadingPleasewait;
    private Button btnRegister;
    private ProgressBar mProgressBar;

    //FireBase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Log.d(TAG, "onCreate: started");

        initwidgets();
        setupFirebaseAuth();
    }

    /**
     * Initialize the Activity Widgets
     */
    private void initwidgets(){
        Log.d(TAG, "initwidgets: initializing widgets");
        mProgressBar = (ProgressBar) findViewById(R.id.progessBar);
        loadingPleasewait = (TextView) findViewById(R.id.loadingPleasewait);
        mEmail = (EditText) findViewById(R.id.input_email);
        mPassword = (EditText) findViewById(R.id.input_password);
        mContext = RegisterActivity.this;
        mProgressBar.setVisibility(View.GONE);
        loadingPleasewait.setVisibility(View.GONE);
    }

    private boolean isStringNull(String string){
        Log.d(TAG, "isStringNull: checking if string is Null.");

        if(string.equals("")){
            return true;
        }
        else
            return false;
    }
        /*
    ------------------------------------------Firebase---------------------------------------------
    */


    /**
     * Setup the firebase auth object
     */

    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth");
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}

