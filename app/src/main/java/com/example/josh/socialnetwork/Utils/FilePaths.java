package com.example.josh.socialnetwork.Utils;

import android.os.Environment;

/**
 * Created by jbghostman on 12/11/17.
 */

public class FilePaths {

    //"/storage/emulated/0"
    public String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();

    public String PICTURES = ROOT_DIR + "/Pictures";
    public String CAMERA = ROOT_DIR + "/DCIM/camera";

}
