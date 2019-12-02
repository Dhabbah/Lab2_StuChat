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

    private ListView listView;

    Contact contact;
    User user;
    String contactStatus;
    //List<User> mUsers;
    ArrayList<Contact> arrayListContacts;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        listView = view.findViewById(R.id.listview_Contacts);

        get();
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL)); //line

        return view;

    }

    public void get(){
        arrayListContacts = new ArrayList<Contact>();
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
                contact = new Contact();
                String contactId = cursorContacts.getString(cursorContacts.getColumnIndex(ContactsContract.Contacts._ID));
                String contactDisplayName = cursorContacts.getString(cursorContacts.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                contactStatus = "Active";
                contact.contactName = contactDisplayName;

                int hasPhoneNumber = Integer.parseInt(cursorContacts.getString(cursorContacts.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if(hasPhoneNumber > 0){

                    Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                    , null
                    , ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =?"
                    , new String[]{contactId}
                    , null);

                    while (phoneCursor.moveToNext()){
                        String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        contact.contactTelephoneNumber = phoneNumber;
                    }
                    phoneCursor.close();
                }

                arrayListContacts.add(contact);

            }

            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                        user = snapshot.getValue(User.class);

//                        System.out.println(user.getPhonenumber());
//                        System.out.println(contact.contactTelephoneNumber.replaceAll("[-() ]+", ""));
                        if(user.getPhonenumber().equals(contact.contactTelephoneNumber.replaceAll("[-() ]+", ""))){
                            //System.out.println("Yikes");
                            contact.contactStatus = contactStatus;


                        }
                        else {
                            contact.contactStatus = "Not Register";
                        }
                        AdapterContacts adapterContacts = new AdapterContacts(getContext(), arrayListContacts);
                        listView.setAdapter(adapterContacts);

                    }
//                    AdapterContacts adapterContacts = new AdapterContacts(getContext(), arrayListContacts);
//                    listView.setAdapter(adapterContacts);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

    }

}
