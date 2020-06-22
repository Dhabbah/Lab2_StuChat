package com.example.lab2_mobiledevelopment.Fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lab2_mobiledevelopment.LoginActivity;
import com.example.lab2_mobiledevelopment.R;
import com.example.lab2_mobiledevelopment.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileFragment extends Fragment {
// This fragment is only to show the user profile who signed in
    CircleImageView stu_mImageProfile;
    TextView stu_mUsername;

    DatabaseReference stu_reference;
    FirebaseUser stu_fuser;
    private FirebaseAuth stu_auth;

    private EditText stu_FirstName, stu_LastName, stu_PhoneNumber;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View stu_view = inflater.inflate(R.layout.fragment_userprofile, container, false);



        stu_mImageProfile = stu_view.findViewById(R.id.profile_image);
        stu_mUsername = stu_view.findViewById(R.id.username);
        stu_FirstName = stu_view.findViewById(R.id.up_firstname);
        stu_LastName = stu_view.findViewById(R.id.up_lastname);
        stu_PhoneNumber = stu_view.findViewById(R.id.up_phonenumber);

        stu_fuser = FirebaseAuth.getInstance().getCurrentUser();
        stu_reference = FirebaseDatabase.getInstance().getReference("Users")
                .child(stu_fuser.getUid());

        stu_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User stu_user = dataSnapshot.getValue(User.class);
                stu_mUsername.setText(stu_user.getFirstname() + " " + stu_user.getLastname());
                stu_FirstName.setText(stu_user.getFirstname());
                stu_LastName.setText(stu_user.getLastname());
                stu_PhoneNumber.setText(stu_user.getPhonenumber());
                if(stu_user.getImageURL().equals("Default")){
                    stu_mImageProfile.setImageResource(R.mipmap.avatar6);
                }
                else if(!stu_user.getImageURL().equals(null)){
                    Glide.with(getContext()).load(stu_user.getImageURL()).into(stu_mImageProfile);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return stu_view;
    }

    public void onUpdate (View v){
//        // add the sign up details to firebase
//        final String stu_Firstname = stu_FirstName.getText().toString().trim();
//        final String stu_Lastname = stu_LastName.getText().toString().trim();
//        final String stu_Phonenumber = stu_PhoneNumber.getText().toString().trim();
//        //final String Uri_image = imageUri.toString().trim();
//        if(TextUtils.isEmpty(stu_Phonenumber)){
//            Toast.makeText(getActivity(),"Please type your new phone number!",Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if(TextUtils.isEmpty(stu_Firstname)){
//            Toast.makeText(getActivity(),"Please type your new first name!",Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if(TextUtils.isEmpty(stu_Lastname)){
//            Toast.makeText(getActivity(),"Please type your new last name!",Toast.LENGTH_SHORT).show();
//            return;
//        }

    }
}