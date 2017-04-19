package com.example.magichuang.offloading.Network;

import android.os.Handler;
import android.os.Message;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by magichuang on 17-3-8.
 */
public class MinaClientHanlder implements IoHandler {
    private MinaClient client;
    private static final String TAG = "MinaClientHanlder";
    private Handler uiHadnler;
    private IoSession serverSession;

    public void setClient(MinaClient client) {
        this.client = client;
    }

    public IoSession getServerSession() {
        return serverSession;
    }

    public void setUiHadnler(Handler uiHadnler) {
        this.uiHadnler = uiHadnler;
    }

    @Override
    public void sessionCreated(IoSession ioSession) throws Exception {
        serverSession = ioSession;
    }

    @Override
    public void sessionOpened(IoSession ioSession) throws Exception {
        // System.out.println(TAG);
    }

    @Override
    public void sessionClosed(IoSession ioSession) throws Exception {

    }

    @Override
    public void sessionIdle(IoSession ioSession, IdleStatus idleStatus) throws Exception {

    }

    @Override
    public void exceptionCaught(IoSession ioSession, Throwable throwable) throws Exception {

    }

    @Override
    public void messageReceived(IoSession ioSession, Object o) throws Exception {
        System.out.println("---------------" + o + "-----------------");
        if (o instanceof String) {
            if (((String) o).startsWith("request:")) {
                String[] temp = ((String) o).split(" ");
                final String ipq = temp[1];

                Message msg = new Message();
                msg.what = 2;
                msg.obj = ipq;
                uiHadnler.sendMessage(msg);
                System.out.println("收到请求");
            }


        }
//        if (o instanceof byte[]) {
//            byte[] buffer = (byte[]) o;
//            File file = new File("Task.class");
//            FileOutputStream fos = new FileOutputStream(file);
//            fos.write(buffer);
//        }

        if (o instanceof Loadable) {
            System.out.println("反序列化成功");
            Message msg = new Message();
            msg.what = 3;
            uiHadnler.sendMessage(msg);
            Loadable x = (Loadable) o;
            x.doTask();
        }
    }

    @Override
    public void messageSent(IoSession ioSession, Object o) throws Exception {


    }

    @Override
    public void inputClosed(IoSession ioSession) throws Exception {
        // System.out.println("Client inputClosed");
    }
}
