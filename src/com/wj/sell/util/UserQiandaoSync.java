package com.wj.sell.util;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.os.Message;

import com.wj.sell.db.QiandaoUtil;
import com.wj.sell.db.models.Office;
import com.wj.sell.db.models.Qiandao;
import com.wj.sell.db.models.UserQiandao;

public class UserQiandaoSync extends UrlSync {

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
		
		UserQiandao uq=new UserQiandao();
		JSONObject jq=getJsonobj().getJSONObject("result");
		getQiandao().setId(jq.getInt("id"));
		getQiandao().setLastEventDate(jq.getString("time"));
		for(NameValuePair par:getPrarm()){
			if("officeid".equals(par.getName())){
				getQiandao().setOfficeid(Integer.valueOf(par.getValue()).intValue());
			}
			if("officename".equals(par.getName())){
				getQiandao().setLastOfficeName(par.getValue());
			}
			if("gps".equals(par.getName())){
				uq.setGps(par.getValue());
			}
			if("address".equals(par.getName())){
				uq.setAddress(par.getValue());
			}
		}
		
		uq.setOfficeid(getQiandao().getOfficeid());
		uq.setQiandaoid(getQiandao().getId());
		uq.setOfficename(getQiandao().getLastOfficeName());
		uq.setQiandaoname(getQiandao().getName());
		uq.setTime(getQiandao().getLastEventDate());
		int id=QiandaoUtil.updateQiandaoEvent(uq, getMainContext());
		if(id!=-1){
			getQiandao().setEventid(id);
		}
		QiandaoUtil.updateQiandaoEventDate(getQiandao(), getMainContext());
		if(getHandler()!=null){
			Message hmsg=getHandler().obtainMessage();
			hmsg.obj=getJsonobj().getString("message");
			hmsg.arg1=100;
			getHandler().sendMessage(hmsg);
			setHandler(null);
		}
	}
	
	public void doFailureResult()throws Exception{
		if(getHandler()!=null){
			Message hmsg=getHandler().obtainMessage();
			hmsg.obj=getToastContentFa();
			hmsg.arg1=2;
			getHandler().sendMessage(hmsg);
			setHandler(null);
		}
	}
	
	private Office office;
	private Qiandao qiandao;

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	public Qiandao getQiandao() {
		return qiandao;
	}

	public void setQiandao(Qiandao qiandao) {
		this.qiandao = qiandao;
	}
}
