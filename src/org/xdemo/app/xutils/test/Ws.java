package org.xdemo.app.xutils.test;


import com.sun.xml.internal.ws.api.SOAPVersion;
import org.xdemo.app.xutils.ext.webservice.SoapBody;
import org.xdemo.app.xutils.ext.webservice.SoapMeta;
import org.xdemo.app.xutils.ext.webservice.SoapExecutor;

import java.util.HashMap;

public class Ws {

    public static void main(String[] args) throws Exception {
        SoapExecutor request=new SoapExecutor();
        SoapMeta meta=new SoapMeta("http://localhost:8888/ws?wsdl","http://xx.org.xd.www.zdjc.xyz/",false, SOAPVersion.SOAP_11);
        request.setMeta(meta);
        SoapBody body = new SoapBody();
        body.setMethod("hello");
        body.setKeyValuePair(new HashMap<String,String>(){{
            put("name", "张三");
        }});
        request.setBody(body);
    }
}
