package com.util;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;

public class InitServlet extends HttpServlet implements Servlet{

	private static final long serialVersionUID = 1L;
	
	public void init(ServletConfig config){
		String realPath = config.getServletContext().getRealPath("")+"/";
		/** 初始化配置文件 */
		new Init().init(realPath);
	}
	
}
