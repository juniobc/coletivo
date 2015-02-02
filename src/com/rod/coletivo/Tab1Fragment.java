package com.rod.coletivo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
  
/**
 * @author mwho
 *
 */
public class Tab1Fragment extends Fragment {
	
	private ListView et_resultado;
	
    /** (non-Javadoc)
     * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	View view = inflater.inflate(R.layout.tab1, container, false); 	
    	
    	/*et_resultado = (ListView) view.findViewById(R.id.et_resultado);
    	
		bbl = new BuscaLinha(getActivity().getApplication(), Tab1Fragment.this, ParametrosGlobais.ORIGEM_ACTIVITY);
		bbl.execute(new String[]{"-16.7010093", "-49.2284037", "5000"});*/
    	
        if (container == null) {
            // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist.  The fragment
            // may still be created from its saved state, but there is
            // no reason to try to create its view hierarchy because it
            // won't be displayed.  Note this is not needed -- we could
            // just run the code below, where we would create and return
            // the view hierarchy; it would just never be used.
            return null;
        }
        
        return view;
    }
    

}
