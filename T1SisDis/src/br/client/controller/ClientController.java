package br.client.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.plaf.synth.SynthSeparatorUI;

import br.client.model.Client;
import br.util.HashMD5;

public class ClientController {

	@SuppressWarnings("resource")
	public void startClient() throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
		
		System.out.println("Digite o endereco do servidor: ");
		Scanner ipServidor = new Scanner(System.in);
		
		System.out.println("Digite a porta do servidor: ");
		Scanner porta = new Scanner(System.in);
		
		Socket socket;
		
		try {
					
			socket = new Socket(ipServidor.nextLine(), porta.nextInt());
			
			// Envio de objeto
			ObjectOutputStream enviaObjeto = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream recebeObjeto = new ObjectInputStream(socket.getInputStream());
			
			
			Scanner teclado = new Scanner(System.in);
			
			while (true) {
				System.out.println("Digite Exit para sair!");
				
				enviaObjeto.writeObject(createClient());
				enviaObjeto.flush();
				
				if (teclado.nextLine().equalsIgnoreCase("Exit")) {
					System.out.println("Fechando conexões");
					socket.close();
					System.out.println("Conexão fechada");
					break;
				}
				
//				Client clienteEnviar = new Client();
//				clienteEnviar = createClient();
//				enviaObjeto.writeObject(clienteEnviar);
				
//				enviaObjeto.writeObject(createClient());
//				enviaObjeto.flush();
				
//				Client to = (Client) recebeObjeto.readObject();
//				if (to != null) {
//					System.out.println(to.getIp());
//				}

			}
			
			// Envio de mensagem
//			PrintStream enviaServidor = new PrintStream(socket.getOutputStream(), true);
//			Scanner retornoServer = new Scanner(socket.getInputStream());
//			Scanner teclado = new Scanner(System.in);
//			while (teclado.hasNextLine()) {
//				enviaServidor.println(teclado.nextLine());
//				System.out.println("Recebido servidor: "+retornoServer.next());
//				showResouces();
//			}

			teclado.close();
			enviaObjeto.close();
			recebeObjeto.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		System.exit(0);
	}
	
	public Client createClient() throws NoSuchAlgorithmException, IOException {
		Client newClient = new Client();
		
		newClient.setIp(InetAddress.getLocalHost().getHostAddress());
		newClient.setListFiles(showResouces());
		
		return newClient;
	}
	
	public List<br.model.File> showResouces() throws IOException, NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		List<br.model.File> listLocalFiles = new ArrayList<>();
		br.model.File newFile = null;
		String digest = "";
		
		File file = new File(".//files//");
		File afile[] = file.listFiles();
		
		for (File arquivo : afile) {
			newFile = new br.model.File();
			newFile.setNome(arquivo.getName());
			newFile.setCaminho(arquivo.getAbsolutePath());
			
			digest = HashMD5.getDigest(new FileInputStream(newFile.getCaminho()), md, 2048);
			newFile.setHash(digest);
			
			listLocalFiles.add(newFile);
			
			System.out.println("MD5 Digest:" + digest);
			System.out.println(arquivo.getName());
		}

		return listLocalFiles;
	}
}
