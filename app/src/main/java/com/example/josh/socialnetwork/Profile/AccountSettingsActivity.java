package com.example.josh.socialnetwork.Profile;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.josh.socialnetwork.R;

import java.util.ArrayList;

/**
 * Created by JOSH on 08-10-2017.
 */

public class AccountSettingsActivity extends AppCompatActivity {
    private static final String TAG = "AccountSettingsActivity";

    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acountsettings);
        Log.d(TAG, "onCreate: started");
        mContext = AccountSettingsActivity.this;

        setupSettingsList();

        //setup back arrow for navigating back to "Profile Activity"
        ImageView backarrow = (ImageView) findViewById(R.id.backArrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating back to 'Profile Avtivity'");
                finish();
            }
        });
    }

    private void setupSettingsList(){
        Log.d(TAG, "setupSettingsList: initializing 'Account Settings' list");
        ListView listView = (ListView) findViewById(R.id.lvAccountSettings);

        ArrayList<String> options = new ArrayList<>();
        options.add(getString(R.string.edit_profile));
        options.add(getString(R.string.sign_out));

        ArrayAdapter adapter = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, options);
        listView.setAdapter(adapter);

    }
}
