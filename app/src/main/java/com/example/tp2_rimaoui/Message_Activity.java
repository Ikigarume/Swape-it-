package com.example.tp2_rimaoui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.database_animals.Message;

import java.util.ArrayList;
import java.util.Arrays;


public class Message_Activity extends AppCompatActivity {
        private ImageView uploadPictureButton ;
        private ImageView takePictureButton ;

        /*
        private MessageListAdapter MessageAdapter;



        private ArrayList messages = new ArrayList<>(Arrays.asList(

                new Message("hello, i would like to ask you a question", 2 , 1 , "00:00"),
                new Message("oh hi, yes you may ask me any question as long as it is related to a product ", 1 , 2 , "00:01"),
                new Message("well i wanted to ask you about the books that you have offered, i was wondering if you could tak pictures of them  ", 2 , 1 , "00:02"),
                new Message("just tp be sure about their conditions ofc ", 2 , 1, "00:03")
        ));



        @Override

        protected void onCreate(Bundle savedInstanceState) {

            // initializes and sets the layout for the Activity
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_messagerie);


            RecyclerView rv = (RecyclerView) findViewById(R.id.recycler_gchat);
            MessageAdapter = new MessageListAdapter(this, messages);
            rv.setLayoutManager(new LinearLayoutManager(this));
            rv.setAdapter(MessageAdapter);


            takePictureButton = (ImageView) findViewById(R.id.take_picture_button);
            uploadPictureButton = (ImageView) findViewById(R.id.upload_picture_button) ;


            // this is the function that takes picture || videos
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

*/

    }

