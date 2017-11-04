package com.example.josh.socialnetwork.Profile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.josh.socialnetwork.R;
import com.example.josh.socialnetwork.Utils.FirebaseMethods;
import com.example.josh.socialnetwork.Utils.UniversalImageLoader;
import com.example.josh.socialnetwork.dialogs.ConfirmPasswordDialog;
import com.example.josh.socialnetwork.models.User;
import com.example.josh.socialnetwork.models.UserAccountSettings;
import com.example.josh.socialnetwork.models.UserSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by JOSH on 09-10-2017.
 */

public class EditProfileFragment extends Fragment implements ConfirmPasswordDialog.OnConfirmPasswordListener{

    @Override
    public void onConfirmPassword(String password) {
        Log.d(TAG, "onConfirmPassword: got the password" + password);
    }

    private static final String TAG = "EditProfileFragment";

    //Edit Profile Fragments Widgets
    private EditText mDisplayName, mUsername, mWebsite, mEmail, mPhoneNumber, mDescription;
    private TextView mChangeProfilePhoto;
    private CircleImageView mProfilePhoto;

    //FireBase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;
    private String userID;

    //varisbles
    private UserSettings mUserSettings;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editprofile ,container , false);
        mProfilePhoto =  view.findViewById(R.id.profile_photo);
        mDisplayName =  view.findViewById(R.id.display_name);
        mUsername =  view.findViewById(R.id.username);
        mWebsite = view.findViewById(R.id.website);
        mEmail =  view.findViewById(R.id.email);
        mDescription =  view.findViewById(R.id.description);
        mPhoneNumber =  view.findViewById(R.id.phoneNumber);
        mChangeProfilePhoto = view.findViewById(R.id.changeProfilePhoto);
        mFirebaseMethods = new FirebaseMethods(getActivity());

        //setProfileImage();
        setupFirebaseAuth();

        //backarrow for navigating back to "Profile Activity"
        ImageView backArrow =  view.findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to Profile Activity");
                getActivity().finish();
            }
        });
        // Check mark to save the changes in Edit Profile
        ImageView checkmark = view.findViewById(R.id.saveChanges);
        checkmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: attempting to save changes");
                saveProfileSettings();
            }
        });

        return view;
    }

//    private void setProfileImage(){
//        Log.d(TAG, "setProfileImage: setting Image Profile");
//        String imgURL = "dailypost.in/wp-content/uploads/2017/06/steve-jobs-31.jpg";
//        UniversalImageLoader.setImage(imgURL, mProfilePhoto, null, "http://");
//
//    }

    /**
     * Retrives the data contained in firebase and submits it to the database
     * Before doing so it makes sure that the user name chosen is unique
     */
    private void saveProfileSettings(){
        final String displayName =  mDisplayName.getText().toString();
        final String username = mUsername.getText().toString();
        final String website = mWebsite.getText().toString();
        final String description = mDescription.getText().toString();
        final String email = mEmail.getText().toString();
        final long phoneNumber = Long.parseLong(mPhoneNumber.getText().toString());



                //case 1: if the user made change to  username
                if (!mUserSettings.getUser().getUsername().equals(username)){

                    checkIfUsernameExists(username);
                }//case 2: if the user made changes to email
                if(!mUserSettings.getUser().getEmail().equals(email)){
                    //step 1: Re-authenticate the user
                    //              Confirm Email and Password
                    ConfirmPasswordDialog dialog =  new ConfirmPasswordDialog();
                    dialog.show(getFragmentManager(), getString(R.string.confirm_password_dialog));
                    dialog.setTargetFragment(EditProfileFragment.this, 1);
                    //step 2: Check if email Already registered
                    //           - 'fetchProvidersforEmail(String Email)'
                    //step 3: Change the email
                    //           - submit the email to Firebase Authentication
                }
                //hence we need to check for uniqueness
            }



    /**
     * Check if @param username is already in the database
     * @param username
     */
    private void checkIfUsernameExists(final String username) {
        Log.d(TAG, "checkIfUsernameExists: Checking if Username:" + username +" already exists");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child(getString(R.string.dbname_users))
                .orderByChild(getString(R.string.field_username))
                .equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    //add the username
                    mFirebaseMethods.updateUsername(username);
                    Toast.makeText(getActivity(), "Saved username", Toast.LENGTH_SHORT).show();
                }
                for(DataSnapshot singleSnapshot: dataSnapshot.getChildren()){
                    if(singleSnapshot.exists()){
                        Log.d(TAG, "CheckIfUsernameExists:onDataChange: FOUND A MATCH:" + singleSnapshot.getValue(User.class).getUsername());
                        Toast.makeText(getActivity(), "That Username already exists.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void  setProfileWidgets(UserSettings userSettings){
        //Log.d(TAG, "setProfileWidgets: setting widgets from data retriving from Firebse " + userSettings.toString());
        Log.d(TAG, "setProfileWidgets: Checking a single value" + userSettings.getUser().getEmail());
        Log.d(TAG, "setProfileWidgets: Checking a single value" + userSettings.getUser().getPhone_number());
        mUserSettings = userSettings;
        //User user =  userSettings.getUser();
        UserAccountSettings settings = userSettings.getSettings();

        UniversalImageLoader.setImage(settings.getProfile_photo(), mProfilePhoto, null, "");

        mDisplayName.setText(settings.getDisplay_name());
        mUsername.setText(settings.getUsername());
        mWebsite.setText(settings.getWebsite());
        mDescription.setText(settings.getDescription());
        mEmail.setText(userSettings.getUser().getEmail());
        mPhoneNumber.setText(String.valueOf(userSettings.getUser().getPhone_number()));

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
        userID = mAuth.getUid();

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
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //retrive user info from database
                 setProfileWidgets(mFirebaseMethods.getUserSettings(dataSnapshot));

                //retrive image from the database

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
