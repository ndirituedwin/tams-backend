package com.cardgame;


import com.cardgame.config.SocketIO.ChatObject;
import com.cardgame.config.workingsocketio.SocketIO;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;



import java.net.URISyntaxException;
import java.util.Random;
import java.util.UUID;


@SpringBootApplication
@Slf4j
public class CardGameApplication {
	public static final Logger logger= LoggerFactory.getLogger(CardGameApplication.class);


	public static void main(String[] args) throws URISyntaxException, InterruptedException {
//		Random random=new Random();
//		StringBuilder randomnu=new StringBuilder();
//		for (int i=1;i<9; i++){
//			randomnu.append(random.nextLong(10));
//		}
//		System.out.println(randomnu);
		SpringApplication.run(CardGameApplication.class, args);
		String[] any={"4211","localhost"};
     SocketIO.mainn(any);
	}




	

}
