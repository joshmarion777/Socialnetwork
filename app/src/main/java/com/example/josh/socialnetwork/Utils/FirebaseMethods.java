package com.example.josh.socialnetwork.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.josh.socialnetwork.R;
import com.example.josh.socialnetwork.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

/**
 * Created by JOSH on 13-10-2017.
 */

public class FirebaseMethods {
    private static final String TAG = "FirebaseMethods";

    //FireBase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String userID;

    private Context mContext;

    public FirebaseMethods(Context context){
        mAuth = FirebaseAuth.getInstance();
        mContext = context;

        if(mAuth.getCurrentUser() != null){
            userID = mAuth.getCurrentUser().getUid();
        }
    }

    public boolean checkifUsernameExists(String username, DataSnapshot dataSnapshot){
        Log.d(TAG, "checkifUsernameExists: checking if " + username + "already exists");

        User user  = new User();
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            Log.d(TAG, "checkifUsernameExists: datasnalshot:" + ds);
            user.setUsername(ds.getValue(User.class).getUsername());
            Log.d(TAG, "checkifUsernameExists: username:" + user.getUsername());
            if(StringManipulation.expandUsername(user.getUsername()).equals(username)){
                Log.d(TAG, "checkifUsernameExists: FOUND A MATCH:" + user.getUsername());
                return true;
            }
        }
        return true;
    }

    /**
     * Register a new email and password to Firebase Auth
     * @param email
     * @param password
     * @param username
     */
    public void registerNewEmail(final String email, String password, final String username){
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
                            userID = mAuth.getCurrentUser().getUid();
                            Log.d(TAG, "onComplete: Authenticate changed:" + userID);
                        }
                        // ...
                    }
                });
    }
}
