package com.example.josh.socialnetwork.Utils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.josh.socialnetwork.R;
import com.example.josh.socialnetwork.models.Comment;
import com.example.josh.socialnetwork.models.Photo;

import java.util.ArrayList;

/**
 * Created by jbghostman on 21/11/17.
 */

public class ViewCommentsFragment extends Fragment {
    private static final String TAG = "ViewCommentsFragment";


    public ViewCommentsFragment(){
        super();
        setArguments(new Bundle());
    }

    //widgets
    private ImageView mBackArrow, mCheckMark;
    private EditText mComment;
    private ListView mListView;

    //vars
    private Photo mPhoto;
    private ArrayList<Comment> mComments;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_view_comments, container, false);

        mBackArrow = view.findViewById(R.id.backArrow);
        mCheckMark = view.findViewById(R.id.ivPostComment);
        mComment = view.findViewById(R.id.comment);
        mListView = view.findViewById(R.id.listView);
        mComments = new ArrayList<>();




        try{
            mPhoto = getPhotoFromBundle();



        }catch (NullPointerException e){
            Log.e(TAG, "onCreateView: NullPointerException: photo was null from bundle"+  e.getMessage());
        }

        Comment firstComment = new Comment();
        firstComment.setComment(mPhoto.getCaption());
        firstComment.setUser_id(mPhoto.getUser_id());
        firstComment.setDate_created(mPhoto.getDate_created());

        mComments.add(firstComment);
        CommentListAdapter adapter =  new CommentListAdapter(getActivity(), R.layout.layout_comment, mComments);
        mListView.setAdapter(adapter);

        return view;
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

}
