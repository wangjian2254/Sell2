package com.wj.sell.db.models;

import java.io.Serializable;

public class Qiandao implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 11L;
	private int id;
	private String name;
	private boolean needTime ;
	private boolean needGPS ;
	private boolean needAddress ;
	private boolean isdel;
	private int eventid;
	private int officeid;
	private String lastEventDate;
	private String lastOfficeName;
	
	
	
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
	public boolean isNeedTime() {
		return needTime;
	}
	public void setNeedTime(boolean needTime) {
		this.needTime = needTime;
	}
	public boolean isNeedGPS() {
		return needGPS;
	}
	public void setNeedGPS(boolean needGPS) {
		this.needGPS = needGPS;
	}
	public boolean isNeedAddress() {
		return needAddress;
	}
	public void setNeedAddress(boolean needAddress) {
		this.needAddress = needAddress;
	}
	public boolean isIsdel() {
		return isdel;
	}
	public void setIsdel(boolean isdel) {
		this.isdel = isdel;
	}
	public String getLastEventDate() {
		return lastEventDate;
	}
	public void setLastEventDate(String lastEventDate) {
		this.lastEventDate = lastEventDate;
	}
	public int getEventid() {
		return eventid;
	}
	public void setEventid(int eventid) {
		this.eventid = eventid;
	}
	public int getOfficeid() {
		return officeid;
	}
	public void setOfficeid(int officeid) {
		this.officeid = officeid;
	}
	public String getLastOfficeName() {
		return lastOfficeName;
	}
	public void setLastOfficeName(String lastOfficeName) {
		this.lastOfficeName = lastOfficeName;
	}
	
	
}
