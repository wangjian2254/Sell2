package com.wj.sell;

import java.util.regex.Pattern;

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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wj.sell.adapter.OfficeAdapter;
import com.wj.sell.db.OfficeUtil;
import com.wj.sell.db.QiandaoUtil;
import com.wj.sell.db.UserInfoUtil;
import com.wj.sell.db.models.Office;
import com.wj.sell.db.models.UserInfo;
import com.wj.sell.util.Convert;
import com.wj.sell.util.OAUtil;
import com.wj.sell.util.OfficeSync;
import com.wj.sell.util.UrlSync;
import com.wj.sell.util.UrlTask;

public class XiaoShouDetail1 extends ListActivity  {
	
	TextView name;
	TextView time;
	TextView address;
	Button restartGPS;
	LinearLayout imglist;
	ImageView imgview;
	ListView list;
	
	XiaoShouDetail1 con;
	
	
	OfficeAdapter oa=null;
	
	
	private Button   mStartBtn;
	private Handler tmpMainHandler4;
	public ProgressDialog myDialog = null;
	
	public static final int SEARCHPLUGIN = Menu.FIRST + 1;

	private UserInfo user=null;
	private int officeid=-1;
	private Office currentoffice=null;
	private Office btnoffice=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		super.onCreate(savedInstanceState);
        con=this;
        setContentView(R.layout.xiaoshou_detail1);
		Bundle bunde = this.getIntent().getExtras();
		user=UserInfoUtil.getCurrentUserInfo(this);
		list = getListView();
		name = (TextView) findViewById(R.id.notice_title);
		time = (TextView) findViewById(R.id.time);
		address = (TextView) findViewById(R.id.address);
		mStartBtn =(Button)findViewById(R.id.setoffice);
		name.setText("选择厅台");
		officeid=OfficeUtil.getTodayOfficeid(this);
		btnoffice=QiandaoUtil.getOfficeByToday(this);
			oa=new OfficeAdapter(this);
			list.setAdapter(oa);
			oa.getData();
			if(btnoffice!=null){
				mStartBtn.setText("设定厅台："+btnoffice.getName());
				mStartBtn.setVisibility(View.VISIBLE);
				mStartBtn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						setTingTai(btnoffice);
						
					}
				});
			}
		
		
		
		
		
		
			tmpMainHandler4 = new Handler() {
			
			@Override
			public void handleMessage(Message msg) {
				
				// // 接收子线程的消息
				String m=(String)msg.obj;
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
				if(msg.arg1<10){
					if(myDialog!=null&&myDialog.isShowing()){
						myDialog.setMessage(m);
					}
					return;
					
				}
				if(msg.arg1==10){
					if(myDialog!=null&&myDialog.isShowing()){
						myDialog.setMessage(m);
					}
					if(msg.arg2==20){
						oa.getData();
						oa.setGPS();
					}
					if(msg.arg2==30){
						if(myDialog!=null){
							myDialog.dismiss();
						}
						myDialog = ProgressDialog.show  
						    	(
						    			con,
						    		"提示",
						    		m
						    	);
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
	            			msg.arg1=11;
	            			tmpMainHandler4.sendMessage(msg);
	            		}
	            	}.start();
					return;
					
				}
				if(msg.arg1==11){
					if(myDialog!=null&&myDialog.isShowing()){
						myDialog.dismiss();
					}
					myDialog=null;
					return;
					
				}
				
				
				if(msg.arg1==404){
					OAUtil.gotoReLogin(con,m);
					return;
				}
				
			}
			
		};
		syncOffice(false);
		//定位按钮
//		restartGPS.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if (mLocClient != null && mLocClient.isStarted()){
//					setLocationOption();
//					mLocClient.requestLocation();	
//				}				
//				else 
//					Log.d(TAG, "locClient is null or not started");
//				Log.d(TAG, "... mlocBtn onClick... pid="+Process.myPid()+" count="+count++);
//				Log.d(TAG,"version:"+mLocClient.getVersion());
//			}
//		});

		//设置按钮
//		mSetBtn.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				setLocationOption();
//			}
//		});

//		mPoiBtn.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				mLocClient.requestPoi();
//			}
//		});  
		
		//离线基站定位按钮
//		mOfflineBtn.setOnClickListener( new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				int req = mLocClient.requestOfflineLocation();
//				Log.d("req","req:"+req);
//			}
//		});
		
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

	
	protected boolean isNumeric(String str) {   
		Pattern pattern = Pattern.compile("[0-9]*");   
		return pattern.matcher(str).matches();   
	}  
	
	public void setTingTai(Office tingtai){
		currentoffice=tingtai;
		TextView eDeleteW = new TextView(con);
    	LinearLayout.LayoutParams eLayoutParam = new  LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT
    			,LinearLayout.LayoutParams.WRAP_CONTENT);
    	eDeleteW.setPadding(10, 10, 10, 10);
    	eDeleteW.setText("是否将"+"\n厅台："+currentoffice.getName()+"\n作为今天销售的场所？");
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
    	myDialog.setButton("是",
    			new DialogInterface.OnClickListener() {
    		
    		@Override
    		public void onClick(DialogInterface dialog, int which) {
    			dialog.dismiss();
    			OfficeUtil.setTodayOfficeid(con,currentoffice.getId());
    			Intent mainIntent = new Intent(XiaoShouDetail1.this,XiaoShouDetail2.class);
    			Bundle extras=new Bundle();
    	    	extras.putSerializable("user", user);
    	    	extras.putInt("officeid", currentoffice.getId());
    	    	
    			mainIntent.putExtras(extras);
    			XiaoShouDetail1.this.startActivity(mainIntent); 
    			XiaoShouDetail1.this.finish();
    			return;
    		}
    	});
    	myDialog.show();
	}
	
	public void onListItemClick(ListView parent, View v, int position, long id) {
		
		
		setTingTai(oa.getOffice(position));
    	
			
	}
	
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		populateMenu(menu);
	}

	public void populateMenu(Menu menu) {
//		menu.add(Menu.NONE, SEARCHPLUGIN, Menu.NONE, "查找插件");
		menu.add(Menu.NONE, SEARCHPLUGIN, Menu.NONE, "刷新厅台信息");
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
			syncOffice(true);
			
			return true;
		}
		return false;
	}

	public void onResume(){
    	super.onResume();
    	
    	MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}