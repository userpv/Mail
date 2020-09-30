package com.example.mail_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Registration extends AppCompatActivity {

    private EditText textName;
    private EditText textFamilyName;
    private EditText textLogin;
    private EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        textName = (EditText) findViewById(R.id.regName);
        textFamilyName = (EditText) findViewById(R.id.regFamilyName);
        textLogin = (EditText) findViewById(R.id.regtEmail);
        passwordField = (EditText) findViewById(R.id.regPassword);
    }

    public void onClick_reg_complete(View view) {
        Intent reg = new Intent(this, ConnectionService.class);
        reg.putExtra("Name", textName.getText().toString());
        reg.putExtra("FamilyName", textFamilyName.getText().toString());
        reg.putExtra("Login", textLogin.getText().toString());
        reg.putExtra("password", passwordField.getText().toString());
        startService(reg);

        textName.clearComposingText();
        textFamilyName.clearComposingText();
        textLogin.clearComposingText();
        passwordField.clearComposingText();
        finish();
    }



}