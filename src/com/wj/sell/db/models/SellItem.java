package com.wj.sell.db.models;

public class SellItem {
	private int type;
	
	private String date;

	private String name;
	private String pinpai;
	
	private String jixing;
	private String leixing;
	private String num;
	private String zhuguan;
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
	
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getPinpai() {
		return pinpai;
	}
	public void setPinpai(String pinpai) {
		this.pinpai = pinpai;
	}
	public String getJixing() {
		return jixing;
	}
	public void setJixing(String jixing) {
		this.jixing = jixing;
	}
	public String getLeixing() {
		return leixing;
	}
	public void setLeixing(String leixing) {
		this.leixing = leixing;
	}
	public String getZhuguan() {
		return zhuguan;
	}
	public void setZhuguan(String zhuguan) {
		this.zhuguan = zhuguan;
	}
	

}
