package com.example.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.database_animals.Discussion;
import com.example.tp2_rimaoui.R;

import java.util.ArrayList;

public class DiscussionAdapter extends RecyclerView.Adapter<DiscussionAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Discussion> discussions ;

    public DiscussionAdapter(Context context , ArrayList<Discussion> discussions){
        this.context = context ;
        this.discussions = discussions ;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext()) ;
        View view = inflater.inflate(R.layout.item_discussions , parent, false) ;
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
       Discussion discussion = (Discussion) discussions.get(position);
       //holder.username.setText();
       //holder.msg.setText();
       //holder.profileImage.setImageBitmap();
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView username ;
        TextView msg ;
        ImageView profileImage ;
        Discussion currentDiscussion ;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username= itemView.findViewById(R.id.discussions_username);
            msg= itemView.findViewById(R.id.latest_msg_discussion);
            profileImage = itemView.findViewById(R.id.other_user_profile_image);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    currentDiscussion = (Discussion) discussions.get(getLayoutPosition())   ;
                    new AlertDialog.Builder(itemView.getContext()).setTitle(currentDiscussion.getOtherChatter()).show() ;
                }
            });
        }
    }
}
