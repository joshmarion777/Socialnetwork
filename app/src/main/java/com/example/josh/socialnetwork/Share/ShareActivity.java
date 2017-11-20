package com.example.josh.socialnetwork.Share;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.josh.socialnetwork.R;
import com.example.josh.socialnetwork.Utils.Permissions;
import com.example.josh.socialnetwork.Utils.SectionsPagerAdapter;

/**
 * Created by JOSH on 04-10-2017.
 */

public class ShareActivity extends AppCompatActivity {

    private static final String TAG = "ShareActivity";

    //constants
    private static final int ACTIVITY_NUM = 2;
    private static final int VERIFY_PERMISSION_REQUEST = 1;

    private Context mContext = ShareActivity.this;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        if(checkPermissionsArray(Permissions.PERMISSIONS)) {
                setupViewPager();
        }
        else{
            verifyPermissions(Permissions.PERMISSIONS);


        }

       //setupBottomNavigationView();
    }

    /**
     * Check an array of Permissions
     * @param permissions
     * @return
     */
    public boolean checkPermissionsArray(String[] permissions){
        Log.d(TAG, "checkPermissionsArray: checking Permissions array");

        for(int i = 0; i < permissions.length; i++){
            String check = permissions[i];
            if(!checkPermissions(check)){
                return false;
            }
        }
        return true;
    }

    /**
     * return the current tab number
     * 0 = GalleryFragment
     * 1 = PhotoFragment
     * @return
     */
    public int getCurrentTabNumber(){
        return mViewPager.getCurrentItem();
    }

    /**
     * setup viewpager for manager the tabs
     */
    public void setupViewPager(){
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new GalleryFragment());
        adapter.addFragment(new PhotoFragment());
        mViewPager =  findViewById(R.id.container);
        mViewPager.setAdapter(adapter);

        TabLayout tabLayout =  findViewById(R.id.tabsBottom);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setText(getString(R.string.gallery));
        tabLayout.getTabAt(1).setText(getString(R.string.photo));
    }

    public int getTask(){
        Log.d(TAG, "getTask: TASK: New_task " );

        return  getIntent().getFlags();
    }

    /**
     * Verify all the permissions passed to the array
     * @param permissions
     */
    public void verifyPermissions(String[] permissions){
        Log.d(TAG, "verifyPermissions: Verifying Permissions");

        ActivityCompat.requestPermissions(
                ShareActivity.this,
                permissions,
                VERIFY_PERMISSION_REQUEST
        );
    }

    /**
     * check a single is it been verified
     * @param permission
     * @return
     */
    public boolean checkPermissions (String permission){
        Log.d(TAG, "checkPermissions: checking Permissions" + permission);

        int permissionRequest = ActivityCompat.checkSelfPermission(ShareActivity.this, permission);

        if(permissionRequest != PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "checkPermissions: \n Permissions was not Granted for: " + permission);
            return false;
        }else{
            Log.d(TAG, "checkPermissions: \n Permissions  Granted for: " + permission);
            return true;
        }
    }


    /**
     * Copied from the Home Activity so that we could use here
     */
//    private void setupBottomNavigationView(){
//        Log.d(TAG, "setupBottomNavigationView: setting the bottom navigation view");
//        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
//        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
//        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
//        Menu menu = bottomNavigationViewEx.getMenu();
//        MenuItem menuItem  = menu.getItem(ACTIVITY_NUM);
//        menuItem.setChecked(true);
//
//    }
}
