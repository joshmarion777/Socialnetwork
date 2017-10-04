package com.example.josh.socialnetwork;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.josh.socialnetwork.Utils.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: This is the starting Bro..I'm being anti feminist");

        setupBottomNavigationView();
    }

    /**
     * BottomNavigation view setup for no moving animation n stuff like that
     */
        private void setupBottomNavigationView(){
            Log.d(TAG, "setupBottomNavigationView: setting the bottom navigation view");
            BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
            BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        }
}

