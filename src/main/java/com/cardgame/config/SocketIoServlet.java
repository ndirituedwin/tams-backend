//package com.cardgame.config;
//
//
//import io.socket.emitter.Emitter;
//import io.socket.engineio.server.EngineIoServer;
//import io.socket.socketio.server.SocketIoNamespace;
//import io.socket.socketio.server.SocketIoServer;
//import io.socket.socketio.server.SocketIoSocket;
//
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@WebServlet("/socket.io/4200")
//public class SocketIoServlet extends HttpServlet {
//
//    private final EngineIoServer mEngineIoServer = new EngineIoServer();
//    private final SocketIoServer mSocketIoServer = new SocketIoServer(mEngineIoServer);
//
//    @Override
//    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        mEngineIoServer.handleRequest(request, response);
//    }
//
//    public EngineIoServer getmEngineIoServer() {
//        return mEngineIoServer;
//    }
//
//    public SocketIoServer getmSocketIoServer() {
//        return mSocketIoServer;
//    }
//
//    public void  listenforconnection(){
//        SocketIoNamespace namespace = getmSocketIoServer().namespace("/");
//        namespace.on("connection", new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                SocketIoSocket socket = (SocketIoSocket) args[0];
//                // Do something with socket
//                System.out.println("ddddd");
//            }
//        });
//
//    }
//
//}