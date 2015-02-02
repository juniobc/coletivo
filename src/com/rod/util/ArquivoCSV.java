package com.rod.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;

public class ArquivoCSV {
	File directory;
	public ArquivoCSV(){
		File sdCard = Environment.getExternalStorageDirectory();
		directory = new File (sdCard.getAbsolutePath() + "/coletivo/conf");
		directory.mkdirs();			
	}
	public Boolean write(String fname, String fcontent, boolean append){
		try {
			File file = new File(directory,fname);
			// If file does not exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile(),append);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.append(fcontent+'\n');
			bw.close();

			return true;
		} catch (IOException e) {
			Log.grava(ParametrosGlobais.arq_log, this.getClass().getName()+"[write]->"+e.toString());
			e.printStackTrace();
			return false;
		}
	}
	public List<String> read(String fname){
		BufferedReader br = null;
		List<String> response = new ArrayList<String>();
		try {
			
			br = new BufferedReader(new FileReader(directory.getAbsolutePath()+"/"+fname));
			String line = "";
			while ((line = br.readLine()) != null) {
				response.add(line);
			}
			br.close();
			
		} catch (IOException e) {
			Log.grava(ParametrosGlobais.arq_log, this.getClass().getName()+"[read]->"+e.toString());
			e.printStackTrace();
			//return null;
		}
		return response;
	}
	public void deleteFile(String fname){
		File file = new File(directory,fname);
		// If file does not exists, then create it
		if (file.exists()) {
			file.delete();
		}
	}
	public boolean removeLine(String fname, int idline){
		boolean removed = false;
		List<String> lista = read(fname);
		for(int i=0;i<lista.size();i++){
			String[] l = lista.get(i).split(";");
			if(Integer.parseInt(l[0]) == idline){
				lista.remove(i);
				removed = true;
				break;			
			}
		}
		if(removed){
			deleteFile(fname);
			for(String l:lista)
				this.write(fname,  l, true);
		}
		return removed;
	}
}
