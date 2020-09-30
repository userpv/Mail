package com.example.mail_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //static ArrayList <Latter> letters  = new ArrayList<Latter>();
    String result = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode==1) {
            result = data.getStringExtra("result");
            Button btn = (Button) findViewById(R.id.btn_enter);
            onClick_enter(btn);
        }

    }

    public void onClick_reg_begin(View view){
        Intent intent = new Intent(this, Registration.class);
        startActivity(intent);
    }

    public void onClick_enter(View view)
    {
        EditText text_enterLogin =  (EditText) findViewById(R.id.text_enterLogin);
        EditText text_enterPassword =  (EditText) findViewById(R.id.text_enterPassword);
        if(result.equals("")){

        String enter_data = "Вход" + text_enterLogin.getText().toString() + ";"
                + text_enterPassword.getText().toString();
        PendingIntent req = createPendingResult(1, new Intent(), 0);
        Intent intent_ser = new Intent(this, ConnectionService.class);
        intent_ser.putExtra("result", req);
        intent_ser.putExtra("enter_data", enter_data);
        startService(intent_ser);}
        else {
            if (result.equals("correct password")) {
                Intent intent = new Intent(this, BoxLetters.class);
                intent.putExtra("login", text_enterLogin.getText().toString());
                startActivity(intent);
            } else {
                Toast.makeText(this, "Incorrect password", Toast.LENGTH_LONG).show();
            }
        }
    }

}
