package br.model;

public class File {

	private String caminho;
	private String nome;
	private String hash;
	
	public File() {
		super();
	}
	
	public String getCaminho() {
		return caminho;
	}
	
	public void setCaminho(String caminho) {
		this.caminho = caminho;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getHash() {
		return hash;
	}
	
	public void setHash(String hash) {
		this.hash = hash;
	}
	
	@Override
	public boolean equals(Object obj) {
		return (this.hash.equals(((File) obj).getHash()));
	}
}