package com.example.magichuang.offloading.Network;

import android.os.Handler;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;

/**
 * Created by magichuang on 17-3-8.
 */
public class MinaClient {
    private NioSocketConnector connector;
    private int port;
    private String ip;
    private NodeMessage nodeMessage;
    private Handler uiHandle;
    private MinaClientHanlder hanlder;

    public MinaClient(String ip, int port) {
        this.port = port;
        this.ip = ip;
        nodeMessage = new NodeMessage(100, 100, 100);
    }

    public boolean isConnected() {
        return connector.isActive();
    }

    public NodeMessage getNodeMessage() {
        return nodeMessage;
    }

    public NioSocketConnector getConnector() {
        return connector;
    }

    public void startClient() {
        LoggingFilter loggingFilter = new LoggingFilter();
        connector = new NioSocketConnector();
        DefaultIoFilterChainBuilder chain = connector.getFilterChain();
        ProtocolCodecFilter filter = new ProtocolCodecFilter(new ObjectSerializationCodecFactory());
        chain.addLast("logger", loggingFilter);
        chain.addLast("objectFilter", filter);
        hanlder = new MinaClientHanlder();
        hanlder.setClient(this);
        hanlder.setUiHadnler(uiHandle);
        connector.setHandler(hanlder);
        connector.connect(new InetSocketAddress(ip, port));
        System.out.println("客户端启动" + ip + " " + port);
//        connector.setConnectTimeoutCheckInterval(30);
//        ConnectFuture cf = connector.connect(new InetSocketAddress("localhost", port));
//        cf.awaitUninterruptibly();
//        cf.getSession().getCloseFuture().awaitUninterruptibly();
//        connector.dispose();
    }

    public void setUiHandler(Handler handler) {
        uiHandle = handler;
    }

    public MinaClientHanlder getClientHanlder() {
        return hanlder;
    }

    public IoSession getSeverSession() {
        return getClientHanlder().getServerSession();
    }

    public void send(String msg) {
        try {
            while (getSeverSession()==null) {
            }
            getSeverSession().write(msg);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void send(NodeMessage msg) {
        try {
            while (getSeverSession()==null) {
            }
            getSeverSession().write(msg);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}