package com.wj.sell.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Message;

import com.wj.sell.db.models.WenDang;

public class WenDangSync extends UrlSync {
	
	private List<String> history=null;
	private String menuid=null;
	private boolean flag=false;

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
			JSONObject result=getJsonobj().getJSONObject("result");
			JSONArray ja=result.getJSONArray("result");
			JSONObject jo=null;
			for(int i=0;i<ja.length();i++){
				jo=ja.getJSONObject(i);
				item=new WenDang();
				item.setType(1);
				item.setId(jo.getInt("id"));
				item.setTitle(jo.getString("title"));
				item.setKindName(jo.getString("kindName"));
				item.setKind(jo.getInt("kind"));
				item.setDateTime(jo.getString("datetime"));
				item.setShow(jo.getInt("show"));
				l.add(item);
			}
		}
		if(getHandler()!=null){
			if(getHistory()!=null){
				getHistory().add(getMenuid());
			}
			Message hmsg=getHandler().obtainMessage();
			hmsg.obj=l;
			if(l.size()==0){
				hmsg.arg1=10;
				hmsg.obj="没有数据了。";
			}else{
				if(flag){
					
					hmsg.arg1=11;
				}else{
					hmsg.arg1=111;
				}
			}
			
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

	public List<String> getHistory() {
		return history;
	}

	public void setHistory(List<String> history) {
		this.history = history;
	}

	public String getMenuid() {
		return menuid;
	}

	public void setMenuid(String menuid) {
		this.menuid = menuid;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}
}
