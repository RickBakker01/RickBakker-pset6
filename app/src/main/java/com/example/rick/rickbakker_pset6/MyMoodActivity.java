package com.example.rick.rickbakker_pset6;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/*
 * This activity is showing the paintings you have selected with your mood. When nog logged in,
 * you will be redirected to the login page. This activity also holds the adapter.
 */

public class MyMoodActivity extends AppCompatActivity {

    ArrayList<String> titleList = new ArrayList<>();
    ArrayList<String> makerList = new ArrayList<>();
    ArrayList<String> urlList = new ArrayList<>();

    ListView paintinglist;

    String[] titles = {};
    String[] makers = {};
    String[] urls = {};

    //Standard Firebase code.
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    //Makes a new listener for the bottom navigation.
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
                case R.id.moods:
                    startActivity(new Intent(getApplicationContext(), MoodsActivity.class));
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

        paintinglist = (ListView) findViewById(R.id.paintlist);
        paintinglist.setOnItemLongClickListener(new clickLink());

        //Standard Firebase code
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
                if (user == null) {
                    //If the user is not signed in, a new int is given to the intent, and
                    // AccountActivity is started.
                    int userSignIn = 1;
                    startActivity(new Intent(getApplicationContext(), AccountActivity.class)
                            .putExtra("user", userSignIn));
                    finish();
                } else {
                    collect();
                }
            }
        };
    }

    //Get data from database
    public void collect() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();

        // Attach a listener to read the data
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseUser user = mAuth.getCurrentUser();

                assert user != null;
                DataSnapshot paintings = dataSnapshot.child(user.getUid());
                for (DataSnapshot painting : paintings.getChildren()) {
                    PaintingClass paintingclass = painting.getValue(PaintingClass.class);
                    String title = paintingclass.getTitle();
                    String maker = paintingclass.getMaker();
                    String url = paintingclass.getUrl();

                    //Add data to a list
                    titleList.add(title);
                    makerList.add(maker);
                    urlList.add(url);
                }
                //Convert list to string array for adapter
                titles = titleList.toArray(new String[titleList.size()]);
                makers = makerList.toArray(new String[makerList.size()]);
                urls = urlList.toArray(new String[urlList.size()]);
                adapter(titles, makers);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        };
        ref.addValueEventListener(postListener);
    }

    //Set the adapter
    public void adapter(String[] titles, String[] makers) {
        PaintingListAdapter adapter = new PaintingListAdapter(this, titles, makers);
        paintinglist = (ListView) findViewById(R.id.paintlist);
        paintinglist.setAdapter(adapter);
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

    //On long click, the app opens a browser with a url to the webpage of the selected painting.
    private class clickLink implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urls[position])));
            return true;
        }
    }
}

