package com.wj.sell;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wj.sell.db.OfficeUtil;
import com.wj.sell.db.UserInfoUtil;
import com.wj.sell.db.XiaoShouUtil;
import com.wj.sell.db.models.Office;
import com.wj.sell.db.models.UserInfo;
import com.wj.sell.util.Convert;
import com.wj.sell.util.OAUtil;
import com.wj.sell.util.UrlSync;
import com.wj.sell.util.UrlTask;
import com.wj.sell.util.UserXiaoShouSync;

public class XiaoShouDetail2 extends Activity  {

	TextView name;
	TextView officenameText;
	TextView productnameText;
	TextView imieText;
	TextView producttypeText;
	EditText telEdit;
	Button btnSubmit;
	Button btnReSubmit;
	LinearLayout giftlayout;
	ImageView imgview;
	
	XiaoShouDetail2 con;
	
	
	
	
	private Button   mStartBtn;
	private Handler tmpMainHandler4;
	public ProgressDialog myDialog = null;
	
	public static final int SEARCHPLUGIN = Menu.FIRST + 1;

	private UserInfo user=null;
	private int officeid=0;
	private Office currentoffice=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		super.onCreate(savedInstanceState);
        con=this;
        setContentView(R.layout.xiaoshou_detail2);
		Bundle bunde = this.getIntent().getExtras();
//		officeid=bunde.getInt("officeid");
		user=UserInfoUtil.getCurrentUserInfo(this);
		name = (TextView) findViewById(R.id.notice_title);
		mStartBtn =(Button)findViewById(R.id.setoffice);
		btnSubmit=(Button)findViewById(R.id.btnSubmit);
		btnReSubmit=(Button)findViewById(R.id.btnReSubmit);
		mStartBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent mainIntent = new Intent(XiaoShouDetail2.this,XiaoShouDetail1.class);
				Bundle extras=new Bundle();
		    	extras.putSerializable("user", user);
				mainIntent.putExtras(extras);
				startActivity(mainIntent); 
				finish();
				return;
				
			}
		});
		name.setText("销售记录");
		
		officeid=bunde.getInt("officeid");
		if(officeid==0){
			officeid=OfficeUtil.getTodayOfficeid(this);
		}
		if(officeid<1){
			
			Intent mainIntent = new Intent(this,XiaoShouDetail1.class);
			Bundle extras=new Bundle();
	    	extras.putSerializable("user", user);
			mainIntent.putExtras(extras);
			startActivity(mainIntent); 
			finish();
			return;
		}
		currentoffice=OfficeUtil.getTodayOfficeByid(this,officeid);
		
		 officenameText=(TextView)findViewById(R.id.officename);
		 productnameText=(TextView)findViewById(R.id.product);
		 imieText=(TextView)findViewById(R.id.imie);
		 producttypeText=(TextView)findViewById(R.id.producttype);
		 telEdit=(EditText)findViewById(R.id.tel);
		 giftlayout=(LinearLayout)findViewById(R.id.giftlayout);
		
//		 randomProductOrder();
		 
		 setProductOrder();
		
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
					btnReSubmit.setVisibility(View.VISIBLE);
					
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
				if(msg.arg1==12){
					if(myDialog!=null&&myDialog.isShowing()){
						myDialog.setMessage(m);
					}
					btnSubmit.setEnabled(false);
					
					new Thread() {
						public void run() {
							try {
								sleep(2000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							Message msg=tmpMainHandler4.obtainMessage();
							msg.arg1=13;
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
				if(msg.arg1==13){
					if(myDialog!=null&&myDialog.isShowing()){
						myDialog.dismiss();
					}
					myDialog=null;
					finish();
					return;
					
				}
				
				
				if(msg.arg1==404){
					OAUtil.gotoReLogin(con,m);
					finish();
					return;
				}
				
			}catch(Exception e){
				Log.e("xiaohsou2", "销售error");
			}
				
			}
			
		};
		
	}   
	
	public void randomProductOrder(){
		Convert.upo.setProductflag("i9268");
		Convert.upo.setProductname("i9268-三星");
		Convert.upo.setTypeflag("heyue");
		Convert.upo.setTypename("合约");
		Convert.upo.setImie("sdfsdf"+(new Date()).getTime());
		Convert.upo.setTel("13515461321531");
		Convert.upo.setGifts("dianchi,dianchi");
		Convert.upo.setGName("电池,电池");
		Convert.upo.setOfficeid(currentoffice.getId());
		
		
	}
	
	public void savePhone(View view){
		finish();
	}
	
	public void setProductOrder(){
		officenameText.setText("厅台："+currentoffice.getName());
		productnameText.setText("设备："+Convert.upo.getProductname());
		imieText.setText("IMIE："+Convert.upo.getImie());
		producttypeText.setText("类型："+Convert.upo.getTypename());
		telEdit.setText(Convert.upo.getTel());
		giftlayout.removeAllViews();
		TextView tv=null;
		if(Convert.upo.getGiftsname()!=null){
			
			for(String s:Convert.upo.getGiftsname()){
				tv=new TextView(this);
				tv.setGravity(Gravity.CENTER);
				tv.setText("礼物："+s);
				tv.setTextAppearance(con, R.style.xiaoshou2);
				giftlayout.addView(tv);
			}
		}
		if(Convert.upo.isSubmit()){
			btnSubmit.setText("修改记录");
			name.setText("销售记录(修改)");
		}else{
			btnSubmit.setText("提交记录");
			name.setText("销售记录(新增)");
		}
	}
	
	public void gotoCapture(View view){
		if(view.getId()==R.id.cleangift){
			Convert.upo.getGiftsflag().clear();
			Convert.upo.getGiftsname().clear();
			setProductOrder();
			return;
			
		}
		Intent mainIntent=null;
		Bundle extras=new Bundle();
		switch(view.getId()){
		case R.id.setoffice:
			mainIntent = new Intent(this,XiaoShouDetail1.class);
			break;
		case R.id.setproduct:
			mainIntent = new Intent(this,CaptureActivity.class);
			extras.putString("type", "productmode");
			break;
		case R.id.setproducttype:
			mainIntent = new Intent(this,CaptureActivity.class);
			extras.putString("type", "producttype");
			break;
		case R.id.setimie:
			mainIntent = new Intent(this,CaptureActivity.class);
			extras.putString("type", "imie");
			break;
		
		case R.id.addgift:
			mainIntent = new Intent(this,CaptureActivity.class);
			extras.putString("type", "gift");
			break;
		}
		extras.putSerializable("user", user);
		mainIntent.putExtras(extras);
		startActivity(mainIntent); 
		finish();
	}
	
	public boolean checkData(){
		String msg="";
		if(Convert.upo.getProductflag()==null||"".equals(Convert.upo.getProductflag())){
			msg="请扫描 设备信息。";
		}else if(Convert.upo.getTypeflag()==null||"".equals(Convert.upo.getTypeflag())){
			msg="请扫描 设备类型。";
		}else if(Convert.upo.getImie()==null||"".equals(Convert.upo.getImie())){
			msg="请扫描 设备IMIE。";
			
		}else{
			return true;
		}
		TextView eDeleteW = new TextView(this);
    	LinearLayout.LayoutParams eLayoutParam = new  LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT
    			,LinearLayout.LayoutParams.WRAP_CONTENT);
    	eDeleteW.setPadding(10, 10, 10, 10);
    	eDeleteW.setText(msg);
    	eDeleteW.setTextColor(Color.WHITE);
    	eDeleteW.setTextSize(14);
    	eDeleteW.setLayoutParams(eLayoutParam);
    	
    	
    	AlertDialog.Builder builder;
    	builder = new  AlertDialog.Builder(this);
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
    	myDialog.setButton("扫描信息",
    			new DialogInterface.OnClickListener() {
    		
    		@Override
    		public void onClick(DialogInterface dialog, int which) {
    			dialog.dismiss();
    			Intent mainIntent=null;
    			Bundle extras=new Bundle();
    			mainIntent = new Intent(XiaoShouDetail2.this,CaptureActivity.class);
    			extras.putString("type", "producttype");
    			extras.putSerializable("user", user);
    			mainIntent.putExtras(extras);
    			con.startActivity(mainIntent); 
    			con.finish();
    		}
    	});
    	myDialog.show();
    	return false;
	}
	
	public void submintXiaoShou(View view){
		if(!checkData()){
			return;
		}
		String msg="您正在提交的数据为：";
		msg+="\n设备："+Convert.upo.getProductname();
		msg+="\n类型："+Convert.upo.getTypename();
		msg+="\nIMIE:"+Convert.upo.getImie();
		if(Convert.upo.getGiftsname().size()>0){
			msg+="\n礼物:";
			for(String s:Convert.upo.getGiftsname()){
				msg+=s+"、";
			}
		}
		Convert.upo.setTel(telEdit.getText().toString());
		msg+="\n客户手机:"+Convert.upo.getTel();
		msg+="\n厅台:"+currentoffice.getName();
		if(Convert.upo.isSubmit()){
			msg+="\n\n" +"此条记录已经存在于服务器中，提交后将以新数据为准。"+
					"\n\n";
		}
		msg+="\n您是否提交？";
		
		
		TextView eDeleteW = new TextView(this);
    	LinearLayout.LayoutParams eLayoutParam = new  LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT
    			,LinearLayout.LayoutParams.WRAP_CONTENT);
    	eDeleteW.setPadding(10, 10, 10, 10);
    	eDeleteW.setText(msg);
    	eDeleteW.setTextColor(Color.WHITE);
    	eDeleteW.setTextSize(14);
    	eDeleteW.setLayoutParams(eLayoutParam);
    	
    	
    	AlertDialog.Builder builder;
    	builder = new  AlertDialog.Builder(this);
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
    	myDialog.setButton("提交",
    			new DialogInterface.OnClickListener() {
    		
    		@Override
    		public void onClick(DialogInterface dialog, int which) {
    			dialog.dismiss();
				UserXiaoShouSync sync=new UserXiaoShouSync();
				sync.setMainContext(con);
				sync.setModth(UrlSync.POST);
				sync.setToast(true);
				sync.setToastContentSu("提交销售记录成功。");
				sync.setToastContentFa("提交销售记录失败。");
				sync.setUser(user);
				sync.setUri(Convert.hosturl+"/oa/userXiaoShouOrderUpdate/");
				List<NameValuePair> param=new ArrayList<NameValuePair>();
				if(Convert.upo.getServerid()>0){
					param.add(new BasicNameValuePair("id",String.valueOf(Convert.upo.getServerid())));
				}
    	    	Convert.upo.setOfficeid(currentoffice.getId());
    	    	param.add(new BasicNameValuePair("Office",String.valueOf(currentoffice.getId())));
    	    	param.add(new BasicNameValuePair("ProductModel",Convert.upo.getProductflag()));
    	    	param.add(new BasicNameValuePair("ProductType",Convert.upo.getTypeflag()));
    	    	for(String s:Convert.upo.getGiftsflag()){
    	    		if(!"".equals(s)){
    	    			param.add(new BasicNameValuePair("Gift",s));
    	    		}
    	    	}
    	    	param.add(new BasicNameValuePair("imie",Convert.upo.getImie()));
    	    	param.add(new BasicNameValuePair("tel",Convert.upo.getTel()));
    	    	param.add(new BasicNameValuePair("order",UUID.randomUUID().toString()));
    	    	Convert.upo.setClientdate(Convert.format1.format(new Date()).substring(0, 10));
    	    	Convert.upo.setClienttime(Convert.format1.format(new Date()).substring(11, 16));
    	    	param.add(new BasicNameValuePair("clientDate",Convert.upo.getClientdate()));
    	    	param.add(new BasicNameValuePair("clientTime",Convert.upo.getClienttime()));
    	    	sync.setPrarm(param);
    	    	sync.setUxso(Convert.upo);
    	    	XiaoShouUtil.updateUserXiaoShouOrder(Convert.upo, con);
				sync.setHandler(tmpMainHandler4);
				UrlTask utk=new UrlTask(con);
				utk.setUrlSync(sync);
				utk.start();
				Message msg=tmpMainHandler4.obtainMessage();
				msg.arg1=1;
				msg.obj="提交销售记录……";
				tmpMainHandler4.sendMessage(msg);
    		}
    	});
    	myDialog.show();
	}

	
	
	
//	public void onCreateContextMenu(ContextMenu menu, View v,
//			ContextMenu.ContextMenuInfo menuInfo) {
//		populateMenu(menu);
//	}
//
//	public void populateMenu(Menu menu) {
////		menu.add(Menu.NONE, SEARCHPLUGIN, Menu.NONE, "查找插件");
//		menu.add(Menu.NONE, SEARCHPLUGIN, Menu.NONE, "刷新厅台信息");
////		menu.add(Menu.NONE, APPLIST, Menu.NONE, "应用列表");
////		menu.add(Menu.NONE, SYSTEM, Menu.NONE, "系统消息");
////		menu.add(Menu.NONE, REFASH, Menu.NONE, "刷新");
////		menu.add(Menu.NONE, ALLREFASH, Menu.NONE, "反馈意见");
//	}
//
//	public boolean onCreateOptionsMenu(Menu menu) {
//		populateMenu(menu);
//		return (super.onCreateOptionsMenu(menu));
//	}
//
//	public boolean onOptionsItemSelected(MenuItem item) {
//		return (applyMenuChoice(item) || super.onOptionsItemSelected(item));
//	}
//
//	public boolean onContextItemSelected(MenuItem item) {
//		return (applyMenuChoice(item) || super.onContextItemSelected(item));
//	}
//
//
//	public boolean applyMenuChoice(MenuItem item) {
//		switch (item.getItemId()) {
//		
//		case SEARCHPLUGIN:
//			
//			return true;
//		}
//		return false;
//	}

	public void onResume(){
    	super.onResume();
    	
    	MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}