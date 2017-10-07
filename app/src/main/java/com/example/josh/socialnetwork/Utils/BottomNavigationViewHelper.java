package com.example.josh.socialnetwork.Utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;

import com.example.josh.socialnetwork.HomeActivity;
import com.example.josh.socialnetwork.LikesActivity;
import com.example.josh.socialnetwork.ProfileActivity;
import com.example.josh.socialnetwork.R;
import com.example.josh.socialnetwork.SearchActivity;
import com.example.josh.socialnetwork.ShareActivity;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * Created by JOSH on 04-10-2017.
 */

public class BottomNavigationViewHelper {
    private static final String TAG = "BottomNavigationViewHel";

    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx){
        Log.d(TAG, "setupBottomNavigationView: setting up the bottom nav view");
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(false);
    }

    public static void enableNavigation(final Context context, BottomNavigationViewEx view){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.ic_house :
                        Intent intent1 = new Intent(context, HomeActivity.class ); //ACTIVITY_NUM = 0
                        context.startActivity(intent1);
                        break;
                    case R.id.ic_search:

                        Intent intent2 = new Intent(context, SearchActivity.class ); // ACTIVITY_NUM =1
                        context.startActivity(intent2);
                        break;
                    case R.id.ic_circle:

                        Intent intent3 = new Intent(context, ShareActivity.class ); //ACTIVITY_NUM = 2
                        context.startActivity(intent3);
                        break;
                    case R.id.ic_aleart:

                        Intent intent4 = new Intent(context, LikesActivity.class ); //ACTIVITY_NUM = 3
                        context.startActivity(intent4);
                        break;
                    case R.id.ic_android:

                        Intent intent5 = new Intent(context, ProfileActivity.class ); //ACTIVITY_NUM =4
                        context.startActivity(intent5);
                        break;
                }

                return false;
            }
        });
    }
}
