package com.wj.sell;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import com.wj.sell.adapter.SellAdapter;
import com.wj.sell.db.UserInfoUtil;
import com.wj.sell.db.models.SellItem;
import com.wj.sell.db.models.UserInfo;
import com.wj.sell.util.Convert;
import com.wj.sell.util.OAUtil;
import com.wj.sell.util.UrlSync;
import com.wj.sell.util.UrlTask;
import com.wj.sell.util.XiaoShouTongJiSync;

public class XiaoShouAnalysis2 extends Activity {
    /** Called when the activity is first created. */
	Context con;
	UserInfo user=null;
	ListView gridApp;
	List<SellItem> pluginList=new ArrayList<SellItem>();
	private Handler tmpMainHandler4;
	public ProgressDialog myDialog = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        con=this;
        user=UserInfoUtil.getCurrentUserInfo(this);
        setContentView(R.layout.xiaoshouanaylsis);
        gridApp=(ListView)findViewById(R.id.table_listView);
        gridApp.setAdapter(new SellAdapter(this, pluginList));// 调用ImageAdapter.java
        
        tmpMainHandler4 = new Handler() {
			
			@Override
			public void handleMessage(Message msg) {
				try{
					
					String m=null;
				// // 接收子线程的消息
					try{
						
						m=(String)msg.obj;
					}catch(Exception e){
						
					}
				if(msg.arg1==1){
					if(myDialog!=null){
						myDialog.dismiss();
					}
					myDialog = ProgressDialog.show  
					    	(
					    			con,
					    		"提示",
					    		m
					    	);
					return;
				}
				if(msg.arg1==10){
					if(myDialog!=null&&myDialog.isShowing()){
						myDialog.setMessage(m);
					}
					
					if(myDialog!=null){
						myDialog.dismiss();
					}
					
					myDialog = ProgressDialog.show  
					    	(
					    			con,
					    		"提示",
					    		m
					    	);
					
					new Thread() {
	            		public void run() {
	            			try {
								sleep(2000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	            			Message msg=tmpMainHandler4.obtainMessage();
	            			msg.arg1=12;
	            			tmpMainHandler4.sendMessage(msg);
	            		}
	            	}.start();
					return;
					
				}
				if(msg.arg1==12){
					if(myDialog!=null&&myDialog.isShowing()){
						myDialog.dismiss();
					}
					myDialog=null;
					return;
					
				}
				
				if(msg.arg1==11){
					if(myDialog!=null&&myDialog.isShowing()){
						myDialog.dismiss();
					}
					myDialog=null;
					List<SellItem> l=(List<SellItem>)msg.obj;
					pluginList.clear();
					pluginList.addAll(l);
					initAppList();
					return;
					
				}
				
				
				if(msg.arg1==404){
					OAUtil.gotoReLogin(con,m);
					finish();
					return;
				}
				}catch(Exception e){
					if(myDialog!=null&&myDialog.isShowing()){
						myDialog.dismiss();
					}
							e.printStackTrace();		
				}
			}
			
		};
		
        initAppList();
        Bundle bunde = this.getIntent().getExtras();
        if(bunde!=null){
        	queryTongji(bunde.getString("startdate"),bunde.getString("enddate"),bunde.getStringArrayList("officeids"),bunde.getStringArrayList("userids"));
        }
        
    }
    

    public void queryTongji(String startdate,String enddate,ArrayList<String> off,ArrayList<String> use){
    	XiaoShouTongJiSync urlSync=new XiaoShouTongJiSync();
		urlSync.setMainContext(con);
		urlSync.setModth(UrlSync.POST);
		urlSync.setToast(true);
		urlSync.setToastContentSu("统计成功。");
		urlSync.setToastContentFa("统计失败。");
		urlSync.setUser(user);
		urlSync.setUri(Convert.hosturl+"/oa/userProductOrderClient/");
		List<NameValuePair> param=new ArrayList<NameValuePair>();
		
    	
    	param.add(new BasicNameValuePair("startdate",startdate));
    	param.add(new BasicNameValuePair("enddate",enddate));
    	for(String o:off){
    		param.add(new BasicNameValuePair("officeids",o));
    		
    	}
    	for(String o:use){
    		param.add(new BasicNameValuePair("userids",o));
    		
    	}
    	urlSync.setPrarm(param);
		urlSync.setHandler(tmpMainHandler4);
		UrlTask utk=new UrlTask(con);
		utk.setUrlSync(urlSync);
		utk.start();
		Message msg=tmpMainHandler4.obtainMessage();
		msg.arg1=1;
		msg.obj="正在查询……";
		msg.sendToTarget();
    }
    
    public void initAppList(){
//    	getPluginList();
		((SellAdapter) gridApp.getAdapter()).notifyDataSetChanged();
    }
    
//    private void getPluginList(){
//    	pluginList.clear();
//    	
//    }
    
    public void onResume(){
    	super.onResume();
    }
    public void onPause() {
        super.onPause();
    }
}