package com.example.tp2_rimaoui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.example.RecyclerView.DiscussionAdapter;
import com.example.database_animals.Discussion;

import java.util.ArrayList;

import myrequestt.Myrequest;
import myrequestt.myMessageRequest ;


public class Fragment_Discussions extends Fragment {

    private RecyclerView rv ;
    private DiscussionAdapter myAdapter ;
    private SessionManager sessionManager ;
    private ArrayList<Discussion> discussions ;
    private myMessageRequest request ;
    private RequestQueue queue ;

    private View view ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment__discussion,container,false);

        ServeurIP app = (ServeurIP) getActivity().getApplicationContext();
        String IPV4_serv = app.getIPV4_serveur() ;
        sessionManager = new SessionManager(getContext());
        queue = VolleySingleton.getInstance(getContext()).getRequestQueue() ;
        request = new myMessageRequest(getContext(), queue, IPV4_serv);

        rv = view.findViewById(R.id.my_profile_Discussions);

        discussions = request.getDiscussions(sessionManager.getId(), new myMessageRequest.getDiscussionsCallBack() {
            public void onSucces(String message){
                myAdapter = new DiscussionAdapter(getContext(),discussions) ;
                rv.setLayoutManager(new LinearLayoutManager(getContext()));
                rv.setAdapter(myAdapter);
                Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
            }
            public void onError(String message){
                Toast.makeText(getContext(),"discussions fragment : "+message,Toast.LENGTH_SHORT).show();
            }
        }) ;



        return view ;
    }

}
