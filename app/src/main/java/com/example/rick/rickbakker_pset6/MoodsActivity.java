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

/*
 * This is the MoodsActivity. It represents a big list of buttons and the buttons correspond to a
  * mood.
 */

public class MoodsActivity extends AppCompatActivity {

    int mLoggedin = 0;

    Button black;
    Button grey;
    Button pink;
    Button red;
    Button yellow;
    Button blue;
    Button green;
    Button orange;
    Button purple;
    Button brown;

    String aMood = "";

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

        //When a button is pressed from the bottom navigation, the switch decides which one.
        //With a new intent, a new activity is started.
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
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
        setContentView(R.layout.activity_moods);

        //navigation is set to the bottom navigation.
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //The right navigation button is checked.
        navigation.getMenu().findItem(R.id.moods).setChecked(true);

        findButtons();
        setListener();

        mAuth = FirebaseAuth.getInstance();
        user2 = mAuth.getCurrentUser();
        if (user2 != null) {
            userId = user2.getUid();
            myRef = database.getReference(userId);
        }
        auth();
    }

    public void findButtons() {
        black = (Button) findViewById(R.id.black);
        grey = (Button) findViewById(R.id.grey);
        pink = (Button) findViewById(R.id.pink);
        red = (Button) findViewById(R.id.red);
        yellow = (Button) findViewById(R.id.yellow);
        blue = (Button) findViewById(R.id.blue);
        green = (Button) findViewById(R.id.green);
        orange = (Button) findViewById(R.id.orange);
        purple = (Button) findViewById(R.id.purple);
        brown = (Button) findViewById(R.id.brown);
    }

    public void setListener() {
        black.setOnClickListener(new myListener());
        grey.setOnClickListener(new myListener());
        pink.setOnClickListener(new myListener());
        red.setOnClickListener(new myListener());
        yellow.setOnClickListener(new myListener());
        blue.setOnClickListener(new myListener());
        green.setOnClickListener(new myListener());
        orange.setOnClickListener(new myListener());
        purple.setOnClickListener(new myListener());
        brown.setOnClickListener(new myListener());
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
                    mLoggedin = 1;
                }
            }
        };
    }

    public void check() {
        if (mLoggedin == 1) {
            moodSearch();
            startActivity(new Intent(getApplicationContext(), MyMoodActivity.class));
            finish();
        } else {
            Toast.makeText(MoodsActivity.this, R.string.login_first, Toast.LENGTH_SHORT).show();
        }
    }

    public void moodSearch() {
        MoodAsyncTask asyncTask = new MoodAsyncTask(this);
        asyncTask.execute(aMood);
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

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    private class myListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.black:
                    aMood = "%20%23000000";
                    check();
                    break;
                case R.id.grey:
                    aMood = "%20%232F4F4F";
                    check();
                    break;
                case R.id.pink:
                    aMood = "%20%23DF4C93";
                    check();
                    break;
                case R.id.red:
                    aMood = "%20%23981313";
                    check();
                    break;
                case R.id.yellow:
                    aMood = "%20%23FFEB00";
                    check();
                    break;
                case R.id.blue:
                    aMood = "%20%234019B1";
                    check();
                    break;
                case R.id.green:
                    aMood = "%20%23367614";
                    check();
                    break;
                case R.id.orange:
                    aMood = "%20%23E09714";
                    check();
                    break;
                case R.id.purple:
                    aMood = "%20%23850085";
                    check();
                    break;
                case R.id.brown:
                    aMood = "%20%23B35A1F";
                    check();
                    break;
            }
        }
    }
}