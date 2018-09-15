package br.server.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.client.model.Client;
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
			if(entry.getValue().getListFiles().contains(file)) {
				listClient.add(entry.getValue());
			}
		}
		return listClient;
	}
	
	public void startServer(int porta) throws IOException, ClassNotFoundException {
		ServerSocket serverSocket = null;
		PrintStream enviaCliente = null;
		Scanner retornoCliente;
		
		try {
			serverSocket = new ServerSocket(porta);
            System.out.println("Servidor iniciado na porta: "+porta);
            
            while(true) {
            	Socket clienteSocket = serverSocket.accept();
            	
            	try {
            		System.out.println("Cliente do IP "+clienteSocket.getInetAddress().getHostAddress()+" conectado.");
            		
            		// Recebimento de Objeto
            		ObjectInputStream ois = new ObjectInputStream(clienteSocket.getInputStream());
            		Client to = (Client) ois.readObject();
            		if (to!=null){
            			System.out.println(to.getIp());
            		}
            		System.out.println((String)ois.readObject());
            		
            		// Bloco de envio e recebimento de mensagens
//            		retornoCliente = new Scanner(clienteSocket.getInputStream());
//            		enviaCliente = new PrintStream(clienteSocket.getOutputStream(), true);
//                    
//                    while(retornoCliente.hasNextLine()){
//                        System.out.println(retornoCliente.nextLine());
//                        enviaCliente.println("dsadsad");
//                    }
                    
            	} finally {
            		clienteSocket.close();
				}
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
        	serverSocket.close();
		}
	}
}
