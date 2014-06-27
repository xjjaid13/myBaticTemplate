package com.template;

import java.io.File;
import java.util.ArrayList;

import com.util.DBHandle;
import com.util.FileHandle;

/**
 * 自动生成代码
 * @author Taylor
 * */
public class Template {

	public static void main(String[] args){
		
		/** 模版文件 */
		String templateFile = "D:/telmplate/model";
		
		/** 保存文件 */
		String saveFile = "D:/workspace1/datac/";
		
		/** 数据库名 */
		String dbName = "fastjavaproject";
		
		/** 是否覆盖 */
		boolean isCover = false;
		
		/** 数据库ip */
		String url = "jdbc:mysql://127.0.0.1:3306/"+dbName+"?characterEncoding=utf-8";
		
		/** 数据库用户名 */
		String dbUser = "root";
		
		/** 数据库驱动 */
		String driver = "com.mysql.jdbc.Driver";
		
		/** 数据库密码 */
		String dbPassword = "111111";
		
		DBHandle db = new DBHandle();
		db.openConnMysqlParam(driver, url, dbUser, dbPassword);
		
		File parentFile = new File(templateFile); 
		File[] packageFile = parentFile.listFiles();
		
		/** 查询表 */
		String selectAllTable = "select table_name from information_schema.tables where table_schema='"+dbName+"'";
		String[][] schSelectAllTable = db.executeQuery(selectAllTable);
		
		/** 表名称数组 */
		ArrayList<String> tableArr = new ArrayList<String>();
		for(int i = 0; schSelectAllTable!= null && i < schSelectAllTable.length; i++){
			tableArr.add(schSelectAllTable[i][0]);
		}
		/** 如果是config文件，只处理一份 */
		boolean first = false;
		
		for(int t = 0; schSelectAllTable != null && t < schSelectAllTable.length; t++){
			/** 查询栏目 */
			String schSelectAllcolumn = "SELECT column_name from information_schema.columns WHERE table_schema = '"+dbName+"' AND table_name = '"+schSelectAllTable[t][0]+"';";
			String[][] schSelectAllColumn = db.executeQuery(schSelectAllcolumn);
			/** 栏目名称数组 */
			ArrayList<String> columnArr = new ArrayList<String>();
			/** 栏目类型数组 */
			ArrayList<String> columnType = new ArrayList<String>();
			for(int c = 0; schSelectAllColumn != null && c < schSelectAllColumn.length; c++){
				columnArr.add(schSelectAllColumn[c][0]);
				int fieldType = db.getColumnType(schSelectAllTable[t][0],schSelectAllColumn[c][0] );
				if(!db.isString(fieldType)){
					columnType.add("Integer");
				}else{
					columnType.add("String");
				}
			}
			for(int i = 0; packageFile != null && i < packageFile.length; i++){
				File file = packageFile[i];
				String packageName = file.getName();
				packageName = packageName.replaceAll("\\.", "/");
				
				String savePath = saveFile + packageName;
				FileHandle.createPath(savePath);
				File[] fileTemplateList = file.listFiles();
				for(int j = 0; fileTemplateList != null && j < fileTemplateList.length; j++){
					File fileTemplate = fileTemplateList[j];
					String fileName = fileTemplate.getName();
					String templateContent = FileHandle.readFile(fileTemplate.getAbsolutePath());
					StringBuffer content = new StringBuffer();
					if(packageName.indexOf("config")!= -1){
						if(!first){
							String result = returnTableContent(templateContent,content,tableArr);
							FileHandle.write(savePath + "/" + fileName,result,isCover);
							first = true;
							continue;
						}else{
							continue;
						}
					}
					String result = returnContent(templateContent,content,columnArr,schSelectAllTable[t][0],columnType);
					FileHandle.write(savePath + "/" + fileName.replaceAll("\\{name\\}", firstUppercase(transfer(schSelectAllTable[t][0]))),result,isCover);
				}
			}
		}
		db.closeConn();
		
	}
	
	/** 解析栏目模板文件 */
	public static String returnContent(String content,StringBuffer result,ArrayList<String> field,String tableName,ArrayList<String> type){
		int start = content.indexOf("{for}");
		int end = content.indexOf("{endfor}");
		if(start == -1){
			
			result.append(transferContent(content,tableName,"",""));
			return result.toString();
		}
		result.append(transferContent(content.substring(0,start),tableName,"",""));
		String forContent = content.substring(start+5,end);
		
		for(int i = 0; field != null && i < field.size(); i++){
			String forTemContent  = transferContent(forContent,tableName,field.get(i),type.get(i));
			if(forTemContent.endsWith(",") && i == field.size() - 1){
				forTemContent = forTemContent.substring(0,forTemContent.length() - 1);
			}
			result.append(forTemContent);
		}
		
		return returnContent(content.substring(end + 8),result,field,tableName,type);
	}
	
	/** 解析表模板文件 */
	public static String returnTableContent(String content,StringBuffer result,ArrayList<String> tableArr){
		int start = content.indexOf("{for}");
		int end = content.indexOf("{endfor}");
		if(start == -1){
			
			result.append(transferContent(content,"","",""));
			return result.toString();
		}
		result.append(transferContent(content.substring(0,start),"","",""));
		String forContent = content.substring(start+5,end);
		for(int i = 0; tableArr != null && i < tableArr.size(); i++){
			String forTemContent = transferContent(forContent,tableArr.get(i),"","");
			if(forTemContent.endsWith(",") && i == tableArr.size() - 1){
				forTemContent = forTemContent.substring(0,forTemContent.length() - 1);
			}
			result.append(forTemContent);
		}
		
		return returnTableContent(content.substring(end + 8),result,tableArr);
	}
	
	/** 解析特定字符串 */
	public static String transferContent(String content,String tableName,String field,String type){
		if(!"".equals(tableName)){
			content = content.replaceAll("\\{TableName\\}", firstUppercase(transfer(tableName)));
			content = content.replaceAll("\\{tableName\\}", transfer(tableName));
			content = content.replaceAll("\\{tablename\\}", tableName);
		}
		if(!"".equals(field)){
			content = content.replaceAll("\\{columnName\\}", transfer(field));
			content = content.replaceAll("\\{columnname\\}", field);
			if("String".equals(type)){
				content = content.replaceAll("\\{#columnname\\}", "'#{"+field+"}'");
			}else{
				content = content.replaceAll("\\{#columnname\\}", "#{"+field+"}");
			}
			content = content.replaceAll("\\{Columnname\\}", firstUppercase(field));
			content = content.replaceAll("\\{ColumnName\\}", firstUppercase(transfer(field)));
			content = content.replaceAll("\\{type\\}", type);
		}
		return content;
	}
	
	/**
	 * 将数据库写法转成驼峰写法
	 * */
	public static String transfer(String content){
		int index = content.indexOf("_");
		if(index != -1){
			String temStr = content.substring(index+1,index+2);
			content = content.replaceAll("_"+temStr, temStr.toUpperCase());
		}else{
			return content;
		}
		return transfer(content);
	}
	
	/**
	 * 首字母大写
	 * */
	public static String firstUppercase(String content){
		return content.substring(0,1).toUpperCase() + content.substring(1);
	}

}
