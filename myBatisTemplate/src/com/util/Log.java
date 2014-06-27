package com.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/** log4j日志类 */
public class Log {
	
	private static Logger logger = Logger.getRootLogger();
	
	public static Logger getLogger(){
		return logger;
	}
	
	/** 设置日志等级 */
	public static void configLevel(Level level){
		Log.getLogger().setLevel(level);
	}
	
	/** 记录错误日志 */
	public static void Error(String errorinfo){
		Log.getLogger().error(TimeHandle.currentTime() + " : " + errorinfo);
	}
	
	/** 记录debug日志 */
	public static void Debug(String debuginfo){
		Log.getLogger().debug(debuginfo);
	}
	
	/** 记录info日志 */
	public static void Info(String info){
		Log.getLogger().info(info);
	}
	
	 public static void main(String args[])  
	  throws Exception{  
	  
	    String candidate =  
	     "000";  
	    String regex = "^((0{1})|([1-9]\\d*))$";  
	    Pattern p = Pattern.compile(regex);  
	    Matcher m = p.matcher(candidate);  
	    System.out.println(m.find());
	  }  

}
