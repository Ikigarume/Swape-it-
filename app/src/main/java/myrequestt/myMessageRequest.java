package myrequestt;

import android.app.DownloadManager;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.database_animals.Discussion;
import com.example.database_animals.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class myMessageRequest {

    private Context context ;
    private RequestQueue queue ;
    private final String IPV4_serv ;

    public myMessageRequest(Context context, RequestQueue requestQueue, String IPV4_serv){
        this.IPV4_serv = IPV4_serv ;
        this.context = context ;
        this.queue = requestQueue ;
    }

    public ArrayList<Discussion> getDiscussions(String currentUserId, getDiscussionsCallBack callBack){
        //String BASE_URL = "http://"+IPV4_serv+"/swapeit/Discussions.php";
        String BASE_URL = "http://"+IPV4_serv+"/swapeit/discussionsTEST.php";
        ArrayList<Discussion> Discussions = new ArrayList<>() ;
        StringRequest request = new StringRequest(Request.Method.POST, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray array = new JSONArray(response);
                            for(int i= 0; i<array.length();i++){
                                JSONObject discussion = array.getJSONObject(i) ;
                                int otherUserId = discussion.getInt("otherUserId" );
                                String otherUserPseudo = discussion.getString("otherUserPseudo");
                                String latestMsg = discussion.getString("latestMsg");
                                String img_rep = discussion.getString("img_rep");

                                Discussion d = new Discussion(otherUserId,otherUserPseudo,latestMsg,img_rep);
                                Discussions.add(d);
                                }
                            callBack.onSucces("heheheheheh");
                        }catch(Exception e){
                            Toast.makeText(context,"Discussion exception"+e,Toast.LENGTH_SHORT).show();
                            callBack.onError("JSONException");
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,"Discussion error"+error,Toast.LENGTH_SHORT).show();
                        callBack.onError("something went wrong, please try later.");
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("currentUserId",currentUserId) ;
                return map;
            }
        };
        queue.add(request) ;
        return Discussions ;
    }
    public interface getDiscussionsCallBack{
        public void onSucces(String message) ;
        public void onError(String message) ;
    }

    public void deleteDiscussion(String currentUserId , String otherUserId, deleteDiscussionCallBack callBack){
        String BASE_URL = "http://"+IPV4_serv+"/swapeit/deleteDiscussion.php";
        StringRequest request = new StringRequest(Request.Method.POST, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject res = new JSONObject(response) ;
                            boolean error = res.getBoolean("error");
                            String message = res.getString("message");
                            if(!error){
                                callBack.onSucces(message);
                            }
                            else{
                                callBack.onError(message);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("APP","ERROR :"+error);

                if(error instanceof NetworkError){
                    callBack.onError("Impossible de se connecter");
                } else if(error instanceof VolleyError){
                    callBack.onError("Une erreur s'est produite");
                }
            }
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError{
                Map<String,String> map = new HashMap<>();
                map.put("currentUserId",currentUserId);
                map.put("otherUserId",otherUserId);
                return map;
            }
        };
        queue.add(request) ;
    }
    public interface deleteDiscussionCallBack{
        public void onSucces(String message) ;
        public void onError(String message) ;
    }
    public interface GetMessageCallBack{
        public void onSucces(String message) ;
        public void onError(String message) ;
    }

    public void sendTextMessage(String id_sender, String id_receiver, String messageBody, SendTextMessageCallBack callBack ) {

        String url = "http://"+IPV4_serv+"/swapeit/sendTextMessage.php" ;
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Map<String, String> errors = new HashMap<>();

                        try {
                            JSONObject json = new JSONObject(response);
                            Boolean error = json.getBoolean("error");
                            String id_sent_message = json.getString("id_sent_message");
                            String serverMessageSentResponse = json.getString("message");

                            if(!error){
                                callBack.onSucces(serverMessageSentResponse,id_sent_message);
                            }
                            else{
                                callBack.onError(serverMessageSentResponse);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("APP","ERROR :"+error);

                        if(error instanceof NetworkError){
                            callBack.onError("Impossible de se connecter");
                        } else if(error instanceof VolleyError){
                            callBack.onError("Une erreur s'est produite");
                        }
                    }
                }) {
            //C'est dans cette méthode qu'on envoie les paramètres que l'on veut tester dans le script PHP
            @Override
            protected Map<java.lang.String, java.lang.String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("id_sender", id_sender); // this should be an int
                map.put("id_receiver",id_receiver); // this also should be an int
                map.put("message_body", messageBody);
                return map;
            }
        } ;

        queue.add(request) ;
    }
    public interface SendTextMessageCallBack{
        public void onSucces(String message, String id_message_sent);
        public void onError(String message) ;
    }
    public void sendImgMessage(String id_sender, String id_receiver, String messageBody, Time messageTime, SendImgMessageCallBack callBack){
    }
    public interface SendImgMessageCallBack{
        void onSucces(String Servermessage, int id_sent_message);
        void inputErrors(Map<String,String> errors);
        void onError(String message);
    }
    public ArrayList<Message> getAllMessages(String currentUserId, String otherUserId, GetAllMessagesCallBack callBack){
        // make a diffrence between loading messages inside the adapter, teher will be probably two adapters, one for text and the other for images :
        ArrayList<Message> allMessages = new ArrayList<>();
        String BASE_URL = "http://"+IPV4_serv+"/swapeit/getALLMessages.php";

        StringRequest request = new StringRequest(Request.Method.POST, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray res = new JSONArray(response);
                            JSONObject error = res.getJSONObject(0);
                            Boolean err = error.getBoolean("err");
                            if (!err){
                                JSONArray messages = res.getJSONArray(1) ;
                                for(int i = 0 ; i<messages.length() ;i++ ){
                                    JSONObject msg = messages.getJSONObject(i) ;
                                    int idSender = msg.getInt("userSender_ID");
                                    int idReceiver = msg.getInt("userReceiver_ID");
                                    String messageBody = msg.getString("messageBody");
                                    String messageTime = msg.getString("messageDate");
                                    Message m = new Message(idSender,idReceiver,messageBody,messageTime);
                                    allMessages.add(0,m);
                                    callBack.onSucces("no errors accured in fetching data");
                                }
                            }

                        }catch(Exception e){
                            callBack.onError("the error parameter faces a problem ");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callBack.onError("a connection problem occured");
                    }
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>() ;
                map.put("currentUserId",currentUserId) ;
                map.put("otherUserId",otherUserId);
                return map;
            }
        } ;

        queue.add(request) ;
        return allMessages ;
    }
    public interface GetAllMessagesCallBack{
        public void onSucces(String message) ;
        public void onError(String message) ;
    }


    public Message getMessage(String MessageSentId, GetMessageCallBack callBack){
        String BASE_url = "http://"+IPV4_serv+"/swapeit/getALLMessages.php" ;
        final Message[] message = new Message[1];
        StringRequest request = new StringRequest(Request.Method.POST, BASE_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String feedback ;
                try{
                    JSONArray res = new JSONArray(response) ;
                    JSONObject first  = res.getJSONObject(0);
                    feedback = first.getString("message");
                    if(first.getBoolean("error")){
                        JSONObject m = first.getJSONObject("messageDetails") ;

                        int id_sender = m.getInt("id_sender") ;
                        int id_Receiver = m.getInt("id_receiver") ;
                        String messageBody = m.getString("messageBody");
                        String messagetime = m.getString("messageDate") ;
                        message[0] = new Message(id_sender , id_Receiver, messageBody,messagetime) ;
                        callBack.onSucces(feedback) ;
                    }
                    else {
                        callBack.onError(feedback);
                    }

                }catch(Exception e){
                    callBack.onError(" bla blo bli Failed to retrieve message");
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callBack.onError("a connection problem has occured please wait");
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> map = new HashMap<>() ;
                map.put("messageSentId",MessageSentId) ;
                return map ;
            }
        };
        queue.add(request);
        return message[0];
    }
    public Message getMessage(String currentUserId , String otherUserId , GetMessageCallBack callBack ){
        String BASE_url = "http://"+IPV4_serv+"/swapeit/getALLMessages.php" ;
        final Message[] message = new Message[1];
        StringRequest request = new StringRequest(Request.Method.POST, BASE_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String feedback ;
                try{
                    JSONArray res = new JSONArray(response) ;
                    JSONObject first  = res.getJSONObject(0);
                    feedback = first.getString("message");
                    if(first.getBoolean("error")){
                        JSONObject m = first.getJSONObject("messageDetails") ;

                        int id_sender = m.getInt("id_sender") ;
                        int id_Receiver = m.getInt("id_receiver") ;
                        String messageBody = m.getString("messageBody");
                        String messagetime = m.getString("messageDate") ;
                        message[0] = new Message(id_sender , id_Receiver, messageBody,messagetime) ;
                        callBack.onSucces(feedback) ;
                    }
                    else {
                        callBack.onError(feedback);
                    }

                }catch(Exception e){
                    callBack.onError(" bla blo bli Failed to retrieve message");
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callBack.onError("a connection problem has occured please wait");
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> map = new HashMap<>() ;
                map.put("currentUserId",currentUserId) ;
                map.put("otherUserId", otherUserId );
                return map ;
            }
        };
        queue.add(request);
        return message[0];
    }
    }




