package com.example.josh.socialnetwork.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.josh.socialnetwork.R;
import com.example.josh.socialnetwork.models.User;
import com.example.josh.socialnetwork.models.UserAccountSettings;
import com.example.josh.socialnetwork.models.UserSettings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by JOSH on 13-10-2017.
 */

public class FirebaseMethods {
    private static final String TAG = "FirebaseMethods";

    //FireBase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    private String userID;

    private Context mContext;

    public FirebaseMethods(Context context){
        mAuth = FirebaseAuth.getInstance();
        mContext = context;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        if(mAuth.getCurrentUser() != null){
            userID = mAuth.getCurrentUser().getUid();
        }
    }

    /**
     * update the username in the 'users 's' and 'user account settings' node
     * @param username
     */
    public void updateUsername(String username){
        Log.d(TAG, "updateUsername: updating Username to " + username);

        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .child(mContext.getString(R.string.field_username))
                .setValue(username);
        myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                .child(userID)
                .child(mContext.getString(R.string.field_username))
                .setValue(username);
    }

    /**
     * update the email in the 'users 's' node
     * @param email
     */
    public void updateEmail(String email){
        Log.d(TAG, "updateUsername: updating Email to " + email);

        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .child(mContext.getString(R.string.field_email))
                .setValue(email);
    }
//
//    public boolean checkifUsernameExists(String username, DataSnapshot dataSnapshot){
//        Log.d(TAG, "checkifUsernameExists: checking if " + username + "already exists");
//
//        User user  = new User();
//        for(DataSnapshot ds : dataSnapshot.child(userID).getChildren()){
//            Log.d(TAG, "checkifUsernameExists: datasnalshot:" + ds);
//            user.setUsername(ds.getValue(User.class).getUsername());
//            Log.d(TAG, "checkifUsernameExists: username:" + user.getUsername());
//            if(StringManipulation.expandUsername(user.getUsername()).equals(username)){
//                Log.d(TAG, "checkifUsernameExists: FOUND A MATCH:" + user.getUsername());
//                return true;
//            }
//        }
//        return true;
//    }

    /**
     * Register a new email and password to Firebase Auth
     * @param email
     * @param password
     * @param username
     */
    public void  registerNewEmail(final String email, String password, final String username){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(mContext, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }

                        else if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: Sending email...");
                            //send verification email
                            sendVerificationEmail();

                            userID = mAuth.getCurrentUser().getUid();
                            Log.d(TAG, "onComplete: Authenticate changed:" + userID);
                        }
                        // ...
                    }
                });
    }

    public void sendVerificationEmail(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null){
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                Log.d(TAG, "onComplete: sendEmailVerification: Email sent");
                            }
                            else {
                                Toast.makeText(mContext,"couldn't send verification email.",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
    /**
     * Add a information to the users nodes
     * Add information to the user_account_settings
     * @param email
     * @param username
     * @param description
     * @param website
     * @param profile_photo
     */
    public void addNewUser(String email, String username, String description, String website, String profile_photo){

        User user = new User( userID, 1, email,StringManipulation.condenseUsername(username));

        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .setValue(user);

        UserAccountSettings settings  = new UserAccountSettings(
                description,
                username,
                0,
                0,
                0,
                profile_photo,
                StringManipulation.condenseUsername(username),
                website
        );

        myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                .child(userID)
                .setValue(settings);


    }

    /**
     * Retrives the currently logged in user
     * Database: user_account_settings node
     * @param dataSnapshot
     * @return
     */
    public UserSettings getUserSettings(DataSnapshot dataSnapshot){
        Log.d(TAG, "getUserAccountsettings: retriving the user account settings");

        UserAccountSettings settings =  new UserAccountSettings();
        User user = new User();

        for (DataSnapshot ds : dataSnapshot.getChildren()){

            //for user account settings node
            if(ds.getKey().equals(mContext.getString(R.string.dbname_user_account_settings))){
                Log.d(TAG, "getUserAccountsettings: datasnapshot" + ds);
                try{
                        settings.setDisplay_name(
                                ds.child(userID)
                                .getValue(UserAccountSettings.class)
                                .getDisplay_name()
                        );

                        settings.setUsername(
                                ds.child(userID)
                                        .getValue(UserAccountSettings.class)
                                        .getUsername()
                        );
                        settings.setWebsite(
                                ds.child(userID)
                                        .getValue(UserAccountSettings.class)
                                        .getWebsite()
                        );
                        settings.setDescription(
                                ds.child(userID)
                                        .getValue(UserAccountSettings.class)
                                        .getDescription()
                        );
                        settings.setProfile_photo(
                                ds.child(userID)
                                        .getValue(UserAccountSettings.class)
                                        .getProfile_photo()
                        );
                        settings.setPosts(
                                ds.child(userID)
                                        .getValue(UserAccountSettings.class)
                                        .getPosts()
                        );
                        settings.setFollowing(
                                ds.child(userID)
                                        .getValue(UserAccountSettings.class)
                                        .getFollowing()
                        );
                        settings.setFollowers(
                                ds.child(userID)
                                        .getValue(UserAccountSettings.class)
                                        .getFollowers()
                );
                    Log.d(TAG, "getUserAccountsettings: retrived the user_account_settings information:" + settings.toString());
                }
                catch(NullPointerException e){
                    Log.d(TAG, "getUserAccountsettings: NullPointerException" + e.getMessage());
                }

            }
            //for users node
            if(ds.getKey().equals(mContext.getString(R.string.dbname_users))) {
                Log.d(TAG, "getUserAccountsettings: datasnapshot" + ds);

                user.setUsername(
                        ds.child(userID)
                                .getValue(User.class)
                                .getUsername()
                );
                user.setEmail(
                        ds.child(userID)
                                .getValue(User.class)
                                .getEmail()
                );
                user.setPhone_number(
                        ds.child(userID)
                                .getValue(User.class)
                                .getPhone_number()
                );
                user.setUser_id(
                        ds.child(userID)
                                .getValue(User.class)
                                .getUser_id()

                );
                Log.d(TAG, "getUserAccountsettings: retrived the users information:" + user.toString());

            }
        }

        return new UserSettings(user, settings);
    }
}
