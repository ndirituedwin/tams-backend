package com.cardgame.config.workingsocketio;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cardgame.Entity.GameRoomTable;
import com.cardgame.Repo.Gameroomtablerepo;
import com.cardgame.config.workingsocketio.interfaces.AddListener;
import com.cardgame.config.workingsocketio.interfaces.ServerType;
import com.cardgame.config.workingsocketio.listeners.*;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
// @Service
// @NoArgsConstructor
// @AllArgsConstructor
@Slf4j
public class SocketIO implements ServerType {

  
    // private SocketIOServer server;
    private SocketIOServer server;
 
    private int port;

    private String ip;
    
    public SocketIO() {
    }
   
    public SocketIO(String ip, int port, List<TableModel> tableModels){
        this.tableModels = tableModels;
        this.port = port;
        this.ip = ip;
    }

//    public static void main(String[] args) throws InterruptedException {
    public static void mainn(String[] args) throws InterruptedException {
        //edited this
        int port = Integer.parseInt(args[0]);
         String ip = args[1];
        /*int port = 4211;
        String ip = "localhost";*/

        //int i, String rowWebsite, int votedThisSession, int maxThreads, int stopAtVotes, int votingDelay,
//		int proxyTimeout
        ArrayList<TableModel> tableModels = new ArrayList<TableModel>(
                Arrays.asList(new TableModel(0, "xyz", 0, 0, 0, 0, 5), new TableModel(1, "efg", 0, 0, 0, 0, 5),new TableModel(1, "mnh", 0, 0, 0, 0, 5)));

        ServerType server = new SocketIO(ip, port, tableModels);
        server.start();
    }

    private List<TableModel> tableModels = new ArrayList<TableModel>();

    @Override
    public void start() {
            Thread t = new Thread(() -> {
            Configuration config = new Configuration();
            config.setOrigin("*");
            config.setHostname(this.ip);
            config.setPort(this.port);
            System.out.println("starting on port: "+port+" and hostname: "+ip);

            this.server = new SocketIOServer(config);

            AddListenerToServer listeners = new AddListenerToServer(new ArrayList<AddListener>(Arrays.asList(
                    new OnDataListener(server), new ConnectListeners(server), new DisconnectListeners(server))));
            listeners.add();

            server.start();

            while (true) {
                String json = new Gson().toJson(this.tableModels);
                // System.out.println(String.format("emitted %s", json));
                server.getBroadcastOperations().sendEvent("tableModel", json);
                server.getBroadcastOperations().sendEvent("stringmessage", "String message from server");

                    server.addEventListener("playersinroomnow", Integer.class, new DataListener<Integer>() {
                        @Override
                        public void onData(SocketIOClient client, Integer data, AckRequest ackSender) throws Exception {
                            // TODO Auto-generated method stub
                            System.out.println("THE DATA ON THE SERVER "+data);
                            System.out.println(String.format("%s %s %s", client, data, ackSender));
                            server.getBroadcastOperations().sendEvent("numberofplayers", data );
                        }
                        
                    });
                    server.addEventListener("playerwithtimer", String.class, new DataListener<String>() {
                        @Override
                        public void onData(SocketIOClient client, String data, AckRequest ackSender) throws Exception {
                            server.getBroadcastOperations().sendEvent("theplayerwithtimer", data );

                        }
                    });


                try {
                    Thread.sleep(5_000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        });
        t.setName("wsserver");
        t.start();
    }

    public static void setTimeout(Runnable runnable, int delay){
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            }
            catch (Exception e){
                System.err.println(e);
            }
        }).start();
    }

    public SocketIOServer getServer() {
        return server;
    }

    public void setServer(SocketIOServer server) {
        this.server = server;
    }

  
    

    
}
