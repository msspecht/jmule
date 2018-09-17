package br.server.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.client.model.Client;
import br.client.model.ClientHandler;
import br.model.File;
import br.server.model.Server;

public class ServerController {

	private Map<String, Client> mapClient;
	private Server server;

	public ServerController() {
		server = new Server();
		mapClient = new HashMap<>();
	}

	public boolean clientRegister(Client client) {
		try {
			mapClient.put(client.getIp(), client);
			server.setClientMap(mapClient);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public Map<String, Client> getClientRegister() {
		return server.getClientMap();
	}

	public List<Client> getFileHash(File file) {
		List<Client> listClient = new ArrayList<>();
		for (Map.Entry<String, Client> entry : getClientRegister().entrySet()) {
			if (entry.getValue().getListFiles().contains(file)) {
				listClient.add(entry.getValue());
			}
		}
		return listClient;
	}

	public void startServer(int porta) throws IOException, ClassNotFoundException {

		ServerSocket serverSocket = new ServerSocket(porta);
		System.out.println("Servidor iniciado na porta: " + porta);

		while (true) {
			Socket clienteSocket = null;

			try {
				clienteSocket = serverSocket.accept();

				System.out.println("Cliente do IP " + clienteSocket.getInetAddress().getHostAddress() + " conectado.");

				// Recebimento de Objeto
				ObjectInputStream recebeObjeto = new ObjectInputStream(clienteSocket.getInputStream());
				ObjectOutputStream enviaObjeto = new ObjectOutputStream(clienteSocket.getOutputStream());

				Thread t = new ClientHandler(clienteSocket, recebeObjeto, enviaObjeto);

				t.start();

			} catch (IOException ex) {
				serverSocket.close();
				clienteSocket.close();
				Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
			}

		}

	}
}
