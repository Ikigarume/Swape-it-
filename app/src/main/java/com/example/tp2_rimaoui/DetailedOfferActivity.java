package com.example.tp2_rimaoui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.example.RecyclerView.CategorieAdapter;
import com.example.database_animals.Annonce;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import myrequestt.Myrequest;

public class DetailedOfferActivity extends AppCompatActivity {

    private TextView TextOfferTitle;
    private ImageView ImageOffer;
    private ImageView ImageUser;
    private TextView Textlogin;
    private TextView Textdesc;
    private RatingBar ratingBar;
    private TextView TextnbrVote;
    private SessionManager  sessionManager ;
    private ArrayList<Categorie> Categories ;
    private TextView Textcompleted ;
    private TextView TextpostType ;
    private CardView Cardprincipal ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_offer);

        ServeurIP app = (ServeurIP) getApplicationContext();
        String IPV4_serv = app.getIPV4_serveur();

        sessionManager = new SessionManager(this);

        Intent intent = getIntent();
        String login = intent.getStringExtra("login");
        String photo_de_profil = intent.getStringExtra("photo_de_profil");
        String photo_annonce = intent.getStringExtra("photo_annonce");
        String titre = intent.getStringExtra("titre");
        String description = intent.getStringExtra("description");
        Float note = intent.getFloatExtra("note", 0.0f);
        int nbr_vote = intent.getIntExtra("nbr_vote", 0);
        int id_annonce = intent.getIntExtra("id_annonce", 0);
        String number = intent.getStringExtra("number");
        int etat = intent.getIntExtra("etat",0);


        TextOfferTitle = findViewById(R.id.offer_title);
        ratingBar = findViewById(R.id.ratingBar);
        TextnbrVote = findViewById(R.id.nbr_vote);
        ImageOffer = findViewById(R.id.imgGallery);
        ImageUser = findViewById(R.id.owner_profile_image);
        Textlogin = findViewById(R.id.owner_username);
        Textdesc = findViewById(R.id.offer_description_detailed);
        Textcompleted = findViewById(R.id.completed_text);
        TextpostType = findViewById(R.id.type_post);
        Cardprincipal = findViewById(R.id.offer_details_container);

        TextOfferTitle.setText(titre);
        Textlogin.setText(login);
        Textdesc.setText(description);
        Picasso.get().load(photo_annonce).into(ImageOffer);
        Picasso.get().load(photo_de_profil).into(ImageUser);
        TextnbrVote.setText("(" + nbr_vote + ")");
        ratingBar.setRating(note);

        RecyclerView rv = (RecyclerView) findViewById(R.id.categories);

        int spanCount = 3; // Nombre de colonnes dans la grille
        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        rv.setLayoutManager(layoutManager);

        MySpanSizeLookup spanSizeLookup = new MySpanSizeLookup();
        layoutManager.setSpanSizeLookup(spanSizeLookup);


        //rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        if(etat==0){
            Textcompleted.setVisibility(View.INVISIBLE);
            Cardprincipal.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        }


        Categories = getPostsCategories(IPV4_serv, id_annonce, new GetPostsCategoriesCallback() {
            @Override
            public void onSucces(String message) {
                CategorieAdapter Cadapter = new CategorieAdapter(getApplicationContext(), Categories);
                rv.setAdapter(Cadapter);
                
                //traitement du type de poste
                String type = null;
                for(Categorie categorie : Categories){
                    if(categorie.getNom().equals("Exchange")){
                        type = "Exchange";
                        break ;
                    } else if (categorie.getNom().equals("Donation")) {
                        type ="Donation";
                        break;
                    }
                }
                TextpostType.setText(type);

            }

            @Override
            public void inputErrors(Map<String, String> errors) {

            }

            @Override
            public void onError(String message) {
                Toast.makeText(DetailedOfferActivity.this, message, Toast.LENGTH_SHORT).show();

            }
        });
        
        

        ImageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),OtherProfileActivity.class);
                intent.putExtra("user_login",login);
                intent.putExtra("user_image",photo_de_profil);
                intent.putExtra("user_note", note);
                intent.putExtra("user_nbr_vote",nbr_vote);
                intent.putExtra("number",number);
                startActivity(intent);
            }
        });

    }


    private ArrayList<Categorie> getPostsCategories(String IPV4_serv, int id_annonce, DetailedOfferActivity.GetPostsCategoriesCallback callback) {
        String BASE_URL = "http://" + IPV4_serv + "/swapeit/annonce_categories.php";
        ArrayList<Categorie> Categories = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {

                                JSONObject object = array.getJSONObject(i);
                                String nom = object.getString("nom");
                                int id_categorie = object.getInt("id");

                                Categorie categorie = new Categorie(id_categorie, nom);
                                Categories.add(categorie);

                                callback.onSucces("Informations downloaded successfully.");
                            }

                            //Toast.makeText(Home_page.this, "piste1 :"+cheminImage, Toast.LENGTH_SHORT).show();


                        } catch (Exception e) {
                            callback.onError("Volley Error.");
                            //Toast.makeText(Home_page.this, "Volley Error", Toast.LENGTH_SHORT).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(Home_page.this, error.toString(),Toast.LENGTH_LONG).show();
                Log.d("APP", "ERROR :" + error);

                if (error instanceof NetworkError) {
                    callback.onError("Impossible de se connecter");
                } else if (error instanceof VolleyError) {
                    callback.onError("Une erreur s'est produite");
                }

            }
        }) {
            //C'est dans cette mÃ©thode qu'on envoie les paramÃ¨tres que l'on veut tester dans le script PHP
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("id_annonce", String.valueOf(id_annonce));
                return map;
            }
        };

        Volley.newRequestQueue(DetailedOfferActivity.this).add(stringRequest);
        return Categories;


    }

    public interface GetPostsCategoriesCallback {
        void onSucces(String message);

        void inputErrors(Map<String, String> errors);

        void onError(String message);
    }


}

class MySpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
    @Override
    public int getSpanSize(int position) {
        // Retourne la largeur en nombre de colonnes pour chaque élément
        if (position % 3 == 0) {
            return 2; // Élément de grande taille qui occupe deux colonnes
        } else {
            return 1; // Élément de petite taille qui occupe une seule colonne
        }
    }
}
