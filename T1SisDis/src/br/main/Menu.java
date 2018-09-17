package br.main;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import br.client.controller.ClientController;
import br.server.controller.ServerController;

public class Menu {

	public static void Menu() {
		System.out.println("\tJMule");
		System.out.println("0 - Fim");
		System.out.println("1 - Modo Servidor");
		System.out.println("2 - Modo Client");
		System.out.println("Opcao:");
	}

	@SuppressWarnings("resource")
	public static void ModoServidor() throws IOException, ClassNotFoundException {
		ServerController serverService = new ServerController();
		int opcao;
		
		do {
			System.out.println("Digite a porta do servidor: ");
			Scanner inputPorta = new Scanner(System.in);
			serverService.startServer(inputPorta.nextInt());
			
			Scanner inputOpcao = new Scanner(System.in);
			
			opcao = inputOpcao.nextInt();
		}
		while (opcao != 0);
		
//		ServerSocket listener = new ServerSocket(9090);
//        try {
//            while (true) {
//                Socket socket = listener.accept();
//                try {
//                    PrintWriter out =
//                        new PrintWriter(socket.getOutputStream(), true);
//                    out.println(new Date().toString());
//                } finally {
//                    socket.close();
//                }
//            }
//        }
//        finally {
//            listener.close();
//        }
		
		System.out.println("Você entrou no método Inclui.");
	}

	public static void ModoClient() throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
		ClientController clientService = new ClientController();
		clientService.startClient();
		System.out.println("Você entrou no método Altera.");
		System.exit(0);
	}

	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException, NoSuchAlgorithmException, ClassNotFoundException {
		int opcao;
		Scanner entrada = new Scanner(System.in);

		Menu();
		opcao = entrada.nextInt();

		switch (opcao) {
		case 1:
			ModoServidor();
			break;

		case 2:
			ModoClient();
			break;

		default:
			System.out.println("Opção inválida.");
		}
	}
}
