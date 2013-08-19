package com.wj.sell.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Message;

import com.wj.sell.db.models.KaoShi;

public class KaoShiSync extends UrlSync {
	

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
		List<KaoShi> l2=new ArrayList<KaoShi>();
		KaoShi item=null;
		if(getJsonobj().has("result")){
			JSONObject result=getJsonobj().getJSONObject("result");
			JSONArray ja=result.getJSONArray("kaoshilist");
			JSONObject jo=null;
			for(int i=0;i<ja.length();i++){
				jo=ja.getJSONObject(i);
				item=new KaoShi();
				if(jo.has("score")){
					item.setScore(jo.getInt("score"));
					item.setType(1);
				}else{
					item.setType(0);
				}
				item.setId(jo.getInt("id"));
				item.setName(jo.getString("name"));
				item.setTime(jo.getInt("time"));
				item.setDateTime(jo.getString("dateTime"));
				l.add(item);
			}
			ja=result.getJSONArray("kaoshilist_nonescore");
			for(int i=0;i<ja.length();i++){
				jo=ja.getJSONObject(i);
				item=new KaoShi();
				if(jo.has("score")){
					
					item.setType(1);
				}else{
					item.setType(0);
				}
				item.setId(jo.getInt("id"));
				item.setName(jo.getString("name"));
				item.setTime(jo.getInt("time"));
				item.setDateTime(jo.getString("dateTime"));
				l2.add(item);
			}
		}
		if(getHandler()!=null){
			Map<String, List<KaoShi>> m=new HashMap<String, List<KaoShi>>();
			m.put("kaoshilist", l);
			m.put("kaoshilist_nonescore", l2);
			Message hmsg=getHandler().obtainMessage();
			hmsg.obj=m;
			
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
