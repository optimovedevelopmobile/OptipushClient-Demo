package com.optimove.optipushclientdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

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
            openSignInActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode != 170)
            super.onActivityResult(requestCode, resultCode, data);
        else if (resultCode != RESULT_OK)
            finish();
    }

    public void orderPizza(View view) {

        String name = auth.getCurrentUser().getDisplayName();
        String message = "Bon apatite dear " + name;
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void signOut(View view) {

        auth.signOut();
        openSignInActivity();
    }

    private void openSignInActivity() {
        startActivityForResult(new Intent(this, SignInActivity.class), 170);
    }
}