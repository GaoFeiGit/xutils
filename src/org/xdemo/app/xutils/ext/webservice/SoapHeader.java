/**
 * 
 */
package org.xdemo.app.xutils.ext.webservice;

import java.util.HashMap;
import java.util.Map;

/**
 * Soap头信息
 * 
 * @author Goofy 2019年8月12日 下午1:32:34
 */
public class SoapHeader {

	/**
	 * 头部节点名称
	 */
	private String header;

	/**
	 * 头部请求参数，请求参数名称为Key，值为Value
	 */
	private Map<String, String> keyValuePair=new HashMap<>();

	/**
	 * 构造方法
	 * 
	 * @param header
	 * @param keyValuePair
	 */
	public SoapHeader(String header, Map<String, String> keyValuePair) {
		super();
		this.header = header;
		this.keyValuePair = keyValuePair;
	}

	/**
	 * 构造方法
	 */
	public SoapHeader() {
		super();
	}

	public String getHeader() {
		return header;
	}

	public void addKeyValue(String key,String value){
		keyValuePair.put(key,value);
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public Map<String, String> getKeyValuePair() {
		return keyValuePair;
	}

	public void setKeyValuePair(Map<String, String> keyValuePair) {
		this.keyValuePair = keyValuePair;
	}

}
