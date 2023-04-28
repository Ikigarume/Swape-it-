package com.example.tp2_rimaoui;

import android.media.MediaCodec;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.example.RecyclerView.AnnonceAdapter;
import com.example.RecyclerView.OfferAdapter;
import com.example.database_animals.Annonce;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import myrequestt.Myrequest;


public class Fragement_Offers extends Fragment {

    View view;
    private OfferAdapter myAdapter ;
    private Myrequest request ;
    private RequestQueue queue ;
    private SessionManager sessionManager ;
    private List Favoris ;
    private ArrayList<Annonce> Annonces ;
    private RecyclerView rv ;
    private OfferAdapter offerAdapter ;



    /*

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragement_Offers() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragement_Offers.

    // TODO: Rename and change types and number of parameters
    public static Fragement_Offers newInstance(String param1, String param2) {
        Fragement_Offers fragment = new Fragement_Offers();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
 */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.fragment_fragement__offers, container, false);
         ServeurIP app = (ServeurIP) getActivity().getApplicationContext();
         String IPV4_serv = app.getIPV4_serveur();
         sessionManager = new SessionManager(getContext());
         queue = VolleySingleton.getInstance(getContext()).getRequestQueue();
         request = new Myrequest(getContext(), queue, IPV4_serv);
         rv = view.findViewById(R.id.my_profile_Offers);

         Favoris = request.getFavoris(sessionManager.getPseudo(), new Myrequest.GetFavorisCallback() {
             @Override
             public void onSucces(String message) {
                 Annonces = request.getUserPosts(sessionManager.getPseudo(), Favoris, new Myrequest.GetUserPostsCallback() {
                     @Override
                     public void onSucces(String message) {
                         offerAdapter = new OfferAdapter(getContext(), Annonces);
                         rv.setLayoutManager(new LinearLayoutManager(getContext()));
                         rv.setAdapter(offerAdapter);

                     }

                     @Override
                     public void inputErrors(Map<String, String> errors) {

                     }

                     @Override
                     public void onError(String message) {
                         Toast.makeText(getContext(), "fragment offer : "+message, Toast.LENGTH_SHORT).show();

                     }
                 });
             }

             @Override
             public void inputErrors(Map<String, String> errors) {

             }

             @Override
             public void onError(String message) {

             }
         }) ;



         return view ;
    }
}