package com.example.tp2_rimaoui;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewAccount_OTP_Send_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewAccount_OTP_Send_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseAuth auth ;
    private String storedVerificationId ;
    private PhoneAuthProvider.ForceResendingToken resendToken ;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks ;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewAccount_OTP_Send_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment2.
     */
    // TODO: Rename and change types and number of parameters
    public static NewAccount_OTP_Send_Fragment newInstance(String param1, String param2) {
        NewAccount_OTP_Send_Fragment fragment = new NewAccount_OTP_Send_Fragment();
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
        View view = inflater.inflate(R.layout.fragment_new_account_otp_send, container, false);
        Button getCode = view.findViewById(R.id.button_get_otp);
        ProgressBar loading = view.findViewById(R.id.loading);
        loading.setVisibility(View.INVISIBLE);
        auth=FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        EditText mobileNumber = view.findViewById(R.id.mobile_number);


        getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String number = mobileNumber.getText().toString().trim();

                if (!number.isEmpty()) {
                    getCode.setVisibility(View.INVISIBLE);
                    loading.setVisibility(View.VISIBLE);
                    number = "+212" + number;
                    PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                            .setPhoneNumber(number) // Numéro de téléphone à vérifier
                            .setTimeout(60L, TimeUnit.SECONDS) // Délai d'attente et unité
                            .setActivity(getActivity()) // Activité (pour la liaison de rappel)
                            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
                            .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);

                } else {
                    Toast.makeText(getContext(), "Enter mobile number", Toast.LENGTH_SHORT).show();
                }
                
            }
        });

        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // Créer un objet Intent pour l'activité de destination
                Intent intent = new Intent(getActivity(), Home_page.class);
                startActivity(intent);
                getActivity().finish();

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                getCode.setVisibility(View.VISIBLE);
                loading.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                Log.d("TAG","onCodeSent:" + verificationId);
                storedVerificationId = verificationId;
                resendToken = token;
                NewAccount_OTP_Receive_Fragment fragmentB = new NewAccount_OTP_Receive_Fragment();

                Bundle bundle = new Bundle();
                bundle.putString("storedVerificationId",storedVerificationId); //ajouter des données dans le bundle

                fragmentB.setArguments(bundle); //ajouter le bundle à l'objet fragment

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragmentB);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        };



        
        
        return view ;


    }


}