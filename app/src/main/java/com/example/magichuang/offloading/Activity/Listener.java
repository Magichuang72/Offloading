package com.example.magichuang.offloading.Activity;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.magichuang.offloading.Network.MinaClient;
import com.example.magichuang.offloading.Network.MinaServer;
import com.example.magichuang.offloading.Network.Pso;
import com.example.magichuang.offloading.R;

import java.io.IOException;

/**
 * Created by magichuang on 17-4-17.
 */
public class Listener {
    private Context context;
    private String serverIP;
    private Handler handler;
    private Pso pso;
    private MinaServer server;
    private MinaClient client;

    Listener(Context context, String serverIP, Handler handler) {
        this.handler = handler;
        this.serverIP = serverIP;
        this.context = context;
    }

    public void addListener(Button button) {
        switch (button.getId()) {
            case R.id.button:
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Thread t = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                server = new MinaServer(5000);
                                server.setUiHandler(handler);
                                server.startServer();
                            }
                        });
                        t.start();
                        Toast.makeText(context, "开启端口", Toast.LENGTH_LONG).show();
                    }
                });
                break;
            case R.id.button2:
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Thread t = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                client = new MinaClient(serverIP, 5000);
                                client.setUiHandler(handler);
                                client.startClient();
                            }
                        });
                        t.start();
                        Toast.makeText(context, "连接热点中心，加入组网", Toast.LENGTH_LONG).show();
                    }
                });
                break;
            case R.id.button3:
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (server == null) {
                            server = new MinaServer(5000);
                            server.setUiHandler(handler);
                            server.startServer();
                        }
                        client.send("broadcast: " + NodeInfo.ip);
                    }

                });
                break;
        }
    }
}
