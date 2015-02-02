package com.rod.util;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferencias {
	private static SharedPreferences open(Context context, String file){
		return context.getSharedPreferences(file, Context.MODE_PRIVATE);
		
	}
	public static void putBoolean(Context context, String file, String key, boolean value){		
		SharedPreferences.Editor editor = open(context,file).edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	public static boolean getBoolean(Context context, String file, String key){		
		return open(context,file).getBoolean(key, false);
	}
	public static void putInt(Context context, String file, String key, int value){		
		SharedPreferences.Editor editor = open(context,file).edit();
		editor.putInt(key, value);
		editor.commit();
	}
	public static int getInt(Context context, String file, String key){		
		return open(context,file).getInt(key, 1);
	}
}
