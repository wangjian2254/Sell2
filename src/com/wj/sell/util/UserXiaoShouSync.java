package com.wj.sell.util;

import android.os.Message;

import com.wj.sell.db.XiaoShouUtil;
import com.wj.sell.db.models.UserXiaoShouOrder;

public class UserXiaoShouSync extends UrlSync {

	private UserXiaoShouOrder uxso=null;
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
		getUxso().setServerid(getJsonobj().getInt("result"));
		getUxso().setSubmit(true);
		XiaoShouUtil.updateUserXiaoShouOrder(getUxso(), getMainContext());
		Convert.upo=new UserXiaoShouOrder();
		if(getHandler()!=null){
			Message hmsg=getHandler().obtainMessage();
			hmsg.obj=getJsonobj().getString("message");
			hmsg.arg1=12;
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
	
	


	public UserXiaoShouOrder getUxso() {
		return uxso;
	}

	public void setUxso(UserXiaoShouOrder uxso) {
		this.uxso = uxso;
	}
}
