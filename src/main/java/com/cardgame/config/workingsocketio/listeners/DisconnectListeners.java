package com.cardgame.config.workingsocketio.listeners;

import com.cardgame.config.workingsocketio.ServerBase;
import com.cardgame.config.workingsocketio.interfaces.AddListener;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ExceptionListener;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

public class DisconnectListeners extends ServerBase implements AddListener {

	public DisconnectListeners(SocketIOServer server) {
		super(server);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void addListeners() {
		getServer().addListeners(new ExceptionListener() {

			@Override
			public void onEventException(Exception e, List<Object> args, SocketIOClient client) {
				System.out.println(String.format("on event exception %s %s %s", e, args, client));
			}

			@Override
			public void onDisconnectException(Exception e, SocketIOClient client) {
				System.out.println(String.format("on disconnect exception %s %s", e, e, client));
			}

			@Override
			public void onConnectException(Exception e, SocketIOClient client) {
				System.out.println(String.format("on connect exception %s %s", e, client));
			}

			@Override
			public void onPingException(Exception e, SocketIOClient client) {
				System.out.println(String.format("on ping exception %s %s", e, client));
			}

			@Override
			public boolean exceptionCaught(ChannelHandlerContext ctx, Throwable e) throws Exception {
				// TODO Auto-generated method stub
				return false;
			}

		});
	}

}
