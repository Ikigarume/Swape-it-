package com.example.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.database_animals.Message;
import com.example.tp2_rimaoui.R;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MessageAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_MESSAGE_SENT = 1 ;
    private static String lastDateMessage = null ;
    private static final int VIEW_TYPE_MESSAGE_RECIEVED = 2 ;
    private int currentUserId ;
    private int otherUserId ;
    private String otherUserLogin ;

    private Context context ;
    private ArrayList<Message> Messages ;
    public MessageAdapter(Context context, ArrayList<Message> messages, int currentUser , int otherUser, String otherUserLogin ){
        this.context = context ;
        this.Messages = messages ;
        this.currentUserId = currentUser ;
        this.otherUserId = otherUser ;
        this.otherUserLogin = otherUserLogin ;
    }
    @Override
    public int getItemCount() {
        return Messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = (Message) Messages.get(position);
        if(message.getIdSender()==currentUserId){
            // if the message is sent by the current user :
            return VIEW_TYPE_MESSAGE_SENT ;
        }
        else {
            // if current user didn't send the message then it was recieved by him :
            return VIEW_TYPE_MESSAGE_RECIEVED ;
        }
    }
    @NonNull
    @Override
    // Inflates the appropriate layout according to the ViewType.
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view ;

        if(viewType == VIEW_TYPE_MESSAGE_SENT){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sent_message,parent,false) ;
            return new SendMessageHolder(view);
        }
        else if (viewType == VIEW_TYPE_MESSAGE_RECIEVED){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_received_message,parent,false);
            return new ReceivedMessageHolder(view);
        }
        return null ;
    }

    // binding the content to the UI :
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message msg = (Message) Messages.get(position);
        switch (holder.getItemViewType()){
            case VIEW_TYPE_MESSAGE_SENT:
                ((SendMessageHolder)holder).bind(msg) ;
                break ;
            case VIEW_TYPE_MESSAGE_RECIEVED:
                ((ReceivedMessageHolder)holder).bind(msg);
        }
    }



    public class SendMessageHolder extends RecyclerView.ViewHolder{
        private final TextView messageText, timeText, dateText ;

        public SendMessageHolder(@NonNull View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.text_gchat_message_me);
            timeText = (TextView) itemView.findViewById(R.id.text_gchat_timestamp_me);
            dateText = itemView.findViewById(R.id.text_message_date_me);
        }

        public void bind(Message msg){
            messageText.setText(msg.getMessage());
            String dateString = msg.getDateMessage();
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat timeoutputFormat = new SimpleDateFormat("HH:mm");
            SimpleDateFormat dateoutputFormat = new SimpleDateFormat("dd MMM yyyy");

            try {
                Date date = inputFormat.parse(dateString);
                String timeonly = timeoutputFormat.format(date);
                String dateOnly = dateoutputFormat.format(date);
                timeText.setText(timeonly);
                if(dateOnly.equals(lastDateMessage)){
                    dateText.setVisibility(View.GONE);
                } else {
                    dateText.setText(dateOnly);
                    lastDateMessage = dateOnly;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }
    public class ReceivedMessageHolder extends RecyclerView.ViewHolder{
        private final TextView messageText , timeText , userNameText, dateText ;
        private final ImageView profileImage ;
        public ReceivedMessageHolder(@NonNull View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
            userNameText = (TextView) itemView.findViewById(R.id.text_message_name);
            profileImage = (ImageView) itemView.findViewById(R.id.other_image);
            dateText = itemView.findViewById(R.id.text_date);
        }
        public void bind(Message msg){
            messageText.setText(msg.getMessage());
            String dateString = msg.getDateMessage();
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat timeoutputFormat = new SimpleDateFormat("HH:mm");
            SimpleDateFormat dateoutputFormat = new SimpleDateFormat("dd MMM yyyy");

            try {
                Date date = inputFormat.parse(dateString);
                String timeonly = timeoutputFormat.format(date);
                String dateOnly = dateoutputFormat.format(date);
                timeText.setText(timeonly);
                if(dateOnly.equals(lastDateMessage)){
                    dateText.setVisibility(View.GONE);
                } else {
                    dateText.setText(dateOnly);
                    lastDateMessage = dateOnly;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            userNameText.setText(otherUserLogin);
        }
    }
}
