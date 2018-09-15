package br.server.model;

import java.util.HashMap;
import java.util.Map;

import br.client.model.Client;

public class Server {

	private Map<String, Client> clientMap;
	
	public Server() {
		clientMap = new HashMap<>();
	}

	public Map<String, Client> getClientMap() {
		return clientMap;
	}

	public void setClientMap(Map<String, Client> clientMap) {
		this.clientMap = clientMap;
	}
	
}
