package com.example.josh.socialnetwork.Profile;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.josh.socialnetwork.R;

/**
 * Created by JOSH on 04-10-2017.
 */

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    private static final int ACTIVITY_NUM = 4;
    private static final int NUM_GRID_COLUMNS = 3;


    private Context mContext = ProfileActivity.this ;

    private ProgressBar mProgrssBar ;
    private ImageView profilePhoto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();

//        setupBottomNavigationView();
//
//        setupToolBar();
//        setupActivityWidget();
//        setProfileImage();
//
//        tempGridSetup();

    }
    private void init(){
        Log.d(TAG, "init: inflating" + getString(R.string.profile_fragment));

        ProfileFragment fragment = new ProfileFragment();
        FragmentTransaction transaction = ProfileActivity.this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.profile_container, fragment);
        transaction.addToBackStack(getString(R.string.profile_fragment));
        transaction.commit();
    }
       /* private void tempGridSetup(){
            ArrayList<String> imgURLs = new ArrayList<>();

            imgURLs.add("https://www.w3schools.com/css/img_fjords.jpg");
            imgURLs.add("https://www.quirkybyte.com/wp-content/uploads/2017/01/marvel.jpg");
            imgURLs.add("http://hd.wallpaperswide.com/thumbs/iron_man_helmet-t2.jpg");
            imgURLs.add("https://vignette3.wikia.nocookie.net/marvelcinematicuniverse/images/4/44/AoU_Hulkbuster_01.png/revision/latest/scale-to-width-down/2000?cb=20160502163433");
            imgURLs.add("https://i.ytimg.com/vi/FAciZRkOKQs/maxresdefault.jpg");
            imgURLs.add("https://www.w3schools.com/css/img_fjords.jpg");
            imgURLs.add("https://www.quirkybyte.com/wp-content/uploads/2017/01/marvel.jpg");
            imgURLs.add("http://hd.wallpaperswide.com/thumbs/iron_man_helmet-t2.jpg");
            imgURLs.add("https://vignette3.wikia.nocookie.net/marvelcinematicuniverse/images/4/44/AoU_Hulkbuster_01.png/revision/latest/scale-to-width-down/2000?cb=20160502163433");
            imgURLs.add("https://i.ytimg.com/vi/FAciZRkOKQs/maxresdefault.jpg");


            setupImageGrid(imgURLs);
        }

        private void setupImageGrid(ArrayList<String> imgURLs){
            GridView gridView = (GridView) findViewById(R.id.gridview);
            int gridWidth = getResources().getDisplayMetrics().widthPixels;
            int imagewidth = gridWidth/NUM_GRID_COLUMNS;
            gridView.setColumnWidth(imagewidth);

            GridImageAdapter adapter =  new GridImageAdapter(mContext, R.layout.layout_grid_imageview,"",imgURLs);
            gridView.setAdapter(adapter);
        }
        private void setProfileImage(){
            Log.d(TAG, "setProfileImage: seting up profile photo");
            String imgURL = "dailypost.in/wp-content/uploads/2017/06/steve-jobs-31.jpg";
            UniversalImageLoader.setImage(imgURL, profilePhoto, mProgrssBar, "http://");
        }

        private void setupActivityWidget(){
            mProgrssBar = (ProgressBar) findViewById(R.id.profileProgressBar);
            mProgrssBar.setVisibility(View.GONE);

            profilePhoto = (ImageView) findViewById(R.id.profile_photo);
        }
    */
    /**
     * This the top toolbar for the profile and the account settings
     *//*

    private void setupToolBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.profileToolBar);
        setSupportActionBar(toolbar);

        ImageView profileMenu = (ImageView) findViewById(R.id.profileMenu);
        profileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to account settings");
                Intent intent = new Intent(mContext, AccountSettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    *//**
     * Copied from the Home Activity so that we could use here
     *//*
    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting the bottom navigation view");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem  = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);

    }
*/

}
