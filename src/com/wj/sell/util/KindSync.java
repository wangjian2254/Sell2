package com.wj.sell.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Message;

import com.wj.sell.db.models.WenDang;

public class KindSync extends UrlSync {

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
		
		List<WenDang> l=new ArrayList<WenDang>();
		WenDang item=null;
		if(getJsonobj().has("result")){
			JSONArray ja=getJsonobj().getJSONArray("result");
			JSONObject jo=null;
			for(int i=0;i<ja.length();i++){
				jo=ja.getJSONObject(i);
				if(jo.getBoolean("isdel")){
					continue;
				}
				item=new WenDang();
				item.setType(0);
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
