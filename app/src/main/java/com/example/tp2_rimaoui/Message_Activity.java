package com.example.tp2_rimaoui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import myrequestt.Myrequest;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.RecyclerView.MessageAdapter;
import com.example.database_animals.Message;

import java.util.ArrayList;
import java.util.Arrays;


public class Message_Activity extends AppCompatActivity {

        private ImageView otherUserProfileImage ;

        Bitmap bitmap ;
        EditText MessageEditText ;
        private TextView Send ;
        private ImageView uploadPictureButton ;
        private ImageView takePictureButton ;
        private int currentUserId  , otherUserId ;

        private RecyclerView rv ;
        private VolleySingleton networkManager ;

        private com.example.RecyclerView.MessageAdapter myAdapter;

        private ArrayList messages = new ArrayList<>(Arrays.asList(

                new Message( 2 , 1 , "hello, i would like to ask you a question","00:00"),
                new Message( 1 , 2 , "oh hi, yes you may ask me any question as long as it is related to a product ","00:01"),
                new Message( 2 , 1 , "well i wanted to ask you about the books that you have offered, i was wondering if you could tak pictures of them  ","00:02"),
                new Message( 2 , 1, "just tp be sure about their conditions ofc ","00:03")
        ));
        @Override

        protected void onCreate(Bundle savedInstanceState) {

            // initializes and sets the layout for the Activity
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_messagerie);
            // loading the elements
            takePictureButton = (ImageView) findViewById(R.id.take_picture_button);
            uploadPictureButton = (ImageView) findViewById(R.id.upload_picture_button) ;
            Send = (TextView) findViewById(R.id.send_button) ;
            MessageEditText = (EditText) findViewById(R.id.message_input_field) ;

            // get the data thta has being passed to the message activity :
            Intent intent = getIntent();
            currentUserId = intent.getIntExtra("currentUserId",0);
            otherUserId = intent.getIntExtra("otherUserId",0);
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
            // adapting the images :
            rv = (RecyclerView) findViewById(R.id.recycler_gchat);
            myAdapter = new MessageAdapter(this, messages,currentUserId,otherUserId);
            rv.setLayoutManager(new LinearLayoutManager(this));
            rv.setAdapter(myAdapter);











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

