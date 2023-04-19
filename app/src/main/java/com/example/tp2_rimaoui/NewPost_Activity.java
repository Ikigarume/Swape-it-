package com.example.tp2_rimaoui;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.RecyclerView.CategorieAdapter;
import com.google.android.material.expandable.ExpandableWidgetHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import myrequestt.Myrequest;

public class NewPost_Activity extends AppCompatActivity {

    private ImageView btnCamera ;
    private ImageView btnGoBack ;
    private SessionManager  sessionManager ;
    private ImageView ImgGallery;
    private RequestQueue queue ;
    private Myrequest request ;
    private Button upload ;
    private final int GALLERY_REQ_CODE=1000;
    private Spinner spinner ;
    private TextView ExchangeText;
    private TextView DonationText ;
    private Button UploadPost ;
    private EditText TitrePost ;
    private EditText DescPost ;
    private int TypePost = 13 ;



    Bitmap bitmap ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        ServeurIP app = (ServeurIP) getApplicationContext();
        String IPV4_serv = app.getIPV4_serveur();

        queue = VolleySingleton.getInstance(this).getRequestQueue();
        request = new Myrequest(this, queue, IPV4_serv);
        sessionManager = new SessionManager(this);


        btnCamera = findViewById(R.id.btnCamera);
        btnGoBack = findViewById(R.id.btnGoBack);
        ImgGallery = findViewById(R.id.imgGallery);
        spinner = findViewById(R.id.spinner1);
        ExchangeText = findViewById(R.id.echangeText);
        DonationText = findViewById(R.id.donationText);
        UploadPost = findViewById(R.id.button_upload);
        TitrePost = findViewById(R.id.TitrePost);
        DescPost = findViewById(R.id.DescPost);

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ExchangeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TypePost = 13 ;
                ExchangeText.setBackgroundColor(getResources().getColor(R.color.light_peche));
                DonationText.setBackgroundColor(getResources().getColor(R.color.white));
            }
        });

        DonationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TypePost = 12 ;
                DonationText.setBackgroundColor(getResources().getColor(R.color.light_peche));
                ExchangeText.setBackgroundColor(getResources().getColor(R.color.white));
            }
        });

        ArrayList<String> Entries = new ArrayList<String>();
        Entries.add("Choose categories");
        ArrayAdapter Adapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1, Entries);
        spinner.setAdapter(Adapter);

        ArrayList<Categorie> Categories_choisies = new ArrayList<Categorie>();
        // get the reference of RecyclerViewRecyclerView
        RecyclerView rv = (RecyclerView) findViewById(R.id.categories);
        //set a LinearLayoutManager with default vertical orientation
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        // call the constructor of MyAdapter to send the reference and data to Adapter
        CategorieAdapter Cadapter = new CategorieAdapter(this, Categories_choisies);
        rv.setAdapter(Cadapter);
        ArrayList<Categorie> Categories = getCategories(IPV4_serv);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Toast.makeText(getApplicationContext(), "Liste :"+Categories, Toast.LENGTH_SHORT).show();
                for ( Categorie categorie : Categories ) {
                    Entries.add(categorie.getNom());
                }
                Adapter.notifyDataSetChanged();

            }
        }, 2000); // Delai de 2 secondes



        Cadapter.setOnItemClickListener(new CategorieAdapter.OnItemClickListener() {
            @Override
            public void deleteCategorie(int position) {
                Categories_choisies.remove(position);
                Cadapter.notifyItemRemoved(position);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    String selectedItem = parent.getItemAtPosition(position).toString();
                    for (Categorie cat : Categories) {
                        if (cat.getNom().equals(selectedItem)) {
                            int flag = 0;
                            for(Categorie catChoisie : Categories_choisies){
                                if(catChoisie.equals(cat)){
                                    flag=1;
                                    break;
                                }
                            }
                            if(flag==0) {
                                Categories_choisies.add(cat);
                                Cadapter.notifyDataSetChanged();
                            }
                            break;
                        }
                    }
                }
                 }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Ajoutez ici le code à exécuter lorsque aucun élément n'est sélectionné dans le Spinner
            }
        });

        ActivityResultLauncher<Intent> activityResultLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            Uri uri = data.getData();
                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                                ImgGallery.setImageBitmap(bitmap);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }


                        }
                    }
                }) ;

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(iGallery);
            }
        });

        /*
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                if(bitmap!= null){
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArray);
                    byte[] bytes = byteArray.toByteArray();
                    final String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    String url ="http://192.168.179.8/swapeit/upload.php";

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(response.equals("success")){
                                        Toast.makeText(getApplicationContext(), "Image uploaded", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }){
                        protected Map<String, String> getParams(){
                            Map<String, String> paramV = new HashMap<>();
                            paramV.put("image", base64Image);
                            return paramV;
                        }
                    };
                    queue.add(stringRequest);
                } else {
                    Toast.makeText(getApplicationContext(), "Select tge image first", Toast.LENGTH_SHORT).show();
                }
            }
        });


         */


        UploadPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<Integer> categories = new ArrayList<>();
                for ( Categorie categorie : Categories_choisies ) {
                    categories.add(categorie.getId());
                }
                categories.add(TypePost);


                String base64Image = null;
                ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                if(bitmap!= null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArray);
                    byte[] bytes = byteArray.toByteArray();
                    base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);
                }

                String titre = TitrePost.getText().toString().trim();
                String desc = DescPost.getText().toString().trim();

                if (titre.length() > 0 && desc.length() > 0 && base64Image.length() > 0) {
                    request.newPost(sessionManager.getId(),titre,base64Image,desc, categories, new Myrequest.newPostCallback() {

                        @Override
                        public void onSucces(String message) {
                            Toast.makeText(NewPost_Activity.this, "Your offer has been successfully added !", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(NewPost_Activity.this, Home_page.class);
                            startActivity(intent);
                            finish();
                        }
                        @Override
                        public void inputErrors(Map<String, String> errors) {
                            //progressbar.setVisibility(View.GONE);

                        }

                        @Override
                        public void onError(String message) {
                            //progressbar.setVisibility(View.GONE);
                            Toast.makeText(NewPost_Activity.this, message, Toast.LENGTH_SHORT).show();

                        }
                    });
                } else {
                    Toast.makeText(NewPost_Activity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }


    private ArrayList<Categorie> getCategories (String IPV4_serv){
        ArrayList<Categorie> liste_categories = new ArrayList<Categorie>();
        String BASE_URL = "http://"+IPV4_serv+"/swapeit/categories.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONArray array = new JSONArray(response);

                            for (int i =0 ; i<array.length(); i++){

                                JSONObject object = array.getJSONObject(i);
                                int id = object.getInt("id");
                                String nom = object.getString("nom");
                                Categorie categorie = new Categorie(id,nom);
                                liste_categories.add(categorie);
                            }


                        }catch (Exception e){
                            Toast.makeText(NewPost_Activity.this, "Volley Error", Toast.LENGTH_SHORT).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(NewPost_Activity.this, error.toString(),Toast.LENGTH_LONG).show();

            }
        }){
            //C'est dans cette mÃ©thode qu'on envoie les paramÃ¨tres que l'on veut tester dans le script PHP
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                return map;
            }
        };

        Volley.newRequestQueue(NewPost_Activity.this).add(stringRequest);
        return liste_categories;

    }


}