package br.file;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;

import br.client.model.Client;

public class FileClient {
	
	private Socket s;
	Date date = new Date();
	
	public FileClient(String host, int port, Client client) {
		try {
			s = new Socket(host, port);
			saveFile(client);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	
	private void saveFile(Client client) throws IOException {
		ObjectOutputStream dos = new ObjectOutputStream(s.getOutputStream());
		dos.writeObject(client);
		dos.flush();
		dos.reset();
		
		DataInputStream dis = new DataInputStream(s.getInputStream());
		FileOutputStream fos = new FileOutputStream("files//download"+client.getIp());
		byte[] buffer = new byte[4096];

		int filesize = 15123; // Send file size in separate msg
		int read = 0;
		int totalRead = 0;
		int remaining = filesize;
		while ((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
			totalRead += read;
			remaining -= read;
			System.out.println("Arquivo Transferido");
			fos.write(buffer, 0, read);
		}

		fos.close();
		dis.close();
	}
}
