package org.xdemo.app.xutils.ext.excel.usage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.xdemo.app.xutils.ext.excel.Excel;
import org.xdemo.app.xutils.ext.excel.ExcelColor;
import org.xdemo.app.xutils.ext.excel.ExcelUtils;

/**
 * @author Goofy 252887950@qq.com <a href="http://www.xdemo.org">www.xdemo.org</a>
 * @Date 2017年9月28日 下午7:29:05
 */
public class Foo {
	
	@Excel(value="名字",bgColor=ExcelColor.BLACK,fontColor=ExcelColor.WHITE)
	private String name;
	
	@Excel(value="日期",formatter=DateFormatter.class)
	private Date date;
	
	//默认的toString方法
	@Excel(value="数字")
	private BigDecimal bd;
	
	@Excel(value="格式化复杂对象",formatter=FooFormatter.class)
	private Foo foo;

	public String getName() {
		return name;
	}




	public void setName(String name) {
		this.name = name;
	}




	public Date getDate() {
		return date;
	}




	public void setDate(Date date) {
		this.date = date;
	}




	public BigDecimal getBd() {
		return bd;
	}




	public void setBd(BigDecimal bd) {
		this.bd = bd;
	}




	public Foo getFoo() {
		return foo;
	}




	public void setFoo(Foo foo) {
		this.foo = foo;
	}

	

}
