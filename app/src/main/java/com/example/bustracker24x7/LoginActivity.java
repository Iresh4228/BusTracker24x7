package com.example.bustracker24x7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {

    private EditText txtusername, txtpassword;
    private Button adminlogin,userlogin;
    private FirebaseAuth authentication;
    private TextView tvlink;
    private ImageView google, facebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtusername = findViewById(R.id.txtusername);
        txtpassword = findViewById(R.id.txtpassword);
        adminlogin = findViewById(R.id.adminlogin);
        authentication = FirebaseAuth.getInstance();
        tvlink = findViewById(R.id.tvlink);
        userlogin = findViewById(R.id.userlogin);

        userlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email,password;
                email = txtusername.getText().toString();
                password = txtpassword.getText().toString();

                if (email.isEmpty() || password.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Please enter both Email and Password!!!", Toast.LENGTH_SHORT).show();
                }else {
                    validateUserlogin(email,password);
                }
            }
        });



        adminlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;
                email = txtusername.getText().toString();
                password = txtpassword.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter both Email and Password!!!", Toast.LENGTH_SHORT).show();

                } else {
                    validateUser(email, password);

                }
            }
        });

        tvlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent int_reg = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(int_reg);
                finish();
            }
        });
        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
    }

    private void validateUserlogin(String email, String password) {
        authentication.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(LoginActivity.this, "Valid User", Toast.LENGTH_SHORT).show();
                if (email.equals("admin@gmail.com")){
                    startActivity(new Intent(LoginActivity.this,UserHomeActivity.class));
                }else {
                    startActivity(new Intent(LoginActivity.this,UserHomeActivity.class));
                }
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, "Invalid Email or Password, Please try again....", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void gotoUrl(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    private void validateUser(String email, String password) {
        authentication.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                Toast.makeText(LoginActivity.this, "Valid User", Toast.LENGTH_SHORT).show();
                if (email.equals("admin@gmail.com")) {
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                } else {
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                }
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, "Invalid Email or Password, Please try again....", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
//        /*
//        if(FirebaseAuth.getInstance().getCurrentUser()!=null);
//        startActivity(new Intent(LoginActivity.this,MainActivity.class));
//        finish();;
//
//         */
    }
}
