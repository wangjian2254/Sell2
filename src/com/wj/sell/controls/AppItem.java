package com.wj.sell.controls;



import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;

import com.wj.sell.Main;
import com.wj.sell.R;
import com.wj.sell.util.Convert;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class AppItem extends LinearLayout {
	TextView title;
	ImageView app_item_img;
//	Button app_item_img;
	String code=null;
	Main con;
	Pattern pattern = Pattern.compile(Convert.imgreg);
	Matcher matcher = null;

	public AppItem(Main context) {
		super(context);
		con=context;
		LayoutInflater.from(context).inflate(R.layout.app_item, this, true);
		title = (TextView) findViewById(R.id.app_item_text);
		app_item_img=(ImageView)findViewById(R.id.app_item_img);
		// TODO Auto-generated constructor stub
	}

	public AppItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setImg(Drawable d){
		if(d==null){
			app_item_img.setImageResource(R.drawable.icon);
			return ;
		}
		app_item_img.setImageDrawable(d);
	}
	
	public void setImg(int d){
		app_item_img.setImageResource(d);
	}
	public void setImg(String d){
		try {
			app_item_img.setImageBitmap(BitmapFactory.decodeStream(con.getAssets().open(d+".png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setTitle(String str){
		
		
		title.setText(str);
	}
	
//	public void setCode(String c){
//		this.code=c;
//	}

//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		con.setList(this.code);
//		
//	}
	

	

}
