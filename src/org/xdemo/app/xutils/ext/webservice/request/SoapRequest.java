/**
 * 
 */
package org.xdemo.app.xutils.ext.webservice.request;

import java.io.ByteArrayOutputStream;

import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.convert.Convert;
import org.simpleframework.xml.core.Persister;

/**
 * SOAP请求
 * @author Goofy 2019年8月12日 下午1:31:41
 */
@Root(name = "soap:Envelope")
@Convert(SoapXmlConverter.class)
public class SoapRequest {

	/**
	 * 默认的命名空间
	 */
	private String xmlns = "http://tempuri.org/";
	
	private String xsi = "http://www.w3.org/2001/XMLSchema-instance";

	private String xsd = "http://www.w3.org/2001/XMLSchema";

	private String soap = "http://schemas.xmlsoap.org/soap/envelope/";

	/**
	 * WSDL文档地址，即WebService接口地址
	 */
	public String wsdl="";

	/**
	 * 头部信息，定义头部节点名称和参数信息
	 */
	private SoapHeader header;

	/**
	 * 请求体，定义请求方法名称和请求参数
	 */
	private SoapBody body;

	public String getXmlns() {
		return xmlns;
	}

	public void setXmlns(String xmlns) {
		this.xmlns = xmlns;
	}

	public SoapHeader getHeader() {
		return header;
	}

	public void setHeader(SoapHeader header) {
		this.header = header;
	}

	public SoapBody getBody() {
		return body;
	}

	public void setBody(SoapBody body) {
		this.body = body;
	}

	public String getWsdl() {
		return wsdl;
	}

	public void setWsdl(String wsdl) {
		this.wsdl = wsdl;
	}

	public String getXsi() {
		return xsi;
	}

	public void setXsi(String xsi) {
		this.xsi = xsi;
	}

	public String getXsd() {
		return xsd;
	}

	public void setXsd(String xsd) {
		this.xsd = xsd;
	}

	public String getSoap() {
		return soap;
	}

	public void setSoap(String soap) {
		this.soap = soap;
	}

	/**
	 * 生成SOAP请求的XML字符串
	 * @return
	 * @throws Exception
	 */
	public String xml() throws Exception {
		Serializer serializer = new Persister(new AnnotationStrategy());
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			serializer.write(this, os);
			return new String(os.toByteArray());
		} finally {
			os.close();
		}
	}
	

}
