package com.rod.util;


public final class ParametrosGlobais {
	public static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
	public static final long MINIMUM_TIME_BETWEEN_UPDATES = 10000; // in Milliseconds
	public static final long MINIMUM_TIME_BETWEEN_UPDATES_ONRESUME = 0; // in Milliseconds
	public static final String ORIGEM_ACTIVITY = "Activity";
	public static final String ORIGEM_SERVICE = "Service";
	public static String device_id;
	public static int intervalo = 30000;//30 segundos
	public static final long intervalo_checa_tabela = 600000;//1hora
	public static int dist = 60;//metros
	public static String arq_log = "coletivo_service.txt";
}
