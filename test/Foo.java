package org.xdemo.app.xutils.test;

import org.xdemo.app.xutils.ext.excel.Excel;
import org.xdemo.app.xutils.ext.excel.ExcelColor;

/**
 * @author Goofy 252887950@qq.com <a href="http://www.xdemo.org">www.xdemo.org</a>
 * @Date 2017年8月18日 上午8:48:25
 */
public class Foo implements Cloneable{
	
	@Excel(value="AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",width=30)
	private String a;
	
	
	@Excel(value="BBB",width=50,bgColor=ExcelColor.GREY_40_PERCENT,fontColor=ExcelColor.RED)
	private String b;
	
	public Foo(){
	}
	
	public Foo(String a,String b){
		this.a=a;
		this.b=b;
		
	}

	public String getA() {
		return a;
	}

	public void setA(String a) {
		this.a = a;
	}

	public String getB() {
		return b;
	}

	public void setB(String b) {
		this.b = b;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
