package com.example.lab2_mobiledevelopment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lab2_mobiledevelopment.Adapter.MessageAdapter;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    CircleImageView stu_profile_image;
    TextView stu_username;
    FirebaseUser stu_fuser;
    DatabaseReference stu_reference;
    Intent intent;

    ImageButton stu_btn_send, stu_btn_record;
    EditText stu_textSend;

    MessageAdapter stu_messageAdapter;
    List<Chat> stu_mchat;

    RecyclerView stu_recyclerView;
    private static final int REQ_CODE_SPEECH_INPUT = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        stu_recyclerView = findViewById(R.id.recycler_view);
        stu_recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        stu_recyclerView.setLayoutManager(linearLayoutManager);

        stu_profile_image = findViewById(R.id.profile_image);
        stu_username = findViewById(R.id.username);
        stu_btn_send = findViewById(R.id.btn_send);
        stu_btn_record = findViewById(R.id.btn_record);
        stu_textSend = findViewById(R.id.text_send);

        intent = getIntent();
        final String userid = intent.getStringExtra("userid");
        stu_fuser = FirebaseAuth.getInstance().getCurrentUser();
        stu_btn_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startVoiceInput();            }
        });

        stu_btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = stu_textSend.getText().toString();
                if(!msg.equals("")){
                    sendMessage(stu_fuser.getUid(), userid, msg);
                }else {
                    Toast.makeText(MessageActivity.this, "You can't send an empty message", Toast.LENGTH_LONG).show();
                }
                stu_textSend.setText("");
            }
        });

        stu_fuser = FirebaseAuth.getInstance().getCurrentUser();
        stu_reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        stu_reference.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     User user = dataSnapshot.getValue(User.class);
                     stu_username.setText(user.getFirstname() + " " + user.getLastname());


                   if (user.getImageURL().equals("Default")){
                       stu_profile_image.setImageResource(R.mipmap.avatar6);
                   }
                   else if (!user.getImageURL().equals(null)){
                        Glide.with(MessageActivity.this).load(user.getImageURL()).into(stu_profile_image);
                    }

                     readMessages(stu_fuser.getUid(), userid, user.getImageURL());
               }

               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {

               }
          });
        }

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
//        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello, How can I help you?");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    stu_textSend.setText(result.get(0));
                }
                break;
            }
        }
    }
        private void sendMessage(String sender, String receiver, String message){
            DatabaseReference stu_reference = FirebaseDatabase.getInstance().getReference();

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("sender", sender);
            hashMap.put("receiver", receiver);
            hashMap.put("message", message);

            stu_reference.child("Chats").push().setValue(hashMap);

        }

    private void readMessages(final String myid, final String userid, final String imageurl) {
        stu_mchat = new ArrayList<>();

        stu_reference = FirebaseDatabase.getInstance().getReference("Chats");
        stu_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stu_mchat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);

                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid)) {

                        stu_mchat.add(chat);
                    }

                    stu_messageAdapter = new MessageAdapter(MessageActivity.this, stu_mchat, imageurl);
                    stu_recyclerView.setAdapter(stu_messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        }
    }