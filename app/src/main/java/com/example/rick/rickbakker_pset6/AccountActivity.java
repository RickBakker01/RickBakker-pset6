package com.example.rick.rickbakker_pset6;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
        auth();

    }

    private void auth() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    finish();
                    startActivity(new Intent(getApplicationContext(), UserInfoActivity.class));
                }
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
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                Intent intent = getIntent();
                Bundle bundle = intent.getExtras();

                if (bundle == null) {
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    finish();
                    startActivity(new Intent(getApplicationContext(), MyMoodActivity.class));
                }

                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.sign_in_failed, Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(getApplicationContext(), AccountActivity.class));
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    private class myListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.gotoregister:
                    finish();
                    startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                    break;
                case R.id.login:
                    String email = mEmail.getText().toString();
                    String password = mPassword.getText().toString();
                    if (email.matches("") || password.matches("")) {
                        Toast.makeText(getApplicationContext(), R.string.empty, Toast.LENGTH_SHORT).show();
                    } else {
                        signIn(email, password);
                    }
            }
        }
    }
}

