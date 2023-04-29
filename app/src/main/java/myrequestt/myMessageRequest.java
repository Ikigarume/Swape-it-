package myrequestt;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.HashMap;
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

    public void loadDiscussions(String currentUserId, LoadDiscussionsCallBack callBack){}
    public interface LoadDiscussionsCallBack{}

    public void sendTextMessage(String id_sender, String id_receiver, String messageBody, Time messageTime, SendTextMessageCallBack callBack ) {}
    public interface SendTextMessageCallBack{}


    public void sendImgMessage(String id_sender, String id_receiver, String messageBody, Time messageTime, SendImgMessageCallBack callBack){
        //URL pour aller chercher le script PHP
        String url = "http://"+IPV4_serv+"/swapeit/sendTextMessage.php" ;
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Map<String, String> errors = new HashMap<>();

                        try {
                            JSONObject json = new JSONObject(response);
                            Boolean error = json.getBoolean("error");
                            int id_sent_message = json.getInt("id_sent_message");
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
                //map.put("message_time", messageTime); // this should be a time




                return map;
            }
        } ;

        queue.add(request) ;
    }
    public interface SendImgMessageCallBack{
        void onSucces(String Servermessage, int id_sent_message);
        void inputErrors(Map<String,String> errors);
        void onError(String message);
    }



    public void loadAllMessages(String id_sender, String id_receiver, LoadAllMessagesCallBack callBack){}
    public interface LoadAllMessagesCallBack{}


    public void loadMessage(String MessageSentId, LoadMessageCallBack callBack){}
    public interface LoadMessageCallBack{}


}
