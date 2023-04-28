package com.example.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.database_animals.Annonce;
import com.example.tp2_rimaoui.DetailedOfferActivity;
import com.example.tp2_rimaoui.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class FavAdapter extends RecyclerView.Adapter<FavAdapter.MyViewHolder> {

    private Context context ;
    private ArrayList<Annonce> AnnoncesFav ;

    public FavAdapter(Context context, ArrayList<Annonce> AnnoncesFav){
        this.context = context ;
        this.AnnoncesFav = AnnoncesFav ;
    }





    @Override
    // retournele nb total de cellule que contiendra la liste
    public int getItemCount() {
        return AnnoncesFav.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //pour créer un laouyt depuis un XML
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_favorites, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    //appliquer Une donnée à une vue
    //// binds the data to the TextView... in each row
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Annonce annonce= (Annonce) AnnoncesFav.get(position);
        holder.titre_annonce.setText(annonce.getTitre());
        holder.owner_annonce.setText(annonce.getLogin_utilisateur());
        Picasso.get()
                .load(annonce.getChemin_image())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.errorimage)
                .into(holder.photo_annonce);
        if(annonce.getEtat()==0){
            holder.completed_text.setVisibility(View.INVISIBLE);
            holder.principal_cardview.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white));
        }



    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {

        private final TextView titre_annonce;
        private final ImageView photo_annonce ;
        private final TextView completed_text ;
        private final CardView principal_cardview ;
        private final TextView owner_annonce ;
        private Annonce currentAnnonce;



        public MyViewHolder(final View itemView) {
            super(itemView);
            titre_annonce = itemView.findViewById(R.id.offer_title);
            photo_annonce = itemView.findViewById(R.id.img);
            completed_text = itemView.findViewById(R.id.completed_text);
            principal_cardview = itemView.findViewById(R.id.principal_cardview);
            owner_annonce = itemView.findViewById(R.id.owner_annonce);


            //         itemView.setOnClickListener(this);
            //  item = (TextView) itemView.findViewById(R.id.row_item);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    currentAnnonce = (Annonce) AnnoncesFav.get(getLayoutPosition());
                    Intent intent = new Intent(context, DetailedOfferActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
                    intent.putExtra("favorite", currentAnnonce.getFavorite());



                    context.startActivity(intent);



                }
            });

        }

    }

}
