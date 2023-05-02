package com.example.tp2_rimaoui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import myrequestt.myMessageRequest;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.example.RecyclerView.MessageAdapter;
import com.example.database_animals.Message;
import com.google.android.material.appbar.AppBarLayout;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;


public class Message_Activity extends AppCompatActivity {

        private ImageView otherUserProfileImage ;
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

        private ArrayList messages = new ArrayList<>();
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

            // get the data thta has being passed to the message activity :

            // just in case they were = 0 then roll back to the previous activity :
            if(currentUserId==0 || otherUserId==0){
                onBackPressed();
            }
            // where the functions for the buttons are :
            Send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String message = MessageEditText.getText().toString().trim();
                    int messageType = 0;
                    request.sendTextMessage(sessionManager.getId(), Integer.toString(otherUserId),message , Integer.toString(messageType),
                            new myMessageRequest.SendTextMessageCallBack(){
                                @Override
                                public void onSucces(String message, int id_message_sent) {
                                    request.getMessage(Integer.toString(id_message_sent), messages, new myMessageRequest.GetMessageCallBack() {
                                        @Override
                                        public void onSucces(String message) {
                                            myAdapter.notifyDataSetChanged();
                                            Toast.makeText(getApplicationContext(), "getting message :"+message, Toast.LENGTH_SHORT).show();
                                        }
                                        @Override
                                        public void onError(String message) {

                                        }
                                    });
                                }
                                @Override
                                public void onError(String message) {
                                    Toast.makeText(getApplicationContext(), "sending message in teh activity :"+message, Toast.LENGTH_SHORT).show();
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
                               Intent data = result.getData();
                               Uri uri = data.getData() ;
                                Toast.makeText(getApplicationContext(), "image have being uploaded", Toast.LENGTH_SHORT).show();
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
                                Intent intent = new Intent(Message_Activity.this, DisplayImg_Activity.class);
                                intent.putExtra("imageUri", uri.toString());
                                startActivity(intent);
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

        }
    }

