//package com.cardgame.config.SocketIO;
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
//@WebServlet("/socket.io/*")
//public class SocketIoServlet extends HttpServlet {
//
//    private final EngineIoServer mEngineIoServer = new EngineIoServer();
//    private final SocketIoServer mSocketIoServer = new SocketIoServer(mEngineIoServer);
//
//    SocketIoSocket socket;
//
//    SocketIoNamespace namespace = mSocketIoServer.namespace("/");
//
//
//    @Override
//    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        mEngineIoServer.handleRequest(request, response);
//    }
//    public void listeningforconnections(){
//        namespace.on("connection", new Emitter.Listener() {
//
//
////            io.on("connection", (socket) => {
////                socket.emit("hello", "please acknowledge", (response) => {
////                        console.log(response); // prints "hi!"
////  });
////            });
//            @Override
//            public void call(Object... args) {
//                SocketIoSocket socket = (SocketIoSocket) args[0];
//                // Do something with socket
//                socket.emit("connected to the server");
//                System.out.println("connecteds");
//            }
//        });
//    }
//
//
//    public void listentomessagefromclient(){
//        // Attaching to 'foo' event
//        socket.on("foo", new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                // Arugments from client available in 'args'
//                System.out.println("foooooddhdh");
//            }
//        });
//    }
//}