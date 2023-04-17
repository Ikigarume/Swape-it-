package com.example.tp2_rimaoui;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.android.material.textfield.TextInputLayout;

import myrequestt.Myrequest;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Login_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Login_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RequestQueue queue ;
    private Myrequest request ;

    private ProgressBar loading ;

    public Login_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Login_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Login_Fragment newInstance(String param1, String param2) {
        Login_Fragment fragment = new Login_Fragment();
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        TextInputLayout login_field = view.findViewById(R.id.loginFieldTil);
        TextInputLayout pass_field = view.findViewById(R.id.passwordFieldTil);
        loading = view.findViewById(R.id.loading);
        Button loginButton = view.findViewById(R.id.loginButton);

        queue = VolleySingleton.getInstance(getContext()).getRequestQueue();
        request = new Myrequest(getContext(), queue);
        loading.setVisibility(View.INVISIBLE);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pseudo = login_field.getEditText().getText().toString().trim();
                String password = pass_field.getEditText().getText().toString().trim();
                if(pseudo.length()>0 && password.length()>0) {
                    //  handler.postDelayed(new Runnable(){
                    //   @Override
                    //   public void run() {
                    loading.setVisibility(View.VISIBLE);
                    request.connection(pseudo, password, new Myrequest.LoginCallback() {
                        @Override
                        public void onSuccess(String pseudo) {
                            Intent intent = new Intent(getContext(), Home_page.class);
                            intent.putExtra("pseudo",pseudo);
                            startActivity(intent);
                        }

                        @Override
                        public void onError(String message) {
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                //},1000);
                //}
                else {
                    Toast.makeText(getContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                }
            }
        });




        return view ;
    }
}