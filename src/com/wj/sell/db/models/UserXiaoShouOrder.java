package com.wj.sell.db.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserXiaoShouOrder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 11L;
	private int id;
	private int officeid;
	private int serverid;
	private boolean submit=false;
	private String productflag;
	private String productname="";
	private String imie="";
	private String tel="";
	private String typeflag;
	private String typename="";
	private String clientdate;
	private String clienttime;
	private List<String> giftsflag=new ArrayList<String>();
	private List<String> giftsname=new ArrayList<String>();
	
	
	public void setGifts(String s){
		for(String str:s.split(",")){
			if(!"".equals(str)){
				giftsflag.add(str);
			}
		}
	}
	public void setGName(String s){
		for(String str:s.split(",")){
			if(!"".equals(str)){
				giftsname.add(str);
			}
		}
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getOfficeid() {
		return officeid;
	}
	public void setOfficeid(int officeid) {
		this.officeid = officeid;
	}
	public String getProductflag() {
		return productflag;
	}
	public void setProductflag(String productflag) {
		this.productflag = productflag;
	}
	public String getImie() {
		return imie;
	}
	public void setImie(String imie) {
		this.imie = imie;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getTypeflag() {
		return typeflag;
	}
	public void setTypeflag(String typeflag) {
		this.typeflag = typeflag;
	}
	
	public String getProductname() {
		return productname;
	}
	public void setProductname(String productname) {
		this.productname = productname;
	}
	public String getTypename() {
		return typename;
	}
	public void setTypename(String typename) {
		this.typename = typename;
	}
	public List<String> getGiftsflag() {
		return giftsflag;
	}
	public void setGiftsflag(List<String> giftsflag) {
		this.giftsflag = giftsflag;
	}
	public List<String> getGiftsname() {
		return giftsname;
	}
	public void setGiftsname(List<String> giftsname) {
		this.giftsname = giftsname;
	}
	public String getClientdate() {
		return clientdate;
	}
	public void setClientdate(String clientdate) {
		this.clientdate = clientdate;
	}
	public String getClienttime() {
		return clienttime;
	}
	public void setClienttime(String clienttime) {
		this.clienttime = clienttime;
	}
	public int getServerid() {
		return serverid;
	}
	public void setServerid(int serverid) {
		this.serverid = serverid;
	}
	public boolean isSubmit() {
		return submit;
	}
	public void setSubmit(boolean submit) {
		this.submit = submit;
	}
	
	
	
	
	
}
