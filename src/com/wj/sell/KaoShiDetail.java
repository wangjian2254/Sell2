package com.wj.sell;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.umeng.analytics.MobclickAgent;
import com.wj.sell.adapter.KaoShiAdapter;
import com.wj.sell.adapter.WendangAdapter;
import com.wj.sell.controls.DropDownListView;
import com.wj.sell.db.OfficeUtil;
import com.wj.sell.db.UserInfoUtil;
import com.wj.sell.db.models.Choice;
import com.wj.sell.db.models.KaoShi;
import com.wj.sell.db.models.Office;
import com.wj.sell.db.models.Topic;
import com.wj.sell.db.models.UserInfo;
import com.wj.sell.db.models.WenDang;
import com.wj.sell.util.Convert;
import com.wj.sell.util.KaoShiJiaoJuanSync;
import com.wj.sell.util.KaoShiSync;
import com.wj.sell.util.KaoShiTopicSync;
import com.wj.sell.util.KindSync;
import com.wj.sell.util.OAUtil;
import com.wj.sell.util.OfficeSync;
import com.wj.sell.util.UrlSync;
import com.wj.sell.util.UrlTask;
import com.wj.sell.util.WenDangSync;
import com.wj.sell.KaoShiDetail;
import com.wj.sell.R;

import android.app.Activity;
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
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class KaoShiDetail extends Activity  {
	
	private TextView title;
	private TextView topictitle;
	private TextView document_answer;
	private Button jishi; 
	RadioGroup choicelist;
	
	KaoShiDetail con;
	
	
	private List<List<Integer>> answer=new ArrayList<List<Integer>>();
	private Handler tmpMainHandler4;
	public ProgressDialog myDialog = null;
	
	public static final int SEARCHPLUGIN = Menu.FIRST + 1;

	private int index=0;
	private int second=0;
	private boolean stop=true;
	private boolean jiaojuan=false;
	private Handler handler = new Handler();
	private Thread timeThread=new Thread() {
		public void run() {
			
			
			if(stop){
				
				handler.postDelayed(this, 1000);
			}
			second+=1;
			Message msg=tmpMainHandler4.obtainMessage();
			msg.arg1=0;
			msg.arg2=second;
			msg.obj="";
			tmpMainHandler4.sendMessage(msg);
		}
	};
	private UserInfo user=null;
	private KaoShi currentkaoshi=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        con=this;
        setContentView(R.layout.kaoshi_detail);
		Bundle bunde = this.getIntent().getExtras();
		currentkaoshi=(KaoShi)bunde.getSerializable("examination");
		user=UserInfoUtil.getCurrentUserInfo(this);
		
		title=(TextView)findViewById(R.id.document_title);
		topictitle=(TextView)findViewById(R.id.document_info);
		document_answer=(TextView)findViewById(R.id.document_answer);
		choicelist=(RadioGroup)findViewById(R.id.choicelist);
		jishi=(Button)findViewById(R.id.jishi_btn);
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
				if(msg.arg1==0){
					showTime(msg.arg2);
					return;
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
					currentkaoshi=(KaoShi)msg.obj;
					showKaoShi();
					if(second==0){
						timeThread.start();
					}
					return;
					
				}
				if(msg.arg1==100){
					if(myDialog!=null&&myDialog.isShowing()){
						myDialog.setMessage(m);
					}
					showKaoShi();
					stop=false;
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
				if(msg.arg1==110){
					if(myDialog!=null&&myDialog.isShowing()){
						myDialog.setMessage(m);
					}
					jiaojuan=false;
					stop=true;
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
	
	public void showTime(int s){
		String m=""+s/60;
		m+=":"+s%60;
		jishi.setText(m+"(交卷)");
		if(s>currentkaoshi.getTime()*60){
			jiaojuan();
			jiaojuan=true;
		}
		
	}
	
	public void syncKind(boolean toast){
		KaoShiTopicSync urlSync=new KaoShiTopicSync();
		urlSync.setMainContext(con);
		urlSync.setModth(UrlSync.POST);
		urlSync.setToast(toast);
		urlSync.setKaoshi(currentkaoshi);
		if(toast){
			
			urlSync.setToastContentSu("刷新考试列表成功。");
			urlSync.setToastContentFa("刷新考试列表失败。");
		}
		urlSync.setUser(user);
		urlSync.setUri(Convert.hosturl+"/oa/getExamination/");
		List<NameValuePair> param=new ArrayList<NameValuePair>();
		
    	
    	param.add(new BasicNameValuePair("examinationid",String.valueOf(currentkaoshi.getId())));
    	urlSync.setPrarm(param);
		urlSync.setHandler(tmpMainHandler4);
		UrlTask utk=new UrlTask(con);
		utk.setUrlSync(urlSync);
		utk.start();
		if(toast){
			Message msg=tmpMainHandler4.obtainMessage();
			msg.arg1=1;
			msg.obj="正在加载考试信息……";
			tmpMainHandler4.sendMessage(msg);
		}
	}
	
	
	public void showKaoShi(){
		if(currentkaoshi.getType()==0){
			
			title.setText(currentkaoshi.getName());
		}else{
			title.setText(currentkaoshi.getName()+"("+currentkaoshi.getScore()+"分)");
		}
		setTopic(0);
		if(currentkaoshi.getType()==1){
			stop=false;
		}
	}
	
	public void setTopic(int i){
		if(i<0){
			myDialog = ProgressDialog.show  
			    	(
			    			con,
			    		"提示",
			    		"已经到第一题了。"
			    	);
			Message msg=tmpMainHandler4.obtainMessage();
			msg.arg1=10;
			msg.obj="已经到第一题了。";
			tmpMainHandler4.sendMessage(msg);
			return;
		}
		if(i>currentkaoshi.getTl().size()-1){
			myDialog = ProgressDialog.show  
			    	(
			    			con,
			    		"提示",
			    		"已经到最后一题了。"
			    	);
			Message msg=tmpMainHandler4.obtainMessage();
			msg.arg1=10;
			msg.obj="已经到最后一题了。";
			tmpMainHandler4.sendMessage(msg);
			return;
		}
		index=i;
		Topic t=currentkaoshi.getTl().get(i);
		topictitle.setText(t.getTitle());
		choicelist.removeAllViews();
		int flag=0;
		for(Choice c :t.getCl()){
			if(c.isRight()){
				flag+=1;
			}
		}
//		RadioButton rb=null;
		RadioButton cb=null;
		for(Choice c :t.getCl()){
//			if(flag==1){
//				rb=new RadioButton(con);
//				rb.setText(c.getContent());
//			}else{
				cb=new RadioButton(con);
				cb.setTextColor(Color.BLACK);
				cb.setText(c.getContent());
				choicelist.addView(cb);
//			}
		}
		getTopic();
	}
	public void getTopic(){
		if(answer.size()<=index){
			answer.add(new ArrayList<Integer>());
		}
		for(int i=0;i<choicelist.getChildCount();i++){
			RadioButton c=(RadioButton)choicelist.getChildAt(i);
			c.setChecked(false);
		}
		for(int i=0;i<answer.get(index).size();i++){
			Integer integer=answer.get(index).get(i);
			RadioButton cb=(RadioButton)choicelist.getChildAt(integer.intValue());
			cb.setChecked(true);
		}
		if(currentkaoshi.getType()==1){
			document_answer.setVisibility(View.VISIBLE);
			for(int i=0;i<currentkaoshi.getTl().get(index).getCl().size();i++){
				if(currentkaoshi.getTl().get(index).getCl().get(i).isRight()){
					document_answer.setText("正确答案是第 "+(i+1)+" 个选项。");
				}
			}
		}else{
			document_answer.setVisibility(View.GONE);
		}
	}
	public void getTopic2(){
		if(answer.size()<=index){
			answer.add(new ArrayList<Integer>());
		}
		answer.get(index).clear();
		for(int i=0;i<choicelist.getChildCount();i++){
			RadioButton c=(RadioButton)choicelist.getChildAt(i);
			if(c.isChecked()){
				answer.get(index).add(new Integer(i));
			}
		}
	}
	
	public void shangyiti(View view){
		getTopic2();
		setTopic(index-1);
	}
	
	public void xiayiti(View view){
		getTopic2();
		setTopic(index+1);
	}
	
	public void onjiaojuan(View view){
		if(currentkaoshi.getType()==1){
			myDialog = ProgressDialog.show  
			    	(
			    			con,
			    		"提示",
			    		"已经考过了，不能再交卷。"
			    	);
			Message msg=tmpMainHandler4.obtainMessage();
			msg.arg1=10;
			msg.obj="已经考过了，不能再交卷。";
			tmpMainHandler4.sendMessage(msg);
			return;
		}
		
		TextView eDeleteW = new TextView(con);
    	LinearLayout.LayoutParams eLayoutParam = new  LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT
    			,LinearLayout.LayoutParams.WRAP_CONTENT);
    	eDeleteW.setPadding(10, 10, 10, 10);
    	eDeleteW.setText("是否交卷？");
    	eDeleteW.setTextColor(Color.WHITE);
    	eDeleteW.setTextSize(14);
    	eDeleteW.setLayoutParams(eLayoutParam);
    	
    	
    	AlertDialog.Builder builder;
    	builder = new  AlertDialog.Builder(con);
    	AlertDialog myDialog  = builder.create();  
    	myDialog.setMessage(eDeleteW.getText());
    	myDialog.setTitle("提示");
    	myDialog.setCancelable(false);
    	
    	myDialog.setButton2("继续答题", new DialogInterface.OnClickListener() {
    		
    		@Override
    		public void onClick(DialogInterface dialog, int which) {
    			dialog.dismiss();
    			
    		}
    	});
    	myDialog.setButton("交卷",
    			new DialogInterface.OnClickListener() {
    		
    		@Override
    		public void onClick(DialogInterface dialog, int which) {
    			dialog.dismiss();
    			jiaojuan();
    			return;
    		}
    	});
    	myDialog.show();
	}
	
	public void jiaojuan(){
		getTopic2();
		if(currentkaoshi.getType()==1){
			myDialog = ProgressDialog.show  
			    	(
			    			con,
			    		"提示",
			    		"已经考过了，不能再交卷。"
			    	);
			Message msg=tmpMainHandler4.obtainMessage();
			msg.arg1=10;
			msg.obj="已经考过了，不能再交卷。";
			tmpMainHandler4.sendMessage(msg);
			return;
		}
		JSONObject value=new JSONObject();
		for(int i=0;i<currentkaoshi.getTl().size();i++){
			try {
				if(answer.get(i).size()>0){
					
						value.put(String.valueOf(currentkaoshi.getTl().get(i).getId()), currentkaoshi.getTl().get(i).getCl().get(answer.get(i).get(0).intValue()).getId());
				}else{
					value.put(String.valueOf(currentkaoshi.getTl().get(i).getId()), -1);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		KaoShiJiaoJuanSync urlSync=new KaoShiJiaoJuanSync();
		urlSync.setMainContext(con);
		urlSync.setModth(UrlSync.POST);
		urlSync.setToast(true);
		urlSync.setKaoshi(currentkaoshi);
			
			urlSync.setToastContentSu("交卷成功。");
			urlSync.setToastContentFa("交卷失败。");
		urlSync.setUser(user);
		urlSync.setUri(Convert.hosturl+"/oa/getScore/");
		List<NameValuePair> param=new ArrayList<NameValuePair>();
		
    	
    	param.add(new BasicNameValuePair("examinationid",String.valueOf(currentkaoshi.getId())));
    	param.add(new BasicNameValuePair("value",value.toString()));
    	urlSync.setPrarm(param);
		urlSync.setHandler(tmpMainHandler4);
		UrlTask utk=new UrlTask(con);
		utk.setUrlSync(urlSync);
		utk.start();
		Message msg=tmpMainHandler4.obtainMessage();
		msg.arg1=1;
		msg.obj="正在交卷……";
		tmpMainHandler4.sendMessage(msg);
		jiaojuan=true;
	}

	
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) { // 监控/拦截/屏蔽返回键
			if(currentkaoshi.getType()==1){
				
				
			}else{
				TextView eDeleteW = new TextView(con);
		    	LinearLayout.LayoutParams eLayoutParam = new  LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT
		    			,LinearLayout.LayoutParams.WRAP_CONTENT);
		    	eDeleteW.setPadding(10, 10, 10, 10);
		    	eDeleteW.setText("是否交卷？(退出前必须交卷。)");
		    	eDeleteW.setTextColor(Color.WHITE);
		    	eDeleteW.setTextSize(14);
		    	eDeleteW.setLayoutParams(eLayoutParam);
		    	
		    	
		    	AlertDialog.Builder builder;
		    	builder = new  AlertDialog.Builder(con);
		    	AlertDialog myDialog  = builder.create();  
		    	myDialog.setMessage(eDeleteW.getText());
		    	myDialog.setTitle("提示");
		    	myDialog.setCancelable(false);
		    	
		    	myDialog.setButton2("继续答题", new DialogInterface.OnClickListener() {
		    		
		    		@Override
		    		public void onClick(DialogInterface dialog, int which) {
		    			dialog.dismiss();
		    			
		    		}
		    	});
		    	myDialog.setButton("交卷",
		    			new DialogInterface.OnClickListener() {
		    		
		    		@Override
		    		public void onClick(DialogInterface dialog, int which) {
		    			dialog.dismiss();
		    			jiaojuan();
		    			return;
		    		}
		    	});
		    	myDialog.show();
		    	return false;
		    	
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
    	MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}