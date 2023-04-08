package com.example.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.*;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.database_animals.Pet;
import com.example.tp2_rimaoui.R;
import com.example.tp2_rimaoui.SecondActivity;

import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<Pet> Animalerie ;
    private Context context ;
    String setting_state = SecondActivity.setting_state;
    public static String current_pet ="";
    private OnItemClickListener mClickListener;


    public MyAdapter(Context context, List<Pet> Animalerie){
        this.context = context ;
        this.Animalerie = Animalerie ;
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }

    public interface OnItemClickListener {
        void deletePet(int position, Pet pet);
        void updatePet(Pet pet);
    }




    @Override
    // retournele nb total de cellule que contiendra la liste
    public int getItemCount() {
        return Animalerie.size();
    }
    @Override
    //// inflates the row layout from xml when needed
    //crée la vu d'une cellule
    // parent pour créer la vu et int pour spécifier le type de la cellule si on a plusieurrs type (orgnaisation differts)
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //pour créer un laouyt depuis un XML
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item, parent, false);
        return new MyViewHolder(view);
    }
    @Override
    //appliquer Une donnée à une vue
    //// binds the data to the TextView... in each row
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Pet Animal = (com.example.database_animals.Pet) Animalerie.get(position);
        holder.name.setText(Animal.getName());
        holder.label.setText(Animal.getLabel());
        holder.img.setImageResource(Animal.getImg());

    }

    public void setCurrentSetting(String itemName) {
        // Mettre à jour la valeur de la variable setting_state avec le nom de l'élément courant
        setting_state = itemName;
    }


    //// stores and recycles views as they are scrolled off screen

    public class MyViewHolder extends RecyclerView.ViewHolder  {

        private final TextView label;
        private final TextView name;
        private final ImageView img;

        //  private Pair<String, String> currentPair;
        private Pet currentEtab;



        public MyViewHolder(final View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.label);
            name = itemView.findViewById(R.id.name);
            img=  itemView.findViewById(R.id.img);

            //         itemView.setOnClickListener(this);
            //  item = (TextView) itemView.findViewById(R.id.row_item);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                    if (mClickListener != null) {
                        if(setting_state.equals("delete")) {
                            currentEtab = (Pet) Animalerie.get(getLayoutPosition());
                            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
                            builder.setMessage("Are you sure you want to delete this pet from the list ?");
                            builder.setTitle("Deleting Pet");
                            builder.setCancelable(false);
                            builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                                mClickListener.deletePet(getLayoutPosition(), currentEtab);
                            });
                            builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> { });
                            androidx.appcompat.app.AlertDialog alertDialog = builder.create();
                            alertDialog.show();

                        } else if (setting_state.equals("update")){
                            currentEtab = (Pet) Animalerie.get(getLayoutPosition());
                            mClickListener.updatePet(currentEtab);

                        }




                    }



                }
            });

        }

    }

}
