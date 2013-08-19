package com.wj.sell.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wj.sell.R;
import com.wj.sell.db.QiandaoUtil;
import com.wj.sell.db.models.Qiandao;
import com.wj.sell.util.Convert;

public class QiandaoAdapter extends BaseAdapter {
	private Context mContext;
	List<Qiandao> qiandaolist=new ArrayList<Qiandao>();
	private LayoutInflater mLayoutInflater = null;
//	List<String> imgarrlistcode;
	
//	Pattern pattern = Pattern.compile(Convert.imgreg);
//	Matcher matcher = null;

	public QiandaoAdapter(Context c) {
		mContext = c;
		mLayoutInflater = LayoutInflater.from(mContext);
		
	}
	
	public void getDate(){
		qiandaolist.clear();
		QiandaoUtil.getAllQiandao(mContext, qiandaolist);
		this.notifyDataSetChanged();
	}
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return qiandaolist.size();
	}
	
	public Qiandao getQiandao(int position){
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

		Qiandao qiandao=qiandaolist.get(position);
		View localView = convertView;
		//判断当前view视图参数是否为null
	    if (localView == null){
	        //加载一级视图的布局文件
	    	localView = mLayoutInflater.inflate(R.layout.qiandao_item, null);
	    }
		TextView name=(TextView)localView.findViewById(R.id.recent_name);
		TextView date = (TextView)localView.findViewById(R.id.recent_info);
		name.setText(qiandao.getName());
		if(qiandao.getLastEventDate()!=null&&Convert.format1.format(new Date()).substring(0, 10).equals(qiandao.getLastEventDate().substring(0, 10))){
			date.setText(qiandao.getLastEventDate());
		}else{
			date.setText("今天还没签到！");
		}
		
		return localView;
	}
	

}
