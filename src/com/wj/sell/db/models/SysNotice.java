package com.wj.sell.db.models;

import java.io.Serializable;

public class SysNotice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 10L;
	private int id;
	private String appcode;
	private String pluginimg;
	private int type;
	private String title ;
	private String lastUpdateTime;
	private String startdate;
	private String enddate;
	private String isread;
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAppcode() {
		return appcode;
	}
	public void setAppcode(String appcode) {
		this.appcode = appcode;
	}
	public String getPluginimg() {
		return pluginimg;
	}
	public void setPluginimg(String pluginimg) {
		this.pluginimg = pluginimg;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public String getStartdate() {
		return startdate;
	}
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public String getIsread() {
		return isread;
	}
	public void setIsread(String isread) {
		this.isread = isread;
	}
	
}
