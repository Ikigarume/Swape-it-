package com.example.tp2_rimaoui;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tp2_rimaoui.ui.home.HomeFragment;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

//import com.example.tp2_rimaoui.databinding.ActivityMyProfileBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class MyProfileActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {


    public static final String USER_KEY = "user_id";
    Bundle bundle = new Bundle() ;
    //bundle.putInt("user_id", userId);


    //private Button btnMore , btnBack ;
    private SessionManager sessionManager ;
    String cheminImage ;
    int id_utilisateur ;
    String userNumber ;
    Float userNote ;
    int nbr_vote ;

    @Override
    protected void onResume() {
        super.onResume();

        ServeurIP app = (ServeurIP) getApplicationContext();
        String IPV4_serv = app.getIPV4_serveur();

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        ImageView userImage = findViewById(R.id.profile_image);
        TextView userLogin = findViewById(R.id.my_user_name);
        TextView userNum = findViewById(R.id.phone_number);
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        TextView userNbrVote = findViewById(R.id.nbr_vote);

        getUserInfo(IPV4_serv, new GetUserInfoCallback() {
            @Override
            public void onSucces(String message) {
                Picasso.get().load(cheminImage).into(userImage);
                userLogin.setText(sessionManager.getPseudo());
                userNum.setText("+212 "+userNumber);
                ratingBar.setRating(userNote);
                userNbrVote.setText("("+nbr_vote+")");
            }

            @Override
            public void inputErrors(Map<String, String> errors) {

            }

            @Override
            public void onError(String message) {

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        ServeurIP app = (ServeurIP) getApplicationContext();
        String IPV4_serv = app.getIPV4_serveur();

       //btnBack.findViewById(R.id.imageBack);

        //btnMore.findViewById(R.id.imagemore);

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setItemIconTintList(null);
        ImageView userImage = findViewById(R.id.profile_image);
        TextView userLogin = findViewById(R.id.my_user_name);
        TextView userNum = findViewById(R.id.phone_number);
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        TextView userNbrVote = findViewById(R.id.nbr_vote);

        sessionManager = new SessionManager(this);

        replaceFragment(new fragment_Favorites());


        getUserInfo(IPV4_serv, new GetUserInfoCallback() {
            @Override
            public void onSucces(String message) {
                Picasso.get().load(cheminImage).into(userImage);
                userLogin.setText(sessionManager.getPseudo());
                userNum.setText("+212 "+userNumber);
                ratingBar.setRating(userNote);
                userNbrVote.setText("("+nbr_vote+")");
            }

            @Override
            public void inputErrors(Map<String, String> errors) {

            }

            @Override
            public void onError(String message) {

            }
        });


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.navigation_Discussions:
                        replaceFragment(new Fragment_Discussions());

                        return true;
                    case R.id.navigation_Offers:
                        replaceFragment(new Fragement_Offers());

                        return true;
                    case R.id.navigation_favorites:
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

    private void getUserInfo(String IPV4_serv, GetUserInfoCallback callback){
        String BASE_URL = "http://"+IPV4_serv+"/swapeit/user_info.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONArray array = new JSONArray(response);
                            JSONObject object = array.getJSONObject(0);
                            cheminImage = object.getString("chemin");
                            id_utilisateur = object.getInt("id_utilisateur");
                            userNumber = object.getString("telephone");
                            nbr_vote = object.getInt("nbr_vote");
                            Double rate = object.getDouble("note");
                            String rating = String.valueOf(rate);
                            userNote = Float.valueOf(rating);

                            callback.onSucces("Informations downloaded successfully.");

                            //Toast.makeText(Home_page.this, "piste1 :"+cheminImage, Toast.LENGTH_SHORT).show();




                        }catch (Exception e){
                            callback.onError("Volley Error.");
                            //Toast.makeText(Home_page.this, "Volley Error", Toast.LENGTH_SHORT).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(Home_page.this, error.toString(),Toast.LENGTH_LONG).show();
                Log.d("APP","ERROR :"+error);

                if(error instanceof NetworkError){
                    callback.onError("Impossible de se connecter");
                } else if(error instanceof VolleyError){
                    callback.onError("Une erreur s'est produite");
                }

            }
        }){
            //C'est dans cette mÃ©thode qu'on envoie les paramÃ¨tres que l'on veut tester dans le script PHP
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("serveurIP", IPV4_serv); // Ajoutez ce paramètre
                map.put("userpseudo", sessionManager.getPseudo()) ;
                return map;
            }
        };

        Volley.newRequestQueue(MyProfileActivity.this).add(stringRequest);


    }



    public interface GetUserInfoCallback{
        void onSucces(String message);
        void inputErrors(Map<String,String> errors);
        void onError(String message);
    }

    public void goingBack(View v){
        onBackPressed();
    }
    public void showPopUpMenu(View v){
        PopupMenu popup = new PopupMenu(this,v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.log_edit_menu);
        popup.show();
    }
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option_edit_profile :
                Intent intent = new Intent(getApplicationContext(), EditProfile_Activity.class);
                intent.putExtra("userImage",cheminImage);
                intent.putExtra("userLogin",sessionManager.getPseudo());
                intent.putExtra("userNumber",userNumber);
                intent.putExtra("id_utilisateur",id_utilisateur);
                this.startActivity(intent);
            case R.id.option_log_out :
                sessionManager.logout();
                Intent intent2 = new Intent(getApplicationContext(), BoardingActivity.class);
                startActivity(intent2);
                finish();
                return true ;
            default:return false ;
        }

    }

    
}