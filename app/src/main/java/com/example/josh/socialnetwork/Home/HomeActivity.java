package com.example.josh.socialnetwork.Home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.josh.socialnetwork.Login.LoginActivity;
import com.example.josh.socialnetwork.R;
import com.example.josh.socialnetwork.Utils.BottomNavigationViewHelper;
import com.example.josh.socialnetwork.Utils.SectionsPagerAdapter;
import com.example.josh.socialnetwork.Utils.UniversalImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.nostra13.universalimageloader.core.ImageLoader;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private static final int ACTIVITY_NUM = 0;

    private Context mContext = HomeActivity.this ;

    //FireBase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, "onCreate: This is the starting Bro..I'm being anti feminist");

        setupFirebaseAuth();
        initImageLoader();
        setupBottomNavigationView();
        setupViewPager();
    }

    private void initImageLoader(){
        Log.d(TAG, "initImageLoader: initializing Image loader");
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(mContext);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    /**
     * For adding the three tabs
     *
     */
    private void setupViewPager(){
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CameraFragment());//index = 0
        adapter.addFragment(new HomeFragment());////index = 1
        adapter.addFragment(new MessagesFragment());//index = 2
        ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_camera);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_insta);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_arrow);
    }

    /**
     * BottomNavigation view setup for no moving animation n stuff like that
     */
        private void setupBottomNavigationView(){
            Log.d(TAG, "setupBottomNavigationView: setting the bottom navigation view");
            BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
            BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
            BottomNavigationViewHelper.enableNavigation(mContext,this, bottomNavigationViewEx);
            Menu menu = bottomNavigationViewEx.getMenu();
            MenuItem menuItem  = menu.getItem(ACTIVITY_NUM);
            menuItem.setChecked(true);

           }
    /*
    ------------------------------------------Firebase---------------------------------------------
    */

    /**
     * Checking if the @param user in loggedd in
     * @param user
     */
    private void checkCurrentUser(FirebaseUser user){
            Log.d(TAG, "checkCurrentUser: checking if user logged in.");

            if(user == null){
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            }
        }

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

                //check if the user is logged in
                checkCurrentUser(user);
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
        checkCurrentUser(mAuth.getCurrentUser());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}

