package com.wj.sell.db.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class KaoShi implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 11L;
	private int id;
	private String name="";
	private String dateTime;
	
	private int time;
	private int score;
	private int type=0;
	
	private List<Topic> tl=new ArrayList<Topic>();
	
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

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public List<Topic> getTl() {
		return tl;
	}

	public void setTl(List<Topic> tl) {
		this.tl = tl;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
}
