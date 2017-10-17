package com.example.rick.rickbakker_pset6;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;

public class MoodsActivity extends AppCompatActivity {

    Button test;
    String aMood = "";
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
    }

    //If the back button is pressed, the application goes to MainActivity.
    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    public void moodSearch() {
        TrackAsyncTask asyncTask = new TrackAsyncTask(this);
        asyncTask.execute(aMood);
    }

    public void moodStartIntent(HashMap moodPaintings) {
        Intent dataIntent = new Intent(this, MyMoodActivity.class);
        dataIntent.putExtra("data", moodPaintings);
        this.startActivity(dataIntent);
    }

    public class myListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.TEST:
                    aMood = "%20%23000000";
                    moodSearch();
                    break;
            }
        }
    }
}