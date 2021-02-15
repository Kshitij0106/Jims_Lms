package com.medic.jimslms;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.medic.jimslms.Fragments.LogInFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().add(R.id.mainActivity,new LogInFragment()).commit();
    }
}