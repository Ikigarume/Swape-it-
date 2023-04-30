package myrequestt;

import android.content.Context;

import com.android.volley.RequestQueue;

import java.sql.Time;

public class myMessageRequest {

    private Context context ;
    private RequestQueue queue ;
    private final String IPV4_serv ;

    public myMessageRequest(Context context, RequestQueue requestQueue, String IPV4_serv){
        this.IPV4_serv = IPV4_serv ;
        this.context = context ;
        this.queue = requestQueue ;
    }

    public void loadDiscussions(String currentUserId, LoadDiscussionsCallBack callBack){}
    public interface LoadDiscussionsCallBack{}

    public void sendTextMessage(String id_sender, String id_receiver, String messageBody, Time messageTime, SendTextMessageCallBack callBack ) {}
    public interface SendTextMessageCallBack{}


    public void sendImgMessage(String id_sender, String id_receiver, String messageBody, Time messageTime, SendImgMessageCallBack callBack){}
    public interface SendImgMessageCallBack{}



    public void loadAllMessages(String id_sender, String id_receiver, LoadAllMessagesCallBack callBack){}
    public interface LoadAllMessagesCallBack{}


    public void loadMessage(String MessageSentId, LoadMessageCallBack callBack){}
    public interface LoadMessageCallBack{}


}
