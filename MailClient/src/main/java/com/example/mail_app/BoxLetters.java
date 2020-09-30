package com.example.mail_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;


public class BoxLetters extends AppCompatActivity {

    ListView list_latter;
    TextView view_latter;
    TextView latter_title;
    TextView latter_sender;
    ArrayList<Latter> latters;
    ArrayList<Latter> latters_deleted;
    ArrayList<Latter> sended_latters;
    Latter this_latter;
    boolean in_box = false;
    boolean in_box_lat = true;
    boolean in_box_send = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box_letters);

        latters = new ArrayList<>();
        latters_deleted = new ArrayList<>();
        sended_latters = new ArrayList<>();
        startGetLatterService("latters",2);
        startGetLatterService("deleted_latters",3);
        startGetLatterService("sended_latters",4);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode==2) {
            String[] split_later = data.getStringArrayExtra("latters");
            if (split_later != null) {
                addLatter(split_later, latters);

            }
        }

        if (data != null && requestCode==3) {
                String[] split_later = data.getStringArrayExtra("deleted_latters");
                if (split_later != null) {
                    addLatter(split_later, latters_deleted);
                }
            }

        if (data != null && requestCode==4) {
            String[] split_later = data.getStringArrayExtra("sended_latters");
            if (split_later != null) {
                addLatter(split_later, sended_latters);
            }
        }

        String[] text = new String[latters.size()];
        for (int i = 0; i<text.length; i++) {
            text[i] = latters.get(i).getSender()+ "                           " + latters.get(i).getTitle();
        }
        list_latter = (ListView)findViewById(R.id.Latters_List);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, text);
        list_latter.setAdapter(adapter);
        list_latter.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id){select_latter(position);}
        });

        }

    public void wright_Latter(View view){
        Intent intent = new Intent(this, Write_Latter.class);
        this.getIntent().getStringExtra("login");
        intent.putExtra("login", this.getIntent().getStringExtra("login"));
        startActivity(intent);
    }



    public void  select_latter(int selected_item){
        list_latter.setVisibility(View.INVISIBLE);
        view_latter = (TextView)findViewById(R.id.view_latter);
        latter_title = (TextView)findViewById(R.id.title_latter_filde);
        latter_sender = (TextView)findViewById(R.id.sender_filde);
        if (in_box_lat){view_latter.setText(latters.get(selected_item).getText());
            latter_title.setText(latters.get(selected_item).getTitle());
            latter_sender.setText(latters.get(selected_item).getSender());
            this_latter = latters.get(selected_item);
        }
        else {
            if (in_box_send){
                view_latter.setText(sended_latters.get(selected_item).getText());
                latter_title.setText(sended_latters.get(selected_item).getTitle());
                latter_sender.setText(sended_latters.get(selected_item).getSender());
                this_latter = sended_latters.get(selected_item);
            }
            else {
                view_latter.setText(latters_deleted.get(selected_item).getText());
                latter_title.setText(latters_deleted.get(selected_item).getTitle());
                latter_sender.setText(latters_deleted.get(selected_item).getSender());
                this_latter = latters_deleted.get(selected_item);
            }
        }

        view_latter.setVisibility(View.VISIBLE);
        latter_title.setVisibility(View.VISIBLE);
        latter_sender.setVisibility(View.VISIBLE);

    }


    public void back_to_latters(View view){
        view_latter = (TextView)findViewById(R.id.view_latter);
        latter_title = (TextView)findViewById(R.id.title_latter_filde);
        latter_sender = (TextView)findViewById(R.id.sender_filde);
        in_box = false;
        in_box_lat = true;
        in_box_send = false;
        String[] text = new String[latters.size()];
        for (int i = 0; i<text.length; i++) {
            text[i] = latters.get(i).getSender()+ "                           " + latters.get(i).getTitle();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, text);
        findViewById(R.id.constraintLayout).setVisibility(View.VISIBLE);
        findViewById(R.id.constraintLayout2).setVisibility(View.INVISIBLE);
        list_latter.setAdapter(adapter);
        list_latter.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id){select_latter(position);}
        });
        list_latter.setVisibility(View.VISIBLE);
        view_latter.setVisibility(View.INVISIBLE);
        latter_title.setVisibility(View.INVISIBLE);
        latter_sender.setVisibility(View.INVISIBLE);
    }

    public void to_sended_latters(View view){
        view_latter = (TextView)findViewById(R.id.view_latter);
        latter_title = (TextView)findViewById(R.id.title_latter_filde);
        latter_sender = (TextView)findViewById(R.id.sender_filde);
        in_box = false;
        in_box_lat = false;
        in_box_send = true;
        String[] text = new String[sended_latters.size()];
        for (int i = 0; i<text.length; i++) {
            text[i] = sended_latters.get(i).getSender()+ "                           " + sended_latters.get(i).getTitle();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, text);
        findViewById(R.id.constraintLayout).setVisibility(View.VISIBLE);
        findViewById(R.id.constraintLayout2).setVisibility(View.INVISIBLE);
        list_latter.setAdapter(adapter);
        list_latter.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id){select_latter(position);}
        });
        list_latter.setVisibility(View.VISIBLE);
        view_latter.setVisibility(View.INVISIBLE);
        latter_title.setVisibility(View.INVISIBLE);
        latter_sender.setVisibility(View.INVISIBLE);
    }

    public void to_deleted_latters(View view) {
        view_latter = (TextView)findViewById(R.id.view_latter);
        latter_title = (TextView)findViewById(R.id.title_latter_filde);
        latter_sender = (TextView)findViewById(R.id.sender_filde);
        in_box = true;
        in_box_lat = false;
        in_box_send = false;
        String[] text = new String[latters_deleted.size()];
        for (int i = 0; i < text.length; i++) {
            text[i] = latters_deleted.get(i).getSender() + "                           " + latters_deleted.get(i).getTitle();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, text);
        list_latter.setAdapter(adapter);
        list_latter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                select_latter(position);
            }
        });
        findViewById(R.id.constraintLayout).setVisibility(View.VISIBLE);
        findViewById(R.id.constraintLayout2).setVisibility(View.INVISIBLE);
        list_latter.setVisibility(View.VISIBLE);
        view_latter.setVisibility(View.INVISIBLE);
        latter_title.setVisibility(View.INVISIBLE);
        latter_sender.setVisibility(View.INVISIBLE);
    }

    public void to_menu(View view) {
        findViewById(R.id.constraintLayout).setVisibility(View.INVISIBLE);
        findViewById(R.id.constraintLayout2).setVisibility(View.VISIBLE);
    }

    public void delete(View view){
        if (!in_box){
            startDeleteService("latter_to_box");
        }
        else
        {
            startDeleteService("delete");
        }
        if (in_box_lat){
            latters.remove(this_latter);
            back_to_latters(view);
        }
        if (in_box){
            latters_deleted.remove(this_latter);
            to_deleted_latters(view);
        }
        if (in_box_send){
            sended_latters.remove(this_latter);
            to_sended_latters(view);
        }

    }

    private void addLatter(String[] split_later, ArrayList<Latter> latters_list){

        for (int i = 0; i < split_later.length; i = i + 6) {
            Latter latter = new Latter();
            latter.setSender(split_later[1 + i]);
            latter.setText(split_later[0 + i]);
            latter.setTitle(split_later[2 + i]);
            latter.setName(split_later[3 + i]);
            latter.setFamily_name(split_later[4 + i]);
            latter.setReceiver(this.getIntent().getStringExtra("login"));
            latters_list.add(latter);
        }
    }

    void startDeleteService(String keyword){
        Intent intent_ser = new Intent(this, ConnectionService.class);
        intent_ser.putExtra(keyword, "");
        intent_ser.putExtra("receiver", this_latter.getReceiver());
        intent_ser.putExtra("titleLater", this_latter.getTitle());
        intent_ser.putExtra("sender", this_latter.getSender());
        intent_ser.putExtra("text", this_latter.getText());
        intent_ser.putExtra("name", this_latter.getText());
        intent_ser.putExtra("family_name", this_latter.getText());
        startService(intent_ser);
    }

    void startGetLatterService(String keyword, int code){
        PendingIntent req = createPendingResult(code, new Intent(), 0);
        Intent intent_ser = new Intent(this, ConnectionService.class);
        intent_ser.putExtra(keyword, req);
        startService(intent_ser);
    }
}