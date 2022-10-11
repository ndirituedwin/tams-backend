//package com.cardgame.config.SocketIO;
//
//import io.socket.emitter.Emitter;
//import io.socket.engineio.server.EngineIoServer;
//import io.socket.engineio.server.EngineIoSocket;
//
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@WebServlet("/engine.io/*")
//public class EngineIoServlet extends HttpServlet {
//
//
//    private final EngineIoServer mEngineIoServer = new EngineIoServer();
//    private EngineIoServer server;
//    private EngineIoSocket socket;
//
//    @Override
//    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        mEngineIoServer.handleRequest(request, response);
//    }
//
//    public void listenforconnections(){
////        EngineIoServer server = null;  // server instance
//        server.on("connection", new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                EngineIoSocket socket = (EngineIoSocket) args[0];
//                // Do something with socket like store it somewhere
//            }
//        });
//    }
//    public void listenformessagefromclient(){
//        socket.on("message", new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                Object message = args[0];
//                // message can be either String or byte[]
//                // Do something with message.
//            }
//        });
//    }
//}
