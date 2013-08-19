package com.wj.sell;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


import com.umeng.analytics.MobclickAgent;
import com.wj.sell.adapter.KaoShiAdapter;
import com.wj.sell.db.UserInfoUtil;
import com.wj.sell.db.models.KaoShi;
import com.wj.sell.db.models.UserInfo;
import com.wj.sell.util.Convert;
import com.wj.sell.util.KaoShiSync;
import com.wj.sell.util.OAUtil;
import com.wj.sell.util.OfficeSync;
import com.wj.sell.util.UrlSync;
import com.wj.sell.util.UrlTask;
import com.wj.sell.KaoShiList;
import com.wj.sell.R;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

public class KaoShiList extends ListActivity  {
	
	private TextView nomessage;
	private RadioButton kaoshi; 
	private RadioButton kaoshiscore; 
	ListView list;
	
	KaoShiList con;
	
	
	KaoShiAdapter oa=null;
	List<KaoShi> kaoshiList=new ArrayList<KaoShi>();
	List<KaoShi> kaoshi_nonescoreList=new ArrayList<KaoShi>();
	List<KaoShi> pluginList=new ArrayList<KaoShi>();
	
	private Handler tmpMainHandler4;
	public ProgressDialog myDialog = null;
	
	public static final int SEARCHPLUGIN = Menu.FIRST + 1;

	private int type=0;
	private UserInfo user=null;
	private KaoShi currentoffice=null;
	private int limit=30;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        con=this;
        setContentView(R.layout.kaoshi_search_list);
//		Bundle bunde = this.getIntent().getExtras();
		user=UserInfoUtil.getCurrentUserInfo(this);
		list=(ListView)getListView();
		kaoshi=(RadioButton)findViewById(R.id.rbtn_installall);
		kaoshiscore=(RadioButton)findViewById(R.id.rbtn_install);
//		// set drop down listener 
//		list.setOnDropDownListener(new OnDropDownListener() {
//			
//			@Override
//			public void onDropDown() {
//				// TODO Auto-generated method stub
//				
//			}
//        }); 
        // set on bottom listener 
		
		oa=new KaoShiAdapter(con, pluginList);
		list.setAdapter(oa);
			
		
		
		
		
		
		
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
					Map<String,List<KaoShi>> l=(Map<String,List<KaoShi>>)msg.obj;
					kaoshiList=l.get("kaoshilist");
					kaoshi_nonescoreList=l.get("kaoshilist_nonescore");
					
					initList(true);
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
		syncKind(true);
		
	}   
	
	public void kaoshi1(View view){
		initList(false);
	}
	public void kaoshi2(View view){
		initList(false);
	}
	
	
	public void syncKind(boolean toast){
		UrlSync urlSync=new KaoShiSync();
		urlSync.setMainContext(con);
		urlSync.setModth(UrlSync.POST);
		urlSync.setToast(toast);
		if(toast){
			
			urlSync.setToastContentSu("刷新考试列表成功。");
			urlSync.setToastContentFa("刷新考试列表失败。");
		}
		urlSync.setUser(user);
		urlSync.setUri(Convert.hosturl+"/oa/getMyKaoShi/");
		
		urlSync.setHandler(tmpMainHandler4);
		UrlTask utk=new UrlTask(con);
		utk.setUrlSync(urlSync);
		utk.start();
		if(toast){
			Message msg=tmpMainHandler4.obtainMessage();
			msg.arg1=1;
			msg.obj="正在刷新考试信息……";
			tmpMainHandler4.sendMessage(msg);
		}
	}
	
	
	public void showDocument(int documentid){
		Intent mainIntent = new Intent(con,WenDangDetail.class);
		Bundle extras=new Bundle();
    	extras.putInt("documentid", documentid);
    	mainIntent.putExtras(extras);
		startActivity(mainIntent); 
	}
	
	
	public void syncOffice(boolean toast){
		UrlSync urlSync=new OfficeSync();
		urlSync.setMainContext(con);
		urlSync.setModth(UrlSync.POST);
		urlSync.setToast(toast);
		if(toast){
			
			urlSync.setToastContentSu("刷新厅台列表成功。");
			urlSync.setToastContentFa("刷新厅台列表失败。");
		}
		urlSync.setUser(user);
		urlSync.setUri(Convert.hosturl+"/oa/officeListClient/");
		
		urlSync.setHandler(tmpMainHandler4);
		UrlTask utk=new UrlTask(con);
		utk.setUrlSync(urlSync);
		utk.start();
		if(toast){
			Message msg=tmpMainHandler4.obtainMessage();
			msg.arg1=1;
			msg.obj="正在刷新厅台列表……";
			tmpMainHandler4.sendMessage(msg);
		}
		
	}
	
	public void initList(boolean flag){
		if(flag){
			pluginList.clear();
			if(kaoshi.isChecked()){
				pluginList.addAll(kaoshi_nonescoreList);
			}else{
				pluginList.addAll(kaoshiList);
			}
		}else{
			if(type==1&&kaoshi.isChecked()){
				type=0;
				pluginList.clear();
				if(kaoshi.isChecked()){
					pluginList.addAll(kaoshi_nonescoreList);
				}else{
					pluginList.addAll(kaoshiList);
				}
			}else if(type==0&&kaoshiscore.isChecked()){
				type=1;
				pluginList.clear();
				if(kaoshi.isChecked()){
					pluginList.addAll(kaoshi_nonescoreList);
				}else{
					pluginList.addAll(kaoshiList);
				}
				
			}
		}
		
		oa.notifyDataSetChanged();
    }
	
	

	
	protected boolean isNumeric(String str) {   
		Pattern pattern = Pattern.compile("[0-9]*");   
		return pattern.matcher(str).matches();   
	}  
	
	public void showKaoShi(KaoShi ks,boolean flag){
		currentoffice=ks;
		if(flag==false){
			Intent mainIntent = new Intent(KaoShiList.this,KaoShiDetail.class);
			Bundle extras=new Bundle();
	    	extras.putSerializable("user", user);
	    	extras.putSerializable("examination", currentoffice);
	    	
			mainIntent.putExtras(extras);
			KaoShiList.this.startActivity(mainIntent);
			return;
		}
		
		TextView eDeleteW = new TextView(con);
    	LinearLayout.LayoutParams eLayoutParam = new  LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT
    			,LinearLayout.LayoutParams.WRAP_CONTENT);
    	eDeleteW.setPadding(10, 10, 10, 10);
    	eDeleteW.setText("是否开始"+"\n"+ks.getName()+"\n考试？\n"+"考试时间为：\n"+ks.getTime()+"分钟。");
    	eDeleteW.setTextColor(Color.WHITE);
    	eDeleteW.setTextSize(14);
    	eDeleteW.setLayoutParams(eLayoutParam);
    	
    	
    	AlertDialog.Builder builder;
    	builder = new  AlertDialog.Builder(con);
    	AlertDialog myDialog  = builder.create();  
    	myDialog.setMessage(eDeleteW.getText());
    	myDialog.setTitle("提示");
    	myDialog.setCancelable(false);
    	
    	myDialog.setButton2("取消", new DialogInterface.OnClickListener() {
    		
    		@Override
    		public void onClick(DialogInterface dialog, int which) {
    			dialog.dismiss();
    			
    		}
    	});
    	myDialog.setButton("开始",
    			new DialogInterface.OnClickListener() {
    		
    		@Override
    		public void onClick(DialogInterface dialog, int which) {
    			dialog.dismiss();
    			Intent mainIntent = new Intent(KaoShiList.this,KaoShiDetail.class);
    			Bundle extras=new Bundle();
    	    	extras.putSerializable("user", user);
    	    	extras.putSerializable("examination", currentoffice);
    	    	
    			mainIntent.putExtras(extras);
    			KaoShiList.this.startActivity(mainIntent); 
    			return;
    		}
    	});
    	myDialog.show();
	}
	
	
	public void onListItemClick(ListView parent, View v, int position, long id) {
		
		
		KaoShi wd= oa.getKaoShi(position);
//		if(wd.getType()==0){
			showKaoShi(wd,wd.getType()==0?true:false);
//		}
    	
			
	}
	
	

	
	
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		populateMenu(menu);
	}

	public void populateMenu(Menu menu) {
//		menu.add(Menu.NONE, SEARCHPLUGIN, Menu.NONE, "查找插件");
		menu.add(Menu.NONE, SEARCHPLUGIN, Menu.NONE, "刷新考试列表");
//		menu.add(Menu.NONE, APPLIST, Menu.NONE, "应用列表");
//		menu.add(Menu.NONE, SYSTEM, Menu.NONE, "系统消息");
//		menu.add(Menu.NONE, REFASH, Menu.NONE, "刷新");
//		menu.add(Menu.NONE, ALLREFASH, Menu.NONE, "反馈意见");
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		populateMenu(menu);
		return (super.onCreateOptionsMenu(menu));
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		return (applyMenuChoice(item) || super.onOptionsItemSelected(item));
	}

	public boolean onContextItemSelected(MenuItem item) {
		return (applyMenuChoice(item) || super.onContextItemSelected(item));
	}


	public boolean applyMenuChoice(MenuItem item) {
		switch (item.getItemId()) {
		
		case SEARCHPLUGIN:
			syncKind(true);
			
			return true;
		}
		return false;
	}

	public void onResume(){
    	super.onResume();
    	initList(false);
    	MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}