package com.cardgame.config.workingsocketio;

import com.corundumstudio.socketio.SocketIOServer;

public class ServerBase {

	public ServerBase(SocketIOServer server) {
		this.setServer(server);
	}

	public SocketIOServer getServer() {
		return server;
	}

	public void setServer(SocketIOServer server) {
		this.server = server;
	}

	private SocketIOServer server;

}
