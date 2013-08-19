package com.wj.sell.adapter;

import java.util.List;

import com.wj.sell.controls.AppItem;
import com.wj.sell.db.models.PluginMod;
import com.wj.sell.db.models.SellItem;
import com.wj.sell.db.models.UserXiaoShouOrder;
import com.wj.sell.Main;
import com.wj.sell.R;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TableRow;
import android.widget.TextView;

public class TodaySellAdapter extends BaseAdapter {
	private Context mContext;
	List<UserXiaoShouOrder> imgarrlist;
	//加载布局文件的对象
		private LayoutInflater mLayoutInflater = null;
//	List<String> imgarrlistcode;
	
//	Pattern pattern = Pattern.compile(Convert.imgreg);
//	Matcher matcher = null;

	public TodaySellAdapter(Context c,List<UserXiaoShouOrder> itemContent) {
		mContext = c;
		imgarrlist=itemContent;
		mLayoutInflater = LayoutInflater.from(mContext);
//		imgarrlistcode=itemCode;
//		contentmap=contentmap;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imgarrlist.size();
	}
	
	public UserXiaoShouOrder getImgArr(int position){
		return imgarrlist.get(position);
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		View localView = convertView;
		//判断当前view视图参数是否为null
	    if (localView == null){
	        //加载一级视图的布局文件
	    	localView = mLayoutInflater.inflate(R.layout.anaylsis_xiaoshoutoday_item, null);
	    }
	    TableRow row;
//	    TextView date=(TextView)localView.findViewById(R.id.date);
	    TextView name=(TextView)localView.findViewById(R.id.name);
	    TextView pinpai=(TextView)localView.findViewById(R.id.pinpai);
//	    TextView jixing=(TextView)localView.findViewById(R.id.jixing);
	    TextView leixing=(TextView)localView.findViewById(R.id.leixing);
	    TextView num=(TextView)localView.findViewById(R.id.num);
	    TextView zhuguan=(TextView)localView.findViewById(R.id.zhuguan);
	    
	    UserXiaoShouOrder sellItem=imgarrlist.get(position);
	    
	    	row=(TableRow)localView.findViewById(R.id.table1);
	    	row.setVisibility(View.GONE);
	    	row=(TableRow)localView.findViewById(R.id.table2);
	    	row.setVisibility(View.VISIBLE);
	    	
	    	name.setText("");
	    	for(String g:sellItem.getGiftsname()){
	    		name.append(g+"\n");
	    	}
	    	pinpai.setText(sellItem.getProductname());
	    	leixing.setText(sellItem.getTypename());
	    	num.setText(sellItem.getClientdate().substring(6)+"-"+sellItem.getClienttime());
	    	zhuguan.setText(sellItem.getTel()+"\n"+(sellItem.isSubmit()?"上传成功":"上传失败"));
	    	if(!sellItem.isSubmit()){
	    		zhuguan.setBackgroundColor(Color.RED);
	    	}else{
	    		zhuguan.setBackgroundColor(0xFFBEF56E);
	    	}
	    
		return localView;
	}
	

}
