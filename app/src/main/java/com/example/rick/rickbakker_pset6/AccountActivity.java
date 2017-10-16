package com.example.rick.rickbakker_pset6;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class AccountActivity extends AppCompatActivity {

    Button gotoregister;
    Button login;
    EditText mEmail;
    EditText mPassword;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new myListener());

        gotoregister = (Button) findViewById(R.id.gotoregister);
        gotoregister.setOnClickListener(new myListener());

        mEmail = (EditText) findViewById(R.id.user_email);
        mPassword = (EditText) findViewById(R.id.user_password);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    finish();
                    startActivity(new Intent(AccountActivity.this,  UserInfoActivity.class));
                    Log.d("ASAASD", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("ASADSD", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        finish();
                        startActivity(new Intent(AccountActivity.this,  MainActivity.class));
                        Log.d("FDSDFSD", "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("FSDF", "signInWithEmail:failed", task.getException());
                            Toast.makeText(AccountActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(AccountActivity.this,  AccountActivity.class));
                        }
                    }
                });
    }

    private class myListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.gotoregister:
                    finish();
                    startActivity(new Intent(AccountActivity.this, RegisterActivity.class));
                    break;
                case R.id.login:
                    String email = mEmail.getText().toString();
                    String password = mPassword.getText().toString();
                    if (email.matches("") || password.matches("")){
                        Toast.makeText(AccountActivity.this, R.string.empty,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        signIn(email, password);
                    }
            }

        }

    }

}

