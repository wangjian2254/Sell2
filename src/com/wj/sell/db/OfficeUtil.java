package com.wj.sell.db;


import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.wj.sell.db.models.Office;
import com.wj.sell.util.Convert;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class OfficeUtil {
	
	private static SQLiteDatabase readdb;
	private static SQLiteDatabase wdb;

	public static boolean deleteOffice(int id,Context context){
		boolean flag=true;
		 synchronized (String.class) {
			 try{
			 getWdb(DBhelper.getDBHelper(context)).execSQL("delete from Office where id="+id+";");
			 
			 }catch(Exception e){
				 flag=false;
			 }
         }
		
		return flag;
		
	}
	
	public static List<Office> getAllOffice(Context context,List <Office> l){
		 l.clear();
		synchronized (String.class) {
			try{
				Cursor cur=getWdb(DBhelper.getDBHelper(context)).rawQuery("select * from Office order by id asc", null);
				cur.moveToFirst();
				Office p=null;
				while(!cur.isAfterLast()){
					p=new Office();
					p.setId(cur.getInt(0));
					p.setName(cur.getString(1));
					p.setFlag(cur.getString(2));
					p.setGps(cur.getString(3));
					p.setAddress(cur.getString(4));
					p.setIsdel("true".equals(cur.getString(5)));
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
	
	
	public static int getTodayOfficeid(Context context){
		int id=-1;
		String date=Convert.format1.format(new Date());
		String[] w={date.substring(0, 10)};
		synchronized (String.class) {
			try{
				Cursor cur=getWdb(DBhelper.getDBHelper(context)).rawQuery("select officeid from xiaoshouoffice where daydate=?", w);
				cur.moveToFirst();
				while(!cur.isAfterLast()){
					id=cur.getInt(0);
					break;
				}
				cur.close();
			}catch(Exception e){
				e.printStackTrace();
				return -1;
			}
		}
		return id;
		
	}
	public static Office getTodayOfficeByid(Context context,int id){
		Office p=null;
		String[] w={String.valueOf(id)};
		synchronized (String.class) {
			try{
				Cursor cur=getWdb(DBhelper.getDBHelper(context)).rawQuery("select * from office where id=?", w);
				cur.moveToFirst();
				while(!cur.isAfterLast()){
					p=new Office();
					p.setId(cur.getInt(0));
					p.setName(cur.getString(1));
					p.setFlag(cur.getString(2));
					p.setGps(cur.getString(3));
					p.setAddress(cur.getString(4));
					p.setIsdel("true".equals(cur.getString(5)));
					cur.moveToNext();
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
	
	public static boolean setTodayOfficeid(Context context,int id){
		String date=Convert.format1.format(new Date()).substring(0, 10);
		synchronized (String.class) {
			try{
				ContentValues cv = new ContentValues();
				 cv.put("daydate", date);
				 cv.put("officeid", id);
				 String[] w={date};
			 int num=getWdb(DBhelper.getDBHelper(context)).update("xiaoshouoffice", cv, "daydate=?", w);
			 if(num==0){
				 getWdb(DBhelper.getDBHelper(context)).insert("xiaoshouoffice", null, cv);
			 }
			}catch(Exception e){
				e.printStackTrace();
				return false;
			}
		}
		return true;
		
	}

	
	
	public static void createOffice(JSONObject jsonobj,Context context){
		if(jsonobj==null){
			return;
		}
		
		try {
			JSONArray Officelist=jsonobj.getJSONArray("result");
			JSONObject q;
			Office office;
			for(int i=0;i<Officelist.length();i++){
				q=(JSONObject) Officelist.get(i);
				office=new Office();
				office.setId(q.getInt("id"));
				office.setName(q.getString("name"));
				office.setFlag(q.getString("flag"));
				office.setGps(q.getString("gps"));
				office.setAddress(q.getString("address"));
				office.setIsdel(q.getBoolean("isdel"));
				
				updateOffice(office, context);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void updateOffice(Office n,Context context){
		synchronized (String.class) {
			 try{
				 ContentValues cv = new ContentValues();
//				 cv.put("id", n.getId());
				 cv.put("name", n.getName());
				 cv.put("flag", n.getFlag());
				 cv.put("gps", n.getGps());
				 cv.put("address", n.getAddress());
				 cv.put("isdel", n.isIsdel()?"true":"false");
				 String[] w={String.valueOf(n.getId())};
			 int num=getWdb(DBhelper.getDBHelper(context)).update("office", cv, "id=?", w);
			 if(num==0){
				 cv.put("id", n.getId());
				 getWdb(DBhelper.getDBHelper(context)).insert("office", null, cv);
			 }
			 if(n.isIsdel()){
				 getWdb(DBhelper.getDBHelper(context)).delete("office", "id=?", w);
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
