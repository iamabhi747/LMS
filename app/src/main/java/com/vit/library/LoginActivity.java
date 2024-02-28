package com.vit.library;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        DBAdapter db = new DBAdapter(this);
        db.open();

        EditText e_password    = (EditText) findViewById(R.id.login_password);
        Button   loginbutton   = (Button)   findViewById(R.id.login_button);
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = e_password.getText().toString();
                String s2 = db.getMetadata("password", "---|uw4ittgvwuk|p[][");
                if (s1.equals(s2))
                {
                    Intent i = new Intent(LoginActivity.this, Dashboard.class);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Invalid Authorization", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}