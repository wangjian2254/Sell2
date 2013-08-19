package com.wj.sell.db;


import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.wj.sell.db.models.Office;
import com.wj.sell.db.models.Qiandao;
import com.wj.sell.db.models.UserQiandao;
import com.wj.sell.util.Convert;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class QiandaoUtil {
	
	private static SQLiteDatabase readdb;
	private static SQLiteDatabase wdb;

	public static boolean deleteQiandao(int id,Context context){
		boolean flag=true;
		 synchronized (String.class) {
			 try{
			 getWdb(DBhelper.getDBHelper(context)).execSQL("delete from qiandao where id="+id+";");
			 
			 }catch(Exception e){
				 flag=false;
			 }
         }
		
		return flag;
		
	}
	

	public static Office getOfficeByToday(Context context){
		Office p=null;
		String[] date={Convert.format1.format(new Date()).substring(0, 10)+"%"};
		synchronized (String.class) {
			try{
				Cursor cur=getWdb(DBhelper.getDBHelper(context)).rawQuery("select lastOfficeName,Officeid from qiandao where lastEventDate like ?", date);
				cur.moveToFirst();
				while(!cur.isAfterLast()){
					p=new Office();
					p.setName(cur.getString(0));
					p.setId(cur.getInt(1));
					break;
				}
				cur.close();
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}
		}
		return p;
		
	}
	
	public static List<Qiandao> getAllQiandao(Context context,List <Qiandao> l){
		 l.clear();
		synchronized (String.class) {
			try{
				Cursor cur=getWdb(DBhelper.getDBHelper(context)).rawQuery("select * from qiandao order by id asc", null);
				cur.moveToFirst();
				Qiandao p=null;
				while(!cur.isAfterLast()){
					p=new Qiandao();
					p.setId(cur.getInt(0));
					p.setName(cur.getString(1));
					p.setNeedTime("true".equals(cur.getString(2)));
					p.setNeedGPS("true".equals(cur.getString(3)));
					p.setNeedAddress("true".equals(cur.getString(4)));
					p.setIsdel("true".equals(cur.getString(5)));
					p.setLastEventDate(cur.getString(6));
					p.setEventid(cur.getInt(7));
					p.setLastOfficeName(cur.getString(8));
					p.setOfficeid(cur.getInt(9));
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
	
	public static UserQiandao getUserQiandao(int id,Context context){
		UserQiandao p=null;
		synchronized (String.class) {
			try{
				String[] w={String.valueOf(id)};
				Cursor cur=getWdb(DBhelper.getDBHelper(context)).rawQuery("select * from userqiandao where id=?", w);
				cur.moveToFirst();
				while(!cur.isAfterLast()){
					p=new UserQiandao();
					p.setId(cur.getInt(0));
					p.setQiandaoid(cur.getInt(1));
					p.setQiandaoname(cur.getString(2));
					p.setOfficeid(cur.getInt(3));
					p.setOfficename(cur.getString(4));
					p.setTime(cur.getString(5));
					p.setGps(cur.getString(6));
					p.setAddress(cur.getString(7));
					break;
				}
				cur.close();
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}
		}
		return p;
	}
	
	public static int updateQiandaoEvent(UserQiandao n,Context context){
		long id=-1;
		synchronized (String.class) {
			 try{
				 ContentValues cv = new ContentValues();
//				 cv.put("id", n.getId());
				 cv.put("qiandaoid", n.getQiandaoid());
				 cv.put("officeid", n.getOfficeid());
				 cv.put("qiandaoname", n.getQiandaoname());
				 cv.put("officename", n.getOfficename());
				 cv.put("time", n.getTime());
				 cv.put("gps", n.getGps());
				 cv.put("address", n.getAddress());
				 
				 
			 id=getWdb(DBhelper.getDBHelper(context)).insert("userqiandao", null, cv);
			 
			 
			 }catch(Exception e){
				 e.printStackTrace();
			 }
        }
		return Integer.valueOf(""+id).intValue();
		
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
