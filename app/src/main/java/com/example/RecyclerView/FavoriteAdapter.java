package com.example.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.database_animals.Favorites;
import com.example.tp2_rimaoui.R;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.MyViewHolder> {

    private Context context ;
    private ArrayList<Favorites> favorites ;
    public FavoriteAdapter(Context c, ArrayList<Favorites> f){
        this.context = c ;
        this.favorites = f ;
    }
    @NonNull
    @Override

    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_favorites,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.MyViewHolder holder, int position) {
        Favorites favorite = (Favorites) favorites.get(position);
        holder.annonceTitle.setText(favorite.getTitle());
        holder.annonceOwner.setText(favorite.getUserName());
    }

    @Override
    public int getItemCount() {
        return favorites.size() ;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView annonceOwner ;
        TextView annonceTitle ;
        private Favorites currentFavorite ;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            annonceOwner = itemView.findViewById(R.id.owner_annonce);
            annonceTitle = itemView.findViewById(R.id.titre_annonce);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentFavorite = (Favorites) favorites.get(getLayoutPosition());
                    new AlertDialog.Builder(itemView.getContext()).setTitle(currentFavorite.getTitle())
                            .show();                }
            }
            );
        }


    }
}
