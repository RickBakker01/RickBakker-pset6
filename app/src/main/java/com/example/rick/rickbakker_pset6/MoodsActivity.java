package com.example.rick.rickbakker_pset6;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MoodsActivity extends AppCompatActivity {

    Button test;
    String aMood = "";

    int Loggedin = 0;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = null;
    String userId = "";
    FirebaseUser user2 = null;

    //Standard Firebase code.
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    //Makes a new listener for the bottom navigation
    private BottomNavigationView.OnNavigationItemSelectedListener
            mOnNavigationItemSelectedListener = new BottomNavigationView
            .OnNavigationItemSelectedListener() {

        Intent intent;

        //When a button is pressed from the bottom navigation, the switch decides which one.
        //With a new intent, a new activity is started.
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.my_mood:
                    intent = new Intent(getApplicationContext(), MyMoodActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moods);

        //navigation is set to the bottom navigation.
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //The right navigation button is checked.
        navigation.getMenu().findItem(R.id.moods).setChecked(true);

        test = (Button) findViewById(R.id.TEST);
        test.setOnClickListener(new myListener());

        mAuth = FirebaseAuth.getInstance();
        user2 = mAuth.getCurrentUser();
        if (user2 != null) {
            userId = user2.getUid();
            myRef = database.getReference(userId);
        }
        auth();
    }

    //If the back button is pressed, the application goes to MainActivity.
    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    public void moodSearch() {
        MoodAsyncTask asyncTask = new MoodAsyncTask(this);
        asyncTask.execute(aMood);
    }


    //Standard Firebase code. Removed else from if clause.
    private void auth() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //If the user is signed in, a new int is given to the intent, and
                    // AccountActivity is started.
                    Loggedin = 1;
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

    public void Check() {
        if (Loggedin == 1) {
            moodSearch();
            startActivity(new Intent(getApplicationContext(), MyMoodActivity.class));
            finish();
        } else {
            Toast.makeText(MoodsActivity.this, "Please login first", Toast.LENGTH_SHORT).show();
        }
    }

    private class myListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.TEST:
                    aMood = "%20%23000000";
                    Check();
                    break;
            }
        }
    }
}