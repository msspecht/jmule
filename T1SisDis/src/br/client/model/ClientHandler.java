package br.client.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import br.server.controller.ServerController;

public class ClientHandler extends Thread {

	final ServerController serverService;
	final ObjectInputStream recebeObjeto;
	final ObjectOutputStream enviaObjeto;
	final Socket s;

	public ClientHandler(Socket s, ObjectInputStream recebeObjeto, ObjectOutputStream enviaObjeto, ServerController serverService) {
		this.s = s;
		this.recebeObjeto = recebeObjeto;
		this.enviaObjeto = enviaObjeto;
		this.serverService = serverService;
	}

	@Override
	public void run() {
			
			int received;
			while (true) {

				try {
					Object objetoRetorno = recebeObjeto.readObject();

					if (objetoRetorno instanceof Integer) {
						received = ((Integer) objetoRetorno).intValue();

						if (received == 0) {
							System.out.println("Cliente " + this.s + " enviou exit...");
							System.out.println("Fechando conexão.");
							this.s.close();
							System.out.println("Conexão Fechada");

							// closing resources
							this.recebeObjeto.close();
							this.enviaObjeto.close();
							break;
						}

						switch (received) {

						case 1:
							enviaObjeto.writeObject(serverService.getClientRegister());
							break;

						case 2:
							
							break;

						default:
							enviaObjeto.writeUTF("Opção invalida");
							break;
						}
					} else {
						Client to = (Client) objetoRetorno;
						if (to != null) {
							serverService.clientRegister(to);
						}
					}

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
//		}
		
	}
}
