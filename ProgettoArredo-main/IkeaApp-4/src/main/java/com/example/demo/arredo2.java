package com.example.demo;

public class arredo2 {
	
	String nome;
	String marca;
	double prezzo;
	String url;
	int pezzi;
	
	public arredo2() {
		
	}
	public arredo2(String nome, String marca, double prezzo, String url, int pezzi) {
		super();
		this.nome = nome;
		this.marca = marca;
		this.prezzo = prezzo;
		this.url = url;
		this.pezzi = pezzi;
	}
	public int getPezzi() {
		return pezzi;
	}
	public void setPezzi(int pezzi) {
		this.pezzi = pezzi;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getMarca() {
		return marca;
	}
	public void setMarca(String marca) {
		this.marca = marca;
	}
	public double getPrezzo() {
		return prezzo;
	}
	public void setPrezzo(double prezzo) {
		this.prezzo = prezzo;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

}
