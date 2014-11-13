package com.rod.util;

import java.util.LinkedHashSet;
import java.util.List;

import com.rod.coletivo.db.MySQLitePossivelLinhaHelper;
import com.rod.coletivo.entidade.PossivelLinha;

public class DeterminaLinha {
	LinkedHashSet<par> histograma = new LinkedHashSet<par>();
	MySQLitePossivelLinhaHelper dbPL;
	List<PossivelLinha> lista;
	public DeterminaLinha(MySQLitePossivelLinhaHelper db){
		this.dbPL = db;
	}
	private void add(int cont){
		histograma.add(new par(cont, 1));
	}
	private par find(int cont){
		for(par p:histograma){
			if(p.cont == cont)
				return p;
		}
		return null;
	}
	private void update(int cont, par p){
		int freq = p.freq;
		histograma.remove(p);
		histograma.add(new par(cont,++freq));		
	}
	private void save(int cont){
		par p = find(cont);
		if(p != null)
			update(cont, p);
		else
			add(cont);
	}
	private List<PossivelLinha> processaDados(){
		List<PossivelLinha> listAll = dbPL.getAllPossivelLinha();
		for(PossivelLinha pl:listAll){
			for(int j=1;j<=2;j++){
				List<PossivelLinha> listLinha = dbPL.getSentidolLinha(pl.idlinha,j);
				int sentido = 0;
				for(PossivelLinha pll:listLinha){
					sentido = pll.seq - Math.abs(sentido);
				}	
				if(sentido < 0){					
					pl.cont -= listLinha.size();
				}				
			}	
			save(pl.cont);
		}
		return listAll;
	}
	public PossivelLinha getLinha(){
		List<PossivelLinha> lista = processaDados();
		Object[] check = histograma.toArray();		
		if(check.length>1){
			//verificacao das frequencias
			/*
			 * p -> primeiro
			 * ip-> indice do primeiro
			 * s -> segundo
			 * is-> indice do segundo
			 * */
			int p=-1,s=-1,ip=-1,is=-1;
			for(int i =0;i<check.length;i++){
				if(((par)check[i]).cont > p){
					s = p;
					is = ip;
					p = ((par)check[i]).cont;
					ip = i;
				}
				else{
					if(((par)check[i]).cont > s){
						s = ((par)check[i]).freq;
						is = i;
					}	
				}
			}
			if(((par)check[ip]).freq == 1){
				if(((par)check[ip]).cont - ((par)check[is]).cont >=5)
					return lista.get(ip);
			}
		}
		else{
			if(check.length == 1){
				if(((par)check[0]).cont >=5)
					return lista.get(0);
			}
		}
		return null;
	}
	class par{
		int cont;
		int freq;
		public par(int cont, int freq){
			this.cont = cont;
			this.freq = freq;
		}
	}
}
