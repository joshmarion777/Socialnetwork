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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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
        setupWidgets();

        return view;
    }

    private void setupWidgets(){
        String timeStampDiff = getTimestampDifference();
        if (!timeStampDiff.equals("0")){
            mTimestamp.setText(timeStampDiff + " DAYS AGO");
        }else{
            mTimestamp.setText("TODAY");
        }
    }

    /**
     * Returns a String the number of days ago the post was made
     * @return
     */
    private  String getTimestampDifference(){
        Log.d(TAG, "getTimestampDifference: getting timestamp difference");

        String difference = " ";
        Calendar c =Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta")); //googled it... 'Android list of timeZones'
        Date timestamp;
        Date today = c.getTime();
        sdf.format(today);
        final String photoTimestamp = mPhoto.getDate_created();
        try{
            timestamp = sdf.parse(photoTimestamp);
            difference = String.valueOf(Math.round(((today.getTime() - timestamp.getTime())/ 1000 / 60 / 60 / 24 ) ));
        }catch (ParseException e){
            Log.e(TAG, "getTimestampDifference: ParseException :  " + e.getMessage() );
            difference = "0";
        }

        return difference;
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
