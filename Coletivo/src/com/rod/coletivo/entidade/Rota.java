package com.rod.coletivo.entidade;

public class Rota {
	 
    private int idrota;
    private int idlinha;
    private int seq;
    private double lat;
    private double lng;
    private int ida;
    
    public Rota(){}
	public Rota(int idrota, int idlinha, int seq, double lat, double lng,
			int ida) {
		
		this.idrota = idrota;
		this.idlinha = idlinha;
		this.seq = seq;
		this.lat = lat;
		this.lng = lng;
		this.ida = ida;
	}
	public int getIdrota() {
		return idrota;
	}
	public void setIdrota(int idrota) {
		this.idrota = idrota;
	}
	public int getIdlinha() {
		return idlinha;
	}
	public void setIdlinha(int idlinha) {
		this.idlinha = idlinha;
	}
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public int getIda() {
		return ida;
	}
	public void setIda(int ida) {
		this.ida = ida;
	}
    
}