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

    //Buttons and EditTexts are being called.
    Button gotoregister;
    Button login;
    EditText mEmail;
    EditText mPassword;

    //Standard Firebase code.
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        //A new OnClickListener is added to the login and register button.
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new myListener());

        gotoregister = (Button) findViewById(R.id.gotoregister);
        gotoregister.setOnClickListener(new myListener());

        //mEmail and mPassword are created.
        mEmail = (EditText) findViewById(R.id.user_email);
        mPassword = (EditText) findViewById(R.id.user_password);

        //Standard Firebase code.
        mAuth = FirebaseAuth.getInstance();
        //auth-method is called.
        auth();
    }

    //Standard Firebase code. Removed else from if clause.
    private void auth() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //If user is signed in, pressing the login button redirects user to the
                    // UserInfoActivity
                    finish();
                    startActivity(new Intent(getApplicationContext(), UserInfoActivity.class));
                }
            }
        };
    }

    //Standard Firebase code.
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    //Standard Firebase code.
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    //Standard Firebase code.
    public void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new
                OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //Create new intent and get extras from the intent.
                Intent intent = getIntent();
                Bundle bundle = intent.getExtras();
                //If the bundle is empty, user signed in via home.
                if (bundle == null) {
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));

                    //If the bundle is not empty, user signed in from the moodlist
                    // and must be redirected back to the personal moodlist after the user signed
                    // in.
                } else {
                    finish();
                    startActivity(new Intent(getApplicationContext(), MyMoodActivity.class));
                }

                //If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.sign_in_failed, Toast
                            .LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(getApplicationContext(), AccountActivity.class));
                }
            }
        });
    }

    //If the back button is pressed, the application goes back to the MainActivity.
    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    //A non-anonymous listener
    private class myListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                //When the register button is pressed.
                case R.id.gotoregister:
                    //Create new intent and get extras from the intent.
                    Intent intent = getIntent();
                    Bundle bundle = intent.getExtras();
                    //If the bundle is not empty, user is redirected to registration from the
                    // moodlist. A new int is given to an intent for further use, and
                    // RegisterActivity is started.
                    if (bundle != null) {
                        int userSignIn = 1;
                        startActivity(new Intent(getApplicationContext(), RegisterActivity.class)
                                .putExtra("user", userSignIn));
                        //If the bundle is empty, user is edirected to registration from the
                        // normal AccountActivity, and RegisterActivity is started.
                    } else {
                        finish();
                        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                        break;
                    }
                    break;
                //When the login button is pressed
                case R.id.login:
                    //Get the strings from the email and password EditTexts
                    String email = mEmail.getText().toString();
                    String password = mPassword.getText().toString();
                    //If email or password is empty, show a warning
                    if (email.matches("") || password.matches("")) {
                        Toast.makeText(getApplicationContext(), R.string.empty, Toast
                                .LENGTH_SHORT).show();
                    } else {
                        //When everything is fine, sign in.
                        signIn(email, password);
                    }
            }
        }
    }
}