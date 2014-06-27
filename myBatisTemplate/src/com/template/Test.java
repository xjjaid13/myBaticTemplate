package com.template;

import com.util.FileHandle;

public class Test {

	public static void main(String[] args){
		String content = FileHandle.readFile("D:/telmplate/model/com.test.entity/template.java");
		System.out.println(content.indexOf("{for}"));
		System.out.println("ssds.java".replaceAll("\\.", "aadsds."));
		
	}
	
}
