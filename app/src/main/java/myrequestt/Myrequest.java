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

import java.util.HashMap;
import java.util.Map;

public class Myrequest {

    private Context context;
    private RequestQueue queue;

    public Myrequest(Context context, RequestQueue queue) {
        this.context = context ;
        this.queue = queue;
    }

    public void register(String pseudo, String password, RegisterCallback callback ) {
        //URL pour aller chercher le script PHP
        String url = "http://10.1.4.232/swapeit/register.php" ;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Map<String, String> errors = new HashMap<>();

                try {
                    JSONObject json = new JSONObject(response);
                    Boolean error = json.getBoolean("error");

                    if(!error){
                        //l'inscription s'est bien déroulée
                        callback.onSucces("Vous vous êtes bien inscrit");
                    }
                    else{
                        JSONObject messages = json.getJSONObject("message");
                        if(messages.has("pseudo")){
                            errors.put("pseudo", messages.getString("pseudo"));
                        }
                        if(messages.has("password")){
                            errors.put("password", messages.getString("password"));
                        }
                        callback.inputErrors(errors);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("APP","ERROR :"+error);
                /*
                if(error instanceof NetworkError){
                    callback.onError("Impossible de se connecter");
                } else if(error instanceof VolleyError){
                    callback.onError("Une erreur s'est produite");
                }

                 */


            }
        }) {
            //C'est dans cette méthode qu'on envoie les paramètres que l'on veut tester dans le script PHP
            @Override
            protected Map<java.lang.String, java.lang.String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("pseudo", pseudo); // correspond à $_POST('pseudo')
                map.put("password",password);


                return map;
            }
        } ;

        queue.add(request) ;
    }


    public interface RegisterCallback{
        void onSucces(String message);
        void inputErrors(Map<String,String> errors);
        void onError(String message);
    }
}
