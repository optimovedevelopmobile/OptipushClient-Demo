package com.optimove.optipushclientdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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
            startActivityForResult(new Intent(this, SignInActivity.class), 170);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode != 170)
            super.onActivityResult(requestCode, resultCode, data);
        else if (resultCode != RESULT_OK)
            finish();
    }
}