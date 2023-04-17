package com.example.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.database_animals.Annonce;
import com.example.database_animals.Pet;
import com.example.tp2_rimaoui.Categorie;
import com.example.tp2_rimaoui.R;

import java.util.ArrayList;


public class CategorieAdapter extends RecyclerView.Adapter<CategorieAdapter.MyViewHolder> {

    private Context context ;
    private ArrayList<Categorie> Categories ;
    private OnItemClickListener mClickListener;

    public CategorieAdapter(Context context, ArrayList<Categorie> Categories){
        this.context = context ;
        this.Categories = Categories ;
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = (OnItemClickListener) listener;
    }



    public interface OnItemClickListener {
        void deleteCategorie(int position);
    }




    @Override
    // retournele nb total de cellule que contiendra la liste
    public int getItemCount() {
        return Categories.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //pour créer un laouyt depuis un XML
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.categorie_item, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    //appliquer Une donnée à une vue
    //// binds the data to the TextView... in each row
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Categorie categorie= (Categorie) Categories.get(position);
        holder.nom_categorie.setText(categorie.getNom());


    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {

        private final TextView nom_categorie;


        //  private Pair<String, String> currentPair;
        private Categorie currentCategorie;


        public MyViewHolder(final View itemView) {
            super(itemView);
            nom_categorie = itemView.findViewById(R.id.categorie_name);


            //         itemView.setOnClickListener(this);
            //  item = (TextView) itemView.findViewById(R.id.row_item);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    currentCategorie = (Categorie) Categories.get(getLayoutPosition());

                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
                    builder.setMessage("Do you want to delete this category ?");
                    builder.setTitle("Deleting Category");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                        mClickListener.deleteCategorie(getLayoutPosition());
                    });
                    builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> { });
                    androidx.appcompat.app.AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }
            });

        }

    }

}
