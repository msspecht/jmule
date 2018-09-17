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

		System.out.println("Digite a porta do servidor: ");
		Scanner inputPorta = new Scanner(System.in);
		serverService.startServer(inputPorta.nextInt());
		System.exit(0);
	}

	public static void ModoClient() throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
		ClientController clientService = new ClientController();
		clientService.startClient();
		System.out.println("Até logo!");
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
