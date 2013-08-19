package com.wj.sell.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Message;

import com.wj.sell.db.models.WenDang;
import com.wj.sell.db.models.WenDangContent;
import com.wj.sell.db.models.WenDangImage;

public class WenDangContentSync extends UrlSync {
	
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
		
		List<WenDangImage> l=new ArrayList<WenDangImage>();
		WenDangImage item=null;
		WenDangContent wdc=new WenDangContent();
		if(getJsonobj().has("result")){
			JSONObject result=getJsonobj().getJSONObject("result");
			
			wdc.setId(result.getInt("id"));
			wdc.setTitle(result.getString("title"));
			wdc.setContent(result.getString("content"));
			wdc.setKindName(result.getString("kindName"));
			wdc.setKind(result.getInt("kind"));
			wdc.setDateTime(result.getString("datetime"));
			wdc.setShow(result.getInt("show"));
			wdc.setWl(l);
			JSONArray ja=result.getJSONArray("imglist");
			JSONObject jo=null;
			for(int i=0;i<ja.length();i++){
				jo=ja.getJSONObject(i);
				item=new WenDangImage();
				item.setId(jo.getInt("id"));
				item.setUrl(jo.getString("url"));
				item.setIndex(jo.getInt("index"));
				l.add(item);
			}
			
		}
		if(getHandler()!=null){
			if(getHistory()!=null){
				getHistory().add(getMenuid());
			}
			Message hmsg=getHandler().obtainMessage();
			hmsg.obj=wdc;
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
