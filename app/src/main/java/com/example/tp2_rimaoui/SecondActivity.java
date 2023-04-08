package com.example.tp2_rimaoui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.RecyclerView.CustomItemAnimator;
import com.example.RecyclerView.MyAdapter;
import com.example.RecyclerView.MyDividerItem;
import com.example.database_animals.Pet;
import com.example.database_animals.PetDatabase;

import java.util.List;
import java.util.Random;

public class SecondActivity extends AppCompatActivity {

    //private TextInputEditText username ;
    //Non nécessaire de les garder ici aussi
    private MyAdapter animalAdapter ;
    static List<Pet> Animalerie2 = null ;
    static PetDatabase animaldatabase;
    private CardView cardview_setting ;
    private TextView title_setting ;
    private EditText name_setting ;
    private EditText desc_setting ;
    private Button button_setting;
    private ImageView image_setting ;
    public static String setting_state ="";

/*
    private  final    ArrayList Animalerie = new ArrayList<>(Arrays.asList(
            new Pet("Chaton", "Ces nouveaux nés sont de adorables boules de poils, douces et affectueuses. Ils adorent se faire câliner et passer du temps avec les gens.", R.drawable.chats),
            new Pet("Bulldog", "Ce pitbull est un vrai amour. Il adore interagir avec les gens et les autres chiens, mais il peut parfois sembler un peu seul et chercher de l'attention.", R.drawable.chien),
            new Pet("Caniche", "Ce chien est connu pour son énergie et sa personnalité douce. Il est toujours en mouvement et aime jouer, courir et explorer son environnement. Avec ses yeux vifs et sa queue qui remue sans arrêt, il est difficile de ne pas être charmé par sa joie de vivre.", R.drawable.caniche),
            new Pet("Bulldog", "Ce chien est un vrai rayon de soleil. Toujours heureux et enthousiaste, il est le chien parfait pour ceux qui aiment jouer et s'amuser.", R.drawable.chien2)
    ));


 */



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        cardview_setting = findViewById(R.id.animal_setting);
        title_setting = findViewById(R.id.Title_setting);
        name_setting= findViewById(R.id.Name_setting);
        desc_setting = findViewById(R.id.Descrip_setting);
        button_setting = findViewById(R.id.confirm_setting);
        image_setting = findViewById(R.id.image_setting);


        //Retrieve the list of animals
        animaldatabase = Room.databaseBuilder(getApplicationContext(),PetDatabase.class, "animal_bd")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
        Animalerie2 = SecondActivity.animaldatabase.petdao().getPets();
        // get the reference of RecyclerView
        RecyclerView rv = findViewById(R.id.list);
        animalAdapter = new MyAdapter(this, Animalerie2);
        // set a LinearLayoutManager with default vertical orientation
        rv.setLayoutManager(new LinearLayoutManager(this));
        //Item animator
        rv.setItemAnimator(new CustomItemAnimator());
        //Item decoration :
        rv.addItemDecoration(new MyDividerItem(LinearLayoutManager.VERTICAL,this, 16) );

        //Here we will add the decoration for our recyclerview
        // rv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));


        //here we bind recycler view to adapter
        rv.setAdapter(animalAdapter);

        animalAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void deletePet(int position, Pet pet) {
                // Appel de la fonction onItemClick de la SecondActivity
                Animalerie2.remove(position);
                animalAdapter.notifyItemRemoved(position);
                SecondActivity.animaldatabase.petdao().deletPet(pet);
                Toast.makeText(SecondActivity.this, "Animal delited", Toast.LENGTH_SHORT).show();
                setting_state="";
                animalAdapter.setCurrentSetting(setting_state);
            }

            @Override
            public void updatePet(Pet pet) {
                cardview_setting.setVisibility(View.VISIBLE);
                name_setting.setText(pet.getLabel());
                desc_setting.setText(pet.getName());
                image_setting.setImageResource(pet.getImg());

            }


        });

        button_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsClick() ;

            }
        });





    }

    private void settingsClick() {
        String name = "";
        String desc = "";
            switch (setting_state) {
                case "add":
                    name = name_setting.getText().toString();
                    desc = desc_setting.getText().toString();
                    // générer automatiquement des animaux
                    Random random = new Random();
                    // Générer un nombre aléatoire entre 0 et 2
                    int nombreAleatoire = random.nextInt(5);
                    // Sélectionner l'image en fonction du nombre aléatoire
                    int resId;
                    switch (nombreAleatoire) {
                        case 0:
                            resId = R.drawable.chien;
                            break;
                        case 1:
                            resId = R.drawable.chien2;
                            break;
                        case 2:
                            resId = R.drawable.caniche;
                            break;
                        case 3:
                            resId = R.drawable.labrador;
                            break;
                        case 4:
                            resId = R.drawable.chats;
                            break;
                        default:
                            resId = R.drawable.chien; // valeur par défaut
                            break;
                    }

                    //générer une image aléatoire
                    if (!(name.isEmpty() | desc.isEmpty())) {
                        Pet animal = new Pet(name, desc, resId);
                        SecondActivity.animaldatabase.petdao().addpet(animal);
                        Animalerie2.add(0, animal);
                        animalAdapter.notifyItemInserted(0);
                        cardview_setting.setVisibility(View.INVISIBLE);
                        setting_state = "";
                    }
                case "update":
                    name = name_setting.getText().toString();
                    desc = desc_setting.getText().toString();
                    if (!(name.isEmpty() | desc.isEmpty())) {
                        SecondActivity.animaldatabase.petdao().updatPet(name, desc);
                    }
                    cardview_setting.setVisibility(View.INVISIBLE);
                    setting_state = "";
                    for (Pet pet : Animalerie2) {
                        if (pet.getLabel().equals(name)) {
                            pet.setName(desc);
                            break;
                        }
                    }
                    animalAdapter.notifyDataSetChanged();


            }

    }


    @Override public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
         MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu); }



    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.item1:
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                break;
            case R.id.item2:
                Toast.makeText(this, "Add a pet", Toast.LENGTH_LONG).show();
                setting_state="add";
                cardview_setting.setVisibility(View.VISIBLE);
                break;
            case R.id.item3:
                Toast.makeText(this, "Select a pet to update", Toast.LENGTH_LONG).show();
                setting_state="update";
                animalAdapter.setCurrentSetting(setting_state);
                break;
            case R.id.item4:
                Toast.makeText(this, "Select a pet to delete", Toast.LENGTH_LONG).show();
                setting_state="delete";
                animalAdapter.setCurrentSetting(setting_state);
                //  SupprimerAnimal();
                break;
            case R.id.item5 :
                startActivity(new Intent(SecondActivity.this, SettingsActivity.class));
        }
        return super.onOptionsItemSelected(item); }














}



