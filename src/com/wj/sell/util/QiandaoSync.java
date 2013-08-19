package com.wj.sell.util;

import android.os.Message;

import com.wj.sell.db.QiandaoUtil;

public class QiandaoSync extends UrlSync {
	
	

	public void doResult() throws Exception {
		// TODO Auto-generated method stub
		if(!doPerResult()){
			return;
		}
		QiandaoUtil.createQiandao(getJsonobj(), getMainContext());
		if(getHandler()!=null){
			Message hmsg=getHandler().obtainMessage();
			hmsg.obj=getToastContentSu();
			hmsg.arg1=1;
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
	
}
