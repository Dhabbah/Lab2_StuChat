package com.example.lab2_mobiledevelopment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.lab2_mobiledevelopment.Fragment.UserProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_READ_CONTACTS = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView stu_bottomNavigationView = findViewById(R.id.bottom_navigation);
        AppBarConfiguration stu_appBarConfiguration = new AppBarConfiguration.Builder( R.id.nav_chats, R.id.nav_Public,
                R.id.nav_profile, R.id.nav_Contacts).build();
        NavController stu_navController = Navigation.findNavController(this, R.id.fragment_container);
        NavigationUI.setupActionBarWithNavController(this, stu_navController, stu_appBarConfiguration);
        NavigationUI.setupWithNavController(stu_bottomNavigationView, stu_navController);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater stu_inflater = getMenuInflater();
        stu_inflater.inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        FirebaseAuth stu_a = FirebaseAuth.getInstance();
        switch(item.getItemId()){
            case R.id.my_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UserProfileFragment()).commit();
                break;
            case R.id.Sign_out:
                stu_a.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
