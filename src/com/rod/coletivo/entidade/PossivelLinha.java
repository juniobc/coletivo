package com.rod.coletivo.entidade;

public class PossivelLinha {
	public int idpossivellinha;
	public int idlinha;
	public String numero;
	public String nome;
	public Long datahora;
	public double lat;
	public double lng;
	public int seq;
	public int ida;
	public int cont;
	
	public PossivelLinha(){}
	public PossivelLinha(int idpossivellinha, int idlinha, String numero,
			String nome, long datahora, double lat, double lng, int seq, int ida) {
		super();
		this.idpossivellinha = idpossivellinha;
		this.idlinha = idlinha;
		this.numero = numero;
		this.nome = nome;
		this.datahora = datahora;
		this.lat = lat;
		this.lng = lng;
		this.seq = seq;
		this.ida = ida;
	}
}
