package com.example.rick.rickbakker_pset6;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserInfoActivity extends AppCompatActivity {

    //Button and TextView are being called.
    TextView user_info_email;
    Button logout;

    //Standard Firebase code.
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        //Standard Firebase code.
        mAuth = FirebaseAuth.getInstance();

        //A new OnClickListener is added to the logout button.
        logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new myListener());

        //user_info_email is created.
        user_info_email = (TextView) findViewById(R.id.user_info_email);

        //getCurrentUser-method is being called.
        getCurrentUser();
    }

    //Standard Firebase code.
    public void getCurrentUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            user_info_email.setText(email);
        }
    }

    //If the back button is pressed, the application goes to MainActivity.
    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(UserInfoActivity.this, MainActivity.class));
    }

    //A non-anonymous listener
    private class myListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            //When the sign out button is clicked, Firebase signs out, a toast shows and the app
            // goes back to the MainActivity.
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(UserInfoActivity.this, R.string.sign_out, Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(UserInfoActivity.this, MainActivity.class));
        }
    }
}
