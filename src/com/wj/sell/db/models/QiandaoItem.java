package com.wj.sell.db.models;

public class QiandaoItem {
	private int type;
	
	private String date;

	private String name;
	private String qiandaoname;
	
	private String officename;
	private String dateTime;
	private String time;
	private String gps;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getQiandaoname() {
		return qiandaoname;
	}
	public void setQiandaoname(String qiandaoname) {
		this.qiandaoname = qiandaoname;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public String getGps() {
		return gps;
	}
	public void setGps(String gps) {
		this.gps = gps;
	}
	public String getOfficename() {
		return officename;
	}
	public void setOfficename(String officename) {
		this.officename = officename;
	}
	

}
