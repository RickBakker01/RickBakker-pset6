package com.example.rick.rickbakker_pset6;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserInfoActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    TextView user_info_email;
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        mAuth = FirebaseAuth.getInstance();

        logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new myListener());

        user_info_email = (TextView) findViewById(R.id.user_info_email);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    getCurrentUser();
                    Log.d("SFDFSD", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("SDFSDF", "onAuthStateChanged:signed_out");
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

    public void getCurrentUser(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            user_info_email.setText(email);
        }
    }

    private class myListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(UserInfoActivity.this, R.string.sign_out,
                    Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(UserInfoActivity.this, MainActivity.class));
        }
    }

    @Override
    public void onBackPressed() {

        finish();
        startActivity(new Intent(UserInfoActivity.this, MainActivity.class));
    }
}
