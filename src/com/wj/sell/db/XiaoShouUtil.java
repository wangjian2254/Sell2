package com.wj.sell.db;


import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.wj.sell.db.models.Office;
import com.wj.sell.db.models.Qiandao;
import com.wj.sell.db.models.UserQiandao;
import com.wj.sell.db.models.UserXiaoShouOrder;
import com.wj.sell.util.Convert;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class XiaoShouUtil {
	
	private static SQLiteDatabase readdb;
	private static SQLiteDatabase wdb;

	public static boolean deleteXiaoShou(int id,Context context){
		boolean flag=true;
		 synchronized (String.class) {
			 try{
			 getWdb(DBhelper.getDBHelper(context)).execSQL("delete from xiaoshouoorder where id="+id+";");
			 
			 }catch(Exception e){
				 flag=false;
			 }
         }
		
		return flag;
		
	}
	public static boolean deleteXiaoShouByServerid(int id,Context context){
		boolean flag=true;
		synchronized (String.class) {
			try{
				getWdb(DBhelper.getDBHelper(context)).execSQL("delete from xiaoshouoorder where serverid="+id+";");
				
			}catch(Exception e){
				flag=false;
			}
		}
		
		return flag;
		
	}
	

	
	public static List<UserXiaoShouOrder> getAllXiaoShou(Context context,List <UserXiaoShouOrder> l){
		 l.clear();
		synchronized (String.class) {
			try{
				Cursor cur=getWdb(DBhelper.getDBHelper(context)).rawQuery("select * from xiaoshouoorder order by id asc", null);
				cur.moveToFirst();
				UserXiaoShouOrder p=null;
				while(!cur.isAfterLast()){
					p=new UserXiaoShouOrder();
					p.setId(cur.getInt(0));
					p.setClientdate(cur.getString(1));
					p.setClienttime(cur.getString(2));
					p.setProductflag(cur.getString(3));
					p.setProductname(cur.getString(4));
					p.setTypeflag(cur.getString(5));
					p.setTypename(cur.getString(6));
					p.setGifts(cur.getString(7));
					p.setGName(cur.getString(8));
					p.setImie(cur.getString(9));
					p.setTel(cur.getString(10));
					p.setOfficeid(cur.getInt(11));
					p.setServerid(cur.getInt(12));
					p.setSubmit("true".equals(cur.getString(13)));
					l.add(p);
					cur.moveToNext();
				}
				cur.close();
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}
		}
		return l;
		
	}
	
	
	public static List<UserXiaoShouOrder> getAllXiaoShouToday(Context context,List <UserXiaoShouOrder> l){
		l.clear();
		String[] w={Convert.format1.format(new Date()).substring(0, 10),"false"};
		synchronized (String.class) {
			try{
				Cursor cur=getWdb(DBhelper.getDBHelper(context)).rawQuery("select * from xiaoshouoorder where clientdate=? or issubmit=? order by id asc", w);
				cur.moveToFirst();
				UserXiaoShouOrder p=null;
				while(!cur.isAfterLast()){
					p=new UserXiaoShouOrder();
					p.setId(cur.getInt(0));
					p.setClientdate(cur.getString(1));
					p.setClienttime(cur.getString(2));
					p.setProductflag(cur.getString(3));
					p.setProductname(cur.getString(4));
					p.setTypeflag(cur.getString(5));
					p.setTypename(cur.getString(6));
					p.setGifts(cur.getString(7));
					p.setGName(cur.getString(8));
					p.setImie(cur.getString(9));
					p.setTel(cur.getString(10));
					p.setOfficeid(cur.getInt(11));
					p.setServerid(cur.getInt(12));
					p.setSubmit("true".equals(cur.getString(13)));
					l.add(p);
					cur.moveToNext();
				}
				cur.close();
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}
		}
		return l;
		
	}
//	
//	public static UserXiaoShouOrder getUserQiandao(int id,Context context){
//		UserXiaoShouOrder p=null;
//		synchronized (String.class) {
//			try{
//				String[] w={String.valueOf(id)};
//				Cursor cur=getWdb(DBhelper.getDBHelper(context)).rawQuery("select * from userqiandao where id=?", w);
//				cur.moveToFirst();
//				while(!cur.isAfterLast()){
//					p=new UserXiaoShouOrder();
//					p.setId(cur.getInt(0));
//					p.setClientdate(cur.getString(1));
//					p.setClienttime(cur.getString(2));
//					p.setProductflag(cur.getString(3));
//					p.setProductname(cur.getString(4));
//					p.setTypeflag(cur.getString(5));
//					p.setTypename(cur.getString(6));
//					p.setGifts(cur.getString(7));
//					p.setGName(cur.getString(8));
//					p.setImie(cur.getString(9));
//					p.setTel(cur.getString(10));
//					p.setOfficeid(cur.getInt(11));
//					p.setServerid(cur.getInt(12));
//					p.setSubmit("true".equals(cur.getString(13)));
//					break;
//				}
//				cur.close();
//			}catch(Exception e){
//				e.printStackTrace();
//				return null;
//			}
//		}
//		return p;
//	}
//	
	public static void updateUserXiaoShouOrder(UserXiaoShouOrder n,Context context){
		synchronized (String.class) {
			 try{
				 ContentValues cv = new ContentValues();
				 cv.put("clientdate", n.getClientdate());
				 cv.put("clienttime", n.getClienttime());
				 cv.put("productflag", n.getProductflag());
				 cv.put("productname", n.getProductname());
				 cv.put("typeflag", n.getTypeflag());
				 cv.put("typename", n.getTypename());
				 String str="";
				 for(String s:n.getGiftsflag()){
					 str+=s+",";
				 }
				 cv.put("giftsflag", str);
				  str="";
				 for(String s:n.getGiftsname()){
					 str+=s+",";
				 }
				 cv.put("giftsname", str);
				 cv.put("imie", n.getImie());
				 cv.put("tel", n.getTel());
				 cv.put("officeid", n.getOfficeid());
				 if(n.getServerid()!=0){
					 cv.put("serverid", n.getServerid());
				 }
				 cv.put("issubmit", n.isSubmit()?"true":"false");
				
				 if(n.getId()<1){
					 long id=getWdb(DBhelper.getDBHelper(context)).insert("xiaoshouoorder", null,cv);
					 if(id>0){
						 n.setId(Integer.valueOf(String.valueOf(id)).intValue());
					 }
				 }else{
					 String[] w={String.valueOf(n.getId())};
					 getWdb(DBhelper.getDBHelper(context)).update("xiaoshouoorder", cv, "id=?", w);
				 }
			 
			 }catch(Exception e){
				 e.printStackTrace();
			 }
        }
		
	}
	public static void updateQiandaoEventDate(Qiandao n,Context context){
		synchronized (String.class) {
			try{
				ContentValues cv = new ContentValues();
//				 cv.put("id", n.getId());
				cv.put("lastEventDate", n.getLastEventDate());
				 cv.put("eventid",n.getEventid());
				cv.put("officeid", n.getOfficeid());
				cv.put("lastOfficeName", n.getLastOfficeName());
				
				String[] w={String.valueOf(n.getId())};
				getWdb(DBhelper.getDBHelper(context)).update("qiandao", cv, "id=?", w);
				
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}
	
	public static void createQiandao(JSONObject jsonobj,Context context){
		if(jsonobj==null){
			return;
		}
		
		try {
			JSONArray qiandaolist=jsonobj.getJSONArray("result");
			JSONObject q;
			Qiandao qiandao;
			for(int i=0;i<qiandaolist.length();i++){
				q=(JSONObject) qiandaolist.get(i);
				qiandao=new Qiandao();
				qiandao.setId(q.getInt("id"));
				qiandao.setName(q.getString("name"));
				qiandao.setNeedTime(q.getBoolean("needTime"));
				qiandao.setNeedGPS(q.getBoolean("needGPS"));
				qiandao.setNeedAddress(q.getBoolean("needAddress"));
				qiandao.setIsdel(q.getBoolean("isdel"));
				
				updateQiandao(qiandao, context);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void updateQiandao(Qiandao n,Context context){
		synchronized (String.class) {
			 try{
				 ContentValues cv = new ContentValues();
//				 cv.put("id", n.getId());
				 cv.put("name", n.getName());
				 cv.put("needTime", n.isNeedTime()?"true":"false");
				 cv.put("needGPS", n.isNeedGPS()?"true":"false");
				 cv.put("needAddress", n.isNeedAddress()?"true":"false");
				 cv.put("isdel", n.isIsdel()?"true":"false");
				 String[] w={String.valueOf(n.getId())};
			 int num=getWdb(DBhelper.getDBHelper(context)).update("qiandao", cv, "id=?", w);
			 if(num==0){
				 cv.put("id", n.getId());
				 getWdb(DBhelper.getDBHelper(context)).insert("qiandao", null, cv);
			 }
			 if(n.isIsdel()){
				 getWdb(DBhelper.getDBHelper(context)).delete("qiandao", "id=?", w);
			 }
			 
			 }catch(Exception e){
				 e.printStackTrace();
			 }
        }
		
	}
	

	
	
	private static SQLiteDatabase getReaddb(ActiveUserHelper auh) {
		if (readdb == null || !readdb.isOpen()) {
			readdb = auh.getReadableDatabase();
		}
		return readdb;
	}
	private static SQLiteDatabase getWdb(ActiveUserHelper auh) {
		if (wdb == null || !wdb.isOpen()) {
			wdb = auh.getWritableDatabase();
		}
		return wdb;
	}
}
