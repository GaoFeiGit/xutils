package org.xdemo.app.xutils.ext.excel.usage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.xdemo.app.xutils.ext.excel.ExcelUtils;

/**
 * @author Goofy 252887950@qq.com <a href="http://www.xdemo.org">www.xdemo.org</a>
 * @Date 2017年9月28日 下午8:00:28
 */
public class Main {
	public static void main(String[] args) throws Exception {
		List<Foo> list=new ArrayList<Foo>();
		Foo foo=new Foo();
		foo.setBd(new BigDecimal(222.2222D));
		foo.setDate(new Date());
		foo.setName("fdsa");
		foo.setFoo(foo);
		
		list.add(foo);
		
		ExcelUtils.writeToFile(list, "D:\\test.xlsx");
	}
}
