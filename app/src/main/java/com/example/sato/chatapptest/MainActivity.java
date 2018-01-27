package com.example.sato.chatapptest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {

    private Socket mSocket = null;
    private ListView mListView = null;
    private ArrayAdapter<String> mAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = findViewById(R.id.listView);
        mAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1);
        mListView.setAdapter(mAdapter);

        try {
//            mSocket = IO.socket("http://192.168.100.103:3000");
//            mSocket = IO.socket("http://192.168.179.5:3000");
            mSocket = IO.socket("https://thawing-thicket-62920.herokuapp.com/");

            mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    Log.d("xxxxxx", "event called EVENT_CONNECT");
                }

            }).on("chat message", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    final String message = (String)args[0];
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.add(message);
                        }
                    });
                    Log.d("xxxxxx", "event called 1");
                }

            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    Log.d("xxxxxx", "event called EVENT_DISCONNECT");
                }

            });
            mSocket.connect();

        } catch (Exception e) {
            e.printStackTrace();
        }

        final TextView editText = findViewById(R.id.editText);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSocket != null) {
                    // Sending an object
                    String message = editText.getText().toString();
                    mSocket.emit("chat message", message);
                    editText.setText("");
                    Log.d("xxxxxxxx", message);
                }
            }
        });
    }
}
