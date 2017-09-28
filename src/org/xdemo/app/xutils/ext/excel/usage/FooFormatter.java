package org.xdemo.app.xutils.ext.excel.usage;

import java.text.SimpleDateFormat;

import javax.swing.text.SimpleAttributeSet;

import org.xdemo.app.xutils.ext.excel.ExcelFormatter;


/**
 * @author Goofy 252887950@qq.com <a href="http://www.xdemo.org">www.xdemo.org</a>
 * @Date 2017年9月28日 下午7:41:49
 */
public class FooFormatter extends ExcelFormatter {

	@Override
	public String format(Object obj) {
		Foo foo=(Foo) obj;
		return "格式化Foo:"+foo.getName()+","+new SimpleDateFormat("yyyy-MM-dd").format(foo.getDate());
	}

}
