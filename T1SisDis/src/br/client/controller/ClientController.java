package br.client.controller;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.client.model.Client;
import br.server.model.Server;
import br.util.HashMD5;

public class ClientController {
	final static int TRANSFER_PORT = 3220;
	
	public static void menu() {
		System.out.println("\n-----------------------------");
		System.out.println("\tMenu");
		System.out.println("0 - Fim");
		System.out.println("1 - Listar recursos");
		System.out.println("2 - Baixar arquivos");
		System.out.println("Opcao:");
	}

	@SuppressWarnings("resource")
	public void startClient() throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
		
		Scanner readInput = new Scanner(System.in);
		
		System.out.println("Digite o endereco do servidor: ");
		String ipServidor = readInput.nextLine();
		
		System.out.println("Digite a porta do servidor: ");
		int porta = readInput.nextInt();
		
		Socket socket;
		
		try {
			socket = new Socket(ipServidor, porta);
			
			// Escutando para receber arquivos
			listenTransferFile();
			
			System.out.println("\nBem vindo ao JMule - Client");
			
			// Envio de objeto
			ObjectOutputStream enviaObjeto = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream recebeObjeto = new ObjectInputStream(socket.getInputStream());
			Map<String, Client> map;
			
			enviaObjeto.writeObject(createClient());
			enviaObjeto.flush();
			enviaObjeto.reset();
			
			int opcao;
			Scanner entrada = new Scanner(System.in);

			do {

				menu();
				opcao = entrada.nextInt();

				switch (opcao) {

				case 0:
					enviaObjeto.writeObject(opcao);
					enviaObjeto.flush();
					enviaObjeto.reset();

					System.out.println("Fechando conexões");
					socket.close();
					System.out.println("Conexão fechada");
					break;

				case 1:
					enviaObjeto.writeObject(opcao);
					enviaObjeto.flush();
					enviaObjeto.reset();

					Object objetoClient = recebeObjeto.readObject();
					if (objetoClient != null && objetoClient instanceof Map) {
						map = (Map<String, Client>) objetoClient;
						if (!map.isEmpty()) {
							for (Map.Entry<String, Client> entry : map.entrySet()) {
								System.out.println("Lista de Recursos -> Cliente: --- " + entry.getValue().getIp());

								for (br.model.File f : entry.getValue().getListFiles()) {
									System.out
											.println("\tArquivos -> \tNome: " + f.getNome() + "\tHASH: " + f.getHash());
								}
							}
						}
					}
					break;

				case 2:
					
					break;

				default:
					System.out.println("Opção inválida.");
				}
			} while (opcao != 0);

			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
		}

		return listLocalFiles;
	}
	
	private void listenTransferFile() {
		ServerSocket serverSocket;
		try {
			
			serverSocket = new ServerSocket(TRANSFER_PORT);
			Socket clienteSocket = null;
			System.out.println("Servidor iniciado na porta: " + TRANSFER_PORT);
			
			while (true) {

				try {
					clienteSocket = serverSocket.accept();

					System.out.println("Cliente do IP " + clienteSocket.getInetAddress().getHostAddress() + " conectado.");
					
					saveFile(clienteSocket);

//					// Recebimento de Objeto
//					ObjectInputStream recebeObjeto = new ObjectInputStream(clienteSocket.getInputStream());
//					ObjectOutputStream enviaObjeto = new ObjectOutputStream(clienteSocket.getOutputStream());
//					
//					Thread t = new ClientHandler(clienteSocket, recebeObjeto, enviaObjeto, this);
//
//					t.start();

				} catch (IOException ex) {
					clienteSocket.close();
					Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
				}

			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void saveFile(Socket clienteSocket) throws IOException {
		DataInputStream dis = new DataInputStream(clienteSocket.getInputStream());
		FileOutputStream fos = new FileOutputStream("");
		byte[] buffer = new byte[4096];

		int filesize = 15123; // Send file size in separate msg
		int read = 0;
		int totalRead = 0;
		int remaining = filesize;
		while ((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
			totalRead += read;
			remaining -= read;
			System.out.println("read " + totalRead + " bytes.");
			fos.write(buffer, 0, read);
		}

		fos.close();
		dis.close();
	}
}
