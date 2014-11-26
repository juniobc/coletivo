package com.rod.coletivo;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView;
  
/**
 * @author mwho
 *
 */
public class TabsViewPagerFragmentActivity extends FragmentActivity implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
 
	private TabHost mTabHost;
    private ViewPager mViewPager;
    private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, TabsViewPagerFragmentActivity.TabInfo>();
    private PagerAdapter mPagerAdapter;
    /**
     *
     * @author mwho
     * Maintains extrinsic info of a tab's construct
     */
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
    /**
     * A simple factory that returns dummy views to the Tabhost
     * @author mwho
     */
    class TabFactory implements TabContentFactory {
 
        private final Context mContext;
 
        /**
         * @param context
         */
        public TabFactory(Context context) {
            mContext = context;
        }
 
        /** (non-Javadoc)
         * @see android.widget.TabHost.TabContentFactory#createTabContent(java.lang.String)
         */
        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }
 
    }
    /** (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout
        setContentView(R.layout.teste2);
        // Initialise the TabHost
        this.initialiseTabHost(savedInstanceState);
        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); //set the tab as per the saved state
        }
        // Intialise ViewPager
        this.intialiseViewPager();
    }
 
    /** (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os.Bundle)
     */
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("tab", mTabHost.getCurrentTabTag()); //save the tab selected
        super.onSaveInstanceState(outState);
    }
 
    /**
     * Initialise ViewPager
     */
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
 
    /**
     * Initialise the Tab Host
     */
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

 
    /**
     * Add Tab content to the Tabhost
     * @param activity
     * @param tabHost
     * @param tabSpec
     * @param clss
     * @param args
     */
    private static void AddTab(TabsViewPagerFragmentActivity activity, TabHost tabHost, TabHost.TabSpec tabSpec, TabInfo tabInfo) {
        // Attach a Tab view factory to the spec
        tabSpec.setContent(activity.new TabFactory(activity));
        tabHost.addTab(tabSpec);
    }
 
    /** (non-Javadoc)
     * @see android.widget.TabHost.OnTabChangeListener#onTabChanged(java.lang.String)
     */
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
 
}
