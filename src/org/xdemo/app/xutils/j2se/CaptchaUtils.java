package org.xdemo.app.xutils.j2se;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;


/**
 * 验证码工具类
 * @author Goofy <a href="http://<a href="http://www.xdemo.org">www.xdemo.org</a>/">http://<a href="http://www.xdemo.org">www.xdemo.org</a>/</a>
 * 252878950@qq.com
 */
public class CaptchaUtils {
	
	/**
	 * 验证码复杂程序 ComplexLevel.SIMPLE简单 ComplexLevel.MEDIUM中等 ComplexLevel.HARD复杂
	 */
	public enum ComplexLevel {SIMPLE,MEDIUM,HARD}
	
	private static final char []SIMPLE={'0','1','2','3','4','5','6','7','8','9'};
	private static final char []MEDIUM={'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','Z','Y','Z'};
	private static final char []HARD={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','Z','Y','Z'};
	
	private static Random random = new Random();
	
	/**
	 * 验证码生产类<br>
	 * 简单的验证码由4位数字组成<br>
	 * 中等复杂的验证码有5位，字母（包括大小写）和数字组成，有干扰点<br>
	 * 最复杂的验证码由6位字母（包括大小写）和数字组成，有干扰点和干扰线<br>
	 * 可自定义干扰点和干扰线的数量
	 * @param width 验证码图片宽度
	 * @param height 验证码图片高度
	 * @param fontSize 字体大小
	 * @param lineCount 干扰线条数 仅对复杂验证码模式有效
	 * @param pointCount 干扰点 对复杂验证码和中等复杂度验证码有效
	 * @param colours 是否彩色字体
	 * @param border 是否绘制边框
	 * @param complexLevel 复杂度枚举类型，如传入ComplexLevel.SIMPLE
	 * @return Object 数组<br>
	 * 		Object[0]是BufferImage图像信息，可以通过ImageIO.write((BufferImage)Object[0],"JPEG",HttpResponse.getOutputStream())写到客户端<br>
	 * 		Object[1]第二位是验证码字符串
	 */
	public static Object[] getCaptchaImage(int width,int height,int fontSize,int lineCount,int pointCount,boolean border,boolean colours,ComplexLevel complexLevel){

		Object[] object=new Object[2];
		//验证码字符串
		StringBuffer sb=new StringBuffer();
		//从字符数组中去字符的随机位置
		int position=0;
		
		//图像数据
		BufferedImage bi=new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		//获取一张图像
		Graphics grap=bi.getGraphics();
		//绘制一个矩形
		grap.fillRect(0, 0, width, height);
		//设置字体
		grap.setFont(new Font("Arial",Font.BOLD,fontSize));
		//设置字体颜色
		grap.setColor(Color.BLACK);
		//绘制一个边框
		if(border)grap.drawRect(0, 0, width-1, height-1);
		switch (complexLevel) {
			case SIMPLE:
				for(int i=0;i<4;i++){
					//设置随机颜色
					if(colours)grap.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
					position=random.nextInt(SIMPLE.length);
					//写字符到图片合适的位置
					grap.drawString(SIMPLE[position]+"",(i*((width-10)/4))+5+random.nextInt(((width-10)/4)/2),height/2+random.nextInt(height/2));
					sb.append(SIMPLE[position]);
				}
				break;
			case MEDIUM:
				//绘制干扰点
				for (int k = 0; k < pointCount; k++) {
					grap.drawRect(random.nextInt(width), random.nextInt(height), 1, 1);
				}
				for(int i=0;i<5;i++){
					//设置随机颜色
					if(colours)grap.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
					position=random.nextInt(MEDIUM.length);
					//写字符到图片合适的位置
					grap.drawString(MEDIUM[position]+"",(i*((width-10)/5))+5+random.nextInt(((width-10)/5)/2),height/2+random.nextInt(height/2));
					sb.append(MEDIUM[position]);
				}
				break;
			case HARD:
				//绘制干扰线
				for (int j = 0; j < lineCount; j++) {
					grap.drawLine(random.nextInt(width), random.nextInt(height),random.nextInt(width), random.nextInt(height));
				}
				//绘制干扰点
				for (int k = 0; k < pointCount; k++) {
					grap.drawRect(random.nextInt(width), random.nextInt(height), 1, 1);
				}
				for(int i=0;i<6;i++){
					//设置随机颜色
					if(colours)grap.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
					position=random.nextInt(HARD.length);
					//写字符到图片合适的位置
					grap.drawString(HARD[position]+"",(i*((width-10)/6))+5+random.nextInt(((width-10)/6)/2),height/2+random.nextInt(height/2));
					sb.append(HARD[position]);
				}
				break;
			default:
				break;
		}
		//释放Graphics对象
		grap.dispose();
		
		object[0]=bi;
		object[1]=sb.toString();
		
		return object;
	}
	//测试代码
	public static void main(String[] args) throws IOException, InterruptedException {
		for(int i=0;i<10;i++){
			Object[] obj=getCaptchaImage(150, 50, 35, 50, 500, false,false, ComplexLevel.HARD);
			System.out.println("验证码"+obj[1]);
			new File("C:\\test").mkdir();
			ImageIO.write((BufferedImage)obj[0],"jpg",new File("C:\\test\\"+System.currentTimeMillis()+".jpg"));
			Thread.sleep(500L);
		}
	}
}
