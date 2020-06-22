package com.example.lab2_mobiledevelopment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lab2_mobiledevelopment.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    int REQUEST_CAMERA = 0;
    int SELECT_FILE = 1;
    ImageView stu_userImage;
    Bitmap stu_userphoto = null;

    private FirebaseAuth stu_auth;
    private DatabaseReference stu_databaseReference;
    private StorageReference stu_storageReference;
    private StorageTask uploadTask;
    private Uri stu_imageUri;
    User stu_user;


    private EditText stu_userEmail, stu_userPassword, stu_FirstName, stu_LastName, stu_PhoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        stu_userImage = (ImageView) findViewById(R.id.UserImage);
        stu_userEmail = (EditText) findViewById(R.id.signup_email);
        stu_userPassword = (EditText) findViewById(R.id.signup_password);
        stu_FirstName = (EditText) findViewById(R.id.signup_firstname);
        stu_LastName = (EditText) findViewById(R.id.signup_lastname);
        stu_PhoneNumber = (EditText) findViewById(R.id.signup_phonenumber);
        stu_auth = FirebaseAuth.getInstance();



        stu_storageReference = FirebaseStorage.getInstance().getReference("Uploads");
    }

    public void onClickPhotoButton(View v){
        selectImage();

    }

    private void selectImage(){
        final CharSequence[] items = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
        builder.setTitle("Add Photo!");

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if(items[item].equals("Take Photo")){
                    cameraIntent();
                }
                else if(items[item].equals("Choose from Gallery")){
                    galleryItent();
                }else if(items[item].equals("Cancel")){
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryItent(){
        Intent intent = new Intent();

        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){
            if(requestCode == SELECT_FILE){
                onSelecFromGalleryResult(data);
            }else if(requestCode == REQUEST_CAMERA){
                onCaptureImageResult(data);
            }
        }
    }

    private void onSelecFromGalleryResult(Intent data){

        Bitmap Mybitmap = null;

        if(data != null){
            try{

                Mybitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), stu_imageUri = data.getData());
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        //stu_imageUri = data.getData(); // added
        this.stu_userphoto = Mybitmap;

        stu_userImage.setImageBitmap(Mybitmap);
    }
    private String getFileExtensions(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void onCaptureImageResult(Intent data){
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        stu_imageUri = data.getData();
        String saveImageUrl = MediaStore.Images.Media.insertImage(
                getContentResolver(),
                thumbnail,
                "Image",
                "Image of image"
        );
        stu_imageUri = Uri.parse(saveImageUrl);


        stu_userImage.setImageURI(stu_imageUri);
    }

    public void onSignUp(View v){

        // add the sign up details to firebase
        String stu_email = stu_userEmail.getText().toString().trim();
        String stu_password = stu_userPassword.getText().toString().trim();
        final String stu_Firstname = stu_FirstName.getText().toString().trim();
        final String stu_Lastname = stu_LastName.getText().toString().trim();
        final String stu_Phonenumber = stu_PhoneNumber.getText().toString().trim();
        //final String Uri_image = imageUri.toString().trim();
        if(TextUtils.isEmpty(stu_Phonenumber)){
            Toast.makeText(getApplicationContext(), "please enter your phone number in digits!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(stu_email)){
            Toast.makeText(getApplicationContext(), "please enter your stu_email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(stu_password)){
            Toast.makeText(getApplicationContext(), "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }
        if(stu_password.length() < 6 ){
            Toast.makeText(getApplicationContext(), "Password is too short, enter minimum 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }
        if(true){
            Toast.makeText(getApplicationContext(), "Account is beeing create...", Toast.LENGTH_SHORT).show();
        }

        //create user

        stu_auth.createUserWithEmailAndPassword(stu_email, stu_password)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.


                        if(task.isSuccessful()){

                            FirebaseUser firebaseUser = stu_auth.getCurrentUser();
                            final String stu_userid = firebaseUser.getUid();
                            final String stu_search = stu_Firstname.toLowerCase();
                            final String stu_status = "offline";

                            stu_databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(stu_userid);

                            if (stu_imageUri != null) {
                                StorageReference fileReference = stu_storageReference.child(System.currentTimeMillis()
                                        + "." + getFileExtensions(stu_imageUri));

                                uploadTask = fileReference.putFile(stu_imageUri)
                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                stu_storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        String url = uri.toString();

                                                        Toast.makeText(SignUpActivity.this, "Upload successful", Toast.LENGTH_LONG).show();

                                                        stu_user = new User(stu_userid, stu_search, stu_Firstname, stu_Lastname, url, stu_Phonenumber, stu_status);

                                                        stu_databaseReference.setValue(stu_user);

                                                    }
                                                });

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {

                                stu_user = new User(stu_userid, stu_search, stu_Firstname, stu_Lastname, "Default", stu_Phonenumber, stu_status);
                                stu_databaseReference.setValue(stu_user);
                                Toast.makeText(SignUpActivity.this, "No file selected", Toast.LENGTH_SHORT).show();
                            }
                            stu_databaseReference.setValue(stu_user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){

                                        Toast.makeText(SignUpActivity.this, "Create user complete" + task.isSuccessful(),Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                        finish();
                                    }
                                }
                            });

                        }
                        else{
                            Toast.makeText(SignUpActivity.this, "Failed to create account" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
