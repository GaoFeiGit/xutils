package org.xdemo.app.xutils.ext.excel.usage;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.xdemo.app.xutils.ext.excel.ExcelFormatter;

/**
 * @author Goofy 252887950@qq.com <a href="http://www.xdemo.org">www.xdemo.org</a>
 * @Date 2017年9月28日 下午7:30:51
 */
public class DateFormatter extends ExcelFormatter {

	@Override
	public String format(Object obj) {
		if(obj==null)return "";
		Date d=(Date) obj;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-DD HH:mm:ss");
		return sdf.format(d);
	}

}
