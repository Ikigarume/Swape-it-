package com.example.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.*;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.database_animals.Annonce;
import com.example.tp2_rimaoui.DetailedOfferActivity;
import com.example.tp2_rimaoui.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

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
        holder.ratingBar.setRating(annonce.getNote());
        holder.nbr_vote.setText(annonce.getNbr_vote()+" votes");
        // Charger l'image avec Picasso
        Picasso.get()
                .load(annonce.getPhoto_de_profil())
                .placeholder(R.drawable.placeholder) // Image de placeholder
                .error(R.drawable.errorimage) // Image d'erreur
                .into(holder.photo_user);
        Picasso.get()
                .load(annonce.getChemin_image())
                .placeholder(R.drawable.placeholder) // Image de placeholder
                .error(R.drawable.errorimage) // Image d'erreur
                .into(holder.photo_annonce);
        if(annonce.getFavorite()==1){
            holder.favorite.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.favorite_gold));
        } else {
            holder.favorite.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.favorite_gray));
        }


    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {

        private final TextView login_user;
        private final TextView titre_annonce;
        private final TextView desc_annonce;
        private final ImageView photo_user ;
        private final ImageView photo_annonce ;
        private final RatingBar ratingBar ;
        private final TextView nbr_vote ;
        private final ImageView favorite ;

        //  private Pair<String, String> currentPair;
        private Annonce currentAnnonce;


        public MyViewHolder(final View itemView) {
            super(itemView);
            login_user = itemView.findViewById(R.id.login_user);
            titre_annonce = itemView.findViewById(R.id.titre_annonce);
            desc_annonce=  itemView.findViewById(R.id.description_annonce);
            photo_annonce = itemView.findViewById(R.id.img);
            photo_user = itemView.findViewById(R.id.profile_image);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            nbr_vote = itemView.findViewById(R.id.nbr_vote);
            favorite = itemView.findViewById(R.id.favorite);

            //         itemView.setOnClickListener(this);
            //  item = (TextView) itemView.findViewById(R.id.row_item);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    currentAnnonce = (Annonce) Annonces.get(getLayoutPosition());
                    Intent intent = new Intent(context, DetailedOfferActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("id_utilisateur",currentAnnonce.getId_user());
                    intent.putExtra("login", currentAnnonce.getLogin_utilisateur());
                    intent.putExtra("photo_de_profil",currentAnnonce.getPhoto_de_profil());
                    intent.putExtra("photo_annonce",currentAnnonce.getChemin_image());
                    intent.putExtra("titre",currentAnnonce.getTitre());
                    intent.putExtra("description",currentAnnonce.getDescription());
                    intent.putExtra("note",currentAnnonce.getNote());
                    intent.putExtra("nbr_vote",currentAnnonce.getNbr_vote());
                    intent.putExtra("id_annonce", currentAnnonce.getId_annonce());
                    intent.putExtra("number", currentAnnonce.getNumber());
                    intent.putExtra("etat",currentAnnonce.getEtat());
                    intent.putExtra("favorite",currentAnnonce.getFavorite());
                    intent.putExtra("longitude", currentAnnonce.getLongitude());
                    intent.putExtra("latitude", currentAnnonce.getLatitude());

                    context.startActivity(intent);




                }
            });

        }

    }

}
