package com.wj.sell;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.os.Vibrator;
import android.util.Log;
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

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.umeng.analytics.MobclickAgent;
import com.wj.sell.adapter.OfficeAdapter;
import com.wj.sell.db.QiandaoUtil;
import com.wj.sell.db.UserInfoUtil;
import com.wj.sell.db.models.Office;
import com.wj.sell.db.models.Qiandao;
import com.wj.sell.db.models.UserInfo;
import com.wj.sell.db.models.UserQiandao;
import com.wj.sell.util.Convert;
import com.wj.sell.util.OAUtil;
import com.wj.sell.util.OfficeSync;
import com.wj.sell.util.UrlSync;
import com.wj.sell.util.UrlTask;
import com.wj.sell.util.UserQiandaoSync;

public class QiandaoDetail2 extends ListActivity  {
	
	TextView name;
	TextView time;
	TextView address;
	Button restartGPS;
	LinearLayout imglist;
	ImageView imgview;
	ListView list;
	
	QiandaoDetail2 con;
	Qiandao qiandao;
	Office office;
	
//	LocationManager lm;  
	
	String TAG="gps";
	OfficeAdapter oa=null;
	
	
	
//	private TextView mTv = null;
//	private EditText mSpanEdit;
//	private EditText mCoorEdit;
//	private CheckBox mGpsCheck;
//	private CheckBox mPriorityCheck;
	private Button   mStartBtn;
//	private Button	 mSetBtn;
//	private Button 	 mLocBtn;
//	private Button 	 mPoiBtn;
//	private Button 	 mOfflineBtn;
//	private CheckBox mIsAddrInfoCheck;
	private boolean  mIsStart;
	private static int count = 1;
	private Vibrator mVibrator01 =null;
	private LocationClient mLocClient;
	private Handler tmpMainHandler4;
	public ProgressDialog myDialog = null;
	
	public static final int SEARCHPLUGIN = Menu.FIRST + 1;

	private UserInfo user=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		super.onCreate(savedInstanceState);
        con=this;
        setContentView(R.layout.qiandao_detail);
		Bundle bunde = this.getIntent().getExtras();
		user=UserInfoUtil.getCurrentUserInfo(this);
		qiandao=(Qiandao)bunde.getSerializable("qiandao");
		list = getListView();
		name = (TextView) findViewById(R.id.notice_title);
		time = (TextView) findViewById(R.id.time);
		address = (TextView) findViewById(R.id.address);
		mStartBtn =(Button)findViewById(R.id.restartGPS);
//		restartGPS.setOnClickListener(new bntOnClickListen());
		name.setText(qiandao.getName());
        
//		mTv = (TextView)findViewById(R.id.textview);
//		mSpanEdit = (EditText)findViewById(R.id.edit);
//		mCoorEdit = (EditText)findViewById(R.id.coorEdit);
//		mGpsCheck = (CheckBox)findViewById(R.id.gpsCheck);
//		mPriorityCheck = (CheckBox)findViewById(R.id.priorityCheck);
//		mStartBtn = (Button)findViewById(R.id.StartBtn);
//		mLocBtn = (Button)findViewById(R.id.locBtn);
//		mSetBtn = (Button)findViewById(R.id.setBtn);       
//		mPoiBtn = (Button)findViewById(R.id.PoiReq);
//		mOfflineBtn  = (Button)findViewById(R.id.offLineBtn);
//		mIsAddrInfoCheck = (CheckBox)findViewById(R.id.isAddrInfocb);
		mIsStart = false;

		mLocClient = ((Location)getApplication()).mLocationClient;
		((Location)getApplication()).time = time;
		((Location)getApplication()).address = address;
		
		mVibrator01 =(Vibrator)getApplication().getSystemService(Service.VIBRATOR_SERVICE);
		((Location)getApplication()).mVibrator01 = mVibrator01;
		oa=new OfficeAdapter(this);
		((Location)getApplication()).oa=oa;
		list.setAdapter(oa);
		oa.getData();
		
		
		
		//开始/停止按钮 
		mStartBtn.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!mIsStart) {
					openGPS();

				} else {
					closeGPS();
				} 
				Log.d(TAG, "... mStartBtn onClick... pid="+Process.myPid()+" count="+count++);
			}
		});
		
			tmpMainHandler4 = new Handler() {
			
			@Override
			public void handleMessage(Message msg) {
				try{
					
				
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
					myDialog.setMessage(m);
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
				
				if(msg.arg1==100){
					if(myDialog!=null&&myDialog.isShowing()){
						myDialog.setMessage(m);
					}
					new Thread() {
	            		public void run() {
	            			try {
								sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	            			Message msg=tmpMainHandler4.obtainMessage();
	            			msg.arg1=110;
	            			tmpMainHandler4.sendMessage(msg);
	            		}
	            	}.start();
				}
				if(msg.arg1==110){
					if(myDialog!=null&&myDialog.isShowing()){
						myDialog.dismiss();
					}
					myDialog=null;
					new Thread() {
						public void run() {
							try {
								sleep(200);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							Message msg=tmpMainHandler4.obtainMessage();
							msg.arg1=120;
							tmpMainHandler4.sendMessage(msg);
						}
					}.start();
				}
				if(msg.arg1==120){
					
					isNeedQiandao();
					return;
					
				}
				if(msg.arg1==404){
					OAUtil.gotoReLogin(con,m);
					return;
				}
				}catch(Exception e){
									
				}
			}
			
		};
		((Location)getApplication()).tmpMainHandler4 = tmpMainHandler4;
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
		isNeedQiandao();
		
	}   
	public void isNeedQiandao(){
		if(qiandao==null){
			return;
		}
		if(qiandao!=null&&!qiandao.isNeedTime()){
			openGPS();
			return;
		}
		String date=Convert.format1.format(new Date());
		if(qiandao.getLastEventDate()==null||!date.substring(0, 10).equals(qiandao.getLastEventDate().substring(0, 10))){
			openGPS();
			return;
		}
		
		
		UserQiandao uqd=QiandaoUtil.getUserQiandao(qiandao.getEventid(),con);
		String scc="";
		try {
			long sc=((new Date()).getTime()-Convert.format1.parse(qiandao.getLastEventDate()).getTime())/1000;
			if(sc<60){
				scc="一分钟内";
			}else if(sc<600){
				scc="十分钟内";
			}else if(sc<3600){
				scc="一小时内";
			}else{
				scc=""+(sc%3600)+"小时前";
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TextView eDeleteW = new TextView(con);
    	LinearLayout.LayoutParams eLayoutParam = new  LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT
    			,LinearLayout.LayoutParams.WRAP_CONTENT);
    	eDeleteW.setPadding(10, 10, 10, 10);
    	eDeleteW.setText(qiandao.getName()+" 签到\n每天只允许一次。\n\n"+scc+" 您已经在\n\n时间："+qiandao.getLastEventDate()+"\n厅台："+qiandao.getLastOfficeName()+"\n地点："+uqd.getAddress()+"\n\n进行了一次签到行为。再次签到，将以新签到数据为准。");
    	eDeleteW.setTextColor(Color.WHITE);
    	eDeleteW.setTextSize(14);
    	eDeleteW.setLayoutParams(eLayoutParam);
    	
    	
    	AlertDialog.Builder builder;
    	builder = new  AlertDialog.Builder(con);
    	AlertDialog myDialog  = builder.create();  
    	myDialog.setMessage(eDeleteW.getText());
    	myDialog.setTitle("提示");
    	myDialog.setCancelable(false);
    	
    	myDialog.setButton2("返回签到列表", new DialogInterface.OnClickListener() {
    		
    		@Override
    		public void onClick(DialogInterface dialog, int which) {
    			dialog.dismiss();
    			con.finish();
    		}
    	});
    	myDialog.setButton("返回主界面",//继续签到
    			new DialogInterface.OnClickListener() {
    		
    		@Override
    		public void onClick(DialogInterface dialog, int which) {
    			dialog.dismiss();
    			Intent mainIntent = new Intent(con, Main.class);
    			con.startActivity(mainIntent);
    			con.finish();
//    			openGPS();
    		}
    	});
    	myDialog.show();
	}
	
	
	public void openGPS(){
			setLocationOption();
			mLocClient.start();
			mStartBtn.setText("停止");
			mIsStart = true;

	}
	public void closeGPS(){
			mLocClient.stop();
			mIsStart = false;
			mStartBtn.setText("获取GPS");
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

	@Override
	public void onDestroy() {
		if(mLocClient!=null){
			mLocClient.stop();
		}
		if(myDialog!=null&&myDialog.isShowing()){
			myDialog.dismiss();
			myDialog=null;
		}
		qiandao=null;
//		((Location)getApplication()).mTv = null;
		super.onDestroy();
	}

	//设置相关参数
	private void setLocationOption(){
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);				//打开gps
		option.setCoorType("bd09ll");		//设置坐标类型
		option.setServiceName("com.baidu.location.service_v2.9");
		option.setPoiExtraInfo(true);	
			option.setAddrType("all");
			option.setScanSpan(3000);	//设置定位模式，小于1秒则一次定位;大于等于1秒则定时定位
		
//		option.setScanSpan(3000);
		
		
			option.setPriority(LocationClientOption.NetWorkFirst);      //设置网络优先
		

		option.setPoiNumber(10);
		option.disableCache(true);		
		mLocClient.setLocOption(option);
	}

	protected boolean isNumeric(String str) {   
		Pattern pattern = Pattern.compile("[0-9]*");   
		return pattern.matcher(str).matches();   
	}  
	public void needGPS(){
    	Message hmsg=tmpMainHandler4.obtainMessage();
		hmsg.obj="";
		hmsg.arg1=1;
		tmpMainHandler4.sendMessage(hmsg);
		hmsg=tmpMainHandler4.obtainMessage();
		hmsg.obj="请先获取GPS信息后，再签到。";
		hmsg.arg1=10;
		tmpMainHandler4.sendMessage(hmsg);
    }
	public void onListItemClick(ListView parent, View v, int position, long id) {
		if(oa.lat1==0.0||oa.lon1==0.0){
			needGPS();
			return;
		}
		
		office=oa.getOffice(position);
		TextView eDeleteW = new TextView(con);
    	LinearLayout.LayoutParams eLayoutParam = new  LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT
    			,LinearLayout.LayoutParams.WRAP_CONTENT);
    	eDeleteW.setPadding(10, 10, 10, 10);
    	eDeleteW.setText("员工："+user.getUsername()+"\n签到："+qiandao.getName()+" \n厅台："+office.getName()+"\n时间："+time.getText().toString()+"\n位置："+address.getText().toString());
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
    	myDialog.setButton("签到",
    			new DialogInterface.OnClickListener() {
    		
    		@Override
    		public void onClick(DialogInterface dialog, int which) {
    			dialog.dismiss();
    			UserQiandaoSync urlSync=new UserQiandaoSync();
    			urlSync.setQiandao(qiandao);
    			urlSync.setOffice(office);
    			urlSync.setMainContext(con);
    			urlSync.setModth(UrlSync.POST);
    			urlSync.setToast(true);
				urlSync.setToastContentSu("签到成功。");
				urlSync.setToastContentFa("签到失败。");
    			urlSync.setUser(user);
    			urlSync.setUri(Convert.hosturl+"/oa/userqiandaoUploadClient/");
    			List<NameValuePair> param=new ArrayList<NameValuePair>();
    	    	param.add(new BasicNameValuePair("qiandaoid",String.valueOf(qiandao.getId())));
    	    	param.add(new BasicNameValuePair("officeid",String.valueOf(office.getId())));
    	    	param.add(new BasicNameValuePair("officename",String.valueOf(office.getName())));
    	    	param.add(new BasicNameValuePair("gps",oa.lat1+","+oa.lon1));
    	    	param.add(new BasicNameValuePair("address",address.getText().toString()));
    	    	urlSync.setPrarm(param);
    			urlSync.setHandler(tmpMainHandler4);
    			UrlTask utk=new UrlTask(con);
    			utk.setUrlSync(urlSync);
    			utk.start();
    			Message msg=tmpMainHandler4.obtainMessage();
    			msg.arg1=1;
    			msg.obj="正在进行签到……";
    			tmpMainHandler4.sendMessage(msg);
    		}
    	});
    	myDialog.show();
    	
			
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