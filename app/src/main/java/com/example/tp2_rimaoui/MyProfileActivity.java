package com.example.tp2_rimaoui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.tp2_rimaoui.ui.home.HomeFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

//import com.example.tp2_rimaoui.databinding.ActivityMyProfileBinding;
import com.google.android.material.navigation.NavigationView;


public class MyProfileActivity extends AppCompatActivity {


    public static final String USER_KEY = "user_id";
    Bundle bundle = new Bundle() ;
    //bundle.putInt("user_id", userId);
    private Button buttonDiscussion , buttonOffers , buttonFavorites , buttonGoBack, buttonMore;
    private SessionManager sessionManager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        buttonDiscussion = findViewById(R.id.discussions_button);
        buttonOffers = findViewById(R.id.offers_button);
        buttonFavorites = findViewById(R.id.favorites_button);
        buttonGoBack = findViewById(R.id.imageBack) ;
        buttonMore = findViewById(R.id.imagemore) ; // to log out and to edit informations

        sessionManager = new SessionManager(this);

        replaceFragment(new fragment_Favorites());
        buttonDiscussion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                replaceFragment(new Fragment_Discussions());

            }
        });

        buttonOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                replaceFragment(new Fragement_Offers());

            }
        });

        buttonFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                replaceFragment(new fragment_Favorites());

            }
        });

        buttonGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager() ;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }
/*
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_Discussions:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                return true;
            case R.id.navigation_Offers:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                return true;
        }
        return false;
    }
 */

    
}