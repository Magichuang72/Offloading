package com.example.magichuang.offloading.Helper;

import android.os.Handler;

import com.example.magichuang.offloading.Network.MinaClient;

/**
 * Created by magichuang on 17-4-19.
 */
public class CreateClient implements Runnable {
    MinaClient minaClient;
    Handler uihandler;

    public CreateClient(String ip, int port, Handler uihandler) {
        this.minaClient = new MinaClient(ip, port);
        this.uihandler = uihandler;
        minaClient.setUiHandler(uihandler);
    }

    @Override
    public void run() {
        minaClient.startClient();
    }

    public MinaClient getMinaClient() {
        return minaClient;
    }
}
