package com.example.rick.rickbakker_pset6;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    EditText mEmail;
    EditText mPassword;
    EditText mPasswordConfirm;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(new myListener());

        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        mPasswordConfirm = (EditText) findViewById(R.id.password_confirm);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("onAuth1", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("onAuth2", "onAuthStateChanged:signed_out");
                }
            }
        };
    }


        private class myListener implements View.OnClickListener {

            @Override
            public void onClick(View view) {

                String email = mEmail.getText().toString();
                if (mPassword.length() > 6) {

                    if (Objects.equals(mPassword.getText().toString(), mPasswordConfirm.getText().toString())) {
                        String password = mPassword.getText().toString();

                        createAccount(email, password);

                    } else {
                        mPasswordConfirm.getText().clear();
                        Toast.makeText(RegisterActivity.this, R.string.different_passwords,
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mPasswordConfirm.getText().clear();
                    Toast.makeText(RegisterActivity.this, R.string.to_short,
                            Toast.LENGTH_SHORT).show();
                }
            }
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

    public void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            if (!task.getException().getMessage().equals("")) {
                                Toast.makeText(RegisterActivity.this,
                                        task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                                Log.d("1", "createUserWithEmail:onComplete:" + task.isSuccessful());
                                finish();
                                Toast.makeText(RegisterActivity.this, R.string.registration_successful,
                                    Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this,  MainActivity.class));
                            }


                        // ...
                    }
                });
    }

    @Override
    public void onBackPressed() {

        finish();
        startActivity(new Intent(RegisterActivity.this, AccountActivity.class));
    }
}
