package com.rod.coletivo.auxiliar;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.coletivo.R;
import com.rod.coletivo.entidade.PossivelLinha;

public class LinhaAdapter extends ArrayAdapter<PossivelLinha> {
	Context context;
	int resource;
	PossivelLinha data[] = null;
	public LinhaAdapter(Context context, int resource, PossivelLinha[] objects) {
		super(context, resource, objects);
		this.context = context;
		this.resource = resource;
		this.data = objects;
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			convertView = inflater.inflate(resource, parent,false);
		}
		PossivelLinha object = data[position];
		
		TextView tv_nome = (TextView) convertView.findViewById(R.id.tv_nome);
		tv_nome.setText(object.nome);
		//tv_nome.setTag(object.getId());

		TextView tv_numero = (TextView) convertView.findViewById(R.id.tv_numero);
		tv_numero.setText(object.numero);

		TextView tv_cont = (TextView) convertView.findViewById(R.id.tv_cont);
		tv_cont.setText(String.valueOf(object.cont));
		
		return convertView;
	}

}
