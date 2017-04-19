package com.example.magichuang.offloading.Activity;

import android.content.DialogInterface;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.magichuang.offloading.DAO.SqlCrud;
import com.example.magichuang.offloading.Helper.CreateClient;
import com.example.magichuang.offloading.Helper.NetworkHelper;
import com.example.magichuang.offloading.Network.MinaClient;
import com.example.magichuang.offloading.R;

import org.w3c.dom.Node;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView ipshow;
    private TextView allipshow;
    private TextView countShow;
    private WifiManager wifimanager;
    private String ip;
    private String serverIP;
    private ArrayList<String> allIps;
    private NetworkHelper networkHelper;
    private Button button;
    private Button button2;
    private Button button3;
    private int countNum;
    private NodeInfo nodeInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        countNum = 0;
        initNetwork();
        initUi();

    }

    private void initNetwork() {
        networkHelper = new NetworkHelper();
        wifimanager = (WifiManager) getSystemService(WIFI_SERVICE);
        DhcpInfo connectionInfo = wifimanager.getDhcpInfo();
        ip = networkHelper.intIP2StringIP(connectionInfo.ipAddress);
        NodeInfo.ip = ip;
        NodeInfo.id = 1;
        NodeInfo.isRoot = ip.equals("0.0.0.0");
        NodeInfo.name = "magichuang";
        serverIP = networkHelper.intIP2StringIP(connectionInfo.serverAddress);
//        System.out.println("--------------------" + ip + "----------------------");
        allIps = networkHelper.getConnectedIP();
    }

    private void initUi() {
        countShow = (TextView) findViewById(R.id.count);
        countShow.setText(String.valueOf(countNum));
        ipshow = (TextView) findViewById(R.id.ip);
        allipshow = (TextView) findViewById(R.id.allip);
        StringBuilder sb = new StringBuilder();
        for (String str : allIps) {
            sb.append(str + " , ");
        }
        ipshow.setText(ip);
        allipshow.setText(sb.toString());
        button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        Listener listener = new Listener(this, serverIP, handler);
        listener.addListener(button);
        listener.addListener(button2);
        listener.addListener(button3);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1://有新的子节点连入
                    countNum++;
                    countShow.setText(String.valueOf(countNum));
                    break;
                case 2://接收到主节点发出请求
                    final String toIp = (String) msg.obj;
                    new AlertDialog.Builder(MainActivity.this).setTitle("收到一" +
                            "个来自" + toIp + "的任务请求").setMessage("是否接收该任务").setPositiveButton("是",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //接收任务处理
                                    CreateClient createClient = new CreateClient(toIp, 5000, handler);
                                    Thread t = new Thread(createClient);
                                    t.start();
                                    MinaClient minaClient = createClient.getMinaClient();
                                    while (minaClient == null || minaClient.getConnector() == null || !minaClient.isConnected()) {
                                    }
                                    minaClient.send(minaClient.getNodeMessage());
                                    Toast.makeText(MainActivity.this, "同意接受任务", Toast.LENGTH_LONG).show();
                                }
                            }).setNegativeButton("否", null).show();
                    break;
                case 3://接收到任务并反序列化成功
                    Toast.makeText(MainActivity.this, "接收到任务并反序列化成功", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };
}
