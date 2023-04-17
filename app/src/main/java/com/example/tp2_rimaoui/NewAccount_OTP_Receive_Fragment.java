package com.example.tp2_rimaoui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewAccount_OTP_Receive_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewAccount_OTP_Receive_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseAuth auth ;

    public NewAccount_OTP_Receive_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewAccount_OTP_Receive_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewAccount_OTP_Receive_Fragment newInstance(String param1, String param2) {
        NewAccount_OTP_Receive_Fragment fragment = new NewAccount_OTP_Receive_Fragment();
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
        View view = inflater.inflate(R.layout.fragment_new_account_otp_receive, container, false);
        ProgressBar loading = view.findViewById(R.id.loading);
        EditText inputCode1 = view.findViewById(R.id.inputCode1);
        EditText inputCode2 = view.findViewById(R.id.inputCode2);
        EditText inputCode3 = view.findViewById(R.id.inputCode3);
        EditText inputCode4 = view.findViewById(R.id.inputCode4);
        EditText inputCode5 = view.findViewById(R.id.inputCode5);
        EditText inputCode6 = view.findViewById(R.id.inputCode6);
        Button verify_otp = view.findViewById(R.id.button_verify_otp);
        loading.setVisibility(View.INVISIBLE);

        auth= FirebaseAuth.getInstance();
        Bundle bundle = getArguments();
        if(bundle != null) {
            String storedVerificationId = bundle.getString("storedVerificationId");
        }

        inputCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (!s.toString().isEmpty()) {
                    inputCode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });

        inputCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (!s.toString().isEmpty()) {
                    inputCode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        inputCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (!s.toString().isEmpty()) {
                    inputCode4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        inputCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (!s.toString().isEmpty()) {
                    inputCode5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        inputCode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (!s.toString().isEmpty()) {
                    inputCode6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        verify_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verify_otp.setVisibility(View.INVISIBLE);
                loading.setVisibility(View.VISIBLE);
                if (inputCode1.getText().toString().trim().isEmpty()
                        || inputCode2.getText().toString().trim().isEmpty()
                        || inputCode3.getText().toString().trim().isEmpty()
                        || inputCode4.getText().toString().trim().isEmpty()
                        || inputCode5.getText().toString().trim().isEmpty()
                        || inputCode6.getText().toString().trim().isEmpty()
                ) {
                    Toast.makeText(requireContext(), "Please enter valid code", Toast.LENGTH_SHORT)
                            .show();
                    verify_otp.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.INVISIBLE);
                }

                String code = inputCode1.getText().toString() +
                        inputCode2.getText().toString() +
                        inputCode3.getText().toString() +
                        inputCode4.getText().toString() +
                        inputCode5.getText().toString() +
                        inputCode6.getText().toString();

                String storedVerificationId = null;
                if (bundle != null) {
                    storedVerificationId = bundle.getString("storedVerificationId");
                }
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(
                        storedVerificationId, code);

                signInWithPhoneAuthCredential(credential,verify_otp,loading);

            }
        });


        return view ;
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential, Button verify_otp, ProgressBar loading) {
            auth.signInWithCredential(credential)
                    .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(getActivity(), Home_page.class);
                                startActivity(intent);
                                getActivity().finish();


                            } else {

                                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                    Toast.makeText(requireContext(), "Invalid OTP", Toast.LENGTH_SHORT).show();
                                    verify_otp.setVisibility(View.VISIBLE);
                                    loading.setVisibility(View.INVISIBLE);
                                }
                            }
                        }
                    });
    }







}