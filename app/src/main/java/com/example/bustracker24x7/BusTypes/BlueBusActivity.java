package com.example.bustracker24x7.BusTypes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.bustracker24x7.BusDetailsActivity;
import com.example.bustracker24x7.R;

public class BlueBusActivity extends AppCompatActivity {

    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_bus);

        back = findViewById(R.id.backtobusdetails);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BlueBusActivity.this, BusDetailsActivity.class));
                finish();
            }
        });
    }
}