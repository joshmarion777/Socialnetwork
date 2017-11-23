package com.example.josh.socialnetwork.Utils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.josh.socialnetwork.R;

/**
 * Created by jbghostman on 21/11/17.
 */

public class ViewCommentsFragment extends Fragment {
    private static final String TAG = "ViewCommentsFragment";



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_view_comments, container, false);


        return view;
    }


}
