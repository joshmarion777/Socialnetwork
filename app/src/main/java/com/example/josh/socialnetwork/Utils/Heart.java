package com.example.josh.socialnetwork.Utils;

import android.util.Log;
import android.widget.ImageView;

/**
 * Created by jbghostman on 22/11/17.
 */

public class Heart {
    private static final String TAG = "Heart";
    
    public ImageView heartWhite, heartRed;

    public Heart(ImageView heartWhite, ImageView heartRed) {
        this.heartWhite = heartWhite;
        this.heartRed = heartRed;
    }
    public void toggleLike(){
        Log.d(TAG, "toggleLike: toggling heart.");
    }
}
