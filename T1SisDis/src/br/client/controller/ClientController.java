package br.client.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import br.client.model.Client;
import br.file.FileClient;
import br.file.FileServer;
import br.util.HashMD5;

public class ClientController {
	final static int TRANSFER_PORT = 3220;
	FileServer fileServer;
	FileClient fileClient;
	
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
			fileServer = new FileServer(TRANSFER_PORT);
			fileServer.start();
			
			System.out.println("\nBem vindo ao JMule - Client");
			
			// Envio de objeto
			ObjectOutputStream enviaObjeto = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream recebeObjeto = new ObjectInputStream(socket.getInputStream());
			Object objetoClient = null;
			int countFile = 0;
			Map<String, Client> map;
			
			enviaObjeto.writeObject(createClient(socket.getLocalAddress().getHostAddress()));
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

					objetoClient = recebeObjeto.readObject();
					if (objetoClient != null && objetoClient instanceof Map) {
						map = (Map<String, Client>) objetoClient;
						if (!map.isEmpty()) {
							for (Map.Entry<String, Client> entry : map.entrySet()) {
								System.out.println("Lista de Recursos -> Cliente: --- " + entry.getValue().getIp());

								for (br.model.File f : entry.getValue().getListFiles()) {
									System.out.println("\tArquivos -> \tPosicao: |"+countFile+"| \tNome: " + f.getNome() + "\tHASH: " + f.getHash());
									countFile ++;
								}
								countFile = 0;
							}
						}
					}
					break;

				case 2:
					if (objetoClient != null && objetoClient instanceof Map) {
						Scanner inputCase = new Scanner(System.in);
						
						System.out.println("Digite o ip do cliente: ");
						String clientId = inputCase.nextLine();
						
						System.out.println("Digite a posicao do arquivo: ");
						int posicaoArquivo = inputCase.nextInt();
						
						map = (Map<String, Client>) objetoClient;
						if (map.get(clientId) != null) {
							fileClient = new FileClient(map.get(clientId).getIp(), TRANSFER_PORT, map.get(clientId).getListFiles().get(posicaoArquivo).getCaminho());
						} else {
							System.out.println("Client nao encontrado");
						}
						
					} else {
						enviaObjeto.writeObject(1);
						enviaObjeto.flush();
						enviaObjeto.reset();
						
						objetoClient = recebeObjeto.readObject();
						map = (Map<String, Client>) objetoClient;
						if (!map.isEmpty()) {
							for (Map.Entry<String, Client> entry : map.entrySet()) {
								System.out.println("Lista de Recursos -> Cliente: --- " + entry.getValue().getIp());

								for (br.model.File f : entry.getValue().getListFiles()) {
									System.out.println("\tArquivos -> \tPosicao: |"+countFile+"| \tNome: " + f.getNome() + "\tHASH: " + f.getHash());
									countFile ++;
								}
								countFile = 0;
							}
						}
					}
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
	
	public Client createClient(String ip) throws NoSuchAlgorithmException, IOException {
		Client newClient = new Client();
		
		newClient.setIp(ip);
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
	
}
