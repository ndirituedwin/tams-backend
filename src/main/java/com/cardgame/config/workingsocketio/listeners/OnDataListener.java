package com.cardgame.config.workingsocketio.listeners;


import com.cardgame.config.workingsocketio.ServerBase;
import com.cardgame.config.workingsocketio.TableModel;
import com.cardgame.config.workingsocketio.interfaces.AddListener;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;

public class OnDataListener extends ServerBase implements AddListener {

	public OnDataListener(SocketIOServer server) {
		super(server);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void addListeners() {
		getServer().addEventListener("nvt", TableModel.class, new DataListener<TableModel>() {
			@Override
			public void onData(SocketIOClient client, TableModel data, AckRequest ackRequest) {
				// broadcast messages to all clients

				System.out.println(String.format("%s %s %s", client, data, ackRequest));
				getServer().getBroadcastOperations().sendEvent("chatevent", data);
			}

		});
	}

}
