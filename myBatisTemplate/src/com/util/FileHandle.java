package com.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * @author Cloud
 * */
public class FileHandle {

	/** 创建文件 */
	public static boolean createPath(String strPath){
		File file = new File(strPath);
		boolean b = false;
		if(!file.exists()){
			b = file.mkdirs();
		}
		return b;
	}
	
	public static void copyFile(File sourceFile,File desFile){
		FileInputStream input = null;
		BufferedInputStream inBuff = null;
		FileOutputStream output = null;
		BufferedOutputStream outBuff = null;
		try{
			input = new FileInputStream(sourceFile);
		    inBuff = new BufferedInputStream(input);
	        output = new FileOutputStream(desFile);
	        outBuff = new BufferedOutputStream(output);
	        byte[] b = new byte[1024 * 5];
	        int len;
	        while ((len =inBuff.read(b)) != -1) {
	            outBuff.write(b, 0, len);
	        }
	        outBuff.flush();
		}catch(Exception e){
			Log.Error("FileHandle.copyFile exception:"+e.getMessage());
		}finally{
			//关闭流 
	        try {
	        	if(inBuff != null){
	        		inBuff.close();
	        	}
	        	if(outBuff != null){
	        		outBuff.close();
	        	}
				if(output != null){
					output.close();
				}
		        if(input != null){
		        	input.close();
		        }
			} catch (IOException e) {
				Log.Error("FileHandle.copyFile IOException:"+e.getMessage());
			}
		}
	}
	
	public static void copyDirectiory(String sourceDir, String targetDir){
		try{
			File target = new File(targetDir);
			if(!target.exists()){
				target.mkdirs();
			}
			File[] file = (new File(sourceDir)).listFiles();
			for (int i = 0; i < file.length; i++) {
			    if (file[i].isFile()) {
			        File sourceFile=file[i];
			        File targetFile=new File(target.getAbsolutePath()+File.separator+file[i].getName());
			        copyFile(sourceFile,targetFile);
			    }
			    if (file[i].isDirectory()) {
			        String dir1=sourceDir + "/" + file[i].getName();
			        String dir2=targetDir + "/"+ file[i].getName();
			        copyDirectiory(dir1, dir2);
			    }
			}
		}catch(Exception e){
			Log.Error("FileHandle.copyDirectiory Exception:"+e.getMessage());
		}
}
	
	/** 删除文件 */
	public static void delPath(File file){
		if(file.exists()){
		    if(file.isFile()){
		        file.delete();
		    }else if(file.isDirectory()){
		        File files[] = file.listFiles();
		        for(int i=0;i<files.length;i++){
		        	delPath(files[i]);
		        }
		    }
		    file.delete();
		}else{
		    Log.Debug("删除文件不存在");
		} 
	}
	
	public static void write(String path, String content) {
		try {
			File f = new File(path);
			if (f.exists()) {
				System.out.println("文件存在");
				 
			} else {
				System.out.println("文件不存在，正在创建...");
				if (f.createNewFile()) {
					System.out.println("文件创建成功！");
				} else {
					System.out.println("文件创建失败！");
				}
			}
			FileOutputStream fos  =   new  FileOutputStream(path);
            OutputStreamWriter osw  =   new  OutputStreamWriter(fos, "UTF-8");
            osw.write(content);
            osw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void write(String path, String content , boolean isCover) {
		try {
			File f = new File(path);
			if (f.exists()) {
				System.out.println("文件存在");
				if(!isCover){
					return;
				}
			} else {
				System.out.println("文件不存在，正在创建...");
				if (f.createNewFile()) {
					System.out.println("文件创建成功！");
				} else {
					System.out.println("文件创建失败！");
				}
			}
			FileOutputStream fos  =   new  FileOutputStream(path);
            OutputStreamWriter osw  =   new  OutputStreamWriter(fos, "UTF-8");
            osw.write(content);
            osw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * file read with encode
	 * @param filePath String
	 * @param encode String
	 * @return String
	 * @throws IOException 
	 * */
	public static String readFile(String filePath,String encode){
		File file = null;
		InputStream in = null;
		BufferedReader rb = null;
		InputStreamReader inReader = null;
		StringBuffer strBuf = null;
		try{
			strBuf = new StringBuffer();
			file = new File(filePath);
			in = new FileInputStream(file);
			inReader = new InputStreamReader(in,encode);
			rb = new BufferedReader(inReader);
			int tempbyte;
			while((tempbyte = rb.read()) != -1){
				strBuf.append((char)tempbyte);
			}
		}catch(IOException e){
			Log.Error("读取路径异常:" + e.getMessage());
			return null;
		}finally{
			try{
				if(in != null){
					in.close();
				}
				if(inReader != null){
					inReader.close();
				}
				if(rb != null){
					rb.close();
				}
			}catch(Exception e){
				Log.Error("FileHandle.readFile exception : " + e.getMessage());
			}
		}
		return strBuf.toString();
	}
	
	/**
	 * the over-loading of readFile()with encoding utf-8
	 * @param filePath String
	 * @return String
	 * */
	public static String readFile(String filePath){
		File file = null;
		InputStream in = null;
		BufferedReader rb = null;
		StringBuffer strBuf = null;
		InputStreamReader inReader = null;
		try{
			strBuf = new StringBuffer();
			file = new File(filePath);
			in = new FileInputStream(file);
			inReader = new InputStreamReader(in,"UTF-8");
			rb = new BufferedReader(inReader);
			int tempbyte;
			while((tempbyte = rb.read()) != -1){
				strBuf.append((char)tempbyte);
			}
		}catch(IOException e){
			Log.Error("FileHandle.readFile IOException :" + e.getMessage());
			return null;
		}finally{
			try{
				if(in != null){
					in.close();
				}
				if(inReader != null){
					inReader.close();
				}
				if(rb != null){
					rb.close();
				}
			}catch(Exception e){
				Log.Error("FileHandle.readFile exception:" + e.getMessage());
			}
		}
		return strBuf.toString();
	}
	
	/**
	 * 文件重命名
	 * */
	public static void rename(String file,String file2){
		new File(file).renameTo(new File(file2));
	}
	
	/**
	 * 写入文件
	 * */
	@SuppressWarnings("resource")
	public boolean writeFile(String filePath,String content){
		FileWriter fw;
		boolean b = false;
		try {
			fw = new FileWriter(filePath);
			fw.write(content,0,content.length());  
			fw.flush();
			b = true;
		} catch (IOException e) {
			Log.Error(e.getMessage());
		}  
		return b;
	}
	
	public static void main(String[] args) throws IOException{
		FileHandle.write("D:/test.txt", "颠三倒四");
	}
}
