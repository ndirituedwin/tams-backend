//package com.cardgame.config;
//
//import io.socket.emitter.Emitter;
//import io.socket.engineio.server.EngineIoServer;
//import io.socket.engineio.server.EngineIoSocket;
//import io.socket.socketio.server.SocketIoSocket;
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
//    private final EngineIoServer mEngineIoServer = new EngineIoServer();
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
//
//}