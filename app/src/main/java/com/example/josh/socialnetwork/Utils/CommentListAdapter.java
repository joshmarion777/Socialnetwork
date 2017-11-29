package com.example.josh.socialnetwork.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.josh.socialnetwork.R;
import com.example.josh.socialnetwork.models.Comment;
import com.example.josh.socialnetwork.models.UserAccountSettings;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.like.LikeButton;
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
 * Created by jbghostman on 27/11/17.
 */

public class CommentListAdapter extends ArrayAdapter<Comment> {

    private static final String TAG = "CommentListAdapter";

    private LayoutInflater mInflater;
    private int layoutResourse;
    private Context mContext;

    public CommentListAdapter(@NonNull Context context, int resource,
                              @NonNull List<Comment> objects) {
        super(context, resource, objects);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        layoutResourse = resource;
    }
    private  static class  ViewHolder{

        TextView comment, username, timestamp, reply, likes ;
        CircleImageView profielImage;
        LikeButton like;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final ViewHolder holder;

        if (convertView == null){
            convertView = mInflater.inflate(layoutResourse, parent, false);
            holder =  new ViewHolder();

            holder.comment = convertView.findViewById(R.id.comment);
            holder.reply = convertView.findViewById(R.id.comment_reply);
            holder.username = convertView.findViewById(R.id.comment_username);
            holder.timestamp = convertView.findViewById(R.id.comment_time_posted);
            holder.like = convertView.findViewById(R.id.comment_like);
            holder.likes = convertView.findViewById(R.id.comment_likes);
            holder.profielImage = convertView.findViewById(R.id.comment_profile_image);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        //set the comment
        holder.comment.setText(getItem(position).getComment());

        //set the timestamp difference
        String timestampDifference = getTimestampDifference(getItem(position));
        if (!timestampDifference.equals(0)){
            holder.timestamp.setText(timestampDifference + " d");
        }
        else{
            holder.timestamp.setText("today");
        }

        //set the username and profile image

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child(mContext.getString(R.string.dbname_user_account_settings))
                .orderByChild(mContext.getString(R.string.field_user_id))
                .equalTo(getItem(position).getUser_id());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){

                    holder.username.setText(singleSnapshot.getValue(UserAccountSettings.class).getUsername());

                    ImageLoader imageLoader = ImageLoader.getInstance();

                    imageLoader.displayImage(singleSnapshot.getValue(UserAccountSettings.class).getProfile_photo(),
                            holder.profielImage);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: query Cancelled");
            }
        });

        try{

            if (position == 0){

                holder.like.setVisibility(View.GONE);
                holder.likes.setVisibility(View.GONE);
                holder.reply.setVisibility(View.GONE);
            }
        }catch(NullPointerException e){
            Log.e(TAG, "getView: NullPointerException: " + e.getMessage());
        }



        return convertView;
    }


    /**
     * Returns a String the number of days ago the post was made
     * @return
     */

    private  String getTimestampDifference(Comment comment){
        Log.d(TAG, "getTimestampDifference: getting timestamp difference");

        String difference = " ";
        Calendar c =Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta")); //googled it... 'Android list of timeZones'
        Date timestamp;
        Date today = c.getTime();
        sdf.format(today);
        final String photoTimestamp = comment.getDate_created();
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
