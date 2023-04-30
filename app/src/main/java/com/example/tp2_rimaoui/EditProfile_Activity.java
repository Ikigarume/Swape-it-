package com.example.tp2_rimaoui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import myrequestt.Myrequest;

public class EditProfile_Activity extends AppCompatActivity {

    private SessionManager sessionManager ;
    private EditText userPseudo, userNumber;
    private TextInputLayout passwordField,oldpasswordField ;
    private ImageView userImage, btnCamera ;
    private Button btnInfo, btnPassword;
    private Myrequest request;
    private RequestQueue queue ;
    Bitmap bitmap ;
    //private EditText pseudo ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        ServeurIP app = (ServeurIP) getApplicationContext();
        String IPV4_serv = app.getIPV4_serveur();
        sessionManager = new SessionManager(this);
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        request = new Myrequest(this, queue, IPV4_serv);

        userPseudo = findViewById(R.id.pseudo_editText) ;
        userNumber = findViewById(R.id.number_editText);
        userImage = findViewById(R.id.profile_image);
        btnInfo = findViewById(R.id.button_upload);
        btnPassword = findViewById(R.id.password_upload);
        oldpasswordField = findViewById(R.id.oldpasswordField);
        passwordField = findViewById(R.id.passwordField);
        btnCamera = findViewById(R.id.btnCamera);

        Intent intent = getIntent();
        String login = intent.getStringExtra("userLogin");
        String number = intent.getStringExtra("userNumber");
        String cheminImage = intent.getStringExtra("userImage");
        int id_utilisateur = intent.getIntExtra("id_utilisateur",0);

        userPseudo.setText(login);
        userNumber.setText(number);
        Picasso.get().load(cheminImage).into(userImage);


        ActivityResultLauncher<Intent> activityResultLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            Uri uri = data.getData();
                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                                userImage.setImageBitmap(bitmap);
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



        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Login = userPseudo.getText().toString().trim();
                String Number = userNumber.getText().toString().trim();

                String base64Image = null;
                ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                if(bitmap!= null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArray);
                    byte[] bytes = byteArray.toByteArray();
                    base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);
                }

                if(base64Image==null){
                    base64Image=cheminImage;
                }


                if(Login.length()>0 && Number.length() > 0 && base64Image.length() > 0){

                    request.updateUserInfo(id_utilisateur, Login, Number, base64Image, new Myrequest.updateUserInfoCallback() {
                        @Override
                        public void onSucces(String message) {
                            Toast.makeText(EditProfile_Activity.this, "Your modifications have been successfully processed", Toast.LENGTH_SHORT).show();
                            sessionManager.setPseudo(Login);
                        }

                        @Override
                        public void inputErrors(Map<String, String> errors) {
                            Toast.makeText(EditProfile_Activity.this, "inputErrors", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onError(String message) {
                            Toast.makeText(EditProfile_Activity.this, message, Toast.LENGTH_SHORT).show();

                        }
                    });



                }

            }
        });

        btnPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPass = passwordField.getEditText().getText().toString().trim();
                String oldPass = oldpasswordField.getEditText().getText().toString().trim();

                if(newPass.length()>0 && oldPass.length()>0){

                    if(newPass.length()>7){
                        request.updateUserPass(sessionManager.getId(), newPass, oldPass, new Myrequest.updateUserPassCallback() {
                            @Override
                            public void onSucces(String message) {
                                Toast.makeText(EditProfile_Activity.this, "Password changed successfully !", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void inputErrors(Map<String, String> errors) {

                            }

                            @Override
                            public void onError(String message) {
                                Toast.makeText(EditProfile_Activity.this, message, Toast.LENGTH_SHORT).show();

                            }
                        });

                    } else {
                        passwordField.setError("Password too short");
                    }

                }
            }
        });





        //Intent intent = getIntent();
/*
        if(intent != null) {
            pseudo.setHint(intent.getExtras().getString("pseudo"));
        }
        else {

        }*/
        //pseudo.setHint("edit your pseudo here");

    }
    public void goingBack(View v){ onBackPressed();}
}
