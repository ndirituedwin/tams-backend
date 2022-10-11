package com.cardgame.config.workingsocketio.listeners;

import com.cardgame.config.workingsocketio.ServerBase;
import com.cardgame.config.workingsocketio.interfaces.AddListener;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;

public class ConnectListeners extends ServerBase implements AddListener {

	public ConnectListeners(SocketIOServer server) {
		super(server);
	}

	@Override
	public void addListeners() {
		getServer().addConnectListener(new ConnectListener() {

			@Override
			public void onConnect(SocketIOClient client) {
				System.out.println(String.format("client connected %s", client));
			}
		});
	}

}
