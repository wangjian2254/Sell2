package com.wj.sell;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wj.sell.adapter.WendangAdapter;
import com.wj.sell.db.UserInfoUtil;
import com.wj.sell.db.models.UserInfo;
import com.wj.sell.db.models.WenDang;
import com.wj.sell.db.models.WenDangContent;
import com.wj.sell.db.models.WenDangImage;
import com.wj.sell.util.Convert;
import com.wj.sell.util.ImageTask;
import com.wj.sell.util.OAUtil;
import com.wj.sell.util.UrlSync;
import com.wj.sell.util.UrlTask;
import com.wj.sell.util.WenDangContentSync;

public class WenDangDetail extends Activity  {
	
	private TextView document_title;
	private TextView document_info;
	private TextView document_content;
	private LinearLayout piclist;
	
	WenDangDetail con;
	
	
	WendangAdapter oa=null;
	List<WenDang> pluginList=new ArrayList<WenDang>();
	List<List<WenDang>> history=new ArrayList<List<WenDang>>();
	List<String> menuHistory=new ArrayList<String>();
	
	private Button   mStartBtn;
	private Handler tmpMainHandler4;
	public ProgressDialog myDialog = null;
	
	public static final int SEARCHPLUGIN = Menu.FIRST + 1;

	private UserInfo user=null;
	private WenDangContent currentwendang=null;
	private int documentid=0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		history.clear();
		menuHistory.clear();
		super.onCreate(savedInstanceState);

        con=this;
        setContentView(R.layout.wendang_detail);
		Bundle bunde = this.getIntent().getExtras();
		documentid=bunde.getInt("documentid");
		user=UserInfoUtil.getCurrentUserInfo(this);
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
		
		document_title=(TextView)findViewById(R.id.document_title);
		document_info=(TextView)findViewById(R.id.document_info);
		document_content=(TextView)findViewById(R.id.document_content);
		piclist=(LinearLayout)findViewById(R.id.piclist);
		
			
		
		
		
		
		
		
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
					currentwendang=(WenDangContent)msg.obj;
					showContent(true);
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
	
	public void syncKind(boolean toast){
		UrlSync urlSync=new WenDangContentSync();
		urlSync.setMainContext(con);
		urlSync.setModth(UrlSync.POST);
		urlSync.setToast(toast);
		if(toast){
			
			urlSync.setToastContentSu("刷新栏目列表成功。");
			urlSync.setToastContentFa("刷新栏目列表失败。");
		}
		urlSync.setUser(user);
		urlSync.setUri(Convert.hosturl+"/oa/getDocumentContent/");
		List<NameValuePair> param=new ArrayList<NameValuePair>();
    	param.add(new BasicNameValuePair("documentid",String.valueOf(documentid)));
    	urlSync.setPrarm(param);
		urlSync.setHandler(tmpMainHandler4);
		UrlTask utk=new UrlTask(con);
		utk.setUrlSync(urlSync);
		utk.start();
		if(toast){
			Message msg=tmpMainHandler4.obtainMessage();
			msg.arg1=1;
			msg.obj="正在刷新栏目信息……";
			tmpMainHandler4.sendMessage(msg);
		}
	}
	
	public void showContent(boolean flag){
		document_title.setText(currentwendang.getTitle());
		document_info.setText("发布于："+currentwendang.getDateTime()+"栏目："+currentwendang.getKindName()+" 访问次数："+currentwendang.getShow()+"次");
		document_content.setText(currentwendang.getContent());
		ImageView iv=null;
		ImageTask it=null;
		for(WenDangImage wdi:currentwendang.getWl()){
			iv=new ImageView(con);
			piclist.addView(iv);
			if(flag){
				Bitmap bm=BitmapFactory.decodeFile(Convert.picPath+wdi.getId()+".jpg");
				try {
					if(bm!=null){
						iv.setImageBitmap(bm);
					}else{
						flag=false;
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					flag=false;
				}
			}
			if(!flag){
				it=new ImageTask(con);
				it.setUri(Convert.hosturl+wdi.getUrl());
				it.setImageView(iv);
				it.setPathName(wdi.getId()+".jpg");
				it.start();
			}
		}
		
		
				
	
	}

	
	
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		populateMenu(menu);
	}

	public void populateMenu(Menu menu) {
//		menu.add(Menu.NONE, SEARCHPLUGIN, Menu.NONE, "查找插件");
		menu.add(Menu.NONE, SEARCHPLUGIN, Menu.NONE, "刷新文档");
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
			showContent(false);
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