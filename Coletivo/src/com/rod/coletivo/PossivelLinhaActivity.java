package com.rod.coletivo;

import java.util.Calendar;
import java.util.List;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.example.coletivo.R;
import com.rod.coletivo.auxiliar.LinhaAdapter;
import com.rod.coletivo.db.MySQLitePossivelLinhaHelper;
import com.rod.coletivo.entidade.PossivelLinha;

public class PossivelLinhaActivity extends ActionBarActivity {
	MySQLitePossivelLinhaHelper dbPL;
	ListView lv_linha;
	Calendar c;
	Button btn_limpa_banco;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_possivel_linha);
		dbPL = new MySQLitePossivelLinhaHelper(this);
		lv_linha=(ListView) findViewById(R.id.lv_linha);
		c = Calendar.getInstance();
		
		btn_limpa_banco = (Button) findViewById(R.id.btn_limpa_banco);
		btn_limpa_banco.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dbPL.deleteAllPossivelLinha();
				atualiza_lista();
			}
		});
	}

	@Override
	protected void onResume(){
		super.onResume();
		atualiza_lista();
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.possivel_rota, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	public void atualiza_lista(){
		List<PossivelLinha> list = dbPL.getAllPossivelLinha();		
		PossivelLinha[] possivelLinha = new PossivelLinha[list.size()]; 

		
        for(int i=0;i<list.size();i++){
        	c.setTimeInMillis(list.get(i).datahora);
    		possivelLinha[i] = list.get(i);
        	//DateFormat.format("dd-MM-yyyy", list.get(i).datahora).toString() + '\n');
        }
        LinhaAdapter adapter = new LinhaAdapter(this,R.layout.linha_row,possivelLinha);            
        lv_linha.setAdapter(adapter);
	}
}
