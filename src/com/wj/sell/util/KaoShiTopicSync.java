package com.wj.sell.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Message;

import com.wj.sell.db.models.Choice;
import com.wj.sell.db.models.KaoShi;
import com.wj.sell.db.models.Topic;

public class KaoShiTopicSync extends UrlSync {
	private KaoShi kaoshi;

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
		if(getJsonobj().has("result")){
			JSONObject result=getJsonobj().getJSONObject("result");
			if(result.has("score")){
				kaoshi.setScore(result.getInt("score"));
				kaoshi.setType(1);
			}else{
				kaoshi.setType(0);
			}
			kaoshi.setId(result.getInt("id"));
			kaoshi.setName(result.getString("name"));
			kaoshi.setTime(result.getInt("time"));
			kaoshi.setDateTime(result.getString("dateTime"));
			JSONArray ja=result.getJSONArray("topics");
			JSONArray jaa=null;
			JSONObject jo=null;
			JSONObject joo=null;
			Topic top=null;
			Choice ch=null;
			for(int i=0;i<ja.length();i++){
				jo=ja.getJSONObject(i);
				top=new Topic();
				top.setId(jo.getInt("id"));
				top.setTitle(jo.getString("title"));
				kaoshi.getTl().add(top);
				jaa=jo.getJSONArray("choicelist");
				for(int j=0;j<jaa.length();j++){
					joo=jaa.getJSONObject(j);
					ch=new Choice();
					ch.setId(joo.getInt("id"));
					ch.setContent(joo.getString("content"));
					ch.setRight(joo.getBoolean("isright"));
					ch.setIndex(joo.getInt("index"));
					top.getCl().add(ch);
				}
			}
			
		}
		if(getHandler()!=null){
		
			Message hmsg=getHandler().obtainMessage();
			hmsg.obj=kaoshi;
			
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

	public KaoShi getKaoshi() {
		return kaoshi;
	}

	public void setKaoshi(KaoShi kaoshi) {
		this.kaoshi = kaoshi;
	}

	
}
