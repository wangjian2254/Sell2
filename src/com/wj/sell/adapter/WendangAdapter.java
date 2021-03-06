package com.wj.sell.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wj.sell.R;
import com.wj.sell.db.models.WenDang;

public class WendangAdapter extends BaseAdapter {
	private Context mContext;
	List<WenDang> qiandaolist=new ArrayList<WenDang>();
	private LayoutInflater mLayoutInflater = null;
//	List<String> imgarrlistcode;
	
//	Pattern pattern = Pattern.compile(Convert.imgreg);
//	Matcher matcher = null;

	public WendangAdapter(Context c,List<WenDang> list) {
		mContext = c;
		this.qiandaolist=list;
		mLayoutInflater = LayoutInflater.from(mContext);
		
	}
	
	
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return qiandaolist.size();
	}
	
	public WenDang getWenDang(int position){
		return qiandaolist.get(position);
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

		WenDang qiandao=qiandaolist.get(position);
		View localView = convertView;
		//判断当前view视图参数是否为null
	    if (localView == null){
	        //加载一级视图的布局文件
	    	localView = mLayoutInflater.inflate(R.layout.qiandao_item, null);
	    }
		TextView name=(TextView)localView.findViewById(R.id.recent_name);
		TextView date = (TextView)localView.findViewById(R.id.recent_info);
		if(qiandao.getType()==0){
			name.setText(qiandao.getName());
			date.setVisibility(View.GONE);
		}else{
			name.setText(qiandao.getTitle());
			date.setVisibility(View.VISIBLE);
			date.setText("发布于："+qiandao.getDateTime()+" 访问次数："+qiandao.getShow()+"次");
		}
		
		
		
		return localView;
	}
	

}
