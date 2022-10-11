package com.cardgame.config.workingsocketio.listeners;

import com.cardgame.config.workingsocketio.ServerBase;
import com.cardgame.config.workingsocketio.interfaces.AddListener;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DisconnectListener;

public class AddDisconnectListener extends ServerBase implements AddListener {

	public AddDisconnectListener(SocketIOServer server) {
		super(server);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void addListeners() {
		getServer().addDisconnectListener(new DisconnectListener() {

			@Override
			public void onDisconnect(SocketIOClient client) {
				// TODO Auto-generated method stub
				System.out.println(String.format("client disconnected %s", client));
			}
		});
	}

}
