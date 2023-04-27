package com.example.tp2_rimaoui;

import android.content.Intent;
import android.media.Image;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.RecyclerView.OfferAdapter;
import com.example.database_animals.Annonce;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OtherProfileActivity extends AppCompatActivity {

    private Button messageButton ;
    private SessionManager sessionManager ;
    private TextView user_login ;
    private TextView user_number ;
    private ImageView user_image ;
    private RatingBar user_ratingBar ;
    private TextView user_nbr_vote ;
    private ArrayList<Annonce> Annonces ;
    private OfferAdapter offerAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);

        messageButton = findViewById(R.id.MessageButton);
        user_login = findViewById(R.id.other_profile_name);
        user_number = findViewById(R.id.phone_number);
        user_image = findViewById(R.id.profile_image);
        user_ratingBar = findViewById(R.id.ratingBar);
        user_nbr_vote = findViewById(R.id.nbr_vote);

        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Message_Activity.class);
                startActivity(intent);
            }
        });

        ServeurIP app = (ServeurIP) getApplicationContext();
        String IPV4_serv = app.getIPV4_serveur();

        RecyclerView rv =findViewById(R.id.offers_list);
        sessionManager = new SessionManager(this);

        Intent intent = getIntent();
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

        Annonces = getPostsInfo(IPV4_serv, login, new GetPostsInfoCallback() {
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
                Toast.makeText(OtherProfileActivity.this, "Erreur : "+message, Toast.LENGTH_SHORT).show();

            }
        });




    }


    private ArrayList<Annonce> getPostsInfo (String IPV4_serv,String user_login, GetPostsInfoCallback callback){
        String BASE_URL = "http://"+IPV4_serv+"/swapeit/users_posts.php";
        ArrayList<Annonce> Annonces = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONArray array = new JSONArray(response);
                            for (int i =0 ; i<array.length(); i++){

                                JSONObject object = array.getJSONObject(i);
                                String login = object.getString("login");
                                String photo_de_profil = object.getString("photo_de_profil");
                                String chemin_image = object.getString("chemin_image");
                                String titre = object.getString("titre");
                                String description = object.getString("description");
                                Double note = object.getDouble("note");
                                String rate = String.valueOf(note);
                                float rating = Float.valueOf(rate);
                                int nbr_vote = object.getInt("nbr_vote");
                                String id_categories = object.getString("id_categories");
                                int id_annonce = object.getInt("id_annonce");
                                String number = object.getString("telephone");
                                int etat = object.getInt("etat");

                                Annonce annonce = new Annonce(id_annonce,login, photo_de_profil, chemin_image,titre, description, rating, nbr_vote,id_categories,number,etat);
                                Annonces.add(annonce);

                                callback.onSucces("Informations downloaded successfully.");
                            }

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
                map.put("serveurIP", IPV4_serv);
                map.put("user_login",user_login);
                return map;
            }
        };

        Volley.newRequestQueue(OtherProfileActivity.this).add(stringRequest);
        return Annonces ;


    }

    public interface GetPostsInfoCallback{
        void onSucces(String message);
        void inputErrors(Map<String,String> errors);
        void onError(String message);
    }
}
