package com.rod.coletivo.entidade;

import java.util.StringTokenizer;

public class Linha {
	int idlinha;
	String numero;
	String nome;
	public Linha(){}
	public int getIdlinha() {
		return idlinha;
	}
	public void setIdlinha(int idlinha) {
		this.idlinha = idlinha;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Linha (String str){
		StringTokenizer tokens = new StringTokenizer(str, ";");
		if(tokens.countTokens() == 3){
			idlinha = Integer.parseInt(tokens.nextToken());
			numero = tokens.nextToken();
			nome = tokens.nextToken();
		}
	}
	public Linha(int idlinha, String numero, String nome) {
		this.idlinha = idlinha;
		this.numero = numero;
		this.nome = nome;
	}
	public String toString(){
		return idlinha+";"+numero+";"+nome;		
	}
}
