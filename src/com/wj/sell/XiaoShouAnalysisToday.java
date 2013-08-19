package com.wj.sell;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.wj.sell.adapter.TodaySellAdapter;
import com.wj.sell.db.UserInfoUtil;
import com.wj.sell.db.XiaoShouUtil;
import com.wj.sell.db.models.UserInfo;
import com.wj.sell.db.models.UserXiaoShouOrder;
import com.wj.sell.util.Convert;

public class XiaoShouAnalysisToday extends ListActivity {
    /** Called when the activity is first created. */
	Context con;
	UserInfo user=null;
	ListView gridApp;
	List<UserXiaoShouOrder> pluginList=new ArrayList<UserXiaoShouOrder>();
	public ProgressDialog myDialog = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        con=this;
        user=UserInfoUtil.getCurrentUserInfo(this);
        setContentView(R.layout.xiaoshouanaylsistoday);
        gridApp=getListView();
        gridApp.setAdapter(new TodaySellAdapter(this, pluginList));// 调用ImageAdapter.java
        
		queryTongji();
        
    }
    

    public void queryTongji(){
    	XiaoShouUtil.getAllXiaoShouToday(con, pluginList);
    	initAppList();
    	
    }
    
    public void initAppList(){
		((TodaySellAdapter) gridApp.getAdapter()).notifyDataSetChanged();
    }
    
    public void onListItemClick(ListView parent, View v, int position, long id) {
		
    	Convert.upo=pluginList.get(position);
		
    	Intent mainIntent = new Intent(this, XiaoShouDetail2.class);
    	Bundle extras=new Bundle();
    	extras.putSerializable("user", user);
    	extras.putInt("officeid", pluginList.get(position).getOfficeid());
    	mainIntent.putExtras(extras);
    	startActivity(mainIntent); 
    	
			
	}
    
    
    public void onResume(){
    	super.onResume();
    	queryTongji();
    }
    public void onPause() {
        super.onPause();
    }
}