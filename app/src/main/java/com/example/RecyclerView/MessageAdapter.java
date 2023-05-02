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
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MessageAdapter extends RecyclerView.Adapter {



    private static final int VIEW_TYPE_TEXT_MESSAGE_SENT = 1 ;
    private static final int VIEW_TYPE_IMAGE_MESSAGE_SENT = 2 ;
    private static final int VIEW_TYPE_TEXT_MESSAGE_RECEIVED = 3 ;
    private static final int VIEW_TYPE_IMAGE_MESSAGE_RECEIVED = 4 ;
    private static String lastDateMessage = null ;

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
        if(message.getIdSender()==currentUserId){// if the message is sent by the current user :
            if (message.getMessageType()==0){// the message is a sent text
                return VIEW_TYPE_TEXT_MESSAGE_SENT;
            }
            else { // the message is an image sent :
                return VIEW_TYPE_IMAGE_MESSAGE_SENT ;
            }
        }
        else {//the current user is a receiver of this message
            if (message.getMessageType()==0){// the message is a received text
                return VIEW_TYPE_TEXT_MESSAGE_RECEIVED;
            }
            else { // the message is an image received :
                return VIEW_TYPE_IMAGE_MESSAGE_RECEIVED ;
            }
        }
    }
    @NonNull
    @Override
    // Inflates the appropriate layout according to the ViewType.
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view ;
        if(viewType == VIEW_TYPE_TEXT_MESSAGE_SENT){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sent_message,parent,false) ;
            return new SendMessageHolder(view);
        } else if (viewType == VIEW_TYPE_IMAGE_MESSAGE_SENT ) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sent_message_img,parent,false) ;
            return new SendImageMessageHolder(view);
        } else if (viewType == VIEW_TYPE_TEXT_MESSAGE_RECEIVED){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_received_message,parent,false);
            return new ReceivedMessageHolder(view);
        } else if (viewType== VIEW_TYPE_IMAGE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_received_message_img,parent,false) ;
            return new ReceivedImageMessageHolder(view);
        }
        return null ;
    }
    // binding the content to the UI :
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message msg = (Message) Messages.get(position);
        switch (holder.getItemViewType()){
            case VIEW_TYPE_TEXT_MESSAGE_SENT:
                ((SendMessageHolder)holder).bind(msg) ;
                break ;
            case VIEW_TYPE_IMAGE_MESSAGE_SENT :
                ((SendImageMessageHolder)holder).bind(msg) ;
                break ;
            case VIEW_TYPE_TEXT_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder)holder).bind(msg);
                break;
            case VIEW_TYPE_IMAGE_MESSAGE_RECEIVED:
                ((ReceivedImageMessageHolder)holder).bind(msg);
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
    public class SendImageMessageHolder extends  RecyclerView.ViewHolder {
        private final TextView  timeImg, dateText ;
        private final ImageView messageImg ;
        public SendImageMessageHolder(@NonNull View itemView) {
            super(itemView);
            messageImg = (ImageView) itemView.findViewById(R.id.img_message_body_sent);
            timeImg= (TextView) itemView.findViewById(R.id.text_gchat_timestamp_me);
            dateText = itemView.findViewById(R.id.text_message_date_me);
        }
        public void bind(Message msg){
            Picasso.get().load(msg.getMessage()).placeholder(R.drawable.placeholder).error(R.drawable.errorimage).into(messageImg);
            String dateString = msg.getDateMessage();
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat timeoutputFormat = new SimpleDateFormat("HH:mm");
            SimpleDateFormat dateoutputFormat = new SimpleDateFormat("dd MMM yyyy");

            try {
                Date date = inputFormat.parse(dateString);
                String timeonly = timeoutputFormat.format(date);
                String dateOnly = dateoutputFormat.format(date);
                timeImg.setText(timeonly);
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

    public class ReceivedImageMessageHolder extends RecyclerView.ViewHolder{
        private final TextView   timeImg , userNameText, dateText ;
        private final ImageView profileImage, messageImg ;
        public ReceivedImageMessageHolder(@NonNull View itemView) {
            super(itemView);
            messageImg = (ImageView) itemView.findViewById(R.id.img_message_body_received);
            timeImg = (TextView) itemView.findViewById(R.id.text_message_time);
            userNameText = (TextView) itemView.findViewById(R.id.text_message_name);
            profileImage = (ImageView) itemView.findViewById(R.id.other_image);
            dateText = itemView.findViewById(R.id.text_date);
        }
        public void bind(Message msg){
            Picasso.get().load(msg.getMessage()).placeholder(R.drawable.placeholder).error(R.drawable.errorimage).into(messageImg);
            String dateString = msg.getDateMessage();
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat timeoutputFormat = new SimpleDateFormat("HH:mm");
            SimpleDateFormat dateoutputFormat = new SimpleDateFormat("dd MMM yyyy");

            try {
                Date date = inputFormat.parse(dateString);
                String timeonly = timeoutputFormat.format(date);
                String dateOnly = dateoutputFormat.format(date);
                timeImg.setText(timeonly);
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




