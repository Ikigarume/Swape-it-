package com.example.tp2_rimaoui;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.util.LocaleData;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import myrequestt.myMessageRequest;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import com.example.RecyclerView.MessageAdapter;
import com.example.database_animals.Message;
import com.google.android.material.appbar.AppBarLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class Message_Activity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    private Bitmap imageBitmap ;

    private ImageView otherUserProfileImage ;
        private int Sender_LastIdMessage = 0 ;
        private int ExchangeState = 0 ;
        private ArrayList<Message> newmessages = new ArrayList<>();
        private Timer timer;
        Bitmap bitmap ;
        EditText MessageEditText ;
        private TextView Send, TextOtherUserLogin, TextOtherUserLogin2 ;
        private ImageView uploadPictureButton, ImageOtherUserImg, ImageOtherUserImg2;
        private ImageView takePictureButton , imageBack ;

        private int currentUserId  , otherUserId ;
        private String otherUserImg, otherUserLogin;

        private RecyclerView rv ;

        private SessionManager sessionManager ;
        private RequestQueue queue ;
        private myMessageRequest request ;

        private MessageAdapter myAdapter;

        private ArrayList<Message> messages = new ArrayList<>();
        @Override

        protected void onCreate(Bundle savedInstanceState) {

            // initializes and sets the layout for the Activity
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_messagerie);


            ServeurIP app = (ServeurIP) this.getApplicationContext();
            String IPV4_serv = app.getIPV4_serveur();
            sessionManager = new SessionManager(this.getApplicationContext()) ;
            queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
            request = new myMessageRequest(getApplicationContext(),queue,IPV4_serv) ;

            getExchangeState(IPV4_serv, new GetExchangeStateCallback() {
                @Override
                public void onSucces(String message) {

                }

                @Override
                public void inputErrors(Map<String, String> errors) {

                }

                @Override
                public void onError(String message) {
                    Toast.makeText(Message_Activity.this, message, Toast.LENGTH_SHORT).show();



                }
            });

            Intent intent = getIntent();
            otherUserId = intent.getIntExtra("otherUserId",0);
            otherUserLogin = intent.getStringExtra("otherUserlogin");
            otherUserImg = intent.getStringExtra("otherUserImg");
            currentUserId = Integer.parseInt(sessionManager.getId());

            // loading the elements
            takePictureButton = (ImageView) findViewById(R.id.take_picture_button);
            uploadPictureButton = (ImageView) findViewById(R.id.upload_picture_button) ;
            TextOtherUserLogin = findViewById(R.id.discussions_username);

            ImageOtherUserImg2 = findViewById(R.id.other_user_profile_image);
            imageBack = findViewById(R.id.imageBack);
            NestedScrollView nestedScrollView = findViewById(R.id.NestedScrollingView);

            TextOtherUserLogin.setText(otherUserLogin);
            Picasso.get().load(otherUserImg).into(ImageOtherUserImg2);

            // adapting the images :
            rv = (RecyclerView) findViewById(R.id.recycler_gchat);

            // Planifier une tâche différée pour faire défiler le NestedScrollView
            //rv.setNestedScrollingEnabled(false);
            rv.setLayoutManager(new LinearLayoutManager(this));

            messages = request.getAllMessages(sessionManager.getId(),Integer.toString(otherUserId) , new myMessageRequest.GetAllMessagesCallBack() {
                @Override
                public void onSucces(String message){
                    myAdapter = new MessageAdapter(getApplicationContext(), messages, currentUserId, otherUserId, otherUserLogin);
                    rv.setAdapter(myAdapter);
                    Sender_LastIdMessage = messages.get(messages.size()-1).getId() ;
                    nestedScrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            // Faire défiler le NestedScrollView jusqu'à la fin
                            nestedScrollView.fullScroll(View.FOCUS_DOWN);
                        }
                    });

                    timer = new Timer();

                    // Définition de la tâche à exécuter toutes les 5 secondes
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            // Affichage du Toast sur le thread principal de l'application
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {
                                public void run() {
                                    newmessages = request.getNewMessages(currentUserId, otherUserId, Sender_LastIdMessage, new myMessageRequest.GetNewMessagesCallBack() {
                                        @Override
                                        public void onSucces(String message) {
                                            if(newmessages.size()>0){
                                                Sender_LastIdMessage = newmessages.get(newmessages.size()-1).getId();
                                                messages.addAll(newmessages);
                                                myAdapter.notifyDataSetChanged();
                                                nestedScrollView.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        // Faire défiler le NestedScrollView jusqu'à la fin
                                                        nestedScrollView.fullScroll(View.FOCUS_DOWN);
                                                    }
                                                });
                                            }
                                        }
                                        @Override
                                        public void onError(String message) {

                                        }
                                    });

                                }
                            });
                        }
                    }, 0, 5000); // Démarrage de la tâche après 0ms et exécution toutes les 5000ms
                }
                @Override
                public void onError(String message) {
                    Toast.makeText(getApplicationContext(),"message error "+message, Toast.LENGTH_SHORT).show();
                }
            }) ;
            imageBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
            Send = (TextView) findViewById(R.id.send_button) ;
            MessageEditText = (EditText) findViewById(R.id.message_input_field) ;
            // where the functions for the buttons are :
            Send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String messageText = MessageEditText.getText().toString().trim();
                    int messageType = 0;
                    request.sendTextMessage(sessionManager.getId(), Integer.toString(otherUserId),messageText , 0,
                            new myMessageRequest.SendTextMessageCallBack(){
                                @Override
                                public void onSucces(String message) {
                                    // Créer un objet de la classe SimpleDateFormat
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    // Obtenir la date et l'heure actuelles
                                    Date date = new Date();
                                    // Formater la date et l'heure selon le modèle souhaité
                                    String heureActuelle = dateFormat.format(date);
                                    Message msg = new Message(currentUserId,otherUserId,messageText,heureActuelle,0);
                                    messages.add(msg);
                                    myAdapter.notifyDataSetChanged();
                                    MessageEditText.setText("");
                                    nestedScrollView.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            // Faire défiler le NestedScrollView jusqu'à la fin
                                            nestedScrollView.fullScroll(View.FOCUS_DOWN);
                                        }
                                    });

                                }
                                @Override
                                public void onError(String message) {
                                    Toast.makeText(getApplicationContext(), "send error:"+message, Toast.LENGTH_SHORT).show();
                                }
                            }
                    );
                }
            });
            ActivityResultLauncher<Intent> cameratakenPicture =registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if(result.getResultCode() == Activity.RESULT_OK){
                               //Intent data = result.getData();
                               //Uri uri = data.getData() ;
                                // Toast.makeText(getApplicationContext(), "image have being uploaded", Toast.LENGTH_SHORT).show();
                                Intent data = result.getData() ;
                                Bundle extras = data.getExtras() ;
                                Bitmap imageBitmap= (Bitmap) extras.get("data");

                                //converting the image to Base64 so we can send it to the database :
                                ByteArrayOutputStream b = new ByteArrayOutputStream();
                                imageBitmap.compress(Bitmap.CompressFormat.JPEG,100,b);
                                byte[] imageBytes = b.toByteArray() ;
                                String base64String = android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT);

                                // saving the file in insternal storage :
                                ContextWrapper cw = new ContextWrapper(getApplicationContext());
                                // path to data/data/swape-it/app_data/imageDir

                                File directory = new File(getApplicationContext().getFilesDir(), "Swape-it");
                                if (!directory.exists()) {

                                    Toast.makeText(getApplicationContext(), "the directory doesnt exist", Toast.LENGTH_SHORT).show();
                                    directory.mkdirs();
                                }
                                File d = getApplicationContext().getFilesDir();
                                Toast.makeText(getApplicationContext(), "the directory does exist and the name is "+d.getName(), Toast.LENGTH_SHORT).show();
                                //File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                                // creating imageDir :
                                Random rand = new Random();
                                int randomNum = rand.nextInt(100);
                                String imageName = "image_"+randomNum+System.currentTimeMillis()+".png";
                                File mypath = new File(directory,imageName);
                                FileOutputStream fos = null;
                                try{
                                    fos = new FileOutputStream(mypath);
                                    // Cusing the compress method to write the image to the outputstream :
                                    imageBitmap.compress(Bitmap.CompressFormat.PNG,100,fos);
                                    Toast.makeText(getApplicationContext(),"the file is compressed",Toast.LENGTH_SHORT).show();

                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                                finally{
                                    try{
                                        fos.close();
                                    }
                                    catch(Exception e){
                                        e.printStackTrace();
                                    }

                                    //Message newMsg = new Message(currentUserId,otherUserId,finalPath, ,1); // this message is to be added in the adapter,
                                }
                                String finalPath = directory.getAbsolutePath() ;
                                request.sendTextMessage(sessionManager.getId(), Integer.toString(otherUserId), base64String, 1, new myMessageRequest.SendTextMessageCallBack() {
                                    @Override
                                    public void onSucces(String message) {
                                        Toast.makeText(getApplicationContext(),"message is sent : "+ message,Toast.LENGTH_SHORT).show();
                                        // Créer un objet de la classe SimpleDateFormat
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        // Obtenir la date et l'heure actuelles
                                        Date date = new Date();
                                        // Formater la date et l'heure selon le modèle souhaité
                                        String heureActuelle = dateFormat.format(date);
                                        Message msg = new Message(currentUserId,otherUserId,finalPath,heureActuelle,1);
                                        messages.add(msg);
                                        myAdapter.notifyDataSetChanged();
                                        MessageEditText.setText("");
                                        nestedScrollView.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                // Faire défiler le NestedScrollView jusqu'à la fin
                                                nestedScrollView.fullScroll(View.FOCUS_DOWN);
                                            }
                                        });
                                    }

                                    @Override
                                    public void onError(String message) {
                                        Toast.makeText(getApplicationContext(),"an error happened : "+message,Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }
                    }
            ) ;
            takePictureButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    //startActivity(cameraIntent);
                    cameratakenPicture.launch(cameraIntent);
                }
            });

            ActivityResultLauncher<Intent> activityResultLauncher =registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if(result.getResultCode() == Activity.RESULT_OK){
                                Intent data = result.getData();
                                Uri uri = data.getData();
                                try {
                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                                    ByteArrayOutputStream B = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,B);
                                    byte[]bytes=B.toByteArray();
                                    String base64Img = Base64.encodeToString(bytes,Base64.DEFAULT);

                                } catch (IOException e) {
                                    Toast.makeText(getApplicationContext(), "something went wrong with getting the bitmap of the image ", Toast.LENGTH_SHORT).show();

                                }
                            }
                        }
                    }
            ) ;
            uploadPictureButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent selectFromGallery = new Intent(Intent.ACTION_PICK);
                    selectFromGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(selectFromGallery) ;
                }
            });
            // ici nous allons executer la commande getNewMessages toutes les 5 secondes pour recuperer les nouveaux messages du destinataire
            // Création d'un objet Timer
        }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Arrêt de la tâche
        timer.cancel();
    }

    private void getExchangeState(String IPV4_serv, GetExchangeStateCallback callback){
        String BASE_URL = "http://"+IPV4_serv+"/swapeit/getExchange.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject json = new JSONObject(response);
                            Boolean error = json.getBoolean("error");
                            if(!error){
                                ExchangeState = json.getInt("etat");
                            }
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
                map.put("current_user_id", String.valueOf(currentUserId)); // Ajoutez ce paramètre
                map.put("other_user_id", String.valueOf(otherUserId)) ;
                return map;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);


    }

    public interface GetExchangeStateCallback{
        void onSucces(String message);
        void inputErrors(Map<String,String> errors);
        void onError(String message);
    }

    public void showPopUpMenu(View v){
        PopupMenu popup = new PopupMenu(this,v);
        popup.setOnMenuItemClickListener(this);
        if(ExchangeState== 0){
            popup.inflate(R.menu.exchange0_menu);
        } else {
            popup.inflate(R.menu.exchange1_menu);

        }

        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option_program:
                FrameLayout fl = findViewById(R.id.linearLayoutOffers);
                View view = findViewById(R.id.darkBackground);
                view.setVisibility(View.VISIBLE);
                fl.setVisibility(View.VISIBLE);
                FragmentManager fragmentManager = getSupportFragmentManager() ;
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragement_Offers fragment = new Fragement_Offers();
                fragmentTransaction.replace(R.id.linearLayoutOffers,fragment);
                fragmentTransaction.commit();
                return true ;
            case R.id.option_cancel:
                Toast.makeText(this, "you wanna cancel ", Toast.LENGTH_SHORT).show();
                return true ;
            case R.id.option_complete:
                Toast.makeText(this, "you wanna complete", Toast.LENGTH_SHORT).show();
                return true ;
            default:
                return false ;
        }
        }

    }



