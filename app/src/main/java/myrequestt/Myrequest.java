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
import com.example.database_animals.Annonce;
import com.example.tp2_rimaoui.Categorie;
import com.example.tp2_rimaoui.DetailedOfferActivity;
import com.example.tp2_rimaoui.Home_page;
import com.example.tp2_rimaoui.NewPost_Activity;
import com.example.tp2_rimaoui.OtherProfileActivity;
import com.example.tp2_rimaoui.ServeurIP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public void register(String pseudo, String password,String number,  RegisterCallback callback ) {
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
                map.put("number",number);


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

    public void updatePost(int id_annonce, int etat, String pseudo_utilisateur,  String titre, String image, String description, ArrayList<Integer> categories, updatePostCallback callback ) {
        //URL pour aller chercher le script PHP
        String url = "http://"+IPV4_serv+"/swapeit/updatePost.php" ;
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
                map.put("id_annonce", String.valueOf(id_annonce));
                map.put("etat", String.valueOf(etat));
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


    public interface updatePostCallback{
        void onSucces(String message);
        void inputErrors(Map<String,String> errors);
        void onError(String message);
    }


    public List<Integer> getFavoris (String pseudo, GetFavorisCallback callback){
        String BASE_URL = "http://"+IPV4_serv+"/swapeit/user_favoris.php";
        List<Integer> Favoris = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.POST, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONArray array = new JSONArray(response);
                            for (int i =0 ; i<array.length(); i++){

                                JSONObject object = array.getJSONObject(i);
                                int id_annonce = object.getInt("id_annonce");
                                Favoris.add(id_annonce);


                            }
                            callback.onSucces("Informations downloaded successfully.");

                            //Toast.makeText(Home_page.this, "piste1 :"+cheminImage, Toast.LENGTH_SHORT).show();




                        }catch (Exception e){
                            callback.onError("Volley Error.");
                            //Toast.makeText(Home_page.this, "Volley Error", Toast.LENGTH_SHORT).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(Home_page.this, error.toString(),Toast.LENGTH_LONG).show();
                Log.d("APP","ERROR :"+error);

                if(error instanceof NetworkError){
                    callback.onError("Impossible de se connecter");
                } else if(error instanceof VolleyError){
                    callback.onError("Une erreur s'est produite");
                }

            }
        }){
            //C'est dans cette mÃ©thode qu'on envoie les paramÃ¨tres que l'on veut tester dans le script PHP
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("pseudo",pseudo);
                return map;
            }
        };

        queue.add(request) ;
        return Favoris ;


    }

    public interface GetFavorisCallback{
        void onSucces(String message);
        void inputErrors(Map<String,String> errors);
        void onError(String message);
    }

    public ArrayList<Annonce> getPostsInfo (List Favoris, GetPostsInfoCallback callback){
        String BASE_URL = "http://"+IPV4_serv+"/swapeit/posts_info.php";
        ArrayList<Annonce> Annonces = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.POST, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONArray array = new JSONArray(response);
                            for (int i =0 ; i<array.length(); i++){

                                JSONObject object = array.getJSONObject(i);
                                String login = object.getString("login");
                                String photo_de_profil = object.getString("photo_de_profil");
                                String chemin_image = object.getString("chemin_image");
                                String titre = object.getString("titre");
                                String description = object.getString("description");
                                Double note = object.getDouble("note");
                                String rate = String.valueOf(note);
                                float rating = Float.valueOf(rate);
                                int nbr_vote = object.getInt("nbr_vote");
                                String id_categories = object.getString("id_categories");
                                int id_annonce = object.getInt("id_annonce");
                                String number = object.getString("telephone");

                                int favorite = 0 ;
                                if (Favoris.contains(id_annonce)) {
                                    favorite = 1 ;
                                } else {
                                    favorite = 0 ;
                                }

                                Annonce annonce = new Annonce(id_annonce,login, photo_de_profil, chemin_image,titre, description, rating, nbr_vote,id_categories,number,0, favorite);
                                Annonces.add(annonce);

                                callback.onSucces("Informations downloaded successfully.");
                            }

                            //Toast.makeText(Home_page.this, "piste1 :"+cheminImage, Toast.LENGTH_SHORT).show();




                        }catch (Exception e){
                            callback.onError("Volley Error.");
                            //Toast.makeText(Home_page.this, "Volley Error", Toast.LENGTH_SHORT).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(Home_page.this, error.toString(),Toast.LENGTH_LONG).show();
                Log.d("APP","ERROR :"+error);

                if(error instanceof NetworkError){
                    callback.onError("Impossible de se connecter");
                } else if(error instanceof VolleyError){
                    callback.onError("Une erreur s'est produite");
                }

            }
        }){
            //C'est dans cette mÃ©thode qu'on envoie les paramÃ¨tres que l'on veut tester dans le script PHP
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("serveurIP", IPV4_serv);
                return map;
            }
        };

        queue.add(request);
        return Annonces ;


    }

    public interface GetPostsInfoCallback{
        void onSucces(String message);
        void inputErrors(Map<String,String> errors);
        void onError(String message);
    }


    public ArrayList<Annonce> getUserPosts(String pseudo, List Favoris, GetUserPostsCallback callback){
        String BASE_URL = "http://"+IPV4_serv+"/swapeit/users_posts.php";
        ArrayList<Annonce> Annonces = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.POST, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONArray array = new JSONArray(response);
                            for (int i =0 ; i<array.length(); i++){

                                JSONObject object = array.getJSONObject(i);
                                String login = object.getString("login");
                                String photo_de_profil = object.getString("photo_de_profil");
                                String chemin_image = object.getString("chemin_image");
                                String titre = object.getString("titre");
                                String description = object.getString("description");
                                Double note = object.getDouble("note");
                                String rate = String.valueOf(note);
                                float rating = Float.valueOf(rate);
                                int nbr_vote = object.getInt("nbr_vote");
                                String id_categories = object.getString("id_categories");
                                int id_annonce = object.getInt("id_annonce");
                                String number = object.getString("telephone");
                                int etat = object.getInt("etat");

                                int favorite = 0 ;
                                if (Favoris.contains(id_annonce)) {
                                    favorite = 1 ;
                                } else {
                                    favorite = 0 ;
                                }

                                Annonce annonce = new Annonce(id_annonce,login, photo_de_profil, chemin_image,titre, description, rating, nbr_vote,id_categories,number,etat,favorite);
                                Annonces.add(annonce);

                                callback.onSucces("Informations downloaded successfully.");
                            }

                            //Toast.makeText(Home_page.this, "piste1 :"+cheminImage, Toast.LENGTH_SHORT).show();




                        }catch (Exception e){
                            callback.onError("Volley Error.");
                            //Toast.makeText(Home_page.this, "Volley Error", Toast.LENGTH_SHORT).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(Home_page.this, error.toString(),Toast.LENGTH_LONG).show();
                Log.d("APP","ERROR :"+error);

                if(error instanceof NetworkError){
                    callback.onError("Impossible de se connecter");
                } else if(error instanceof VolleyError){
                    callback.onError("Une erreur s'est produite");
                }

            }
        }){
            //C'est dans cette mÃ©thode qu'on envoie les paramÃ¨tres que l'on veut tester dans le script PHP
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("serveurIP", IPV4_serv);
                map.put("user_login", pseudo);
                return map;
            }
        };

        queue.add(request);
        return Annonces ;


    }

    public interface GetUserPostsCallback {
        void onSucces(String message);
        void inputErrors(Map<String,String> errors);
        void onError(String message);
    }

    public ArrayList<Annonce> getFavPosts(String pseudo, GetFavPostsCallback callback){
        String BASE_URL = "http://"+IPV4_serv+"/swapeit/fav_posts.php";
        ArrayList<Annonce> Annonces = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.POST, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONArray array = new JSONArray(response);
                            for (int i =0 ; i<array.length(); i++){

                                JSONObject object = array.getJSONObject(i);
                                String login = object.getString("login");
                                String photo_de_profil = object.getString("photo_de_profil");
                                String chemin_image = object.getString("chemin_image");
                                String titre = object.getString("titre");
                                String description = object.getString("description");
                                Double note = object.getDouble("note");
                                String rate = String.valueOf(note);
                                float rating = Float.valueOf(rate);
                                int nbr_vote = object.getInt("nbr_vote");
                                String id_categories = object.getString("id_categories");
                                int id_annonce = object.getInt("id_annonce");
                                String number = object.getString("telephone");
                                int etat = object.getInt("etat");

                                Annonce annonce = new Annonce(id_annonce,login, photo_de_profil, chemin_image,titre, description, rating, nbr_vote,id_categories,number,etat,1);
                                Annonces.add(annonce);
                            }
                            callback.onSucces("Informations downloaded successfully.");

                            //Toast.makeText(Home_page.this, "piste1 :"+cheminImage, Toast.LENGTH_SHORT).show();




                        }catch (Exception e){
                            callback.onError("Volley Error.");
                            //Toast.makeText(Home_page.this, "Volley Error", Toast.LENGTH_SHORT).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(Home_page.this, error.toString(),Toast.LENGTH_LONG).show();
                Log.d("APP","ERROR :"+error);

                if(error instanceof NetworkError){
                    callback.onError("Impossible de se connecter");
                } else if(error instanceof VolleyError){
                    callback.onError("Une erreur s'est produite");
                }

            }
        }){
            //C'est dans cette mÃ©thode qu'on envoie les paramÃ¨tres que l'on veut tester dans le script PHP
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("serveurIP", IPV4_serv);
                map.put("user_login", pseudo);
                return map;
            }
        };

        queue.add(request);
        return Annonces ;


    }

    public interface GetFavPostsCallback {
        void onSucces(String message);
        void inputErrors(Map<String,String> errors);
        void onError(String message);
    }


    public ArrayList<Categorie> getPostsCategories(int id_annonce, GetPostsCategoriesCallback callback) {
        String BASE_URL = "http://" + IPV4_serv + "/swapeit/annonce_categories.php";
        ArrayList<Categorie> Categories = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.POST, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {

                                JSONObject object = array.getJSONObject(i);
                                String nom = object.getString("nom");
                                int id = object.getInt("id");
                                if(id!=12 && id!=13) {
                                    Categorie categorie = new Categorie(id, nom);
                                    Categories.add(categorie);
                                }
                            }
                            callback.onSucces("Informations downloaded successfully.");

                            //Toast.makeText(Home_page.this, "piste1 :"+cheminImage, Toast.LENGTH_SHORT).show();


                        } catch (Exception e) {
                            callback.onError("Volley Error.");
                            //Toast.makeText(Home_page.this, "Volley Error", Toast.LENGTH_SHORT).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(Home_page.this, error.toString(),Toast.LENGTH_LONG).show();
                Log.d("APP", "ERROR :" + error);

                if (error instanceof NetworkError) {
                    callback.onError("Impossible de se connecter");
                } else if (error instanceof VolleyError) {
                    callback.onError("Une erreur s'est produite");
                }

            }
        }) {
            //C'est dans cette mÃ©thode qu'on envoie les paramÃ¨tres que l'on veut tester dans le script PHP
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("id_annonce", String.valueOf(id_annonce));
                return map;
            }
        };

        queue.add(request);
        return Categories;


    }

    public interface GetPostsCategoriesCallback {
        void onSucces(String message);

        void inputErrors(Map<String, String> errors);

        void onError(String message);
    }















}
