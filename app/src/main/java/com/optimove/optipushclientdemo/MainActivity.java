package com.optimove.optipushclientdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.auth.api.signin.SignInAccount;
import com.google.firebase.auth.*;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {

        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null)
            startActivity(new Intent(this, SignInActivity.class));
    }
}