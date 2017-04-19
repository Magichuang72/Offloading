package com.example.magichuang.offloading.Network;

import android.os.Handler;
import android.os.Message;

import com.example.magichuang.offloading.Dex.Task;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by magichuang on 17-3-8.
 */
public class MinaServerHanlder implements IoHandler {
    HashMap<IoSession, NodeMessage> mapData;
    Pso pso;
    private static final String TAG = "MinaServerHanlder";
    private Handler uiHandler;
    ArrayList<IoSession> allSession;

    MinaServerHanlder() {
        allSession = new ArrayList<>();
        mapData = new HashMap<>();
    }

    public void setPso(Pso pso) {
        this.pso = pso;
    }

    @Override
    public void sessionCreated(IoSession ioSession) throws Exception {
        System.out.println(TAG);
        System.out.println(ioSession);
        ioSession.write("hello");
        allSession.add(ioSession);
        Message msg = new Message();
        msg.what = 1;
        uiHandler.sendMessage(msg);
    }

    @Override
    public void sessionOpened(IoSession ioSession) throws Exception {
        System.out.println(TAG);
        System.out.println(ioSession);
        ioSession.write("hello");
    }

    @Override
    public void sessionClosed(IoSession ioSession) throws Exception {
        allSession.remove(ioSession);
    }

    @Override
    public void sessionIdle(IoSession ioSession, IdleStatus idleStatus) throws Exception {


    }

    @Override
    public void exceptionCaught(IoSession ioSession, Throwable throwable) throws Exception {
        System.out.println("exceptionCaught");
    }

    @Override
    public void messageReceived(IoSession ioSession, Object o) throws Exception {

        if (o instanceof String) {
            if (((String) o).startsWith("broadcast: ")) {
                String[] temp = ((String) o).split(" ");
                String ip = temp[1];
                broadcast(ip);
                Message msg = new Message();
                msg.what = 2;
                msg.obj = ip;
                uiHandler.sendMessage(msg);
            }
        }
        if (o instanceof NodeMessage) {
            System.out.println("得到一个回应");
            NodeMessage buff = (NodeMessage) o;
            mapData.put(ioSession, buff);
            ioSession.write(new Task(4));

        }

    }

    private void broadcast(String ip) {
        for (IoSession session : allSession) {
            session.write("request: " + ip);
        }
    }

    @Override
    public void messageSent(IoSession ioSession, Object o) throws Exception {
        //    System.out.println(Thread.currentThread() + "   ----for----" + o);

    }

    @Override
    public void inputClosed(IoSession ioSession) throws Exception {
        //   System.out.println("inputClosed");

    }

    public HashMap<IoSession, NodeMessage> getMapData() {
        return mapData;
    }

    public void setUi(Handler uiHandler) {
        this.uiHandler = uiHandler;
    }
}
