package com.github.iamutkarshtiwari.bakingapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Map;

/**
 * Created by utkarshtiwari on 07/02/2018.
 */
public class AppParameters {

	SharedPreferences AppSettings;
	Editor editor;
	public static final String APP_PREFERENCES = "AppPrefs";
	Context context;

	public AppParameters(Context context)
	{
		this.context = context;		
	}

	private void openConnection() 
	{
		AppSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
		editor = AppSettings.edit();
	}

	private void closeConnection() 
	{
		editor = null;
		AppSettings = null;
	}
	
	public void setInt(int value,String key)
	{
		openConnection();		
		editor.putInt(key, value);		
		editor.commit();
		closeConnection();
	}
	public void setLoong(long value,String key)
	{
		openConnection();
		editor.putLong(key, value);
		editor.commit();
		closeConnection();
	}
	public void setFloat(float value,String key)
	{
		openConnection();		
		editor.putFloat(key, value);		
		editor.commit();
		closeConnection();
	}
	public void setString(String value,String key)
	{
		openConnection();		
		editor.putString(key, value);		
		editor.commit();
		closeConnection();
	}
	
	
	public void setBoolean(Boolean value,String key) 
	{
		openConnection();
		editor.putBoolean(key, value);
		editor.commit();
		closeConnection();
	}
	
	
	public boolean getBoolean(String key,boolean defValue) 
	{
		boolean result = defValue;
		openConnection();

		if (AppSettings.contains(key)) {
			result = AppSettings.getBoolean(key, defValue);
		}

		closeConnection();
		return result;
	}
	
	public int getInt(String key) 
	{
		int result = 0;
		openConnection();

		if (AppSettings.contains(key)) {
			result = AppSettings.getInt(key, 0);
		}

		closeConnection();
		return result;
	}
	
	public int getInt(String key,int defValue) 
	{
		int result = defValue;
		openConnection();

		if (AppSettings.contains(key)) {
			result = AppSettings.getInt(key, defValue);
		}

		closeConnection();
		return result;
	}
	
	public float getFloat(String key) 
	{
		float result = 0;
		openConnection();

		if (AppSettings.contains(key)) {
			result = AppSettings.getFloat(key, 0);
		}

		closeConnection();
		return result;
	}
	public String getString(String key) 
	{
		String result = "";
		openConnection();

		if (AppSettings.contains(key)) {
			result = AppSettings.getString(key, "");
		}

		closeConnection();
		return result;
	}

	public String getString(String key,String strdefault)
	{
		String result = strdefault;
		openConnection();

		if (AppSettings.contains(key)) {
			result = AppSettings.getString(key, strdefault);
		}

		closeConnection();
		return result;
	}
	public long getLoong(String key,long defValue)
	{
		long result = defValue;
		openConnection();

		if (AppSettings.contains(key)) {
			result = AppSettings.getLong(key, defValue);
		}else{
			setLoong(defValue,key);
		}

		closeConnection();
		return result;
	}



	public String getAllSettings() {
		openConnection();

		Map<String,?> keys = AppSettings.getAll();

		String allSettings=keys.toString();
		closeConnection();
		return allSettings;
	}
}
