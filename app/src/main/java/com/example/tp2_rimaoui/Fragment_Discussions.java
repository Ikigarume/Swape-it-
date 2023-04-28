package com.example.tp2_rimaoui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.RecyclerView.DiscussionAdapter;
import com.example.database_animals.Discussion;

import java.util.ArrayList;
import java.util.Arrays;

public class Fragment_Discussions extends Fragment {
    private RecyclerView rv ;
    private DiscussionAdapter myAdapter ;

    private ArrayList<Discussion> discussions = new ArrayList<>(Arrays.asList(
            new Discussion("khaoula zarhouni", "go kill yourself, you loser"),
            new Discussion("laila", "sure i will sned you the pictures"),
            new Discussion("Zahir","yeah no i sold it sorry ")
    )) ;
    private View view ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment__discussion,container,false);
        rv = view.findViewById(R.id.my_profile_Discussions);
        myAdapter = new DiscussionAdapter(this.getContext(), discussions);
        rv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        rv.setAdapter(myAdapter);
        return view ;
    }

}
