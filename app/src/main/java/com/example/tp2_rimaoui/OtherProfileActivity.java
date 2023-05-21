package com.example.tp2_rimaoui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.RecyclerView.OfferAdapter;
import com.example.database_animals.Annonce;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import myrequestt.Myrequest;

public class OtherProfileActivity extends AppCompatActivity {

    private Button messageButton ;
    private SessionManager sessionManager ;
    private TextView user_login ;
    private TextView user_number ;
    private ImageView user_image ;
    private RatingBar user_ratingBar ;
    private Button MessageButton ;
    private TextView user_nbr_vote ;
    private ArrayList<Annonce> Annonces ;
    private OfferAdapter offerAdapter ;
    private RecyclerView rv ;
    private List<Integer> Favoris ;
    private RequestQueue queue;
    private Myrequest request ;
    private ImageView imageBack;

    @Override
    protected void onResume() {
        super.onResume();

        ServeurIP app = (ServeurIP) getApplicationContext();
        String IPV4_serv = app.getIPV4_serveur();
        sessionManager = new SessionManager(this);
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        request = new Myrequest(this, queue, IPV4_serv);

        Intent intent = getIntent();
        String login = intent.getStringExtra("user_login");


        Favoris = request.getFavoris(sessionManager.getPseudo(), new Myrequest.GetFavorisCallback() {
            @Override
            public void onSucces(String message) {
                Annonces = request.getUserPosts(login, Favoris, new Myrequest.GetUserPostsCallback() {
                    @Override
                    public void onSucces(String message) {
                        offerAdapter = new OfferAdapter(getApplicationContext(), Annonces);
                        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        rv.setAdapter(offerAdapter);
                    }

                    @Override
                    public void inputErrors(Map<String, String> errors) {
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(OtherProfileActivity.this, "Erreur : " + message, Toast.LENGTH_SHORT).show();
                    }
                });

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
        setContentView(R.layout.activity_other_profile);

        ServeurIP app = (ServeurIP) getApplicationContext();
        String IPV4_serv = app.getIPV4_serveur();
        sessionManager = new SessionManager(this);
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        request = new Myrequest(this, queue, IPV4_serv);

        messageButton = findViewById(R.id.MessageButton);
        user_login = findViewById(R.id.other_profile_name);
        user_number = findViewById(R.id.phone_number);
        user_image = findViewById(R.id.profile_image);
        user_ratingBar = findViewById(R.id.ratingBar);
        user_nbr_vote = findViewById(R.id.nbr_vote);
        imageBack = findViewById(R.id.imageBack);

        rv =findViewById(R.id.offers_list);

        Intent intent = getIntent();
        int id_utilisateur = intent.getIntExtra("id_utilisateur",0);
        String login = intent.getStringExtra("user_login");
        String image = intent.getStringExtra("user_image");
        Float note = intent.getFloatExtra("user_note",0.0f);
        int nbr_vote = intent.getIntExtra("user_nbr_vote",0);
        String number = intent.getStringExtra("number");

        user_login.setText(login);
        Picasso.get().load(image).into(user_image);
        user_ratingBar.setRating(note);
        user_nbr_vote.setText("("+nbr_vote+")");
        user_number.setText("+212"+number);

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Message_Activity.class);
                intent.putExtra("otherUserId",id_utilisateur);
                intent.putExtra("otherUserlogin",login);
                intent.putExtra("otherUserImg",image);
                startActivity(intent);
            }
        });


        Favoris = request.getFavoris(sessionManager.getPseudo(), new Myrequest.GetFavorisCallback() {
                    @Override
                    public void onSucces(String message) {
                        Annonces = request.getUserPosts(login, Favoris, new Myrequest.GetUserPostsCallback() {
                            @Override
                            public void onSucces(String message) {
                                offerAdapter = new OfferAdapter(getApplicationContext(), Annonces);
                                rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                rv.setAdapter(offerAdapter);
                            }

                            @Override
                            public void inputErrors(Map<String, String> errors) {
                            }

                            @Override
                            public void onError(String message) {
                                Toast.makeText(OtherProfileActivity.this, "Erreur : " + message, Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    @Override
                    public void inputErrors(Map<String, String> errors) {

                    }

                    @Override
                    public void onError(String message) {

                    }
                });







    }







}
