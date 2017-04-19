package com.example.magichuang.offloading.Network;

import android.os.Handler;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by magichuang on 17-3-8.
 */
public class MinaServer {
    private int port;
    private SocketAcceptor socketAcceptor;
    private MinaServerHanlder hanlder;
    private Pso pso;
    private static final String TAG = "MinaServer";
    private Handler uiHandler;


    public MinaServer(int port) {
        this.port = port;
    }

    public Pso getPso() {
        return pso;
    }

    public void startServer() {
        socketAcceptor = new NioSocketAcceptor();
        hanlder = new MinaServerHanlder();
        pso = new Pso(socketAcceptor.getManagedSessions(), hanlder);
        hanlder.setPso(pso);
        hanlder.setUi(uiHandler);
        LoggingFilter loggingFilter = new LoggingFilter();
        DefaultIoFilterChainBuilder chain = socketAcceptor.getFilterChain();
        ProtocolCodecFilter filter = new ProtocolCodecFilter(new ObjectSerializationCodecFactory());
        chain.addLast("logger", loggingFilter);
        chain.addLast("objectFilter", filter);
        socketAcceptor.setHandler(hanlder);
        try {
            socketAcceptor.bind(new InetSocketAddress(port));
            System.out.println("开启监听端口");
            System.out.println(port);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setUiHandler(Handler handler) {
        uiHandler = handler;
    }

}
