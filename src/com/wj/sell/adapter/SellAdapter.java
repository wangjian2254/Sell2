package com.wj.sell.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TableRow;
import android.widget.TextView;

import com.wj.sell.R;
import com.wj.sell.db.models.SellItem;

public class SellAdapter extends BaseAdapter {
	private Context mContext;
	List<SellItem> imgarrlist;
	//加载布局文件的对象
		private LayoutInflater mLayoutInflater = null;
//	List<String> imgarrlistcode;
	
//	Pattern pattern = Pattern.compile(Convert.imgreg);
//	Matcher matcher = null;

	public SellAdapter(Context c,List<SellItem> itemContent) {
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
	
	public SellItem getImgArr(int position){
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
	    	localView = mLayoutInflater.inflate(R.layout.anaylsis_xiaoshou_item, null);
	    }
	    TableRow row;
	    TextView date=(TextView)localView.findViewById(R.id.date);
	    TextView name=(TextView)localView.findViewById(R.id.name);
	    TextView pinpai=(TextView)localView.findViewById(R.id.pinpai);
	    TextView jixing=(TextView)localView.findViewById(R.id.jixing);
	    TextView leixing=(TextView)localView.findViewById(R.id.leixing);
	    TextView num=(TextView)localView.findViewById(R.id.num);
	    TextView zhuguan=(TextView)localView.findViewById(R.id.zhuguan);
	    
	    SellItem sellItem=imgarrlist.get(position);
	    if(sellItem.getType()==1){
	    	row=(TableRow)localView.findViewById(R.id.table2);
	    	row.setVisibility(View.GONE);
	    	row=(TableRow)localView.findViewById(R.id.table1);
	    	row.setVisibility(View.VISIBLE);
	    	
	    	date.setText(sellItem.getDate());
	    	
	    }
	    if(sellItem.getType()==2){
	    	row=(TableRow)localView.findViewById(R.id.table1);
	    	row.setVisibility(View.GONE);
	    	row=(TableRow)localView.findViewById(R.id.table2);
	    	row.setVisibility(View.VISIBLE);
	    	
	    	name.setText(sellItem.getName());
	    	pinpai.setText(sellItem.getPinpai());
	    	jixing.setText(sellItem.getJixing());
	    	leixing.setText(sellItem.getLeixing());
	    	num.setText(sellItem.getNum());
	    	zhuguan.setText(sellItem.getZhuguan());
	    }
	    
		return localView;
	}
	

}
