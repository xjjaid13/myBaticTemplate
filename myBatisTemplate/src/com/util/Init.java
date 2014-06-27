package com.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import org.springframework.stereotype.Component;

public final class Init {
	
	/** 项目路径 */
	public static String realPath = "";
	
	public static HashMap<String, String> hm = new HashMap<String,String>();
	
	public static HashMap<Integer,HashMap<String,Integer>> keywordHm = new HashMap<Integer,HashMap<String,Integer>>();
	
	public final void init(String realPath){
		realPath = DataHandle.autoFillPath(realPath.replaceAll("\\\\","/").replaceAll("//","/"));
		Init.realPath = realPath;
		String config_path = realPath + "WEB-INF/config.ini";
		Properties pp = new Properties();
		try {
			pp.load(new FileInputStream(config_path));
			Iterator<Object> it = pp.keySet().iterator();
			while(it.hasNext()){
				String key = (String)it.next();
				hm.put(key, pp.getProperty(key));
			}
		} catch (FileNotFoundException e) {
			Log.Error("Init.init FileNotFoundException : " + e.getMessage());
		} catch (IOException e) {
			Log.Error("Init.init IOException : " + e.getMessage());
		}
	}
	
}
