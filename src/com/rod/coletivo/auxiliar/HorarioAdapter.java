package com.rod.coletivo.auxiliar;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rod.coletivo.R;
import com.rod.coletivo.entidade.Horario;
import com.rod.util.Log;
import com.rod.util.ParametrosGlobais;

public class HorarioAdapter extends ArrayAdapter<Horario> {
	Context context;
	int resource;
	List<Horario> data = null;
	public List<Integer> checkeds = new ArrayList<Integer>();
	public HorarioAdapter(Context context, int resource, List<Horario> objects) {
		super(context, resource, objects);
		this.context = context;
		this.resource = resource;
		this.data = objects;
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		/*if(convertView == null){
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			convertView = inflater.inflate(resource, parent,false);
		}
		LinearLayout root = (LinearLayout) convertView.findViewById(R.id.row);
		if((position % 2) == 0){
			root.setBackgroundColor(convertView.getResources().getColor(R.color.SkyBlue));
		}	
		else{
			root.setBackgroundColor(convertView.getResources().getColor(R.color.white));
		}	
		Horario object = data.get(position);
		
		CheckBox chk_idhorario = (CheckBox) convertView.findViewById(R.id.chk_idhorario);
		chk_idhorario.setTag(object.getIdhorario());*/
		
		
		/*chk_idhorario.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.grava(ParametrosGlobais.arq_log, "clicou no checkbox");
			}
		});*/
		//tv_nome.setTag(object.getId());
		
		
		

		/*TextView tv_horario_inicio = (TextView) convertView.findViewById(R.id.tv_horario_inicio);
		tv_horario_inicio.setText(String.format("%02d",object.getHora_inicio())+":"+String.format("%02d",object.getMinuto_inicio()));

		TextView tv_horario_termino = (TextView) convertView.findViewById(R.id.tv_horario_termino);
		tv_horario_termino.setText(String.format("%02d",object.getHora_termino())+":"+String.format("%02d",object.getMinuto_termino()));*/

		return convertView;
	}

}
