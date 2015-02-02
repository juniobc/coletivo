package com.rod.coletivo.entidade;

import java.util.StringTokenizer;

public class Horario {
	int idhorario;
	int hora_inicio;
	int minuto_inicio;
	int hora_termino;
	int minuto_termino;
	public Horario(){}
	public Horario (String str){
		StringTokenizer tokens = new StringTokenizer(str, ";");
		if(tokens.countTokens() == 5){
			idhorario = Integer.parseInt(tokens.nextToken());
			hora_inicio = Integer.parseInt(tokens.nextToken());
			minuto_inicio = Integer.parseInt(tokens.nextToken());
			hora_termino = Integer.parseInt(tokens.nextToken());
			minuto_termino = Integer.parseInt(tokens.nextToken());			
		}
	}
	public int getIdhorario() {
		return idhorario;
	}
	public void setIdhorario(int idhorario) {
		this.idhorario = idhorario;
	}
	public int getHora_inicio() {
		return hora_inicio;
	}
	public void setHora_inicio(int hora_inicio) {
		this.hora_inicio = hora_inicio;
	}
	public int getMinuto_inicio() {
		return minuto_inicio;
	}
	public void setMinuto_inicio(int minuto_inicio) {
		this.minuto_inicio = minuto_inicio;
	}
	public int getHora_termino() {
		return hora_termino;
	}
	public void setHora_termino(int hora_termino) {
		this.hora_termino = hora_termino;
	}
	public int getMinuto_termino() {
		return minuto_termino;
	}
	public void setMinuto_termino(int minuto_termino) {
		this.minuto_termino = minuto_termino;
	}
	public Horario(int idhorario, int hora_inicio, int minuto_inicio,
			int hora_termino, int minuto_termino) {
		this.idhorario = idhorario;
		this.hora_inicio = hora_inicio;
		this.minuto_inicio = minuto_inicio;
		this.hora_termino = hora_termino;
		this.minuto_termino = minuto_termino;
	}
	public String toString(){
		return String.valueOf(idhorario)+";"+
				String.valueOf(hora_inicio)+";"+
				String.valueOf(minuto_inicio)+";"+
				String.valueOf(hora_termino)+";"+
				String.valueOf(minuto_termino);
	}
}
