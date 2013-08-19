package com.wj.sell.db.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Topic implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 11L;
	private int id;
	private String title="";
	private List<Choice> cl=new ArrayList<Choice>();
	

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

	public List<Choice> getCl() {
		return cl;
	}

	public void setCl(List<Choice> cl) {
		this.cl = cl;
	}
	
}
