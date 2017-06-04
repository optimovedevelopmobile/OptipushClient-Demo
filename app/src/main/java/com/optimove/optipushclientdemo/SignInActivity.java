package com.optimove.optipushclientdemo;

import android.content.*;
import android.os.*;
import android.support.annotation.NonNull;
import android.support.v7.app.*;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.*;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.*;
import com.google.firebase.auth.*;

public class SignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private static final String TAG = SignInActivity.class.getSimpleName();
    private GoogleApiClient googleApiClient;

    private TextView outputTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        outputTextView = (TextView) findViewById(R.id.outputTextView);

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();

        SignInButton signInButton = (SignInButton) findViewById(R.id.googleSignInButton);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(this);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle("Connection Error")
                .setMessage("Couldn't Sign in with Google, please try again")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, 169);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 169) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                updateUi(null);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                            updateUi(user);
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUi(null);
                        }
                    }
                });
    }

    private void updateUi(FirebaseUser currentUser) {

        if (currentUser == null) {
            outputTextView.setText("Please try again. #login_matters");
            return;
        }

        String welcomeMessage = "Hello " + currentUser.getDisplayName() + "! Let's order some PIZZA!";
        outputTextView.setText(welcomeMessage);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setResult(RESULT_OK);
                finish();
            }
        }, 1500);
    }
}
