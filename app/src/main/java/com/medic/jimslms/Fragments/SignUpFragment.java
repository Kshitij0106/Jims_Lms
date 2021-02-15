package com.medic.jimslms.Fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.medic.jimslms.Common.Connectivity;
import com.medic.jimslms.Common.Session;
import com.medic.jimslms.Model.User;
import com.medic.jimslms.R;

public class SignUpFragment extends Fragment {
    private TextInputEditText userFullNameSignUp, userEmailSignUp, userPhoneSignUp, userSemSignUp, userPasswordSignUp, confirmPasswordSignUp;
    private ImageView showPasswordSign, hidePasswordSign, showConfirmPassword, hideConfirmPassword;
    private Spinner userCourseSpinnerSignUp;
    private TextView userShift1SigUp, userShift2SigUp;
    private MaterialButton signUpButton;
    private MaterialTextView logInText;
    private FirebaseAuth auth;
    private DatabaseReference userRef;
    private Session session;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        userFullNameSignUp = view.findViewById(R.id.userFullNameSignUp);
        userEmailSignUp = view.findViewById(R.id.userEmailSignUp);
        userPhoneSignUp = view.findViewById(R.id.userPhoneSignUp);
        userCourseSpinnerSignUp = view.findViewById(R.id.userCourseSpinnerSignUp);
        userSemSignUp = view.findViewById(R.id.userSemSignUp);
        userShift1SigUp = view.findViewById(R.id.userShift1SigUp);
        userShift2SigUp = view.findViewById(R.id.userShift2SigUp);

        userPasswordSignUp = view.findViewById(R.id.userPasswordSignUp);
        showPasswordSign = view.findViewById(R.id.showPasswordSign);
        hidePasswordSign = view.findViewById(R.id.hidePasswordSign);

        confirmPasswordSignUp = view.findViewById(R.id.confirmPasswordSignUp);
        hideConfirmPassword = view.findViewById(R.id.hideConfirmPassword);
        showConfirmPassword = view.findViewById(R.id.showConfirmPassword);

        signUpButton = view.findViewById(R.id.signUpButton);
        logInText = view.findViewById(R.id.logInText);
        auth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("Students");
        session = new Session(getContext());
        String[] courses = {"Select Course", "BCA", "MCA", "BBA", "PGDM"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, courses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userCourseSpinnerSignUp.setAdapter(adapter);

        showPassword();
        showConfirmPassword();
        showHidePassword();
        createUserAccount();
        getUSerShift();
        goToLogIn();

        return view;
    }

    private void goToLogIn() {
        logInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.mainActivity, new LogInFragment()).addToBackStack(" ").commit();
            }
        });
    }

    private void createUserAccount() {
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Connectivity.isConnectedToInternet(getContext())) {
                    String name = userFullNameSignUp.getText().toString();
                    String email = userEmailSignUp.getText().toString();
                    String phn = userPhoneSignUp.getText().toString();
                    String sem = userSemSignUp.getText().toString();
                    String pass = userPasswordSignUp.getText().toString();
                    String confirmPass = confirmPasswordSignUp.getText().toString();

                    if (name.isEmpty() && email.isEmpty() && phn.isEmpty() && pass.isEmpty() && confirmPass.isEmpty() && sem.isEmpty()) {
                        userFullNameSignUp.setError("Can't be Empty");
                        userEmailSignUp.setError("Can't be Empty");
                        userPhoneSignUp.setError("Can't be Empty");
                        userPasswordSignUp.setError("Can't be Empty");
                        confirmPasswordSignUp.setError("Can't be Empty");
                        userSemSignUp.setError("Can't be Empty");
                    } else if (name.isEmpty()) {
                        userFullNameSignUp.setError("Can't be Empty");
                    } else if (email.isEmpty()) {
                        userEmailSignUp.setError("Can't be Empty");
                    } else if (phn.isEmpty()) {
                        userPhoneSignUp.setError("Can't be Empty");
                    } else if (pass.isEmpty()) {
                        userPasswordSignUp.setError("Can't be Empty");
                    } else if (confirmPass.isEmpty()) {
                        confirmPasswordSignUp.setError("Can't be Empty");
                    } else if (!pass.equals(confirmPass)) {
                        userPasswordSignUp.setError("Password does not match");
                    } else if (userCourseSpinnerSignUp.getSelectedItemPosition() == 0) {
                        Toast.makeText(getContext(), "Select Course", Toast.LENGTH_SHORT).show();
                    } else if (sem.isEmpty()) {
                        userSemSignUp.setError("Can't be Empty");
                    } else {
                        auth.createUserWithEmailAndPassword(email, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                session.setLoggedIn(true);

                                FirebaseUser usr = auth.getCurrentUser();
                                String uid = usr.getUid();

                                User user = new User();
                                user.setUid(uid);
                                user.setUserFullName(name);
                                user.setUserEmail(email);
                                user.setUserPhone(phn);
                                user.setUserCourse(userCourseSpinnerSignUp.getSelectedItem().toString());
                                user.setUserSem(sem);

                                if(userShift2SigUp.isSelected()){
                                    user.setUserShift("2");
                                }else{
                                    user.setUserShift("1");
                                }

                                userRef.child(uid).setValue(user);
                                getFragmentManager().beginTransaction().replace(R.id.mainActivity, new HomePageFragment()).commit();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    Toast.makeText(getContext(), "Check your Internet Connection!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getUSerShift() {
        userShift1SigUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userShift1SigUp.setSelected(true);
                userShift1SigUp.setTextColor(Color.WHITE);
                userShift1SigUp.setBackgroundResource(R.drawable.shift_background);
                disableTextView(userShift2SigUp);
            }
        });

        userShift2SigUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userShift2SigUp.setSelected(true);
                userShift2SigUp.setTextColor(Color.WHITE);
                userShift2SigUp.setBackgroundResource(R.drawable.shift_background);
                disableTextView(userShift1SigUp);
            }
        });
    }

    private void disableTextView(TextView t1){
        t1.setSelected(false);
        t1.setTextColor(Color.BLACK);
        t1.setBackgroundResource(R.drawable.edit_text_background);
    }

    private void showPassword() {
        showPasswordSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPasswordSignUp.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                showPasswordSign.setVisibility(View.GONE);
                hidePasswordSign.setVisibility(View.VISIBLE);
            }
        });
        hidePasswordSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPasswordSignUp.setTransformationMethod(PasswordTransformationMethod.getInstance());
                hidePasswordSign.setVisibility(View.GONE);
                showPasswordSign.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showConfirmPassword() {
        showConfirmPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmPasswordSignUp.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                showConfirmPassword.setVisibility(View.GONE);
                hideConfirmPassword.setVisibility(View.VISIBLE);
            }
        });
        hideConfirmPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmPasswordSignUp.setTransformationMethod(PasswordTransformationMethod.getInstance());
                hideConfirmPassword.setVisibility(View.GONE);
                showConfirmPassword.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showHidePassword(){
        userPasswordSignUp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showPasswordSign.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        confirmPasswordSignUp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showConfirmPassword.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}