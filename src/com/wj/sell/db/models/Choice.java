package com.wj.sell.db.models;

import java.io.Serializable;

public class Choice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 11L;
	private int id;
	private String content="";
	private boolean right;
	
	private int index;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	
	
}
