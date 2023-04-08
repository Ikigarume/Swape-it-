package com.example.tp2_rimaoui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;





public class NewAccount_Fragment extends Fragment {

    private ProgressBar progress;
    private Button nextButton ;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public NewAccount_Fragment() {
        // Required empty public constructor
    }

    public static NewAccount_Fragment newInstance(String param1, String param2) {
        NewAccount_Fragment fragment = new NewAccount_Fragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_account, container, false);
        ProgressBar progressbar = view.findViewById(R.id.loading);
        Button nextButton = view.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewAccount_OTP_Send_Fragment fragmentB = new NewAccount_OTP_Send_Fragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragmentB);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        progressbar.setVisibility(View.INVISIBLE);
        return view ;
    }
}