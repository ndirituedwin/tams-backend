package com.cardgame.config.workingsocketio.listeners;

import com.cardgame.config.workingsocketio.interfaces.AddListener;

import java.util.ArrayList;

public class AddListenerToServer {

	public AddListenerToServer(ArrayList<AddListener> listeners) {
		super();
		this.listeners = listeners;
	}

	private ArrayList<AddListener> listeners;

	public void add() {
		for (AddListener listener : this.listeners) {
			listener.addListeners();
		}
	}
}
