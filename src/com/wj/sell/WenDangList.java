package com.wj.sell;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.umeng.analytics.MobclickAgent;
import com.wj.sell.adapter.WendangAdapter;
import com.wj.sell.controls.DropDownListView;
import com.wj.sell.db.OfficeUtil;
import com.wj.sell.db.UserInfoUtil;
import com.wj.sell.db.models.Office;
import com.wj.sell.db.models.UserInfo;
import com.wj.sell.db.models.WenDang;
import com.wj.sell.util.Convert;
import com.wj.sell.util.KindSync;
import com.wj.sell.util.OAUtil;
import com.wj.sell.util.OfficeSync;
import com.wj.sell.util.UrlSync;
import com.wj.sell.util.UrlTask;
import com.wj.sell.util.WenDangSync;
import com.wj.sell.WenDangList;
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
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class WenDangList extends ListActivity  {
	
	private EditText wendang_search_text;
	DropDownListView list;
	
	WenDangList con;
	
	
	WendangAdapter oa=null;
	List<WenDang> pluginList=new ArrayList<WenDang>();
	List<List<WenDang>> history=new ArrayList<List<WenDang>>();
	List<String> menuHistory=new ArrayList<String>();
	
	private Handler tmpMainHandler4;
	public ProgressDialog myDialog = null;
	
	public static final int SEARCHPLUGIN = Menu.FIRST + 1;

	private UserInfo user=null;
	private Office currentoffice=null;
	private int limit=30;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		history.clear();
		menuHistory.clear();
		super.onCreate(savedInstanceState);

        con=this;
        setContentView(R.layout.wendang_search_list);
//		Bundle bunde = this.getIntent().getExtras();
		user=UserInfoUtil.getCurrentUserInfo(this);
		list=(DropDownListView)getListView();
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
		list.setOnBottomListener(new OnClickListener() { 
            @Override 
            public void onClick(View v) { 
                if(menuHistory.size()>0){
                	try{
                		
                		syncDocument(Integer.valueOf(menuHistory.get(menuHistory.size()-1)).intValue(), limit, oa.getCount(),false);
                	}catch(Exception e){
                		syncSearch(menuHistory.get(menuHistory.size()-1), limit, oa.getCount(), false);
                	}
                }
                list.onBottomComplete();
            } 
        }); 
		wendang_search_text=(EditText)findViewById(R.id.wendang_search_text);
		oa=new WendangAdapter(con, pluginList);
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
					List<WenDang> l=(List<WenDang>)msg.obj;
					pluginList.clear();
					pluginList.addAll(l);
					initList();
					history.add(l);
					return;
					
				}
				if(msg.arg1==111){
					if(myDialog!=null&&myDialog.isShowing()){
						myDialog.dismiss();
					}
					myDialog=null;
					List<WenDang> l=(List<WenDang>)msg.obj;
//					pluginList.clear();
					pluginList.addAll(l);
					initList();
					history.get(history.size()-1).addAll(l);
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
	
	public void searchKeyWord(View view){
		String keyword=wendang_search_text.getText().toString();
		if(menuHistory.size()>0&&keyword.equals( menuHistory.get(menuHistory.size()-1))){
			Toast.makeText(con, keyword+" 关键字已经搜索完成。", 1000);
			return;
		}
		syncSearch(keyword,limit,0,true);
		
	}
	
	public void syncSearch(String keyword,int limit,int start,boolean flag){
		WenDangSync urlSync=new WenDangSync();
		urlSync.setMainContext(con);
		urlSync.setModth(UrlSync.POST);
		urlSync.setToast(true);
		urlSync.setFlag(flag);
//		if(!menuHistory.contains(keyword)){
			urlSync.setHistory(menuHistory);
			urlSync.setMenuid(keyword);
			
//		}
			urlSync.setToastContentSu("搜索文档成功。");
			urlSync.setToastContentFa("搜索文档失败。");
		urlSync.setUser(user);
		urlSync.setUri(Convert.hosturl+"/oa/searchDocument/");
		List<NameValuePair> param=new ArrayList<NameValuePair>();
		
    	
    	param.add(new BasicNameValuePair("search",keyword));
    	param.add(new BasicNameValuePair("start",String.valueOf(start)));
    	param.add(new BasicNameValuePair("limit",String.valueOf(limit)));
    	urlSync.setPrarm(param);
		urlSync.setHandler(tmpMainHandler4);
		UrlTask utk=new UrlTask(con);
		utk.setUrlSync(urlSync);
		utk.start();
			Message msg=tmpMainHandler4.obtainMessage();
			msg.arg1=1;
			msg.obj="正在搜索文档信息……";
			tmpMainHandler4.sendMessage(msg);
	}
	
	public void syncKind(boolean toast){
		UrlSync urlSync=new KindSync();
		urlSync.setMainContext(con);
		urlSync.setModth(UrlSync.POST);
		urlSync.setToast(toast);
		if(toast){
			
			urlSync.setToastContentSu("刷新栏目列表成功。");
			urlSync.setToastContentFa("刷新栏目列表失败。");
		}
		urlSync.setUser(user);
		urlSync.setUri(Convert.hosturl+"/oa/getAllMenu/");
		
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
	public void syncDocument(int Menuid,int limit,int start,boolean flag){
		WenDangSync urlSync=new WenDangSync();
		urlSync.setMainContext(con);
		urlSync.setModth(UrlSync.POST);
		urlSync.setToast(true);
		urlSync.setFlag(flag);
		if(!menuHistory.contains(String.valueOf(Menuid))){
			urlSync.setHistory(menuHistory);
			urlSync.setMenuid(String.valueOf(Menuid));
			
		}
			urlSync.setToastContentSu("下载文档成功。");
			urlSync.setToastContentFa("下载文档失败。");
		urlSync.setUser(user);
		urlSync.setUri(Convert.hosturl+"/oa/getDocument/");
		List<NameValuePair> param=new ArrayList<NameValuePair>();
		
    	
    	param.add(new BasicNameValuePair("menuid",String.valueOf(Menuid)));
    	param.add(new BasicNameValuePair("start",String.valueOf(start)));
    	param.add(new BasicNameValuePair("limit",String.valueOf(limit)));
    	urlSync.setPrarm(param);
		urlSync.setHandler(tmpMainHandler4);
		UrlTask utk=new UrlTask(con);
		utk.setUrlSync(urlSync);
		utk.start();
			Message msg=tmpMainHandler4.obtainMessage();
			msg.arg1=1;
			msg.obj="正在刷新文档信息……";
			tmpMainHandler4.sendMessage(msg);
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
	
	public void initList(){
		oa.notifyDataSetChanged();
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
    			Intent mainIntent = new Intent(WenDangList.this,XiaoShouDetail2.class);
    			Bundle extras=new Bundle();
    	    	extras.putSerializable("user", user);
    	    	extras.putInt("officeid", currentoffice.getId());
    	    	
    			mainIntent.putExtras(extras);
    			WenDangList.this.startActivity(mainIntent); 
    			WenDangList.this.finish();
    			return;
    		}
    	});
    	myDialog.show();
	}
	
	public void onListItemClick(ListView parent, View v, int position, long id) {
		
		
		WenDang wd= oa.getWenDang(position);
		if(wd.getType()==0){
			syncDocument(wd.getId(),limit,0,true);
		}else{
			showDocument(wd.getId());
		}
    	
			
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) { // 监控/拦截/屏蔽返回键
			try{
				if(history.size()>1){
					history.remove(history.size()-1);
					pluginList.clear();
					pluginList.addAll(history.get(history.size()-1));
					initList();
					
					menuHistory.remove(menuHistory.size()-1);
					return false;
				}
			}catch(Exception e){
				Log.e("wendang", "wendang cuowu");
			}
			
		} 
		return super.onKeyDown(keyCode, event);
	}

	
	
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		populateMenu(menu);
	}

	public void populateMenu(Menu menu) {
//		menu.add(Menu.NONE, SEARCHPLUGIN, Menu.NONE, "查找插件");
		menu.add(Menu.NONE, SEARCHPLUGIN, Menu.NONE, "文档分类列表");
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
    	
    	MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}