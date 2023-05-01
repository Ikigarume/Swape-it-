package com.example.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.database_animals.Discussion;
import com.example.tp2_rimaoui.DetailedOfferActivity;
import com.example.tp2_rimaoui.Message_Activity;
import com.example.tp2_rimaoui.R;
import com.example.tp2_rimaoui.SessionManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DiscussionAdapter extends RecyclerView.Adapter<DiscussionAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Discussion> discussions ;


    public DiscussionAdapter(Context context , ArrayList<Discussion> discussions){
        this.context = context ;
        this.discussions = discussions ;
    }





    @Override
    public int getItemCount() {
        return discussions.size();
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
       holder.username.setText(discussion.getOtherChatter());
       holder.msg.setText(discussion.getLatestMsg());

        Picasso.get()
                .load(discussion.getImg_rep())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.errorimage)
                .into(holder.profileImage) ;


    }





    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView username ;
        private TextView msg ;
        private ImageView profileImage ;
        private Discussion currentDiscussion ;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            username= itemView.findViewById(R.id.discussions_username);
            msg= itemView.findViewById(R.id.latest_msg_discussion);
            profileImage = itemView.findViewById(R.id.other_user_profile_image);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    currentDiscussion = (Discussion) discussions.get(getLayoutPosition())   ;
                    Intent intent = new Intent(context, Message_Activity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("otherUserId",currentDiscussion.getOtherChatterId()) ;
                    intent.putExtra("currentUserId", 1); // this one is to get the id of the user
                    context.startActivity(intent);
                }
            });
        }
    }
}
