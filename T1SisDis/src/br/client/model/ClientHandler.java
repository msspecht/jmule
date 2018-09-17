package br.client.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import br.model.File;
import br.server.controller.ServerController;

public class ClientHandler extends Thread {

	DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd");
	DateFormat fortime = new SimpleDateFormat("hh:mm:ss");
	ServerController serverService;
	final ObjectInputStream recebeObjeto;
	final ObjectOutputStream enviaObjeto;
	final Socket s;

	public ClientHandler(Socket s, ObjectInputStream recebeObjeto, ObjectOutputStream enviaObjeto) {
		this.s = s;
		this.recebeObjeto = recebeObjeto;
		this.enviaObjeto = enviaObjeto;
		serverService = new ServerController();
	}

	@Override
	public void run() {
		String received;
		String toreturn;
		while (true) {
			try {

				// Ask user what he wants
				enviaObjeto.writeUTF("What do you want?[Date | Time]..\n" + "Type Exit to terminate connection.");

				if(recebeObjeto.readObject() instanceof String) {
					// receive the answer from client
					received = recebeObjeto.readUTF();

					if (received.equals("Exit")) {
						System.out.println("Client " + this.s + " sends exit...");
						System.out.println("Closing this connection.");
						this.s.close();
						System.out.println("Connection closed");
						break;
					}
				}
				
				Client to = (Client) recebeObjeto.readObject();
				if (to != null) {
					serverService.clientRegister(to);
					System.out.println("Clientes Registrados: ");
					
					for(Map.Entry<String, Client> entry : serverService.getClientRegister().entrySet()) {
						entry.getValue();
						Client value = entry.getValue();
						System.out.println("Ip: "+value.getIp());
					}
				}
				System.out.println(recebeObjeto.readObject());

//				// creating Date object
//				Date date = new Date();
//
//				// write on output stream based on the
//				// answer from the client
//				switch (received) {
//
//				case "Date":
//					toreturn = fordate.format(date);
//					enviaObjeto.writeUTF(toreturn);
//					break;
//
//				case "Time":
//					toreturn = fortime.format(date);
//					enviaObjeto.writeUTF(toreturn);
//					break;
//
//				default:
//					enviaObjeto.writeUTF("Invalid input");
//					break;
//				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			// closing resources
			this.recebeObjeto.close();
			this.enviaObjeto.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
