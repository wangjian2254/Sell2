package com.wj.sell;



import com.umeng.analytics.MobclickAgent;
import com.wj.sell.adapter.QiandaoAdapter;
import com.wj.sell.db.UserInfoUtil;
import com.wj.sell.db.models.Qiandao;
import com.wj.sell.db.models.UserInfo;
import com.wj.sell.util.Convert;
import com.wj.sell.util.QiandaoSync;
import com.wj.sell.util.OAUtil;
import com.wj.sell.util.UrlSync;
import com.wj.sell.util.UrlTask;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class QiandaoList extends ListActivity {
	private UserInfo user;
	private ListView list;
//	private EditText plugin_search_where;
	public  QiandaoList instance = null;
	
	private Qiandao qiandao;
	
	private Handler tmpMainHandler;
	Intent intent;
	QiandaoAdapter qda;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qiandao_list);
		instance=this;
		list=getListView();
		intent=this.getIntent();  
		Bundle bunde = intent.getExtras();
		user=UserInfoUtil.getCurrentUserInfo(this);
		getAllPluginList();
		if(qda.getCount()==0){
			syncQiandao(null);
		}
		
	}
	
	public void getAllPluginList(){
		
		if(qda==null){
			qda=new QiandaoAdapter(this);
			list.setAdapter(qda);
		}
		qda.getDate();
		qda.notifyDataSetChanged();
		
		
	}
	
	public void syncQiandao(View view){
		QiandaoSync qiandaoSync=new QiandaoSync();
		qiandaoSync.setMainContext(this);
		qiandaoSync.setModth(UrlSync.POST);
		qiandaoSync.setToast(true);
		qiandaoSync.setToastContentSu("刷新签到服务列表成功。");
		qiandaoSync.setToastContentFa("刷新签到服务列表失败。");
		qiandaoSync.setUser(user);
		qiandaoSync.setUri(Convert.hosturl+"/oa/qiandaoListClient/");
		tmpMainHandler = new Handler() {
					
					@Override
					public void handleMessage(Message msg) {
						
						// // 接收子线程的消息
						String m=(String)msg.obj;
						if(msg.arg1==1){
							qda.getDate();
						}
						Toast.makeText(getApplicationContext(), m, 1000);
						if(msg.arg1==404){
							OAUtil.gotoReLogin(instance,m);
							finish();
						}
						
					}
					
				};
		qiandaoSync.setHandler(tmpMainHandler);
		UrlTask utk=new UrlTask(instance);
		utk.setUrlSync(qiandaoSync);
		utk.start();
		
	}
	
	
	
	public void onBack(View view) {
    	if(intent==null){
    		return;
    	}
    	this.setResult(RESULT_OK, intent);
    	this.finish(); 
    }
	/*
     * 
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			/* 取得来自Activity2的数据，并显示于画面上 */
			
			
			break;
		default:
			break;
		}
		
    }
    
    public void AddContactItem(Qiandao pitem){
    	qiandao=pitem;
    	
    }
	
	public void onListItemClick(ListView parent, View v, int position, long id) {
		
		
		Qiandao pluginItem=qda.getQiandao(position);
//		if("no".equals(pluginItem.getIsread())){
		
			Intent mainIntent = new Intent(QiandaoList.this,QiandaoDetail2.class);
			Bundle extras=new Bundle();
	    	extras.putSerializable("user", user);
	    	extras.putSerializable("qiandao", pluginItem);
			mainIntent.putExtras(extras);
			QiandaoList.this.startActivity(mainIntent); 
//			AddContactItem(pluginItem);
			
//		}
	}
	
	
	
	
	
	 
	   
	    public void onResume(){
	    	super.onResume();
	    	getAllPluginList();
	    	MobclickAgent.onResume(this);
	    }
	    public void onPause() {
	        super.onPause();
	        MobclickAgent.onPause(this);
	    }
}
