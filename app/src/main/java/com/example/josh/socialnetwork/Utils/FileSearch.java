package com.example.josh.socialnetwork.Utils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by jbghostman on 12/11/17.
 */

public class FileSearch {

    /**
     * Search the directory and return a list of all "directories" contained inside
     * @param directory
     * @return
     */
    public static ArrayList<String> getDirectoryPaths(String directory){

        ArrayList<String> pathArray = new ArrayList<>();
        File file = new File(directory);
        File[] listfiles = file.listFiles();
        for (int i = 0; i < listfiles.length; i++){
            if (listfiles[i].isDirectory()){
                pathArray.add(listfiles[i].getAbsolutePath());
            }
        }
        return pathArray;
    }
    /**
     * Search the directory and return a list of all "files" contained inside
     * @param directory
     * @return
     */

    public static ArrayList<String> getFilePath (String directory){
        ArrayList<String> pathArray = new ArrayList<>();
        File file = new File(directory);
        File[] listfiles = file.listFiles();
        for (int i = 0; i < listfiles.length; i++){
            if (listfiles[i].isFile()){
                pathArray.add(listfiles[i].getAbsolutePath());
            }
        }
        return pathArray;
    }
}
