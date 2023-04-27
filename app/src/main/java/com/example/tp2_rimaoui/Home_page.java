package com.example.tp2_rimaoui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.RecyclerView.AnnonceAdapter;
import com.example.database_animals.Annonce;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import myrequestt.Myrequest;

public class Home_page extends AppCompatActivity {

    private AnnonceAdapter annonceAdapter ;
    private Button new_post ;
    private SessionManager  sessionManager ;
    private ImageView profile_image ;
    private RequestQueue queue ;
    private TextView welcometext ;
    private Myrequest request;
    String cheminImage ;
    private ArrayList<Annonce> Annonces ;
    private EditText editSearch ;



    /*
    private ArrayList Annonces = new ArrayList<>(Arrays.asList(
            new Annonce("Ahmed",3, "Nouveau chapeau","Chapeau encore neuf"),
            new Annonce("lila",2, "Nouvelle chaussure","Chapeau encore neuf"),
            new Annonce("Sara",4, "Nouveau monteau","Monteau encore neuf")
             ));
     */



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        ServeurIP app = (ServeurIP) getApplicationContext();
        String IPV4_serv = app.getIPV4_serveur();

        queue = VolleySingleton.getInstance(this).getRequestQueue();
        request = new Myrequest(this, queue, IPV4_serv);


        getUserImage(IPV4_serv, new GetUserImageCallback() {
            @Override
            public void onSucces(String message) {
                Picasso.get().load(cheminImage).into(profile_image, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                        // Erreur lors du chargement de l'image
                    }
                });
            }

            @Override
            public void inputErrors(Map<String, String> errors) {
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });


        RecyclerView rv = findViewById(R.id.list);
        sessionManager = new SessionManager(this);
        Annonces = getPostsInfo(IPV4_serv, new GetPostsInfoCallback() {
            @Override
            public void onSucces(String message) {
                annonceAdapter = new AnnonceAdapter(getApplicationContext(), Annonces);
                rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                rv.setAdapter(annonceAdapter);
            }

            @Override
            public void inputErrors(Map<String, String> errors) {
                Toast.makeText(Home_page.this, "inputErrors", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(String message) {
                Toast.makeText(Home_page.this, message, Toast.LENGTH_SHORT).show();

            }
        });


        profile_image = findViewById(R.id.profile_image);
        new_post = findViewById(R.id.newPost);
        welcometext = findViewById(R.id.textView4);
        editSearch = findViewById(R.id.recherche);

        welcometext.setText("Welcome "+sessionManager.getPseudo());

        editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                    String text = editSearch.getText().toString().trim();
                    ArrayList<Annonce> Result = new ArrayList<>();
                    for(Annonce annonce : Annonces){
                        if(annonce.getTitre().contains(text) || annonce.getDescription().contains(text)){
                            Result.add(annonce);
                        }
                    }
                    annonceAdapter = new AnnonceAdapter(getApplicationContext(), Result);
                    rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    rv.setAdapter(annonceAdapter);
                    return true;
                }
                return false;
            }
        });




        if(sessionManager.isLogged()) {
            String id_user = sessionManager.getId();
        }

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(this, profile_page.class);
                //startActivity(intent);
            }
        });


        new_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),NewPost_Activity.class);
                startActivity(intent);
            }
        });







    }


    private void getUserImage (String IPV4_serv, GetUserImageCallback callback){
        String BASE_URL = "http://"+IPV4_serv+"/swapeit/user_info.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONArray array = new JSONArray(response);
                            JSONObject object = array.getJSONObject(0);
                            cheminImage = object.getString("chemin");
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

        Volley.newRequestQueue(Home_page.this).add(stringRequest);


    }

    public interface GetUserImageCallback{
        void onSucces(String message);
        void inputErrors(Map<String,String> errors);
        void onError(String message);
    }


    private ArrayList<Annonce> getPostsInfo (String IPV4_serv, GetPostsInfoCallback callback){
        String BASE_URL = "http://"+IPV4_serv+"/swapeit/posts_info.php";
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

                                Annonce annonce = new Annonce(id_annonce,login, photo_de_profil, chemin_image,titre, description, rating, nbr_vote,id_categories,number,0);
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
                return map;
            }
        };

        Volley.newRequestQueue(Home_page.this).add(stringRequest);
        return Annonces ;


    }

    public interface GetPostsInfoCallback{
        void onSucces(String message);
        void inputErrors(Map<String,String> errors);
        void onError(String message);
    }

}