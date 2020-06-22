package com.example.lab2_mobiledevelopment.Fragment;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab2_mobiledevelopment.Adapter.AdapterContacts;
import com.example.lab2_mobiledevelopment.Adapter.UserAdapter;
import com.example.lab2_mobiledevelopment.MainActivity;
import com.example.lab2_mobiledevelopment.R;
import com.example.lab2_mobiledevelopment.model.Contact;
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

public class ContactsFragment extends Fragment {

    private ListView stu_listView;

    Contact stu_contact;
    User stu_user;
    String stu_contactStatus;
    //List<User> mUsers;
    ArrayList<Contact> stu_arrayListContacts;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View stu_View = inflater.inflate(R.layout.fragment_contacts, container, false);
        stu_listView = stu_View.findViewById(R.id.listview_Contacts);

        get();
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL)); //line

        return stu_View;

    }

    public void get(){
        stu_arrayListContacts = new ArrayList<Contact>();
        //mUsers = new ArrayList<>(); Not being use

        //Get all contacts
        Cursor cursorContacts = null;
        ContentResolver contentResolver = getActivity().getApplicationContext().getContentResolver();
        try{
            cursorContacts = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        }catch (Exception ex){
            Log.e("Error on contact", ex.getMessage());
        }


        if(cursorContacts.getCount() > 0){
            while (cursorContacts.moveToNext()){
                stu_contact = new Contact();
                String stu_contactId = cursorContacts.getString(cursorContacts.getColumnIndex(ContactsContract.Contacts._ID));
                String stu_contactDisplayName = cursorContacts.getString(cursorContacts.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                stu_contactStatus = "Active";
                stu_contact.contactName = stu_contactDisplayName;

                int stu_hasPhoneNumber = Integer.parseInt(cursorContacts.getString(cursorContacts.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if(stu_hasPhoneNumber > 0){

                    Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                    , null
                    , ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =?"
                    , new String[]{stu_contactId}
                    , null);

                    while (phoneCursor.moveToNext()){
                        String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        stu_contact.contactTelephoneNumber = phoneNumber;
                    }
                    phoneCursor.close();
                }

                stu_arrayListContacts.add(stu_contact);

            }

            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    for(DataSnapshot stu_snapshot : dataSnapshot.getChildren()){

                        stu_user = stu_snapshot.getValue(User.class);

                        if(stu_user.getPhonenumber().equals(stu_contact.contactTelephoneNumber.replaceAll("[-() ]+", ""))){

                            stu_contact.contactStatus = stu_contactStatus;


                        }
                        else {
                            stu_contact.contactStatus = "Not Register";
                        }
                        AdapterContacts stu_adapterContacts = new AdapterContacts(getContext(), stu_arrayListContacts);
                        stu_listView.setAdapter(stu_adapterContacts);

                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

    }

}
