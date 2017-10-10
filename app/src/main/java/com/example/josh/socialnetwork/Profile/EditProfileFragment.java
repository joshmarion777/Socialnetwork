package com.example.josh.socialnetwork.Profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.josh.socialnetwork.R;
import com.example.josh.socialnetwork.Utils.UniversalImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by JOSH on 09-10-2017.
 */

public class EditProfileFragment extends Fragment {
    private static final String TAG = "EditProfileFragment";

    private ImageView mProfilePhoto;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editprofile ,container , false);
        mProfilePhoto = (ImageView) view.findViewById(R.id.profile_photo);

        initImageLoader();

        setProfileImage();

        return view;
    }
    private void initImageLoader(){
        Log.d(TAG, "initImageLoader: initializing Image loader");
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(getActivity());
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }
    private void setProfileImage(){
        Log.d(TAG, "setProfileImage: setting Image Profile");
        String imgURL = "dailypost.in/wp-content/uploads/2017/06/steve-jobs-31.jpg";
        UniversalImageLoader.setImage(imgURL, mProfilePhoto, null, "http://");

    }
}
