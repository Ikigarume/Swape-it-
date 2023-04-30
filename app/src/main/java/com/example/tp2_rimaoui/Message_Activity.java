package com.example.tp2_rimaoui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.example.database_animals.Message;
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

        private com.example.RecyclerView.MessageAdapter myAdapter;

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
            TextOtherUserLogin2 = findViewById(R.id.other_user_name) ;
            ImageOtherUserImg = findViewById(R.id.profile_image);
            ImageOtherUserImg2 = findViewById(R.id.other_user_profile_image);
            imageBack = findViewById(R.id.imageBack);

            TextOtherUserLogin.setText(otherUserLogin);
            TextOtherUserLogin2.setText(otherUserLogin);
            Picasso.get().load(otherUserImg).into(ImageOtherUserImg);
            Picasso.get().load(otherUserImg).into(ImageOtherUserImg2);





            messages = request.getAllMessages(sessionManager.getId(),Integer.toString(otherUserId) , new myMessageRequest.GetAllMessagesCallBack() {
                @Override
                public void onSucces(String message){

                }

                @Override
                public void onError(String message) {
                    Toast.makeText(getApplicationContext(),"getting all past messages has failed due to "+message, Toast.LENGTH_SHORT).show();
                }
            }) ;





            imageBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });

            // adapting the images :
            rv = (RecyclerView) findViewById(R.id.recycler_gchat);




            rv.setLayoutManager(new LinearLayoutManager(this));
            rv.setAdapter(myAdapter);


            ActivityResultLauncher<Intent> activityResultLauncher =registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if(result.getResultCode() == Activity.RESULT_OK){
                                Intent data = result.getData();
                                Uri uri = data.getData();
                                try {
                                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                                    // replace this with the part of sending the image
                                    //ImgGallery.setImageBitmap(bitmap);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
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
                    sendMessageToDB(message);

                }
            });
            takePictureButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivity(cameraIntent);
                }
            });

            uploadPictureButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }
    public void sendMessageToDB(String messageToSend){
            loadMessageFromDB();
        return;
    }

    public void loadMessageFromDB(){
            // code to retreive the message ;
            Message loadedMessage ;
            //messages.add(loadedMessage);
            myAdapter.notifyDataSetChanged();
            return ;
    }


    }

