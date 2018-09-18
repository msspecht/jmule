package br.file;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import br.client.model.Client;
import br.model.File;

public class FileServer extends Thread {

	private ServerSocket ss;
	Date date = new Date();

	public FileServer(int port) {
		try {
			ss = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while (true) {
			try {
				Socket clientSock = ss.accept();
				sendFile(clientSock);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void sendFile(Socket clientSock) throws IOException {
		ObjectInputStream dos = new ObjectInputStream(clientSock.getInputStream());
		DataOutputStream outStreamSendClient = new DataOutputStream(clientSock.getOutputStream());
		FileInputStream fis = null;
		
		Client client;
		try {
			
			client = (Client) dos.readObject();
			
			for(File f: client.getListFiles()) {
				fis = new FileInputStream(f.getCaminho());
				byte[] buffer = new byte[4096];
				
				while (fis.read(buffer) > 0) {
					outStreamSendClient.write(buffer);
				}
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			dos.close();
		}
		
		fis.close();
		dos.close();
		outStreamSendClient.close();
	}
}
