package com.example.josh.socialnetwork;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.josh.socialnetwork.Utils.BottomNavigationViewHelper;
import com.example.josh.socialnetwork.Utils.SquareImageView;
import com.example.josh.socialnetwork.Utils.UniversalImageLoader;
import com.example.josh.socialnetwork.models.Photo;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * Created by jbghostman on 21/11/17.
 */

public class ViewPostFragment extends Fragment {
    private static final String TAG = "ViewPostFragment";

    public ViewPostFragment(){
        super();
        setArguments(new Bundle());
    }

    //widgets
    private SquareImageView mPostImage;
    private BottomNavigationViewEx bottomNavigationView;
    private TextView mBackLabel, mCaption, mUsername, mTimestamp;
    private ImageView mBackArrow, mEllipses, mHeartRed, mHeartWhite, mProfileImage;

    //vars
    private  Photo mPhoto;
    private int mActivityNumber = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_view_post, container, false);
        mPostImage = view.findViewById(R.id.post_image);
        bottomNavigationView = view.findViewById(R.id.bottomNavViewBar);
        mBackArrow = view.findViewById(R.id.backArrow);
        mCaption = view.findViewById(R.id.caption);
        mBackLabel = view.findViewById(R.id.tvBlackLabel);
        mUsername = view.findViewById(R.id.username);
        mTimestamp = view.findViewById(R.id.image_time_posted);
        mEllipses = view.findViewById(R.id.ivEllipses);
        mHeartRed = view.findViewById(R.id.image_heart_red);
        mHeartWhite = view.findViewById(R.id.image_heart);
        mProfileImage = view.findViewById(R.id.profile_photo);

        try{
            mPhoto = getPhotoFromBundle();
            mActivityNumber = getActivityNumFromBundle();
            UniversalImageLoader.setImage(mPhoto.getImage_path(), mPostImage, null, "" );


        }catch (NullPointerException e){
            Log.e(TAG, "onCreateView: NullPointerException: photo was null from bundle"+  e.getMessage());
        }

        setupBottomNavigationView();

        return view;
    }

    /**
     * retrive the activity_number from the incoming bundle from profile Activity interface
     * @return
     */

    private int getActivityNumFromBundle(){
        Log.d(TAG, "getPhotoFromBundle: arguments " + getArguments());

        Bundle bundle = this.getArguments();

        if (bundle != null){

            return  bundle.getInt(getString(R.string.activity_number));
        }else{
            return  0;
        }

    }

    /**
     * retrive the photo from the incoming bundle from profile Activity interface
     * @return
     */

    private Photo getPhotoFromBundle(){
        Log.d(TAG, "getPhotoFromBundle: arguments " + getArguments());

        Bundle bundle = this.getArguments();
        if (bundle != null){

            return  bundle.getParcelable(getString(R.string.photo));
        }else{
            return  null;
        }

    }
    /**
     * Bottom Navigation Setup
     */
    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting the bottom navigation view");
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView);
        BottomNavigationViewHelper.enableNavigation(getActivity(), getActivity(), bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem  = menu.getItem(mActivityNumber);
        menuItem.setChecked(true);
    }

}
