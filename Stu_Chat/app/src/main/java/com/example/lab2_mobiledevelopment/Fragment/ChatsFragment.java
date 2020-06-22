package com.example.lab2_mobiledevelopment.Fragment;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lab2_mobiledevelopment.Adapter.UserAdapter;
import com.example.lab2_mobiledevelopment.R;
import com.example.lab2_mobiledevelopment.model.Chat;
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

public class ChatsFragment extends Fragment{
// This Fragment is only for displaying the user to whom you send messages
    public RecyclerView stu_recyclerView;
    private UserAdapter stu_userAdapter;
    private List<User> stu_mUsers;

    FirebaseUser stu_fuser;
    DatabaseReference stu_reference;

    private List<String> stu_usersList;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View stu_view = inflater.inflate(R.layout.fragment_chats, container, false);

        stu_recyclerView = stu_view.findViewById(R.id.recycler_view);
        stu_recyclerView.setHasFixedSize(true);
        stu_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        stu_fuser = FirebaseAuth.getInstance().getCurrentUser();

        stu_usersList = new ArrayList<>();

        stu_reference = FirebaseDatabase.getInstance().getReference("Chats");
        stu_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stu_usersList.clear();

                for (DataSnapshot stu_snapshot : dataSnapshot.getChildren()) {
                    Chat stu_chat = stu_snapshot.getValue(Chat.class);

                    if (stu_chat.getSender().equals(stu_fuser.getUid())) {
                        stu_usersList.add(stu_chat.getReceiver());
                    }

                    if (stu_chat.getReceiver().equals(stu_fuser.getUid())) {
                        stu_usersList.add(stu_chat.getSender());

                    }

                    stu_readChats();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return stu_view;
    }
    private void stu_readChats() {

        stu_mUsers = new ArrayList<>();
        stu_reference = FirebaseDatabase.getInstance().getReference("Users");

        stu_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stu_mUsers.clear();


                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User stu_user =  snapshot.getValue(User.class);

                    // display 1 stu_user from chats
                    for (String id : stu_usersList) {

                        System.out.println(stu_usersList);

                        if (stu_user.getId().equals(id)) {
                            if (stu_mUsers.size() != 0) {
                                for (User user1 : stu_mUsers) {
                                    if(!stu_user.getId().equals(user1.getId())) {
                                        stu_mUsers.add(stu_user);

                                    }

                                }
                            }
                            else {
                                stu_mUsers.add(stu_user);
                            }
                        }

                    }
                }

                stu_userAdapter = new UserAdapter(getContext(), stu_mUsers, true);
                stu_recyclerView.setAdapter(stu_userAdapter);;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}