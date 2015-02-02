package com.rod.coletivo;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.rod.coletivo.auxiliar.LinhaAdapter;
import com.rod.coletivo.auxiliar.Retorno;
import com.rod.coletivo.entidade.PossivelLinha;
import com.rod.rede.BuscaBusLinha;
import com.rod.rede.BuscaLinha;
import com.rod.util.ParametrosGlobais;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.TabContentFactory;
  

public class TabsViewPagerFragmentActivity extends FragmentActivity implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener, Retorno {
 
	private TabHost mTabHost;
    private ViewPager mViewPager;
    private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, TabsViewPagerFragmentActivity.TabInfo>();
    private PagerAdapter mPagerAdapter;
    private Fragment currentFragment;
    
    
    private BuscaLinha bbl;
    private ListView et_resultado;
    private EditText et_numero;


    private class TabInfo {
         private String tag;
         private Class<?> clss;
         private Bundle args;
         private Fragment fragment;
         TabInfo(String tag, Class<?> clazz, Bundle args) {
             this.tag = tag;
             this.clss = clazz;
             this.args = args;
         }
 
    }

    class TabFactory implements TabContentFactory {
 
        private final Context mContext;

        public TabFactory(Context context) {
            mContext = context;
        }

        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }
 
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        this.initialiseTabHost(savedInstanceState);
        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); //set the tab as per the saved state
        }

        this.intialiseViewPager();
		
    }
    
    public void buscarLinhas(){
    	
    	currentFragment = mPagerAdapter.getItem(mViewPager.getCurrentItem());
    	
    	et_resultado = (ListView) currentFragment.getView().findViewById(R.id.et_resultado);
    	et_numero = (EditText) currentFragment.getView().findViewById(R.id.et_numero);
    	
		bbl = new BuscaLinha(TabsViewPagerFragmentActivity.this, TabsViewPagerFragmentActivity.this, ParametrosGlobais.ORIGEM_ACTIVITY);
		bbl.execute(new String[]{"-16.7010093", "-49.2284037", "5000"});
		
    }    

    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("tab", mTabHost.getCurrentTabTag()); //save the tab selected
        super.onSaveInstanceState(outState);
    }

    private void intialiseViewPager() {
 
        List<Fragment> fragments = new Vector<Fragment>();
        fragments.add(Fragment.instantiate(this, Tab1Fragment.class.getName()));
        fragments.add(Fragment.instantiate(this, Tab2Fragment.class.getName()));
        fragments.add(Fragment.instantiate(this, Tab3Fragment.class.getName()));
        this.mPagerAdapter  = new PagerAdapter(super.getSupportFragmentManager(), fragments);
        //
        this.mViewPager = (ViewPager)super.findViewById(R.id.viewpager);
        this.mViewPager.setAdapter(this.mPagerAdapter);
        this.mViewPager.setOnPageChangeListener(this);
    }

    private void initialiseTabHost(Bundle args) {
    	
        mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup();
        
        View tabview = createTabView(mTabHost.getContext(), 0);
        View tabview2 = createTabView(mTabHost.getContext(), 1);
        View tabview3 = createTabView(mTabHost.getContext(), 2);
        
        TabInfo tabInfo = null;
        
        TabsViewPagerFragmentActivity.AddTab(this, this.mTabHost, 
        		this.mTabHost.newTabSpec("Tab1").setIndicator(tabview), 
        		( tabInfo = new TabInfo("Tab1", Tab1Fragment.class, args)));        
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        
        TabsViewPagerFragmentActivity.AddTab(this, this.mTabHost, 
        		this.mTabHost.newTabSpec("Tab2").setIndicator(tabview2), 
        		( tabInfo = new TabInfo("Tab2", Tab2Fragment.class, args)));        
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        
        TabsViewPagerFragmentActivity.AddTab(this, this.mTabHost, 
        		this.mTabHost.newTabSpec("Tab3").setIndicator(tabview3), 
        		( tabInfo = new TabInfo("Tab3", Tab3Fragment.class, args)));        
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        
        // Default to first tab
        //this.onTabChanged("Tab1");
        //
        mTabHost.setOnTabChangedListener(this);
    }

    
    private static View createTabView(final Context context, final int tab) {
    	
	    View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg, null);
	    ImageButton img_btn = (ImageButton) view.findViewById(R.id.img_tab);
	    
	    switch(tab){
	    
		    case 0:
		    	img_btn.setImageResource(R.drawable.home);
		    	break;
		    	
		    case 1:
		    	img_btn.setImageResource(R.drawable.mapa);
		    	break;
		    	
		    case 2:
		    	img_btn.setImageResource(R.drawable.config);
		    	break;
	    
	    }

	    return view;
	}

 
    private static void AddTab(TabsViewPagerFragmentActivity activity, TabHost tabHost, TabHost.TabSpec tabSpec, TabInfo tabInfo) {

        tabSpec.setContent(activity.new TabFactory(activity));
        tabHost.addTab(tabSpec);
    }


    public void onTabChanged(String tag) {
        //TabInfo newTab = this.mapTabInfo.get(tag);
        int pos = this.mTabHost.getCurrentTab();
        this.mViewPager.setCurrentItem(pos);
    }
 
    /* (non-Javadoc)
     * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrolled(int, float, int)
     */
    @Override
    public void onPageScrolled(int position, float positionOffset,
            int positionOffsetPixels) {
        // TODO Auto-generated method stub
 
    }
 
    /* (non-Javadoc)
     * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageSelected(int)
     */
    @Override
    public void onPageSelected(int position) {
        // TODO Auto-generated method stub
        this.mTabHost.setCurrentTab(position);
    }
 
    /* (non-Javadoc)
     * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrollStateChanged(int)
     */
    @Override
    public void onPageScrollStateChanged(int state) {
        // TODO Auto-generated method stub
 
    }

	@Override
	public void Trata(Object o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ChecaTabela(Object o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void TrataJson(String str) {
		// TODO Auto-generated method stub  
		
		try {			
			JSONObject json = new JSONObject(str);
			if(json.getInt("erro") == 0){
				JSONArray json_array = json.getJSONArray("linha");	
				PossivelLinha[] possivelLinha = new PossivelLinha[json_array.length()];
				
				for(int i=0;i<json_array.length();i++){
					PossivelLinha pl = new PossivelLinha();
					JSONObject jLinha = json_array.getJSONObject(i);
			        pl.numero = jLinha.getString("numero");
			        pl.nome = jLinha.getString("nome");
			        possivelLinha[i] = pl;
			        
				}

				LinhaAdapter adapter = new LinhaAdapter(currentFragment.getActivity(),R.layout.linha_row,possivelLinha);         
					
				//Toast.makeText(TabsViewPagerFragmentActivity.this, et_numero.getText().toString(), Toast.LENGTH_SHORT).show();
				et_resultado.setAdapter(adapter);
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
 
}
