package com.example.tp2_rimaoui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.RecyclerView.AnnonceAdapter;
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
    private int annonceid ;
    private ImageView ImageOffer;
    private ImageView ImageUser;
    private TextView Textlogin;
    private TextView Textdesc;
    private RatingBar ratingBar;
    private TextView TextnbrVote;
    private SessionManager  sessionManager ;
    private ArrayList<Categorie> Categories ;
    private TextView Textcompleted ;
    private ImageView Imagefavorie;
    private TextView TextpostType ;
    private CardView Cardprincipal ;
    private ImageView Imageback ;
    private Myrequest request ;
    private RequestQueue queue ;
    private ArrayList<Annonce> AnnoncesHP;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_offer);

        ServeurIP app = (ServeurIP) getApplicationContext();
        String IPV4_serv = app.getIPV4_serveur();
        sessionManager = new SessionManager(this);
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        request = new Myrequest(this, queue, IPV4_serv);


        Intent intent = getIntent();
        int id_utilisateur = intent.getIntExtra("id_utilisateur",0);
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
        int favorite = intent.getIntExtra("favorite",0);


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
        Imagefavorie = findViewById(R.id.icone_favorie);
        Imageback = findViewById(R.id.imageBack);


        Imageback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                app.setAnnoncesHP(AnnoncesHP);
            }
        });

        TextOfferTitle.setText(titre);
        Textlogin.setText(login);
        Textdesc.setText(description);
        Picasso.get().load(photo_annonce).into(ImageOffer);
        Picasso.get().load(photo_de_profil).into(ImageUser);
        TextnbrVote.setText("(" + nbr_vote + ")");
        ratingBar.setRating(note);

        if(favorite==0){
            Imagefavorie.setImageDrawable(ContextCompat.getDrawable(DetailedOfferActivity.this,R.drawable.favorite_gray));
        } else {
            Imagefavorie.setImageDrawable(ContextCompat.getDrawable(DetailedOfferActivity.this,R.drawable.favorite_gold));
        }

        RecyclerView rv = (RecyclerView) findViewById(R.id.categories);

        //int spanCount = 3;
        //GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getApplicationContext());
        layoutManager.setFlexDirection(FlexDirection.ROW); // or FlexDirection.COLUMN
        layoutManager.setFlexWrap(FlexWrap.WRAP); // or FlexWrap.NOWRAP or FlexWrap.WRAP_REVERSE
        rv.setLayoutManager(layoutManager);



        //MySpanSizeLookup spanSizeLookup = new MySpanSizeLookup();
        //layoutManager.setSpanSizeLookup(spanSizeLookup);
        AnnoncesHP = app.getAnnoncesHP();

        Imagefavorie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(Annonce annonce : AnnoncesHP){
                    if(annonce.getId_annonce()== id_annonce){
                        annonceid = AnnoncesHP.indexOf(annonce);
                    }
                }
                Drawable currentDrawable = Imagefavorie.getDrawable();
                if (currentDrawable.getConstantState().equals(getResources().getDrawable(R.drawable.favorite_gold).getConstantState())) {
                    Imagefavorie.setImageResource(R.drawable.favorite_gray);
                    removeFavorite(IPV4_serv, id_annonce);
                    AnnoncesHP.get(annonceid).setFavorite(0);
                } else {
                    Imagefavorie.setImageResource(R.drawable.favorite_gold);
                    // On ajoute le favorie à la base de donnée
                    addFavorite(IPV4_serv, id_annonce);
                    AnnoncesHP.get(annonceid).setFavorite(1);
                }
                // On ajoute une petite animation
                ObjectAnimator scaleX = ObjectAnimator.ofFloat(Imagefavorie, "scaleX", 1f, 1.2f, 1f);
                ObjectAnimator scaleY = ObjectAnimator.ofFloat(Imagefavorie, "scaleY", 1f, 1.2f, 1f);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.setDuration(300);
                animatorSet.playTogether(scaleX, scaleY);
                animatorSet.start();

            }
        });


        //rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        if(etat==0){
            Textcompleted.setVisibility(View.INVISIBLE);
            Cardprincipal.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        } else if (etat == 1){
            Textcompleted.setText("CLOSED");
        } else {
            Textcompleted.setText("COMPLETED");
        }




        Categories = request.getPostsCategories(id_annonce, new Myrequest.GetPostsCategoriesCallback() {
            @Override
            public void onSucces(String message) {
                //traitement du type de poste
                String type = null;
                for(Categorie categorie : Categories){
                    if(categorie.getNom().equals("Exchange")){
                        Categories.remove(categorie);
                        type = "Exchange";
                        break ;
                    } else if (categorie.getNom().equals("Donation")) {
                        Categories.remove(categorie);
                        type ="Donation";
                        break;
                    }
                }
                TextpostType.setText(type);

                CategorieAdapter Cadapter = new CategorieAdapter(getApplicationContext(), Categories);
                rv.setAdapter(Cadapter);

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
                if(id_utilisateur== Integer.parseInt(sessionManager.getId())){
                    Intent intent = new Intent(getApplicationContext(), MyProfileActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), OtherProfileActivity.class);
                    intent.putExtra("id_utilisateur", id_utilisateur);
                    intent.putExtra("user_login", login);
                    intent.putExtra("user_image", photo_de_profil);
                    intent.putExtra("user_note", note);
                    intent.putExtra("user_nbr_vote", nbr_vote);
                    intent.putExtra("number", number);
                    startActivity(intent);
                }
            }
        });

    }




    private void addFavorite(String IPV4_serv,  int id_annonce) {
        String BASE_URL = "http://" + IPV4_serv + "/swapeit/add_favorite2.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                           // Toast.makeText(DetailedOfferActivity.this, "Success", Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            Toast.makeText(DetailedOfferActivity.this, "Volley Error", Toast.LENGTH_SHORT).show();
                            //Toast.makeText(Home_page.this, "Volley Error", Toast.LENGTH_SHORT).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(Home_page.this, error.toString(),Toast.LENGTH_LONG).show();
                Log.d("APP", "ERROR :" + error);

                if (error instanceof NetworkError) {
                    Toast.makeText(DetailedOfferActivity.this, "Impossible de se connecter", Toast.LENGTH_SHORT).show();
                } else if (error instanceof VolleyError) {
                    Toast.makeText(DetailedOfferActivity.this, "Une erreur s'est produite", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            //C'est dans cette mÃ©thode qu'on envoie les paramÃ¨tres que l'on veut tester dans le script PHP
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("id_annonce", String.valueOf(id_annonce));
                map.put("pseudo", sessionManager.getPseudo());
                return map;
            }
        };

        Volley.newRequestQueue(DetailedOfferActivity.this).add(stringRequest);

    }

    private void removeFavorite(String IPV4_serv, int id_annonce) {
        String BASE_URL = "http://" + IPV4_serv + "/swapeit/remove_favorite2.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                        } catch (Exception e) {
                            Toast.makeText(DetailedOfferActivity.this, "Volley Error", Toast.LENGTH_SHORT).show();
                            //Toast.makeText(Home_page.this, "Volley Error", Toast.LENGTH_SHORT).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(Home_page.this, error.toString(),Toast.LENGTH_LONG).show();
                Log.d("APP", "ERROR :" + error);

                if (error instanceof NetworkError) {
                    Toast.makeText(DetailedOfferActivity.this, "Impossible de se connecter", Toast.LENGTH_SHORT).show();
                } else if (error instanceof VolleyError) {
                    Toast.makeText(DetailedOfferActivity.this, "Une erreur s'est produite", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            //C'est dans cette mÃ©thode qu'on envoie les paramÃ¨tres que l'on veut tester dans le script PHP
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("id_annonce", String.valueOf(id_annonce));
                map.put("pseudo", sessionManager.getPseudo());
                return map;
            }
        };

        Volley.newRequestQueue(DetailedOfferActivity.this).add(stringRequest);

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
