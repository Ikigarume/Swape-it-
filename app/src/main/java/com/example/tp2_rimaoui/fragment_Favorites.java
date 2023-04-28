package com.example.tp2_rimaoui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.example.RecyclerView.FavAdapter;
import com.example.RecyclerView.FavoriteAdapter;
import com.example.RecyclerView.OfferAdapter;
import com.example.database_animals.Annonce;
import com.example.database_animals.Favorites;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import myrequestt.Myrequest;


public class fragment_Favorites extends Fragment {

    private RecyclerView recyclerview ;
    private FavAdapter myAdapter ;
    private SessionManager sessionManager ;
    private ArrayList<Annonce> AnnoncesFav ;
    private Myrequest request ;
    private RequestQueue queue ;

    private View view ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment__favorites, container, false);

        ServeurIP app = (ServeurIP) getActivity().getApplicationContext();
        String IPV4_serv = app.getIPV4_serveur();
        sessionManager = new SessionManager(getContext());
        queue = VolleySingleton.getInstance(getContext()).getRequestQueue();
        request = new Myrequest(getContext(), queue, IPV4_serv);

        recyclerview = view.findViewById(R.id.my_profile_Favorites);

        AnnoncesFav = request.getFavPosts(sessionManager.getPseudo(), new Myrequest.GetFavPostsCallback() {
            @Override
            public void onSucces(String message) {
                myAdapter = new FavAdapter(getContext(), AnnoncesFav);
                recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerview.setAdapter(myAdapter);
            }

            @Override
            public void inputErrors(Map<String, String> errors) {

            }

            @Override
            public void onError(String message) {
                Toast.makeText(getContext(), "Favorites fragment : "+message, Toast.LENGTH_SHORT).show();

            }
        });




        return view;

    }

        /*
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_Favorites() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_Favorites.

    // TODO: Rename and change types and number of parameters
    public static fragment_Favorites newInstance(String param1, String param2) {
        fragment_Favorites fragment = new fragment_Favorites();
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
}