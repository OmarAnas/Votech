package com.example.votech;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class pollVote extends AppCompatActivity {

    String pollID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll_vote);

        pollID=getIntent().getStringExtra("pollID");
        Toast.makeText(this, pollID, Toast.LENGTH_SHORT).show();
    }
}
