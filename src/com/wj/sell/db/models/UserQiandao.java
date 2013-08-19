package com.wj.sell.db.models;

import java.io.Serializable;

public class UserQiandao implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 11L;
	private int id;
	private int qiandaoid;
	private int officeid;
	private String qiandaoname;
	private String officename;
	private String time;
	private String gps;
	private String address;
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getQiandaoid() {
		return qiandaoid;
	}
	public void setQiandaoid(int qiandaoid) {
		this.qiandaoid = qiandaoid;
	}
	public int getOfficeid() {
		return officeid;
	}
	public void setOfficeid(int officeid) {
		this.officeid = officeid;
	}
	public String getQiandaoname() {
		return qiandaoname;
	}
	public void setQiandaoname(String qiandaoname) {
		this.qiandaoname = qiandaoname;
	}
	public String getOfficename() {
		return officename;
	}
	public void setOfficename(String officename) {
		this.officename = officename;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
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
	
	
	
	
}
