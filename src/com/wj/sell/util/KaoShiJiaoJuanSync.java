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

public class KaoShiJiaoJuanSync extends UrlSync {
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
				hmsg.arg1=110;
				getHandler().sendMessage(hmsg);
				setHandler(null);
				return;
			}
		}
		String m="";
		if(getJsonobj().has("result")){
			JSONObject result=getJsonobj().getJSONObject("result");
			kaoshi.setScore(result.getInt("score"));
			kaoshi.setType(1);
			m+="正确答题："+result.getInt("right");
			m+="\n错误答题："+result.getInt("error");
			m+="\n总题数："+result.getInt("total");
			m+="\n得分数："+result.getInt("score");
			
		}
		if(getHandler()!=null){
		
			Message hmsg=getHandler().obtainMessage();
			hmsg.obj=m;
			
			hmsg.arg1=100;
			getHandler().sendMessage(hmsg);
			setHandler(null);
		}
	}
	
	public void doFailureResult()throws Exception{
		if(getHandler()!=null){
			Message hmsg=getHandler().obtainMessage();
			hmsg.obj=getToastContentFa();
			hmsg.arg1=110;
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
