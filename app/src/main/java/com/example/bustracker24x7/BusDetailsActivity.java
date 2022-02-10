package com.example.bustracker24x7;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bustracker24x7.BusTypes.BlueBusActivity;
import com.example.bustracker24x7.BusTypes.GreenBusActivity;
import com.example.bustracker24x7.BusTypes.RedBusActivity;

public class BusDetailsActivity extends AppCompatActivity {


    private Button greenbt,bluebt,redbt;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_details);

        greenbt = findViewById(R.id.greenbutton);
        bluebt = findViewById(R.id.bluebutton);
        redbt = findViewById(R.id.redbutton);
        back = findViewById(R.id.backtouserhome);

        greenbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BusDetailsActivity.this, GreenBusActivity.class));
                finish();
            }
        });

        bluebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BusDetailsActivity.this, BlueBusActivity.class));
                finish();
            }
        });

        redbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BusDetailsActivity.this, RedBusActivity.class));
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BusDetailsActivity.this,UserHomeActivity.class));
                finish();
            }
        });


    }
}