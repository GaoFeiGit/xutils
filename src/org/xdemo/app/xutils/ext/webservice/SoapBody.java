/**
 * 
 */
package org.xdemo.app.xutils.ext.webservice;

import java.util.HashMap;
import java.util.Map;

/**
 * SOAP请求体，包括方法名和方法的参数，参数是hashmap类型，key为参数，value为值
 * 
 * @author Goofy 2019年8月12日 下午1:32:26
 */
public class SoapBody {

	private String method;

	private Map<String, String> keyValuePair=new HashMap<>();

	/**
	 * 构造方法
	 *
	 * @param method
	 * @param keyValuePair
	 */
	public SoapBody(String method, Map<String, String> keyValuePair) {
		super();
		this.method = method;
		this.keyValuePair = keyValuePair;
	}

	/**
	 * 构造方法
	 */
	public SoapBody() {
		super();
	}

	public String getMethod() {
		return method;
	}

	public void addKeyValue(String key,String value){
		keyValuePair.put(key,value);
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Map<String, String> getKeyValuePair() {
		return keyValuePair;
	}

	public void setKeyValuePair(Map<String, String> keyValuePair) {
		this.keyValuePair = keyValuePair;
	}

}
