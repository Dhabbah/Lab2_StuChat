package com.example.lab2_mobiledevelopment.Fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lab2_mobiledevelopment.R;
import com.example.lab2_mobiledevelopment.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileFragment extends Fragment {
// This fragment is only to show the user profile who signed in
    CircleImageView mImageProfile;
    TextView mUsername;

    DatabaseReference reference;
    FirebaseUser fuser;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_userprofile, container, false);

        mImageProfile = view.findViewById(R.id.profile_image);
        mUsername = view.findViewById(R.id.username);

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users")
                .child(fuser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                mUsername.setText(user.getFirstname() + " " + user.getLastname());

//                if (user.getImageURL().equals("default")) {
////                    mImageProfile.setImageResource(R.mipmap.avatar6);
////
////                } else {
////                    Glide.with(getContext()).load(user.getImageURL()).into(mImageProfile);
////
////                }
                if(!user.getImageURL().equals(null)){
                    Glide.with(getContext()).load(user.getImageURL()).into(mImageProfile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }
}