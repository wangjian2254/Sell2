package com.wj.sell.db;


import java.util.List;


import com.wj.sell.db.models.SysNotice;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SysNoticeUtil {
	
	private static SQLiteDatabase readdb;
	private static SQLiteDatabase wdb;

	public static boolean deleteNotice(int id,Context context){
		boolean flag=true;
		 synchronized (String.class) {
			 try{
			 getWdb(DBhelper.getDBHelper(context)).execSQL("delete from sysnotice where id="+id+";");
			 
			 }catch(Exception e){
				 flag=false;
			 }
         }
		
		return flag;
		
	}
	public static int needReadNotice(Context context){
		int num=0;
		synchronized (String.class) {
			try{
				Cursor cur=getWdb(DBhelper.getDBHelper(context)).rawQuery("select count(*) as num from sysnotice where isread='no';",null);
				cur.moveToFirst();
				while(!cur.isAfterLast()){
					num=cur.getInt(0);
					cur.moveToNext();
					break;
				}
				cur.close();
			}catch(Exception e){
			}
		}
		
		return num;
		
	}
	public static List<SysNotice> getAllNotice(Context context,List <SysNotice> l){
		 l.clear();
		synchronized (String.class) {
			try{
				Cursor cur=getWdb(DBhelper.getDBHelper(context)).rawQuery("select * from sysnotice order by lastUpdateTime desc", null);
				cur.moveToFirst();
				SysNotice p=null;
				while(!cur.isAfterLast()){
					p=new SysNotice();
					p.setId(cur.getInt(0));
					p.setAppcode(cur.getString(1));
					p.setPluginimg(cur.getString(2));
					p.setType(cur.getInt(3));
					p.setTitle(cur.getString(4));
					p.setLastUpdateTime(cur.getString(5));
					p.setStartdate(cur.getString(6));
					p.setEnddate(cur.getString(7));
					p.setIsread(cur.getString(8));
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
	
	public static String getNoticeTimeline(Context context){
		String timeline="0";
		synchronized (String.class) {
			try{
				Cursor cur=getWdb(DBhelper.getDBHelper(context)).rawQuery("select lastUpdateTime from noticeupdate where id=1;", null);
				cur.moveToFirst();
				while(!cur.isAfterLast()){
					timeline=cur.getString(0);
					cur.moveToNext();
					break;
				}
				cur.close();
			}catch(Exception e){
				e.printStackTrace();
				return "0";
			}
		}
		return timeline;
		
	}
	public static void updateNoticeTimeLine(String n,Context context){
		synchronized (String.class) {
			 try{
				 ContentValues cv = new ContentValues();
				 cv.put("lastUpdateTime", n);
				
				
			 int num=getWdb(DBhelper.getDBHelper(context)).update("noticeupdate", cv, "id=1", null);
			 if(num==0){
				 cv.put("id", 1);
				 getWdb(DBhelper.getDBHelper(context)).insert("noticeupdate", null, cv);
			 }
			 }catch(Exception e){
				 e.printStackTrace();
			 }
        }
		
	}
	
	public static void updateNotice(SysNotice n,Context context){
//		synchronized (String.class) {
			 try{
				 ContentValues cv = new ContentValues();
//				 cv.put("id", n.getId());
				 cv.put("appcode", n.getAppcode());
				 cv.put("pluginimg", n.getPluginimg());
				 cv.put("type", n.getType());
				 cv.put("title", n.getTitle());
				 cv.put("lastUpdateTime", n.getLastUpdateTime());
				 cv.put("startdate", n.getStartdate());
				 cv.put("enddate", n.getEnddate());
				 cv.put("isread", n.getIsread());
				 String[] w={String.valueOf(n.getId())};
			 int num=getWdb(DBhelper.getDBHelper(context)).update("sysnotice", cv, "id=?", w);
			 if(num==0){
				 cv.put("id", n.getId());
				 getWdb(DBhelper.getDBHelper(context)).insert("sysnotice", null, cv);
			 }
			 
			 }catch(Exception e){
				 e.printStackTrace();
			 }
//        }
		
	}
	public static void readNotice(SysNotice n,Context context){
		synchronized (String.class) {
		try{
			ContentValues cv = new ContentValues();
//				 cv.put("id", n.getId());
			cv.put("isread", n.getIsread());
			String[] w={String.valueOf(n.getId())};
			int num=getWdb(DBhelper.getDBHelper(context)).update("sysnotice", cv, "id=?", w);
			
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
