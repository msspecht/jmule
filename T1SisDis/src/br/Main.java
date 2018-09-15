package br;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.client.model.Client;
import br.model.File;
import br.server.controller.ServerController;

public class Main {

	public static void main(String[] args) {
		ServerController serverService = new ServerController();
		Client client = new Client();
		Client client2 = new Client();
		File file = new File();
		File file2 = new File();
		File file3 = new File();
		List<File> listFiles = new ArrayList<File>();
		List<File> listFiles2 = new ArrayList<File>();
		
		file.setNome("teste.xml");
		file.setCaminho("C:\teste");
		file.setHash("201039blablablahasshdecuehrola");
		
		file2.setNome("teste2.xml");
		file2.setCaminho("C:\teste2");
		file2.setHash("201039blablablahasshdecuehrola3");
		
		file3.setNome("testes.xml");
		file3.setCaminho("C:\testes");
		file3.setHash("201039blablablahasshdecuehrola2");
		
		listFiles.add(file);
		listFiles2.add(file2);
		listFiles.add(file3);
		
		client.setIp("192.168.0.1");
		client.setListFiles(listFiles);
		
		client2.setIp("10.0.0.1");
		client2.setListFiles(listFiles2);
		
		serverService.clientRegister(client);
		serverService.clientRegister(client2);
		
		System.out.println("Lista de clientes: ");
		
		for (Map.Entry<String, Client> entry : serverService.getClientRegister().entrySet()) {
			
			Client value = entry.getValue();
			
			System.out.println("\nIP "+ value.getIp());
			
			System.out.println("Arquivos: ");
			for (File f: value.getListFiles()) {
				System.out.println(f.getNome());
			}
			
		}
		
		for (Client c : serverService.getFileHash(file2)) {
			System.out.println("\nClient with files: "+c.getIp());
			
			for (File f : c.getListFiles()) {
				System.out.println("Arquivos "+f.getNome()+" HASH "+f.getHash());
			}
		}
		
		System.out.println("------------------\n\nClient: "+client.getIp());
		
		System.out.println("files: "+client.getListFiles());
		
	}
}
