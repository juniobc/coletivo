package com.rod.util;

import java.text.DecimalFormat;

import android.app.ProgressDialog;

public class ObjetosGlobais {
	public static ProgressDialog pd;
	public static Double lat = null;//0.0;
	public static Double lng = null;//0.0;
	public static float precisao = 0;

	//public static Double lat_anterior = null;//0.0;
	//public static Double lng_anterior = null;//0.0;	
	//public static float precisao_anterior = 0;

	public static boolean semaforo = true;
	
	
	//public static Double velocidade = 0.0;
	
	public static boolean latLng_valida = false;
	
	//public static boolean gps_ligado = false;
	
	public static boolean linha_determinada = false;
	
	public static DecimalFormat df = new DecimalFormat("#.##");
	
	//public static Calendar calendar = Calendar.getInstance();
	
	//public static String provider_name = "";
}

