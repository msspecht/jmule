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
import java.util.Map;
import java.util.Scanner;

import javax.swing.plaf.synth.SynthSeparatorUI;

import br.client.model.Client;
import br.util.HashMD5;

public class ClientController {
	
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
								System.out.println("Lista retorno -> Cliente: --- " + entry.getValue().getIp());
								
								for (br.model.File f : entry.getValue().getListFiles()) {
									System.out.println("\tArquivos -> \tNome: "+f.getNome()+"\tHASH: "+f.getHash());
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
			} while(opcao!=0);
			
			
//			Scanner teclado = new Scanner(System.in);
//
//			System.out.println("Digite Exit para sair!");
//
//			while (teclado.hasNextLine()) {
//				String toSend = teclado.nextLine();
//
//				if (toSend.equalsIgnoreCase("Exit")) {
//					// Envia Objeto (String)
//					enviaObjeto.writeObject(toSend);
//					enviaObjeto.flush();
//					enviaObjeto.reset();
//
//					System.out.println("Fechando conexões");
//					socket.close();
//					System.out.println("Conexão fechada");
//					break;
//				}
//
//				// Envia Objeto
//				enviaObjeto.writeObject(createClient());
//				enviaObjeto.flush();
//				enviaObjeto.reset();
//				System.out.println("Cliente Enviou Objeto");
//
//				// Recebe Objeto do tipo Map
//				Object objetoClient = recebeObjeto.readObject();
//				if (objetoClient != null && objetoClient instanceof Map) {
//					map = (Map<String, Client>) objetoClient;
//					if (!map.isEmpty()) {
//						for (Map.Entry<String, Client> entry : map.entrySet()) {
//							System.out.println("Lista retorno: --- " + entry.getValue().getIp());
//
//						}
//					}
//				}
//
//			}
//			teclado.close();
//			enviaObjeto.close();
//			recebeObjeto.close();

			
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
}
