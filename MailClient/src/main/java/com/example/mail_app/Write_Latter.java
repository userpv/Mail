package com.example.mail_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


public class Write_Latter extends AppCompatActivity {

    private EditText receiver;
    private EditText titleLater;
    private EditText wright_Latter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write__latter);

        receiver = (EditText)findViewById(R.id.receiver);
        titleLater = (EditText)findViewById(R.id.title_latter);
        wright_Latter = (EditText)findViewById(R.id.text_latter);
    }

    public void Send(View view){
        Intent send = new Intent(this, ConnectionService.class);
        send.putExtra("receiver", receiver.getText().toString());
        send.putExtra("titleLater", titleLater.getText().toString());
        send.putExtra("sender", this.getIntent().getStringExtra("login"));
        send.putExtra("wright_Latter", wright_Latter.getText().toString());
        startService(send);
        finish();
    }




}