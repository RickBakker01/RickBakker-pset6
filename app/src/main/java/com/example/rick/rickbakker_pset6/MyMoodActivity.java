package com.example.rick.rickbakker_pset6;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyMoodActivity extends AppCompatActivity {


    ListView paintinglist;
    String[] itemname = {"1", "2", "3"};
    //Standard Firebase code.
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    //Makes a new listener for the bottom navigation.
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
                case R.id.moods:
                    intent = new Intent(getApplicationContext(), MoodsActivity.class);
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
        setContentView(R.layout.activity_my_mood);

        //navigation is set to the bottom navigation.
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //The right navigation button is checked.
        navigation.getMenu().findItem(R.id.my_mood).setChecked(true);

        //Standard Firebase code
        mAuth = FirebaseAuth.getInstance();
        //auth-method is called.
        auth();


        PaintingListAdapter adapter = new PaintingListAdapter(this, itemname);
        paintinglist = (ListView) findViewById(R.id.paintlist);
        paintinglist.setAdapter(adapter);

    }

    //Standard Firebase code. Removed else from if clause.
    private void auth() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    //If the user is not signed in, a new int is given to the intent, and
                    // AccountActivity is started.
                    int userSignIn = 1;
                    startActivity(new Intent(getApplicationContext(), AccountActivity.class).putExtra("user", userSignIn));
                    finish();
                } else {
                    collect();


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

    //If the back button is pressed, the application goes to MainActivity.
    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }


    public void collect() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();

        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);
                System.out.println(post);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    public static class Post {

        public String author;
        public String title;

        public Post() {
            // ...
        }

    }
}

