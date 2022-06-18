package com.example.helpu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Profile extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    private FirebaseAuth auth;
    private TextView nameTextView;
    private TextView emailTextView;
    private ImageView userImageView;
    Button btnSignOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.profile);

        nameTextView = findViewById(R.id.userName);
        emailTextView = findViewById(R.id.userEmail);
        userImageView = findViewById(R.id.userImage);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.map:
                        startActivity(new Intent(getApplicationContext(),Map.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.community:
                        startActivity(new Intent(getApplicationContext(),Community.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.profile:
                        return true;
                }
                return false;
            }
        });

        auth = FirebaseAuth.getInstance();
        nameTextView.setText(auth.getCurrentUser().getDisplayName());
        emailTextView.setText(auth.getCurrentUser().getEmail());
        SharedPreferences preferences = this.getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userImageUrl = preferences.getString("userPhoto","");
        Glide.with(this).load(userImageUrl).into(userImageView);
        btnSignOut = findViewById(R.id.btnLogout);

        btnSignOut.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            goToLoginActivity();
        });
    }

    private void goToLoginActivity() {
        startActivity(new Intent(Profile.this, LoginActivity.class));
    }
}