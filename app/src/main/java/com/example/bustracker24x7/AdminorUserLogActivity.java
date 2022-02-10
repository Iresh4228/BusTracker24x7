package com.example.bustracker24x7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class AdminorUserLogActivity extends AppCompatActivity {

    private Button userlog, adminlog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminor_user_log);

        adminlog = findViewById(R.id.adminlog);
        userlog = findViewById(R.id.userlog);

        adminlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminorUserLogActivity.this,LoginActivity.class));
            }
        });

        userlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminorUserLogActivity.this, LoginActivity.class));
            }
        });
    }
}