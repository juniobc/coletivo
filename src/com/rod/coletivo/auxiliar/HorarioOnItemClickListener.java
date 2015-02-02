package com.rod.coletivo.auxiliar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.rod.util.Alert;
import com.rod.util.Log;
import com.rod.util.ParametrosGlobais;



public class HorarioOnItemClickListener implements OnItemClickListener{
	EditText main_author, main_tittle, main_id;
	TextView item_author, item_tittle;
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		/*Context context = arg1.getContext();
		main_tittle = (EditText)((MainActivity) context ).findViewById(R.id.et_tittle);
		main_author = (EditText)((MainActivity) context ).findViewById(R.id.et_author);
		main_id = (EditText)((MainActivity) context).findViewById(R.id.et_id);
				
		item_tittle = (TextView) arg1.findViewById(R.id.tv_tittle);
		item_author = (TextView) arg1.findViewById(R.id.tv_author);
		
		main_tittle.setText(item_tittle.getText().toString());
		main_author.setText(item_author.getText().toString());
		main_id.setText(item_tittle.getTag().toString());*/
		Log.grava(ParametrosGlobais.arq_log, "onItemClickListener");
		Alert a = new Alert(arg1.getContext());
		a.show();
	}

	
}
