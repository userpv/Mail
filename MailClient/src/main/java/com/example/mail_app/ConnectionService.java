package com.example.mail_app;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConnectionService extends Service {
    private Client client = new Client();
    private final int PORT = 1900;
    private final String HOST = "10.0.2.2";
    private ExecutorService service;
    private String enter_data;
    public ConnectionService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        service = Executors.newFixedThreadPool(1);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.hasExtra("enter_data")) {
            PendingIntent res = intent.getParcelableExtra("result");
            enter_data = intent.getStringExtra("enter_data");
            try {
                String result = (String) service.submit(new MyTask()).get();
                assert res != null;
                Intent intent1 = new Intent();
                intent1.putExtra("result", result);
                res.send(ConnectionService.this, 0, intent1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (intent.hasExtra("latters")) {
            PendingIntent lat = intent.getParcelableExtra("latters");
            try {
                String[] result = (String[])service.submit(new Receiver("latters")).get();
                assert lat != null;
                Intent intent1 = new Intent();
                intent1.putExtra("latters", result);
                lat.send(ConnectionService.this, 0, intent1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (intent.hasExtra("deleted_latters")) {
            PendingIntent lat = intent.getParcelableExtra("deleted_latters");
            try {
                String[] result = (String[])service.submit(new Receiver("deleted_latters")).get();
                assert lat != null;
                Intent intent1 = new Intent();
                intent1.putExtra("deleted_latters", result);
                lat.send(ConnectionService.this, 0, intent1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (intent.hasExtra("sended_latters")) {
            PendingIntent lat = intent.getParcelableExtra("sended_latters");
            try {
                String[] result = (String[])service.submit(new Receiver("sended_latters")).get();
                assert lat != null;
                Intent intent1 = new Intent();
                intent1.putExtra("sended_latters", result);
                lat.send(ConnectionService.this, 0, intent1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        if (intent.hasExtra("latter_to_box")) {
            try {
                String result = (String)service.submit(new Delete(intent.getStringExtra("receiver"), intent.getStringExtra("titleLater"),
                        intent.getStringExtra("text"), intent.getStringExtra("sender"),
                        intent.getStringExtra("name"), intent.getStringExtra("family_name"),
                        "latter_to_box")).get();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (intent.hasExtra("delete")) {
            try {
                String result = (String)service.submit(new Delete(intent.getStringExtra("receiver"), intent.getStringExtra("titleLater"),
                        intent.getStringExtra("text"), intent.getStringExtra("sender"),
                        intent.getStringExtra("name"), intent.getStringExtra("family_name"),
                        "delete")).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (intent.hasExtra("Name")) {
            client.setName(intent.getStringExtra("Name"));
            client.setFamily_name(intent.getStringExtra("FamilyName"));
            client.setLogin(intent.getStringExtra("Login"));
            client.setPassword(intent.getStringExtra("password"));
            service.submit(new Registration_Task());
        }

        if (intent.hasExtra("wright_Latter")) {

            service.submit(new Sender(intent.getStringExtra("receiver"), intent.getStringExtra("titleLater"),
                    intent.getStringExtra("wright_Latter"), intent.getStringExtra("sender")));
        }
        //stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    public class MyTask implements Callable
    {
        @Override
        public String call() {
            String lineCheck = "";

            try {
                if (client.getSocket()==null) {client.setSocket(new Socket(HOST, PORT));}
                InputStream inputStream = client.getSocket().getInputStream();
                OutputStream outputStream = client.getSocket().getOutputStream();
                outputStream.write(enter_data.getBytes());

                byte[] data = new byte[128];
                int bytes = 0;
                do
                {
                    bytes = inputStream.read(data, 0, data.length);
                    lineCheck += new String(data, 0, bytes);
                }
                while (inputStream.available()!=0);

                String[] split_message = lineCheck.split(";");
                lineCheck = split_message[1];

                if (lineCheck.equals("Вход")) {
                    return "correct password";
                }
                else
                {
                    return "incorrect password";
                }

            }
            catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public class Receiver implements Callable
    {
        private String switch_case;
        public Receiver(String switch_case){
            this.switch_case = switch_case;
        }

        @Override
        public String[] call() {
            try {
                if(client.getSocket()==null){client.setSocket(new Socket(HOST, PORT));}
                String later_data = "";
                switch (switch_case){
                    case "latters":{later_data = "входящие"+";"+client.getLogin(); break;}
                    case "deleted_latters":{later_data = "корзина"+";"+client.getLogin(); break;}
                    case "sended_latters":{later_data = "отправленные"+";"+client.getLogin(); break;}
                }

                OutputStream outputStream = client.getSocket().getOutputStream();
                outputStream.write(later_data.getBytes());
                InputStream inputStream = client.getSocket().getInputStream();

                //while (!Thread.currentThread().isInterrupted()) {
                String lineLater = "";
                byte[] later = new byte[128];
                int later_bytes = 0;
                try {
                    do {
                        later_bytes = inputStream.read(later, 0, later.length);
                        lineLater += new String(later, 0, later_bytes);
                    }
                    while (inputStream.available() != 0);
                    //client.getSocket().close();
                } catch (IOException e) {
                    // break;
                }
                if (!lineLater.equals("нет писем")) {
                    String[] split_later = lineLater.split(";");
                    return split_later;
                }
                //}
            }
            catch (IOException e){
                e.printStackTrace();}
            return null;
        }
    }

        public class Registration_Task implements Runnable
        {
            @Override
            public void run() {
                try {
                    client.setSocket(new Socket(HOST, PORT));
                    OutputStream outputStream = client.getSocket().getOutputStream();
                    String reg_data = "Регистрация"+ client.getName()+";"+client.getFamily_name()
                            +";"+client.getLogin()+";"+client.getPassword();
                    outputStream.write(reg_data.getBytes());
                    //client.getSocket().close();
                }
                catch (IOException e) {
                    e.printStackTrace();}

            }
        }

    public class Sender implements Runnable{
        String receiver;
        String titleLater;
        String wright_Latter;
        String sender;
        public Sender (String receiver, String titleLater, String wright_Latter, String sender){
            this.receiver = receiver;
            this.titleLater = titleLater;
            this.wright_Latter = wright_Latter;
            this.sender = sender;
        }

        @Override
        public void run() {
            try {
                client.setSocket(new Socket(HOST, PORT));
                OutputStream outputStream = client.getSocket().getOutputStream();
                String later_data = "Письмо"+ receiver+";"+ wright_Latter
                        +";"+sender+";"+titleLater;
                outputStream.write(later_data.getBytes());
                //client.getSocket().close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    public class Delete implements Runnable{
        String receiver;
        String titleLater;
        String wright_Latter;
        String sender;
        String name;
        String family_name;
        String switch_case;

        public Delete (String receiver, String titleLater, String wright_Latter, String sender, String name, String family_name, String switch_case){
            this.receiver = receiver;
            this.titleLater = titleLater;
            this.wright_Latter = wright_Latter;
            this.sender = sender;
            this.name = name;
            this.family_name = family_name;
            this.switch_case = switch_case;
        }

        @Override
        public void run() {
            try {
                if (client.getSocket()==null) {
                    client.setSocket(new Socket(HOST, PORT));
                }
                OutputStream outputStream = client.getSocket().getOutputStream();
                String later_data ="";
                switch (switch_case){
                    case "latter_to_box":{later_data = "перемещение"+ receiver+";"+ wright_Latter
                            +";"+sender+";"+ titleLater+";" + name+";" + family_name; break;}
                    case "delete":{later_data = "удаление"+ receiver+";"+ wright_Latter
                            +";"+sender+";"+ titleLater; break;}
                }

                outputStream.write(later_data.getBytes());
                //client.getSocket().close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
