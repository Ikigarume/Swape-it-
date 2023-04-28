package com.example.tp2_rimaoui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tp2_rimaoui.ui.home.HomeFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

//import com.example.tp2_rimaoui.databinding.ActivityMyProfileBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;


public class MyProfileActivity extends AppCompatActivity {


    public static final String USER_KEY = "user_id";
    Bundle bundle = new Bundle() ;
    //bundle.putInt("user_id", userId);
    private Button buttonDiscussion , buttonOffers , buttonFavorites ;
    private ImageView ImageGoBack, ImageMore ;
    private SessionManager sessionManager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        ImageGoBack = findViewById(R.id.imageBack) ;
        ImageMore = findViewById(R.id.imagemore) ; // to log out and to edit informations
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);


        sessionManager = new SessionManager(this);

        replaceFragment(new fragment_Favorites());




        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_Discussions:
                        Toast.makeText(MyProfileActivity.this, "You clicking on buttondiscc", Toast.LENGTH_SHORT).show();
                        replaceFragment(new Fragment_Discussions());

                        return true;
                    case R.id.navigation_Offers:
                        Toast.makeText(MyProfileActivity.this, "You clicking on buttonoffers", Toast.LENGTH_SHORT).show();
                        replaceFragment(new Fragement_Offers());
                        return true;
                    case R.id.navigation_favorites:
                        Toast.makeText(MyProfileActivity.this, "You clicking on buttonfav", Toast.LENGTH_SHORT).show();
                        replaceFragment(new fragment_Favorites());
                        return true;
                    default:
                        return false;
                }
            }
        });


    }

    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager() ;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }


    
}