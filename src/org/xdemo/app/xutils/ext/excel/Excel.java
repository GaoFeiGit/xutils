package org.xdemo.app.xutils.ext.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Excel
 * @author Goofy 252887950@qq.com <a href="http://www.xdemo.org">www.xdemo.org</a>
 * @Date 2017年8月21日 下午1:15:04
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.TYPE})
public @interface Excel {

	/**
	 * 列名
	 */
	String value();
	
	/**
	 * 宽度,像素
	 */
	int width() default -1;
	
	/**
	 *	单元格背景色 
	 */
	ExcelColor bgColor() default ExcelColor.WHITE;
	
	/**
	 * 字体颜色 
	 */
	ExcelColor fontColor() default ExcelColor.BLACK;
	
	/**
	 * 粗体 
	 */
	boolean boldFont() default false;

	/**
	 * 忽略该字段
	 */
	boolean skip() default false;
	
	/**
	 * 格式化
	 */
	Class<? extends ExcelFormatter> formatter() default ExcelFormatter.class;
	
}