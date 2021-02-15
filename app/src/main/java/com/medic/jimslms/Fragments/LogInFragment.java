package com.medic.jimslms.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.medic.jimslms.Common.Connectivity;
import com.medic.jimslms.Common.Session;
import com.medic.jimslms.R;

public class LogInFragment extends Fragment {
    private TextInputEditText userEmailLogIn,userPassLogIn;
    private ImageView showPasswordLogin,hidePasswordLogin;
    private MaterialButton logInButton;
    private MaterialTextView forgotPassword,createAccountText;
    private FirebaseAuth auth;
    private Session session;

    public LogInFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log_in, container, false);

        userEmailLogIn = view.findViewById(R.id.userEmailLogIn);
        userPassLogIn = view.findViewById(R.id.userPassLogIn);
        showPasswordLogin = view.findViewById(R.id.showPasswordLogin);
        hidePasswordLogin = view.findViewById(R.id.hidePasswordLogin);
        logInButton = view.findViewById(R.id.logInButton);
        forgotPassword = view.findViewById(R.id.forgotPassword);
        createAccountText = view.findViewById(R.id.createAccountText);
        auth = FirebaseAuth.getInstance();
        session = new Session(getContext());


        checkLogIn();
        showPassword();
        showHidePassword();
        logIn();
        forgotPassword();
        goToSignUp();

        return view;
    }
    private void checkLogIn(){
        if(session.isLoggedIn()){
            getFragmentManager().beginTransaction().replace(R.id.mainActivity,new HomePageFragment()).commit();
        }
    }

    private void showPassword(){
        showPasswordLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPassLogIn.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                showPasswordLogin.setVisibility(View.GONE);
                hidePasswordLogin.setVisibility(View.VISIBLE);
            }
        });
        hidePasswordLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPassLogIn.setTransformationMethod(PasswordTransformationMethod.getInstance());
                hidePasswordLogin.setVisibility(View.GONE);
                showPasswordLogin.setVisibility(View.VISIBLE);
            }
        });
    }

    private void logIn(){
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Connectivity.isConnectedToInternet(getContext())){
                    String email = userEmailLogIn.getText().toString();
                    String pass = userPassLogIn.getText().toString();

                    if(email.isEmpty() && pass.isEmpty()){
                        userEmailLogIn.setError("Can't be Empty");
                        userPassLogIn.setError("Can't be Empty");
                    }else if(email.isEmpty()){
                        userEmailLogIn.setError("Can't be Empty");
                    }else if(pass.isEmpty()){
                        userPassLogIn.setError("Can't be Empty");
                    }else{
                        auth.signInWithEmailAndPassword(email,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                session.setLoggedIn(true);
                                getFragmentManager().beginTransaction().replace(R.id.mainActivity,new HomePageFragment()).commit();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                userPassLogIn.setText("");
                            }
                        });
                    }
                }else{
                    Toast.makeText(getContext(), "Check your Internet Connection!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void forgotPassword(){
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Connectivity.isConnectedToInternet(getContext())){
                    AlertDialog.Builder resetDialog = new AlertDialog.Builder(getContext());
                    resetDialog.setCancelable(false);
                    resetDialog.setTitle("Reset password link will be sent to Your Inbox");
                    EditText emailText = new EditText(getContext());
                    emailText.setHint("Email Id");
                    emailText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    emailText.setLayoutParams(lp);
                    resetDialog.setView(emailText);
                    resetDialog.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String resetMailId = emailText.getText().toString();
                            if(resetMailId.isEmpty()){
                                emailText.setError("Can't be Empty");
                            }else{
                                auth.sendPasswordResetEmail(resetMailId).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            dialog.dismiss();
                                            Toast.makeText(getContext(), "An Email has been sent to you", Toast.LENGTH_LONG).show();
                                        }else{
                                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    resetDialog.show();
                }else{
                    Toast.makeText(getContext(), "Check your Internet Connection!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void showHidePassword(){
        userPassLogIn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showPasswordLogin.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void goToSignUp(){
        createAccountText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.mainActivity,new SignUpFragment()).addToBackStack(" ").commit();
            }
        });
    }

}