package com.wj.sell.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Message;

import com.wj.sell.db.models.KaoShi;

public class KaoShiAllSync extends UrlSync {
	

	public void doResult() throws Exception {
		// TODO Auto-generated method stub
		if(!doPerResult()){
			return;
		}
		if(!getJsonobj().getBoolean("success")){
			if(getHandler()!=null){
				Message hmsg=getHandler().obtainMessage();
				hmsg.obj=getJsonobj().getString("message");
				hmsg.arg1=10;
				getHandler().sendMessage(hmsg);
				setHandler(null);
				return;
			}
		}
		
		List<KaoShi> l=new ArrayList<KaoShi>();
		KaoShi item=null;
		if(getJsonobj().has("result")){
			JSONArray ja=getJsonobj().getJSONArray("result");
			JSONObject jo=null;
			for(int i=0;i<ja.length();i++){
				jo=ja.getJSONObject(i);
				item=new KaoShi();
				
				item.setId(jo.getInt("id"));
				item.setName(jo.getString("name"));
				
				l.add(item);
			}
			
		}
		if(getHandler()!=null){
			
			Message hmsg=getHandler().obtainMessage();
			hmsg.obj=l;
			hmsg.arg1=11;
			getHandler().sendMessage(hmsg);
			setHandler(null);
		}
	}
	
	public void doFailureResult()throws Exception{
		if(getHandler()!=null){
			Message hmsg=getHandler().obtainMessage();
			hmsg.obj=getToastContentFa();
			hmsg.arg1=10;
			getHandler().sendMessage(hmsg);
			setHandler(null);
		}
	}

	

	
}
