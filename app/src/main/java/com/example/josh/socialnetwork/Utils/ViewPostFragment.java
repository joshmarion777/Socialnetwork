package com.example.josh.socialnetwork.Utils;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.josh.socialnetwork.R;
import com.example.josh.socialnetwork.models.Comment;
import com.example.josh.socialnetwork.models.Like;
import com.example.josh.socialnetwork.models.Photo;
import com.example.josh.socialnetwork.models.User;
import com.example.josh.socialnetwork.models.UserAccountSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by jbghostman on 21/11/17.
 */

public class ViewPostFragment extends Fragment {
    private static final String TAG = "ViewPostFragment";

    public interface OncommentThreadSelectedListener{
        void onCommentThreadSelectedListener(Photo photo);
    }
    OncommentThreadSelectedListener mOncommentThreadSelectedListener;

    public ViewPostFragment(){
        super();
        setArguments(new Bundle());
    }


    //FireBase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;

    //widgets
    private SquareImageView mPostImage;
    private BottomNavigationViewEx bottomNavigationView;
    private TextView mBackLabel, mCaption, mUsername, mTimestamp, mLikes, mComments;
    private ImageView mBackArrow, mEllipses, mHeartRed, mHeartWhite, mProfileImage, mComment;
    public LikeButton likeButton;

    //vars
    private  Photo mPhoto;
    private int mActivityNumber = 0;
    private String photoUsername =" ";
    private String photoUrl = " ";
    private UserAccountSettings mUserAccountSettings;
    private GestureDetector mGestureDetector;
    private Heart mHeart;
    private boolean mLikedByCurrentUser;
    private StringBuilder mUsers;
    private String mLikesString = " ";
    private User mCurrentUser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_view_post, container, false);
        mPostImage = view.findViewById(R.id.post_image);
        bottomNavigationView = view.findViewById(R.id.bottomNavViewBar);
        mBackArrow = view.findViewById(R.id.backArrow);
        mCaption = view.findViewById(R.id.image_caption);
        mBackLabel = view.findViewById(R.id.tvBlackLabel);
        mUsername = view.findViewById(R.id.username);
        mTimestamp = view.findViewById(R.id.image_time_posted);
        mEllipses = view.findViewById(R.id.ivEllipses);
//        mHeartRed = view.findViewById(R.id.image_heart_red);
//        mHeartWhite = view.findViewById(R.id.image_heart);
        mProfileImage = view.findViewById(R.id.profile_photo);
        mLikes = view.findViewById(R.id.image_likes);
        likeButton = view.findViewById(R.id.like_button);
        mComment = view.findViewById(R.id.speech_bubble);
        mComments = view.findViewById(R.id.image_comments_link);


//        mHeart = new Heart(mHeartWhite, mHeartRed);
//        mGestureDetector = new GestureDetector(getActivity(), new GestureListener());


        setupFirebaseAuth();
        setupBottomNavigationView();



        return view;
    }

    private void init(){

        try{
//            mPhoto = getPhotoFromBundle();
            UniversalImageLoader.setImage(getPhotoFromBundle().getImage_path(), mPostImage, null, "" );
            mActivityNumber = getActivityNumFromBundle();
            String photo_id = getPhotoFromBundle().getPhoto_id();

            Query query = FirebaseDatabase.getInstance().getReference()
                    .child(getString(R.string.dbname_photos))
                    .orderByChild(getString(R.string.field_photo_id))
                    .equalTo(photo_id);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){

                        Photo newPhoto = new Photo();

                        Map<String, Object> objectMap = (HashMap<String, Object>) singleSnapshot.getValue();

                        newPhoto.setCaption(objectMap.get(getString(R.string.field_caption)).toString());
                        newPhoto.setTags(objectMap.get(getString(R.string.field_tags)).toString());
                        newPhoto.setPhoto_id(objectMap.get(getString(R.string.field_photo_id)).toString());
                        newPhoto.setUser_id(objectMap.get(getString(R.string.field_user_id)).toString());
                        newPhoto.setDate_created(objectMap.get(getString(R.string.field_date_created)).toString());
                        newPhoto.setImage_path(objectMap.get(getString(R.string.field_image_path)).toString());



                        List<Comment> commentsList = new ArrayList<Comment>();
                        for(DataSnapshot dSnapshot : singleSnapshot.child(getString(R.string.field_comments)).getChildren()){
                            Comment comment = new Comment();
                            comment.setUser_id(dSnapshot.getValue(Comment.class).getUser_id());
                            commentsList.add(comment);
                        }

                        newPhoto.setComments(commentsList);

                        mPhoto = newPhoto;

                        getCurrentUser();
                        getPhotoDetails();
                        getLikesString();
                    }

                }


                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d(TAG, "onCancelled: query Cancelled");
                }
            });


        }catch (NullPointerException e){
            Log.e(TAG, "onCreateView: NullPointerException: photo was null from bundle"+  e.getMessage());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isAdded()){
            init();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            mOncommentThreadSelectedListener = (OncommentThreadSelectedListener) getActivity();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage());
        }
    }

    private void getCurrentUser(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child(getString(R.string.dbname_users))
                .orderByChild(getString(R.string.field_user_id))
                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    mCurrentUser = singleSnapshot.getValue(User.class);
                }
                getLikesString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: query Cancelled");
            }
        });
    }

    private void getLikesString(){
        Log.d(TAG, "getLikesString: getting likes string");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child(getString(R.string.dbname_photos))
                .child(mPhoto.getPhoto_id())
                .child(getString(R.string.field_likes));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    mUsers =  new StringBuilder();
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                    Query query = reference
                            .child(getString(R.string.dbname_users))
                            .orderByChild(getString(R.string.field_user_id))
                            .equalTo(singleSnapshot.getValue(Like.class).getUser_id());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                                Log.d(TAG, "onDataChange: found like: "
                                        + singleSnapshot.getValue(User.class).getUsername());

                                mUsers.append(singleSnapshot.getValue(User.class).getUsername());
                                mUsers.append(",");
                            }

                            String[] splitUsers = mUsers.toString().split(",");

                            if (mUsers.toString().contains(mCurrentUser.getUsername() + ",")){
                                mLikedByCurrentUser = true;
                            }else {
                                mLikedByCurrentUser = false;
                            }

                            int length = splitUsers.length;

                            if(length == 1){
                                mLikesString = "Liked by " + splitUsers[0];
                            }
                            else  if(length == 2){

                                mLikesString = "Liked by " + splitUsers[0]
                                        + "  and " + splitUsers[1];
                            }
                            else  if(length == 3){
                                mLikesString = "Liked by " + splitUsers[0]
                                        + ", " + splitUsers[1]
                                        + "  and " + splitUsers[2];

                            }
                            else  if(length == 4){
                                mLikesString = "Liked by " + splitUsers[0]
                                        + ", " + splitUsers[1]
                                        + ", " + splitUsers[2]
                                        + "  and " + splitUsers[3];

                            }
                            else  if(length > 4){
                                mLikesString = "Liked by " + splitUsers[0]
                                        + ", " + splitUsers[1]
                                        + ", " + splitUsers[2]
                                        + "  and " + (splitUsers.length - 3) + " Others";
                            }
                            setupWidgets();
                     }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
                if (!dataSnapshot.exists()){
                    mLikesString = " ";
                    mLikedByCurrentUser = false;
                    setupWidgets();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(final LikeButton likeButton) {


                   addNewLike();

            }

            @Override
            public void unLiked(final LikeButton likeButton) {

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            Query query = reference
                    .child(getString(R.string.dbname_photos))
                    .child(mPhoto.getPhoto_id())
                    .child(getString(R.string.field_likes));
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {

                        String keyID = singleSnapshot.getKey();
                        //case 1: The user already liked the photo
                        if (singleSnapshot.getValue(Like.class).getUser_id()
                                .equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            myRef.child(getString(R.string.dbname_photos))
                                    .child(mPhoto.getPhoto_id())
                                    .child(getString(R.string.field_likes))
                                    .child(keyID)
                                    .removeValue();

                            myRef.child(getString(R.string.dbname_user_photos))
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child(mPhoto.getPhoto_id())
                                    .child(getString(R.string.field_likes))
                                    .child(keyID)
                                    .removeValue();

                            likeButton.setLiked(false);
                            getLikesString();
                            mLikedByCurrentUser = false;

                        }

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            }
        });
    }


//    public class GestureListener extends  GestureDetector.SimpleOnGestureListener{
//        @Override
//        public boolean onDown(MotionEvent e) {
//            return true;
//        }
//
////        @Override
////        public boolean onDoubleTap(MotionEvent e) {
////            Log.d(TAG, "onDoubleTap: Double tap detected");
////
////
////            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
////            Query query = reference
////                    .child(getString(R.string.dbname_photos))
////                    .child(mPhoto.getPhoto_id())
////                    .child(getString(R.string.field_likes));
////            query.addListenerForSingleValueEvent(new ValueEventListener() {
////                @Override
////                public void onDataChange(DataSnapshot dataSnapshot) {
////                    for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
////
////                        String keyID = singleSnapshot.getKey();
////                        //case 1: The user already liked the photo
////                        if (likeButton.isLiked() && singleSnapshot.getValue(Like.class).getUser_id()
////                                .equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
////                            myRef.child(getString(R.string.dbname_photos))
////                                    .child(mPhoto.getPhoto_id())
////                                    .child(getString(R.string.field_likes))
////                                    .child(keyID)
////                                    .removeValue();
////
////                            myRef.child(getString(R.string.dbname_user_photos))
////                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
////                                    .child(mPhoto.getPhoto_id())
////                                    .child(getString(R.string.field_likes))
////                                    .child(keyID)
////                                    .removeValue();
////
////                            likeButton.setLiked(true);
////                            getLikesString();
////                        }
////
////                        //case 2: The user has not liked
////                        else if (!likeButton.isLiked()){
////                            //add new Like
////                            addNewLike();
////                            break;
////                        }
////
////                    }
////                    if (!dataSnapshot.exists()){
////                        //add new like
////                        addNewLike();
////                    }
////                }
////
////                @Override
////                public void onCancelled(DatabaseError databaseError) {
////
////                }
////            });
////            return true;
////     }
//    }

    private void addNewLike(){
        Log.d(TAG, "addNewLike: adding new like");

        String newLikeID =  myRef.push().getKey();
        Like like =  new Like();
        like.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());

        myRef.child(getString(R.string.dbname_photos))
                .child(mPhoto.getPhoto_id())
                .child(getString(R.string.field_likes))
                .child(newLikeID)
                .setValue(like);

        myRef.child(getString(R.string.dbname_user_photos))
                .child(mPhoto.getUser_id())
                .child(mPhoto.getPhoto_id())
                .child(getString(R.string.field_likes))
                .child(newLikeID)
                .setValue(like);

        likeButton.setLiked(true);
        getLikesString();
        mLikedByCurrentUser = true;

    }

    private void getPhotoDetails(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child(getString(R.string.dbname_user_account_settings))
                .orderByChild(getString(R.string.field_user_id))
                .equalTo(mPhoto.getUser_id());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    mUserAccountSettings = singleSnapshot.getValue(UserAccountSettings.class);
                }
//               setupWidgets();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: query Cancelled");
            }
        });


    }

    private void setupWidgets(){
        String timeStampDiff = getTimestampDifference();
        if (!timeStampDiff.equals("0")){
            mTimestamp.setText(timeStampDiff + " DAYS AGO");
        }else{
            mTimestamp.setText("TODAY");
        }

        Log.d(TAG, "setupWidgets: Gonna set the profile  pic and username");
        mUsername.setText(mUserAccountSettings.getUsername());
        UniversalImageLoader.setImage(mUserAccountSettings.getProfile_photo(),
                mProfileImage, null, "");
        mLikes.setText(mLikesString);
        mCaption.setText(mPhoto.getCaption());

        if (mPhoto.getComments().size() > 0){
            mComments.setText("View all " + mPhoto.getComments().size() + " commments");
        }else{
            mComments.setText("");
        }

        mComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigatin to comments thresd");

                mOncommentThreadSelectedListener.onCommentThreadSelectedListener(mPhoto);
            }
        });

        mBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigating back...");
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        mComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigating back...");

                mOncommentThreadSelectedListener.onCommentThreadSelectedListener(mPhoto);
            }
        });

        if (mLikedByCurrentUser) {
           likeButton.setLiked(true);
        }else if (!mLikedByCurrentUser){
            likeButton.setLiked(false);
        }




//        if (mLikedByCurrentUser){
////            mHeartWhite.setVisibility(View.GONE);
////            mHeartRed.setVisibility(View.VISIBLE);
//            mHeartRed.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View view, MotionEvent motionEvent) {
//                     Log.d(TAG, "onTouch: red heart touch detected");
//                    return mGestureDetector.onTouchEvent(motionEvent);
//
//
//                }
//            });
//        }else {
////            mHeartWhite.setVisibility(View.VISIBLE);
////            mHeartRed.setVisibility(View.GONE);
//            mHeartWhite.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View view, MotionEvent motionEvent) {
//                        Log.d(TAG, "onTouch: white heart touch detected");
//                    return mGestureDetector.onTouchEvent(motionEvent);
//                }
//            });
//        }

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

          /*
    ------------------------------------------Firebase---------------------------------------------
    */

    /**
     * Setup the firebase auth object
     */

    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth");
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
