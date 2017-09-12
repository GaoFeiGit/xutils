package org.xdemo.app.xutils.ext.excel;


/**
 * @author Goofy 252887950@qq.com <a href="http://www.xdemo.org">www.xdemo.org</a>
 * @Date 2017年8月21日 下午2:55:13
 */
public  class ExcelFormatter {
	public String format(Object obj){
		if(obj==null)return "";
		return obj.toString();
	}
}
