package com.wj.sell.util;

import android.os.Message;

import com.wj.sell.db.OfficeUtil;

public class OfficeSync extends UrlSync {

	public void doResult() throws Exception {
		// TODO Auto-generated method stub
		if(!doPerResult()){
			return;
		}
		OfficeUtil.createOffice(getJsonobj(), getMainContext());
		if(getHandler()!=null){
			Message hmsg=getHandler().obtainMessage();
			if(isToast()){
				hmsg.obj=getToastContentSu();
			}else{
				hmsg.obj="";
			}
			hmsg.arg1=10;
			hmsg.arg2=20;
			getHandler().sendMessage(hmsg);
			setHandler(null);
		}
	}
	
	public void doFailureResult()throws Exception{
		if(getHandler()!=null){
			Message hmsg=getHandler().obtainMessage();
			if(isToast()){
				hmsg.obj=getToastContentFa();
			}else{
				hmsg.obj="";
			}
			hmsg.arg1=10;
			getHandler().sendMessage(hmsg);
			setHandler(null);
		}
	}
}
