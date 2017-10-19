package com.example.rick.rickbakker_pset6;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/*
 * This is the main activity. The app opens on this page. It starts with a bottom navigation bar.
 * It also had a account button at the top. It decides whether a user is signed in or not. When
 * signed in, the button redirects to UserInfoActivity.
 */

public class MainActivity extends AppCompatActivity {

    int userint = 0;

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;

    //Makes a new listener for the bottom navigation.
    private BottomNavigationView.OnNavigationItemSelectedListener
            mOnNavigationItemSelectedListener = new BottomNavigationView
            .OnNavigationItemSelectedListener() {

        //When a button is pressed from the bottom navigation, the switch decides which one.
        //With a new intent, a new activity is started.
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.moods:
                    startActivity(new Intent(getApplicationContext(), MoodsActivity.class));
                    finish();
                    break;
                case R.id.my_mood:
                    startActivity(new Intent(getApplicationContext(), MyMoodActivity.class));
                    finish();
                    break;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //navigation is set to the bottom navigation.
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Standard Firebase code.
        mAuth = FirebaseAuth.getInstance();

        //Auth-method is called.
        auth();
    }

    //Shows the account button.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu. This adds items to the action bar.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //Top menu button. If selected, AccountActivity opens.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.account:
                //If userint is set to 1, user is signed in and the button redirects to
                // UserInfoActivity.
                if (userint == 1) {
                    startActivity(new Intent(getApplicationContext(), UserInfoActivity.class));
                } else {
                    //If userint is not 1, the user is not signed in and the button redirects to
                    // the sign in page.
                    startActivity(new Intent(getApplicationContext(), AccountActivity.class));
                }
                finish();
                return (true);
        }
        return (super.onOptionsItemSelected(item));
    }

    //Standard Firebase code. Removed the else from the if clause.
    private void auth() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //If user is signed in, userint is set to 1.
                    userint = 1;
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

    //If the back button is pressed, the application exits.
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}