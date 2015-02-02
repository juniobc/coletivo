package com.rod.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.os.Environment;

public class Log {
	public static void grava(final String path, final String texto){
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					File sdCard = Environment.getExternalStorageDirectory();
			        File directory = new File (sdCard.getAbsolutePath() + "/coletivo/log");
			        directory.mkdirs();

			        Calendar cal = Calendar.getInstance();
			        Date currentLocalTime = cal.getTime();
			        DateFormat date = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");   
		 
			        String localTime = date.format(currentLocalTime); 
			        
					File myFile = new File(directory, path);
					//if(!myFile.exists())
						myFile.createNewFile();
					FileOutputStream fOut = new FileOutputStream(myFile,true);
					OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
					myOutWriter.append(localTime + " ; " + texto +  '\n');
					myOutWriter.flush();
					myOutWriter.close();
					fOut.close();
					
				} catch (Exception e) {
					
				}
			}}).start();
				
	}

}
