package br.client.model;

import java.util.ArrayList;
import java.util.List;

import br.model.File;

public class Client {

	private String ip;
	private List<File> listFiles;
	
	public Client() {
		listFiles = new ArrayList<>();
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public List<File> getListFiles() {
		return listFiles;
	}

	public void setListFiles(List<File> listFiles) {
		this.listFiles = listFiles;
	}
	
}
