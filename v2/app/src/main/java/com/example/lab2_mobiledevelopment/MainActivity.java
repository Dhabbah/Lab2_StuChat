package com.example.lab2_mobiledevelopment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_READ_CONTACTS = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemReselectedListener(navigationListener);

//        if(savedInstanceState == null){
//            Intent intent = new Intent(this, LoginActivity.class);
//            startActivity(intent);
//        }
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new UserProfileFragment()).commit();
        }
    }
    private BottomNavigationView.OnNavigationItemReselectedListener navigationListener =
            new BottomNavigationView.OnNavigationItemReselectedListener() {
                @Override
                public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;
                    boolean doubleclickChecked = false;
                    switch (menuItem.getItemId()){
                        case R.id.nav_userprofile:
                            selectedFragment = new UserProfileFragment();

                            break;
                        case R.id.nav_calls:
                            selectedFragment = new CallsFragment();
                            break;
                        case R.id.nav_chats:
                            selectedFragment = new ChatsFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

                }
            };
}
