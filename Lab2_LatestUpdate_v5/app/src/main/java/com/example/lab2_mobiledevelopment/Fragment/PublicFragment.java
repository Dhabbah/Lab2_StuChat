package com.example.lab2_mobiledevelopment.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab2_mobiledevelopment.Adapter.UserAdapter;
import com.example.lab2_mobiledevelopment.R;
import com.example.lab2_mobiledevelopment.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PublicFragment extends Fragment {
    private RecyclerView stu_recyclerView;
    private UserAdapter stu_userAdapter;
    private List<User> stu_mUsers;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View stu_view = inflater.inflate(R.layout.fragment_public, container, false);

        stu_recyclerView = stu_view.findViewById(R.id.recycler_view);
        stu_recyclerView.setHasFixedSize(true);
        stu_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        stu_recyclerView.addItemDecoration(new DividerItemDecoration(stu_recyclerView.getContext(), DividerItemDecoration.VERTICAL)); //line

        stu_mUsers = new ArrayList<>();

        stu_readUsers();


        return stu_view;
    }

    private void stu_readUsers() {
        final FirebaseUser stu_firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference stu_reference = FirebaseDatabase.getInstance().getReference("Users");

        stu_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stu_mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User stu_user = snapshot.getValue(User.class);

//                    assert  stu_user != null;
//                    assert firebaseUser != null;
                    if (!stu_user.getId().equals(stu_firebaseUser.getUid())){
                        stu_mUsers.add(stu_user);
                    }
                }

                stu_userAdapter = new UserAdapter(getContext(), stu_mUsers);
                stu_recyclerView.setAdapter(stu_userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
