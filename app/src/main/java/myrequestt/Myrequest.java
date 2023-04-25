package myrequestt;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tp2_rimaoui.Categorie;
import com.example.tp2_rimaoui.NewPost_Activity;
import com.example.tp2_rimaoui.ServeurIP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Myrequest {

    private Context context;
    private RequestQueue queue;
    private final String IPV4_serv ;

    public Myrequest(Context context, RequestQueue queue, String IPV4_serv) {
        this.context = context ;
        this.queue = queue;
        this.IPV4_serv = IPV4_serv;
    }

    public void register(String pseudo, String password, RegisterCallback callback ) {
        //URL pour aller chercher le script PHP
        String url = "http://"+IPV4_serv+"/swapeit/register.php" ;
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

                if(error instanceof NetworkError){
                    callback.onError("Impossible de se connecter");
                } else if(error instanceof VolleyError){
                    callback.onError("Une erreur s'est produite");
                }




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

    public void verify_registration(String pseudo, String password, VerifyRegistrationCallback callback ) {
        //URL pour aller chercher le script PHP
        String url = "http://"+IPV4_serv+"/swapeit/verify_registration.php" ;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Map<String, String> errors = new HashMap<>();

                try {
                    JSONObject json = new JSONObject(response);
                    Boolean error = json.getBoolean("error");

                    if(!error){
                        //l'inscription s'est bien déroulée
                        callback.onSucces("Informations conformes.");
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

                if(error instanceof NetworkError){
                    callback.onError("Impossible de se connecter");
                } else if(error instanceof VolleyError){
                    callback.onError("Une erreur s'est produite");
                }




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


    public interface VerifyRegistrationCallback{
        void onSucces(String message);
        void inputErrors(Map<String,String> errors);

        void onError(String message);
    }


    public void connection(String pseudo, String password, LoginCallback callback){

            String url = "http://"+IPV4_serv+"/swapeit/login.php" ;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    Boolean error = json.getBoolean("error");

                    if(!error){
                        String id = json.getString("id");
                        String pseudo = json.getString("pseudo");
                        //Si on a une classe USER FAIRE
                        //user user= new user(id, pseudo...) ;
                        callback.onSuccess(id,pseudo);

                    } else {
                        callback.onError(json.getString("message"));

                    }
                } catch (JSONException e) {
                    Toast.makeText(context,"error"+e,Toast.LENGTH_SHORT);
                    callback.onError("JSONException");
                    e.printStackTrace();


                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error instanceof NetworkError){
                    callback.onError("Impossible de se connecter");
                } else if(error instanceof VolleyError){
                    callback.onError("VolleyError");
                }


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

    public interface LoginCallback{
        //Si on avait une classe user on aurat fait void onSuccess(User user)
        void onSuccess(String id, String pseudo);
        void onError(String message);
    }


    public void newPost(String pseudo_utilisateur, String titre, String image, String description, ArrayList<Integer> categories, newPostCallback callback ) {
        //URL pour aller chercher le script PHP
        String url = "http://"+IPV4_serv+"/swapeit/newPost.php" ;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Map<String, String> errors = new HashMap<>();

                try {
                    JSONObject json = new JSONObject(response);
                    Boolean error = json.getBoolean("error");

                    if(!error){
                        callback.onSucces("Your offer has been successfully added !");
                    }
                    else{
                        callback.onError(json.getString("message"));
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
                    callback.onError("Impossible de se connecter");
                } else if(error instanceof VolleyError){
                    callback.onError("Une erreur s'est produite");
                }




            }
        }) {
            //C'est dans cette méthode qu'on envoie les paramètres que l'on veut tester dans le script PHP
            @Override
            protected Map<java.lang.String, java.lang.String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("pseudo_utilisateur", pseudo_utilisateur);
                map.put("titre",titre);
                map.put("description", description);
                map.put("image", image);

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < categories.size(); i++) {
                    sb.append(categories.get(i));
                    sb.append(" ");
                }
                String categoriesStr = sb.toString();
                map.put("categories", categoriesStr);


                return map;
            }
        } ;

        queue.add(request) ;
    }


    public interface newPostCallback{
        void onSucces(String message);
        void inputErrors(Map<String,String> errors);
        void onError(String message);
    }













}
