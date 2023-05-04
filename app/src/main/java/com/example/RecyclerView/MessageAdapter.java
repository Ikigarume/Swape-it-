package com.example.RecyclerView;

import static android.view.View.GONE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.example.database_animals.Message;
import com.example.tp2_rimaoui.R;
import com.example.tp2_rimaoui.ServeurIP;
import com.example.tp2_rimaoui.SessionManager;
import com.example.tp2_rimaoui.VolleySingleton;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import myrequestt.myMessageRequest;

public class MessageAdapter extends RecyclerView.Adapter {


    private SessionManager sessionManager ;
    private RequestQueue queue ;
    private myMessageRequest request ;
    private static final int VIEW_TYPE_TEXT_MESSAGE_SENT = 1 ;
    private static final int VIEW_TYPE_IMAGE_MESSAGE_SENT = 2 ;
    private static final int VIEW_TYPE_TEXT_MESSAGE_RECEIVED = 3 ;
    private static final int VIEW_TYPE_IMAGE_MESSAGE_RECEIVED = 4 ;
    private static final int VIEW_TYPE_ANNONCE = 5 ;
    private static final int VIEW_TYPE_RATING_BAR = 6 ;
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
        if(message.getMessageType()==0){
            if(message.getIdSender()==currentUserId){
                return VIEW_TYPE_TEXT_MESSAGE_SENT;
            } else {
                return VIEW_TYPE_TEXT_MESSAGE_RECEIVED;
            }
        } else if(message.getMessageType()==1){
            if(message.getIdSender()==currentUserId){
                return VIEW_TYPE_IMAGE_MESSAGE_SENT;
            } else {
                return VIEW_TYPE_IMAGE_MESSAGE_RECEIVED;
            }
        } else if (message.getMessageType()==2){
            return VIEW_TYPE_TEXT_MESSAGE_SENT;
        } else {
            return VIEW_TYPE_TEXT_MESSAGE_SENT;
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
        } else if (viewType == VIEW_TYPE_ANNONCE){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_programm_exchange,parent,false) ;
            return new ProgramExchangeHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rating_bar,parent,false) ;
            return new RatingBarHolder(view);
        }
    }
    // binding the content to the UI :
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message msg = (Message) Messages.get(position);
        switch (holder.getItemViewType()){
            case VIEW_TYPE_TEXT_MESSAGE_SENT:
                ((SendMessageHolder)holder).bind(msg,position) ;
                break ;
            case VIEW_TYPE_IMAGE_MESSAGE_SENT :
                ((SendImageMessageHolder)holder).bind(msg) ;
                break ;
            case VIEW_TYPE_TEXT_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder)holder).bind(msg);
                break;
            case VIEW_TYPE_IMAGE_MESSAGE_RECEIVED:
                ((ReceivedImageMessageHolder)holder).bind(msg);
                break;
            case VIEW_TYPE_ANNONCE:
                ((ProgramExchangeHolder)holder).bind(msg);
                break;
            case VIEW_TYPE_RATING_BAR:
                ((RatingBarHolder)holder).bind(msg);
                break;
        }
    }
    public class SendMessageHolder extends RecyclerView.ViewHolder{
        private final TextView messageText, timeText, dateText, AnnonceText ;
        private final ConstraintLayout Container_SendMessage ;
        private final CardView Container_PostMessage, Container_RatingBar ;
        private final ImageView clearRatingBar ;
        private final RatingBar ratingBar ;

        public SendMessageHolder(@NonNull View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.text_gchat_message_me);
            timeText = (TextView) itemView.findViewById(R.id.text_gchat_timestamp_me);
            dateText = itemView.findViewById(R.id.text_message_date_me);
            Container_SendMessage = itemView.findViewById(R.id.Container_SentMessage);
            Container_PostMessage = itemView.findViewById(R.id.Container_PostMessage);
            Container_RatingBar = itemView.findViewById(R.id.Container_ratingbar);
            AnnonceText = itemView.findViewById(R.id.textView1);
            clearRatingBar = itemView.findViewById(R.id.clear_ratingbar);
            ratingBar = itemView.findViewById(R.id.ratingBar);

        }

        public void bind(Message msg, int position){
            ServeurIP app = (ServeurIP) context;
            String IPV4_serv = app.getIPV4_serveur();
            sessionManager = new SessionManager(context) ;
            queue = VolleySingleton.getInstance(context).getRequestQueue();
            request = new myMessageRequest(context,queue,IPV4_serv) ;


            if(msg.getMessageType()==0){
                Container_PostMessage.setVisibility(GONE);
                Container_RatingBar.setVisibility(GONE);
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
                        dateText.setVisibility(GONE);
                    } else {
                        dateText.setText(dateOnly);
                        lastDateMessage = dateOnly;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            else if (msg.getMessageType()==2) {
                Container_SendMessage.setVisibility(GONE);
                Container_RatingBar.setVisibility(GONE);
                AnnonceText.setText(msg.getMessage());
            } else {
                Container_SendMessage.setVisibility(GONE);
                Container_PostMessage.setVisibility(GONE);
                Toast.makeText(context, "position : "+position, Toast.LENGTH_SHORT).show();
                clearRatingBar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Container_RatingBar.setVisibility(GONE);
                        Float note = ratingBar.getRating();
                        request.addRating(otherUserId, note, new myMessageRequest.addRatingCallback() {
                            @Override
                            public void onSuccess(String message) {
                                Toast.makeText(context, "Thanking for rating this user !", Toast.LENGTH_SHORT).show();
                                request.doneRating(currentUserId, otherUserId, position, new myMessageRequest.doneRatingCallback() {
                                    @Override
                                    public void onSuccess(String message) {
                                        Toast.makeText(context, "deleted from database successfully", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onError(String message) {

                                    }
                                });
                            }

                            @Override
                            public void onError(String message) {
                                Toast.makeText(context, "Adding rating error : "+message, Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                });
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
                    dateText.setVisibility(GONE);
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
                    dateText.setVisibility(GONE);
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
                    dateText.setVisibility(GONE);
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

    public class ProgramExchangeHolder extends RecyclerView.ViewHolder{
        private final TextView Text ;

        public ProgramExchangeHolder(@NonNull View itemView) {
            super(itemView);
            Text = (TextView) itemView.findViewById(R.id.textView1);

        }

        public void bind(Message msg){
            Text.setText(msg.getMessage());
        }
    }

    public class RatingBarHolder extends RecyclerView.ViewHolder{
        private final RatingBar ratingBar;

        public RatingBarHolder(@NonNull View itemView) {
            super(itemView);
            ratingBar =  itemView.findViewById(R.id.ratingBar);


        }

        public void bind(Message msg){
        }
    }

    }




