//package com.cardgame.config.SocketIO;
//
//import com.corundumstudio.socketio.HandshakeData;
//import com.corundumstudio.socketio.SocketIONamespace;
//import com.corundumstudio.socketio.SocketIOServer;
//import com.corundumstudio.socketio.listener.ConnectListener;
//import com.corundumstudio.socketio.listener.DataListener;
//import com.corundumstudio.socketio.listener.DisconnectListener;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class ChatModule {
//
//    private static final Logger log = LoggerFactory.getLogger(ChatModule.class);
//
//    private final SocketIONamespace namespace;
//
//
//    @Autowired
//    public ChatModule(SocketIOServer server) {
//        this.namespace = server.addNamespace("/chat");
//        this.namespace.addConnectListener(onConnected());
//        this.namespace.addDisconnectListener(onDisconnected());
//        this.namespace.addEventListener("chat", ChatMessage.class, onChatReceived());
//        System.out.println("ffffffffffffffffffffffffffff");
//    }
//
//
//    private ConnectListener onConnected() {
//        System.out.println("vvvvvvvvvvvvvv");
//
//        return client -> {
//            HandshakeData handshakeData = client.getHandshakeData();
//            log.info("connected to the servercf");
//            log.debug("Client[{}] - Connected to chat module through '{}'", client.getSessionId().toString(), handshakeData.getUrl());
//        };
//    }
//    private DataListener<ChatMessage> onChatReceived() {
//        System.out.println("jjjjjjjjjjjjjj");
//
//        return (client, data, ackSender) -> {
//            log.debug("Client[{}] - Received chat message '{}'", client.getSessionId().toString(), data);
//            namespace.getBroadcastOperations().sendEvent("chat", data);
//        };
//    }
//    private DisconnectListener onDisconnected() {
//        System.out.println("hkiiiiii");
//
//        return client -> {
//            System.out.println("failed");
//            log.debug("Client[{}] - Disconnected from chat module.", client.getSessionId().toString());
//        };
//    }
//}
