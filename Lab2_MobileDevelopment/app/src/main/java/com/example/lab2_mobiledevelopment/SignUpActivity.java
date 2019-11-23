package com.example.lab2_mobiledevelopment;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class SignUpActivity extends AppCompatActivity {

    int REQUEST_CAMERA = 0;
    int SELECT_FILE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }
}
