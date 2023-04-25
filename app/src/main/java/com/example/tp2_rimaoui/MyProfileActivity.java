package com.example.tp2_rimaoui;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.tp2_rimaoui.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

//import com.example.tp2_rimaoui.databinding.ActivityMyProfileBinding;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;


public class MyProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //private ActivityMyProfileBinding binding;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //binding = ActivityMyProfileBinding.inflate(getLayoutInflater());
        //setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnItemSelectedListener((NavigationBarView.OnItemSelectedListener) this);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        //AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
        //        R.id.navigation_Discussions, R.id.navigation_Offers)
        //        .build();
        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_my_profile);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        //NavigationUI.setupWithNavController(binding.navView, navController);


    }


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
}