package com.example.tp2_rimaoui;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.RecyclerView.CategorieAdapter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewPost_Activity extends AppCompatActivity {

    private ImageView btnCamera ;
    private ImageView ImgGallery;
    private Button upload ;
    private final int GALLERY_REQ_CODE=1000;
    private Spinner spinner ;


    Bitmap bitmap ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        btnCamera = findViewById(R.id.btnCamera);
        ImgGallery = findViewById(R.id.imgGallery);
        upload = findViewById(R.id.button_upload);
        spinner = findViewById(R.id.spinner1);

        ArrayList Entries = new ArrayList<>(Arrays.asList("Choose Categories", "Categorie1","Categorie2","Categorie3"));
        ArrayList Categories_choisies = new ArrayList<>(Arrays.asList(new Categorie("Categorie1"),new Categorie("Categorie2")));


        ArrayAdapter Adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, Entries);
        spinner.setAdapter(Adapter);

        // get the reference of RecyclerViewRecyclerView
        RecyclerView rv = (RecyclerView) findViewById(R.id.categories);
        //set a LinearLayoutManager with default vertical orientation
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        // call the constructor of MyAdapter to send the reference and data to Adapter
        CategorieAdapter Cadapter = new CategorieAdapter(this, Categories_choisies);
        rv.setAdapter(Cadapter);

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
                if(position>0){
                    String selectedItem = parent.getItemAtPosition(position).toString();
                    Categories_choisies.add(new Categorie(selectedItem));
                    Cadapter.notifyDataSetChanged();
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

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                if(bitmap!= null){
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArray);
                    byte[] bytes = byteArray.toByteArray();
                    final String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    String url ="http://10.1.31.146/swapeit/upload.php";

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
    }


}