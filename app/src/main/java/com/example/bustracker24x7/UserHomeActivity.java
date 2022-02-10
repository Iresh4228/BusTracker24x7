package com.example.bustracker24x7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class UserHomeActivity extends AppCompatActivity {

    private Button btnbusdetails,usersignout,btnmap;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        usersignout = findViewById(R.id.usersignout);
        btnbusdetails = findViewById(R.id.bus_details);
        auth = FirebaseAuth.getInstance();
        btnmap = findViewById(R.id.map);

        btnbusdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserHomeActivity.this,BusDetailsActivity.class));
                finish();
            }
        });

        usersignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(UserHomeActivity.this,LoginActivity.class));
                finish();
            }
        });

        btnmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserHomeActivity.this,UserMapActivity.class));
            }
        });
    }
}