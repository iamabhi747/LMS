package com.vit.library;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class UserActivity extends AppCompatActivity {

    int id;
    TextView t_name, t_email, t_lends, t_fine, t_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        DBAdapter db = new DBAdapter(this);
        db.open();

        Intent i = getIntent();
        String name  = i.getStringExtra("name");
        String email = i.getStringExtra("email");
        id = Integer.parseInt(i.getStringExtra("id"));

        t_name  = (TextView) findViewById(R.id.user_name);
        t_email = (TextView) findViewById(R.id.user_email);
        t_lends = (TextView) findViewById(R.id.user_lends);
        t_fine  = (TextView) findViewById(R.id.user_fine);
        t_id    = (TextView) findViewById(R.id.user_id);

        t_name.setText(name);
        t_email.setText(email);
        t_id.setText(Integer.toString(id));

        t_lends.setText(Integer.toString(db.getUserLends(id)));
        t_fine.setText(Integer.toString(db.getUserFine(id)));
    }
}