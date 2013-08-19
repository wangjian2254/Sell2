package com.wj.sell.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.wj.sell.controls.AppItem;
import com.wj.sell.db.OfficeUtil;
import com.wj.sell.db.models.Office;
import com.wj.sell.Main;
import com.wj.sell.R;
import com.wj.sell.util.Convert;


import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class OfficeAdapter extends BaseAdapter {
	private Context mContext;
	List<Office> officelist=new ArrayList<Office>();
	private LayoutInflater mLayoutInflater = null;
//	List<String> imgarrlistcode;
	
//	Pattern pattern = Pattern.compile(Convert.imgreg);
//	Matcher matcher = null;
	
	private boolean isShowPosite=false;
	public double lat1=0.0;
	public double lon1=0.0;

	public OfficeAdapter(Context c) {
		mContext = c;
		mLayoutInflater = LayoutInflater.from(mContext);
		
	}
	
	public void getData(){
		this.isShowPosite=false;
		officelist.clear();
		OfficeUtil.getAllOffice(mContext, officelist);
		this.notifyDataSetChanged();
	}
	
	public void setGPS(){
		if(lat1!=0.0&&lon1!=0.0){
			
			this.isShowPosite=true;
			for(Office o:officelist){
				o.getDistance(lat1,lon1);
			}
			ComparatorOffice o=new ComparatorOffice();
			Collections.sort(officelist, o);
			this.notifyDataSetChanged();
		}
	}
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return officelist.size();
	}
	
	public Office getOffice(int position){
		return officelist.get(position);
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
	
	public double getDistance( String lat2, String lon2) {  
        float[] results=new float[1];  
        Location.distanceBetween(lat1, lon1, Double.valueOf(lat2).doubleValue(), Double.valueOf(lon2).doubleValue(), results);  
        return results[0];  
    }  

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		Office office=officelist.get(position);
		View localView = convertView;
		//判断当前view视图参数是否为null
	    if (localView == null){
	        //加载一级视图的布局文件
	    	localView = mLayoutInflater.inflate(R.layout.qiandao_item, null);
	    }
		TextView name=(TextView)localView.findViewById(R.id.recent_name);
		TextView date = (TextView)localView.findViewById(R.id.recent_info);
		name.setText((position+1)+"."+office.getName());
		
		if(!isShowPosite){
			date.setVisibility(View.GONE);
		}else{
			date.setVisibility(View.GONE);
			if(office.getJuli()>=0&&office.getGps()!=null&&!"".equals(office.getGps())){
				date.setText("距离厅台还有 "+office.getJulimi()+" 米");
			}else{
				date.setText("信息不足，无法计算。");
			}
		}
		
		return localView;
	}
	
	public class ComparatorOffice implements Comparator<Office>{

		 public int compare(Office arg0, Office arg1) {
		 

		   //首先比较年龄，如果年龄相同，则比较名字

		  if(arg0.getGps()==null||"".equals(arg0.getGps())){
		   return 1;
		  }else{
			  if((arg0.getJuli()-arg1.getJuli())>0){
				  return 1;
			  }
			  if((arg0.getJuli()-arg1.getJuli())<0){
				  return -1;
			  }
			  if((arg0.getJuli()-arg1.getJuli())==0.0){
				  return 0;
			  }
			  return 0;
		  }  
		 }
		 
		}
}
