package com.wj.sell;

import java.util.ArrayList;
import java.util.List;



import com.wj.sell.db.UserInfoUtil;
import com.wj.sell.db.models.KaoShi;
import com.wj.sell.db.models.UserInfo;
import com.wj.sell.util.Convert;
import com.wj.sell.util.KaoShiAllSync;
import com.wj.sell.util.OAUtil;
import com.wj.sell.util.UrlSync;
import com.wj.sell.util.UrlTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

public class KaoShiAnalysis extends Activity {
    /** Called when the activity is first created. */
	Context con;
	UserInfo user=null;
	EditText mi=null;
	LinearLayout qiandao_service=null;
	List<KaoShi> qiandaolist=new ArrayList<KaoShi>();
	private Handler tmpMainHandler4;
	public ProgressDialog myDialog = null;
	public static final int SEARCHPLUGIN = Menu.FIRST + 1;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        con=this;
        Bundle bunde = this.getIntent().getExtras();
        user=UserInfoUtil.getCurrentUserInfo(this);
        setContentView(R.layout.kaoshi_searchwindow);
        qiandao_service=(LinearLayout)findViewById(R.id.qiandao_service);
        
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
					qiandaolist=(List<KaoShi>)msg.obj;
					initKaoShi();
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
		syncKind(false);
        
        
    }
    public void syncKind(boolean toast){
		UrlSync urlSync=new KaoShiAllSync();
		urlSync.setMainContext(con);
		urlSync.setModth(UrlSync.POST);
		urlSync.setToast(toast);
		if(toast){
			
			urlSync.setToastContentSu("刷新考试列表成功。");
			urlSync.setToastContentFa("刷新考试列表失败。");
		}
		urlSync.setUser(user);
		urlSync.setUri(Convert.hosturl+"/oa/getAllKaoShi/");
		
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
    
    public void initKaoShi(){
    	qiandao_service.removeAllViews();
    	CheckBox cb=null;
    	for(KaoShi q:qiandaolist){
    		cb=new CheckBox(con);
    		cb.setChecked(false);
    		cb.setText(q.getName());
    		cb.setTextAppearance(con, R.style.xiaoshou2);
    		qiandao_service.addView(cb);
    	}
    }
    
    
    public String[] getKaoShiIds(){
    	CheckBox cb;
    	String[] ids=new String[qiandaolist.size()];
    	for(int index=0;index<qiandao_service.getChildCount();index++){
    		cb=(CheckBox)qiandao_service.getChildAt(index);
    		if(cb!=null&&cb.isChecked()){
    			ids[index]=String.valueOf(qiandaolist.get(index).getId());
    		}
    	}
    	return ids;
    }
    
    public void queryTongji(View view){
    	Intent mainIntent = new Intent(this, KaoShiAnalysis2.class);
		Bundle extras=new Bundle();

    	extras.putSerializable("user", user);
    	extras.putStringArray("kaoshiid", getKaoShiIds());
		mainIntent.putExtras(extras);
		startActivity(mainIntent); 
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
    }
    public void onPause() {
        super.onPause();
    }
}