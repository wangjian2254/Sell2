package com.wj.sell.db.models;

import java.io.Serializable;
import java.math.BigDecimal;

import android.location.Location;

public class Office implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 11L;
	private int id;
	private String name="";
	private String flag;
	
	private String gps;
	private String address;
	private boolean isdel;
	
	private double juli;
	
//	static Random random1 = new Random(100);
	
	public void getDistance( double lat1,double lon1) {  
		juli=0;
		
//		juli= random1.nextDouble(); 
		if(gps==null){
			return;
		}
        float[] results=new float[1];  
        String[] gpss=gps.split(",");
        if(gpss.length<2){
        	return;
        }
        Location.distanceBetween(lat1, lon1, Double.valueOf(gpss[0]).doubleValue(), Double.valueOf(gpss[1]).doubleValue(), results);  
        juli= results[0];  
    }  
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isIsdel() {
		return isdel;
	}
	public void setIsdel(boolean isdel) {
		this.isdel = isdel;
	}
	public String getGps() {
		return gps;
	}
	public void setGps(String gps) {
		this.gps = gps;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}



	public double getJuli() {
		
		return juli;
	}

	public String getJulimi(){
		BigDecimal bd = new BigDecimal(juli);  
	    return bd.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
	    
	}


	public void setJuli(double juli) {
		this.juli = juli;
	}
}
