package com.vit.library;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class BookActivity extends AppCompatActivity {

    TextView t_name, t_author, t_copies, t_available, t_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        t_name      = (TextView) findViewById(R.id.book_name);
        t_author    = (TextView) findViewById(R.id.book_author);
        t_copies    = (TextView) findViewById(R.id.book_copies);
        t_available = (TextView) findViewById(R.id.book_copies_available);
        t_id        = (TextView) findViewById(R.id.book_id);

        Intent i = getIntent();

        t_name  .setText( i.getStringExtra("name"  ).toString() );
        t_author.setText( i.getStringExtra("author").toString() );
        t_copies.setText( i.getStringExtra("copies").toString() );
        t_id    .setText( i.getStringExtra("id"    ).toString() );
        t_available.setText( Integer.toString(Integer.parseInt(i.getStringExtra("copies").toString()) - Integer.parseInt(i.getStringExtra("lends").toString())) );
    }
}