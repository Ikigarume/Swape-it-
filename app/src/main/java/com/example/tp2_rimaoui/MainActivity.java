package com.example.tp2_rimaoui;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Users.MyDatabase;
import com.example.Users.User;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView Text ;
    private Button Button ;
    private TextInputEditText username ;
    private TextInputEditText password ;
    private ActivityResultLauncher<Intent> activityResultLauncher ;
    //Databse
    static User user = new User() ;
    static MyDatabase mydatabase ;
    private Context context ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Text = (TextView) this.findViewById(R.id.nouveau_compte);
        Button = (Button) this.findViewById(R.id.loginButton);
        username = this.findViewById(R.id.usernameField);
        password = this.findViewById(R.id.passwordField);
        mydatabase = Room.databaseBuilder(getApplicationContext(), MyDatabase.class, "user_bd").allowMainThreadQueries().build();


        Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myclick(view);
            }
        });

        Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myclick(view);
            }
        });









    }


    private void myclick(View view) {
        user.login = username.getText().toString();
        user.pass = password.getText().toString();
        List<User> list_users = null;
        if ( !(username.getText().toString()).matches("")) {
            list_users = MainActivity.mydatabase.mydao().getUser(username.getText().toString());}
        switch (view.getId()){
            case R.id.loginButton:
                //Intent intent = new Intent(this, MainActivity.class);
                //intent.putExtra("user","Nabila Rimaoui");
                //activityResultLauncher.launch(intent);
                //Intent intent = new Intent(this, SecondActivity.class);
                //intent.putExtra("user","Nabila Rimaoui");
                //this.startActivity(intent);
                if (list_users.size() != 0) {
                    if (list_users.get(0).pass.contentEquals(password.getText().toString()) ) {
                        Intent intent = new Intent(this, SecondActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Mot de passe incorrect",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Compte non existant",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nouveau_compte:
                //androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this);
                //builder.setMessage("You want to create a new account");
                //builder.setTitle("New account");
                //builder.setCancelable(false);
                //builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                //});
                //builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> { });
                //androidx.appcompat.app.AlertDialog alertDialog = builder.create();
                //alertDialog.show();
                if (list_users.size() != 0) {
                    Toast.makeText(getApplicationContext(), "Impossible de créer le compte: login existant", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    MainActivity.mydatabase.mydao().adduser(user);
                    Toast.makeText(getApplicationContext(), "Bienvenu ! Votre compte est créé avec succès.",
                            Toast.LENGTH_SHORT).show();
                    break;
                }
        }
    }

    public void onActivityResult(ActivityResult result) {
        // Handle the returned Uri
        if (result.getResultCode()==2000){
            if (result.getData() !=null) {
                username.setText("" + result.getData().getStringExtra("Nabila Rimaoui"));
            }
        }
    }


}