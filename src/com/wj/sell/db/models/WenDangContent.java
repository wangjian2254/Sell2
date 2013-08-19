package com.wj.sell.db.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class WenDangContent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 21L;
	private int id;
	private String title;
	private String content;
	private String dateTime;
	private String kindName;
	private int kind;
	private int show;
	
	private List<WenDangImage> wl=new ArrayList<WenDangImage>();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public String getKindName() {
		return kindName;
	}
	public void setKindName(String kindName) {
		this.kindName = kindName;
	}
	
	public int getShow() {
		return show;
	}
	public void setShow(int show) {
		this.show = show;
	}
	public int getKind() {
		return kind;
	}
	public void setKind(int kind) {
		this.kind = kind;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public List<WenDangImage> getWl() {
		return wl;
	}
	public void setWl(List<WenDangImage> wl) {
		this.wl = wl;
	}

	
	
}
