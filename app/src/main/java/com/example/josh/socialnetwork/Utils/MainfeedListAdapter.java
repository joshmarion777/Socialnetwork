package com.example.josh.socialnetwork.Utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.josh.socialnetwork.Home.HomeActivity;
import com.example.josh.socialnetwork.Profile.ProfileActivity;
import com.example.josh.socialnetwork.R;
import com.example.josh.socialnetwork.models.Comment;
import com.example.josh.socialnetwork.models.Like;
import com.example.josh.socialnetwork.models.Photo;
import com.example.josh.socialnetwork.models.User;
import com.example.josh.socialnetwork.models.UserAccountSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by jbghostman on 08/12/17.
 */

public class MainfeedListAdapter extends ArrayAdapter<Photo> {
    private static final String TAG = "MainfeedListAdapter";


    private LayoutInflater mInflater;
    private int mLayoutResource;
    private Context mContext;
    private DatabaseReference mReference;
    private String currentUsername = "";

    public MainfeedListAdapter(@NonNull Context context, int resource, @NonNull List<Photo> objects) {
        super(context, resource, objects);

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLayoutResource = resource;
        this.mContext = context;
    }

    static class ViewHolder {
        CircleImageView mProfileImage;
        String likesString;
        TextView username, timedetails, caption, likes, comments;
        SquareImageView image;
        LikeButton likeButton;
        ImageView comment;

        UserAccountSettings settings = new UserAccountSettings();
        User user = new User();
        StringBuilder users;
        String mLikesString;
        boolean likesByCurrentUser;
        Photo photo;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final ViewHolder holder;

        if (convertView == null) {

            convertView = mInflater.inflate(mLayoutResource, parent, false);
            holder = new ViewHolder();

            holder.username = convertView.findViewById(R.id.username);
            holder.image = convertView.findViewById(R.id.post_image);
            holder.likeButton = convertView.findViewById(R.id.like_button);
            holder.comment = convertView.findViewById(R.id.speech_bubble);
            holder.likes = convertView.findViewById(R.id.image_likes);
            holder.comments = convertView.findViewById(R.id.image_comments_link);
            holder.caption = convertView.findViewById(R.id.image_caption);
            holder.timedetails = convertView.findViewById(R.id.image_time_posted);
            holder.mProfileImage = convertView.findViewById(R.id.profile_image);
            holder.photo = getItem(position);
            holder.users = new StringBuilder();


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //get the current users username
        getCurrentUsername();
        //get likes string
        getLikesString(holder);
        //set the comments
        List<Comment> comments = getItem(position).getComments();
        holder.comments.setText("View all " + comments.size() + " commetns" );
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: loading comments thread for " + getItem(position).getPhoto_id());
                ((HomeActivity)mContext).onCommentThreadselected(getItem(position), holder.settings);

                //TODO:will come back
            }
        });

        //set the time posted
        String timeStampDifference = getTimestampDifference(getItem(position));
        if (!timeStampDifference.equals("0")){
            holder.timedetails.setText(timeStampDifference + " DAYS AGO");
        }else{
            holder.timedetails.setText("TODAY");
        }
        //set tje profile image
        final ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(getItem(position).getImage_path(),holder.image);

        //get the profile image and the username
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child(mContext.getString(R.string.dbname_user_account_settings))
                .orderByChild(mContext.getString(R.string.field_user_id))
                .equalTo(getItem(position).getPhoto_id());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
//                    currentUsername = singleSnapshot.getValue(UserAccountSettings.class).getUsername();
                    Log.d(TAG, "onDataChange: found user: " +
                            singleSnapshot.getValue(UserAccountSettings.class).getUsername());
                    holder.username.setText(singleSnapshot.getValue(UserAccountSettings.class).getUsername());
                    holder.username.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d(TAG, "onClick: navigating to profile of : " +
                                    holder.user.getUsername());

                            Intent intent = new Intent(mContext, ProfileActivity.class);
                            intent.putExtra(getContext().getString(R.string.calling_activity),
                                    mContext.getString(R.string.home_activity));
                            intent.putExtra(mContext.getString(R.string.intent_user), holder.user);
                            mContext.startActivity(intent);
                        }
                    });
                    imageLoader.displayImage(singleSnapshot.getValue(UserAccountSettings.class).getProfile_photo(),
                            holder.mProfileImage);
                    holder.mProfileImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });

                    holder.settings = singleSnapshot.getValue(UserAccountSettings.class);
                    holder.comment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ((HomeActivity)mContext).onCommentThreadselected(getItem(position), holder.settings);

                            //TODO:look into this...
                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return convertView;
    }

        private void getCurrentUsername() {
            Log.d(TAG, "getCurrentUsername: retriving user account settings");

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            Query query = reference
                    .child(mContext.getString(R.string.dbname_users))
                    .orderByChild(mContext.getString(R.string.field_user_id))
                    .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        currentUsername = singleSnapshot.getValue(UserAccountSettings.class).getUsername();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


    private void addNewLike(final ViewHolder holder) {
        Log.d(TAG, "addNewLike: adding new like");

        String newLikeID = mReference.push().getKey();
        Like like = new Like();
        like.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());

        mReference.child(mContext.getString(R.string.dbname_photos))
                .child(holder.photo.getPhoto_id())
                .child(mContext.getString(R.string.field_likes))
                .child(newLikeID)
                .setValue(like);

        mReference.child(mContext.getString(R.string.dbname_user_photos))
                .child(holder.photo.getUser_id())
                .child(holder.photo.getPhoto_id())
                .child(mContext.getString(R.string.field_likes))
                .child(newLikeID)
                .setValue(like);

        holder.likeButton.setLiked(true);
        getLikesString(holder);
        holder.likesByCurrentUser = true;
    }

    private void getLikesString(final ViewHolder holder) {
        Log.d(TAG, "getLikesString: getting likes string");

        try {


            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            Query query = reference
                    .child(mContext.getString(R.string.dbname_photos))
                    .child(holder.photo.getPhoto_id())
                    .child(mContext.getString(R.string.field_likes));
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    holder.users = new StringBuilder();
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        Query query = reference
                                .child(mContext.getString(R.string.dbname_users))
                                .orderByChild(mContext.getString(R.string.field_user_id))
                                .equalTo(singleSnapshot.getValue(Like.class).getUser_id());
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                    Log.d(TAG, "onDataChange: found like: "
                                            + singleSnapshot.getValue(User.class).getUsername());

                                    holder.users.append(singleSnapshot.getValue(User.class).getUsername());
                                    holder.users.append(",");
                                }

                                String[] splitUsers = holder.users.toString().split(",");

                                if (holder.users.toString().contains(holder.user.getUsername() + ",")) {
                                    holder.likesByCurrentUser = true;
                                } else {
                                    holder.likesByCurrentUser = false;
                                }

                                int length = splitUsers.length;

                                if (length == 1) {
                                    holder.likesString = "Liked by " + splitUsers[0];
                                } else if (length == 2) {

                                    holder.likesString = "Liked by " + splitUsers[0]
                                            + "  and " + splitUsers[1];
                                } else if (length == 3) {
                                    holder.likesString = "Liked by " + splitUsers[0]
                                            + ", " + splitUsers[1]
                                            + "  and " + splitUsers[2];

                                } else if (length == 4) {
                                    holder.likesString = "Liked by " + splitUsers[0]
                                            + ", " + splitUsers[1]
                                            + ", " + splitUsers[2]
                                            + "  and " + splitUsers[3];

                                } else if (length > 4) {
                                    holder.likesString = "Liked by " + splitUsers[0]
                                            + ", " + splitUsers[1]
                                            + ", " + splitUsers[2]
                                            + "  and " + (splitUsers.length - 3) + " Others";
                                }
                                //setup likes string

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                    if (!dataSnapshot.exists()) {
                        holder.likesString = " ";
                        holder.likesByCurrentUser = false;
//                          setup likes string
                        setupLikesString(holder, holder.likesString);

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            holder.likeButton.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {

                    addNewLike(holder);
                }

                @Override
                public void unLiked(final LikeButton likeButton) {

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                    Query query = reference
                            .child(mContext.getString(R.string.dbname_photos))
                            .child(holder.photo.getPhoto_id())
                            .child(mContext.getString(R.string.field_likes));
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {

                                String keyID = singleSnapshot.getKey();
                                //case 1: The user already liked the photo
                                if (singleSnapshot.getValue(Like.class).getUser_id()
                                        .equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                    mReference.child(mContext.getString(R.string.dbname_photos))
                                            .child(holder.photo.getPhoto_id())
                                            .child(mContext.getString(R.string.field_likes))
                                            .child(keyID)
                                            .removeValue();

                                    mReference.child(mContext.getString(R.string.dbname_user_photos))
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child(holder.photo.getPhoto_id())
                                            .child(mContext.getString(R.string.field_likes))
                                            .child(keyID)
                                            .removeValue();

                                    likeButton.setLiked(false);
                                    getLikesString(holder);
                                    holder.likesByCurrentUser = false;

                                }

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            });


        } catch (NullPointerException e) {
            Log.e(TAG, "getLikesString: NullPointerException: " + e.getMessage());
            holder.likesString = "";
            holder.likesByCurrentUser = false;
            //setup likes string
            setupLikesString(holder, holder.likesString);

        }

    }

        private void setupLikesString(final ViewHolder holder, String likesString){
            Log.d(TAG, "setupLikesString: likes string: " +holder.likesString);

                Log.d(TAG, "setupLikesString: photo is liked by current user");
                holder.likeButton.setLiked(holder.likesByCurrentUser);
                holder.likes.setText(likesString);
        }

        /**
         * Returns a String the number of days ago the post was made
         * @return
         */
        private  String getTimestampDifference(Photo photo){
            Log.d(TAG, "getTimestampDifference: getting timestamp difference");

            String difference = " ";
            Calendar c =Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta")); //googled it... 'Android list of timeZones'
            Date timestamp;
            Date today = c.getTime();
            sdf.format(today);
            final String photoTimestamp = photo.getDate_created();
            try{
                timestamp = sdf.parse(photoTimestamp);
                difference = String.valueOf(Math.round(((today.getTime() - timestamp.getTime())/ 1000 / 60 / 60 / 24 ) ));
            }catch (ParseException e){
                Log.e(TAG, "getTimestampDifference: ParseException :  " + e.getMessage() );
                difference = "0";
            }

            return difference;
        }

    }


