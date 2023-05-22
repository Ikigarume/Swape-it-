package com.example.tp2_rimaoui;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Map;

import myrequestt.Myrequest;


public class NewAccount_Fragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private RequestQueue queue ;
    private Myrequest request ;

    private AccountActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AccountActivity) {
            mActivity = (AccountActivity) context;
        }
    }

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
        TextInputLayout login_field = view.findViewById(R.id.loginField);
        TextInputLayout pass_field = view.findViewById(R.id.passwordField);
        Button nextButton = view.findViewById(R.id.nextButton);

        progressbar.setVisibility(View.INVISIBLE);
        //Le fait d'utiliser un singleton permet de ne pas réinstancier à chaque fois la requête queue, il va y en avoir une pour toute l'application

        ServeurIP app = (ServeurIP) mActivity.getApplicationContext();
        String IPV4_serv = app.getIPV4_serveur();

        queue = VolleySingleton.getInstance(getContext()).getRequestQueue();
        request = new Myrequest(getContext(), queue, IPV4_serv);



        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pseudo = login_field.getEditText().getText().toString().trim();
                String password = pass_field.getEditText().getText().toString().trim();

                if (pseudo.length() > 0 && password.length() > 0) {
                    request.verify_registration(pseudo, password, new Myrequest.VerifyRegistrationCallback() {

                        @Override
                        public void onSucces(String message) {
                            progressbar.setVisibility(View.GONE);
                            OTP_Send_Fragment fragmentB = new OTP_Send_Fragment();
                            Bundle args = new Bundle();
                            args.putString("pseudo", pseudo);
                            args.putString("password", password);
                            fragmentB.setArguments(args);
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragment_container, fragmentB);
                            transaction.addToBackStack(null);
                            transaction.commit();


                        }

                        @Override
                        public void inputErrors(Map<String, String> errors) {
                            progressbar.setVisibility(View.GONE);
                            if (errors.get("pseudo") != null) {
                                login_field.setError(errors.get("pseudo"));
                            } else {
                                login_field.setErrorEnabled(false);
                            }
                            if (errors.get("password") != null) {
                                pass_field.setError(errors.get("password"));
                            } else {
                                pass_field.setErrorEnabled(false);
                            }
                        }

                        @Override
                        public void onError(String message) {
                            progressbar.setVisibility(View.GONE);
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Toast.makeText(getContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                }
            }


                /*
                NewAccount_OTP_Send_Fragment fragmentB = new NewAccount_OTP_Send_Fragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragmentB);
                transaction.addToBackStack(null);
                transaction.commit();
                 */
        });
                return view ;
        }



}