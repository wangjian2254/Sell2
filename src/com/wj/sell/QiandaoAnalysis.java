package com.wj.sell;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;


import com.wj.sell.adapter.AppItemAdapter;
import com.wj.sell.adapter.SellAdapter;
import com.wj.sell.db.QiandaoUtil;
import com.wj.sell.db.UserInfoUtil;
import com.wj.sell.db.models.PluginMod;
import com.wj.sell.db.models.Qiandao;
import com.wj.sell.db.models.SellItem;
import com.wj.sell.db.models.UserInfo;
import com.wj.sell.util.Convert;
import com.wj.sell.util.OAUtil;
import com.wj.sell.util.QiandaoTongJiSync;
import com.wj.sell.util.UrlSync;
import com.wj.sell.util.UrlTask;
import com.wj.sell.util.UserQiandaoSync;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;

public class QiandaoAnalysis extends Activity {
    /** Called when the activity is first created. */
	Context con;
	UserInfo user=null;
	EditText mi=null;
	LinearLayout qiandao_service=null;
	List<Qiandao> qiandaolist=new ArrayList<Qiandao>();
	EditText datepicker;
	EditText datepicker2;
	String date1=null;
	String date2=null;
	DatePickerDialog.OnDateSetListener dateListener;
	DatePickerDialog.OnDateSetListener dateListener2;
	DatePickerDialog dialog;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        con=this;
        Bundle bunde = this.getIntent().getExtras();
        user=UserInfoUtil.getCurrentUserInfo(this);
        setContentView(R.layout.qiandao_searchwindow);
        mi=(EditText)findViewById(R.id.mi);
        qiandao_service=(LinearLayout)findViewById(R.id.qiandao_service);
        QiandaoUtil.getAllQiandao(con, qiandaolist);
        initQiandao();
        
        datepicker = (EditText) findViewById(R.id.datepicker);  
        datepicker2 = (EditText) findViewById(R.id.datepicker2);
        
        datepicker.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {  
            @Override  
            public void onFocusChange(View v, boolean hasFocus) {  

                if(hasFocus) {
		        // 此处为得到焦点时的处理内容
                	setDate1(null);
		        } else {
		
		        // 此处为失去焦点时的处理内容
		
		        }
            }
        });
        datepicker2.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {  
        	@Override  
        	public void onFocusChange(View v, boolean hasFocus) {  
        		
        		if(hasFocus) {
        			// 此处为得到焦点时的处理内容
        			setDate2(null);
        		} else {
        			
        			// 此处为失去焦点时的处理内容
        			
        		}
        	}
        });
        dateListener =  
            new DatePickerDialog.OnDateSetListener() { 
                @Override 
                public void onDateSet(DatePicker datePicker,  
                        int year, int month, int dayOfMonth) { 
                	datepicker.setText(getDate(datePicker));
                	date1=getDate(datePicker);
                } 
            }; 
        dateListener2 =  
            	new DatePickerDialog.OnDateSetListener() { 
            	@Override 
            	public void onDateSet(DatePicker datePicker,  
            			int year, int month, int dayOfMonth) { 
            		datepicker2.setText(getDate(datePicker));
            		date2=getDate(datePicker);
            	} 
            }; 
        
        initDatepick();
        
        
    }
    
    public void initQiandao(){
    	qiandao_service.removeAllViews();
    	CheckBox cb=null;
    	for(Qiandao q:qiandaolist){
    		cb=new CheckBox(con);
    		cb.setChecked(true);
    		cb.setText(q.getName());
    		cb.setTextAppearance(con, R.style.xiaoshou2);
    		qiandao_service.addView(cb);
    	}
    }
    
    public void setDate1(View view){
    	if(dialog!=null&&dialog.isShowing()){
    		dialog.dismiss();
    		dialog=null;
    	}
    	dialog = new DatePickerDialog(this, 
                dateListener, 
                Integer.parseInt(date1.split("-")[0]) , 
                Integer.parseInt(date1.split("-")[1])-1, 
                Integer.parseInt(date1.split("-")[2]));
    	dialog.show();
    }
    public void setDate2(View view){
    	if(dialog!=null&&dialog.isShowing()){
    		dialog.dismiss();
    		dialog=null;
    	}
    	dialog = new DatePickerDialog(this, 
    			dateListener2, 
    			Integer.parseInt(date2.split("-")[0]) , 
                Integer.parseInt(date2.split("-")[1])-1, 
                Integer.parseInt(date2.split("-")[2]));
    	dialog.show();
    }
    
    public void initDatepick(){
    	datepicker.setText(Convert.format1.format(new Date()).substring(0, 10));
		date1=Convert.format1.format(new Date()).substring(0, 10);
		datepicker2.setText(Convert.format1.format(new Date()).substring(0, 10));
		date2=Convert.format1.format(new Date()).substring(0, 10);
    }
    public String[] getQianDaoIds(){
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
    
    public String getDate(DatePicker dp){
    	String  year=String.valueOf(dp.getYear());
    	String  month=String.valueOf(dp.getMonth()+1);
    	String  day=String.valueOf(dp.getDayOfMonth());
    	if(month.length()==1){
    		month="0"+month;
    	}
    	if(day.length()==1){
    		day="0"+day;
    	}
    	return year+"-"+month+"-"+day;
    }
    public void queryTongji(View view){
    	Intent mainIntent = new Intent(this, QiandaoAnalysis2.class);
		Bundle extras=new Bundle();

    	extras.putSerializable("user", user);
    	extras.putStringArray("qiandaoid", getQianDaoIds());
    	extras.putString("mi", String.valueOf(mi.getText().toString()));
    	extras.putString("startdate", date1);
    	extras.putString("enddate",date2);
		mainIntent.putExtras(extras);
		startActivity(mainIntent); 
    }
    
    
    public void onResume(){
    	super.onResume();
    }
    public void onPause() {
        super.onPause();
    }
}