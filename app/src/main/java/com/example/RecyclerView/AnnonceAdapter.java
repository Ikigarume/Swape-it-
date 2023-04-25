package com.example.RecyclerView;

import android.app.AlertDialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.*;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.database_animals.Annonce;
import com.example.tp2_rimaoui.R;

import java.util.ArrayList;


public class AnnonceAdapter extends RecyclerView.Adapter<AnnonceAdapter.MyViewHolder> {

    private Context context ;
    private ArrayList<Annonce> Annonces ;

    public AnnonceAdapter(Context context, ArrayList<Annonce> Annonces){
        this.context = context ;
        this.Annonces = Annonces ;
    }





    @Override
    // retournele nb total de cellule que contiendra la liste
    public int getItemCount() {
        return Annonces.size();
    }

    @Override
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

        Annonce annonce= (Annonce) Annonces.get(position);
        holder.login_user.setText(annonce.getLogin_utilisateur());
        holder.titre_annonce.setText(annonce.getTitre());
        holder.desc_annonce.setText(annonce.getDescription());

    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {

        private final TextView login_user;
        private final TextView titre_annonce;
        private final TextView desc_annonce;

        //  private Pair<String, String> currentPair;
        private Annonce currentAnnonce;


        public MyViewHolder(final View itemView) {
            super(itemView);
            login_user = itemView.findViewById(R.id.login_user);
            titre_annonce = itemView.findViewById(R.id.titre_annonce);
            desc_annonce=  itemView.findViewById(R.id.description_annonce);

            //         itemView.setOnClickListener(this);
            //  item = (TextView) itemView.findViewById(R.id.row_item);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    currentAnnonce = (Annonce) Annonces.get(getLayoutPosition());

                    new AlertDialog.Builder(itemView.getContext())
                            .setTitle(currentAnnonce.getTitre())
                            .show();
                }
            });

        }

    }

}
