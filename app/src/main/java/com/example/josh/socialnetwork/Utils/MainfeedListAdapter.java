package com.example.josh.socialnetwork.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.josh.socialnetwork.R;
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

import java.util.List;

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

    static class ViewHolder{
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
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

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

            holder.likeButton.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {

//                    addNewLike();
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
//                                    getLikesString();
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

            convertView.setTag(holder);
        }else{
            holder  = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    private void addNewLike(){
        Log.d(TAG, "addNewLike: adding new like");

        String newLikeID =  mReference.push().getKey();
        Like like =  new Like();
        like.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());

        mReference.child(mContext.getString(R.string.dbname_photos))
                .child(.getPhoto_id())
                .child(mContext.getString(R.string.field_likes))
                .child(newLikeID)
                .setValue(like);

        myRef.child(mContext.getString(R.string.dbname_user_photos))
                .child(mPhoto.getUser_id())
                .child(mPhoto.getPhoto_id())
                .child(mContext.getString(R.string.field_likes))
                .child(newLikeID)
                .setValue(like);

        likeButton.setLiked(true);
        getLikesString();
        mLikedByCurrentUser = true;

    }


}
