package org.xdemo.app.xutils.ext.webservice.usage;

import com.sun.xml.internal.ws.api.SOAPVersion;
import org.xdemo.app.xutils.ext.webservice.SoapBody;
import org.xdemo.app.xutils.ext.webservice.SoapExecutor;
import org.xdemo.app.xutils.ext.webservice.SoapHeader;
import org.xdemo.app.xutils.ext.webservice.SoapMeta;

public class Test2 {

    public static void main(String[] args) {

        {
            SoapExecutor soap=new SoapExecutor();
            soap.setAsync(false).setDebug(true);

            SoapMeta soapMeta=new SoapMeta("http://www.webxml.com.cn/WebServices/ChinaZipSearchWebService.asmx?wsdl","http://WebXml.com.cn/",false, SOAPVersion.SOAP_11);

            soap.setMeta(soapMeta);


            SoapHeader header = new SoapHeader();
            header.setHeader("Auth");
            header.addKeyValue("UserName","张三");
            header.addKeyValue("Password","123456");

            soap.setHeader(header);

            SoapBody body = new SoapBody();
            body.setMethod("getAddressByZipCode");
            body.addKeyValue("theZipCode", "223600");
            body.addKeyValue("userID","");
            soap.setBody(body);
            soap.execute();
            System.out.println(SoapExecutor.prettyFormat(soap.getResponsedXML(),2));

        }

        {
            SoapExecutor soap=new SoapExecutor();
            soap.setAsync(false).setDebug(true);

            SoapMeta soapMeta=new SoapMeta("http://www.webxml.com.cn/WebServices/IpAddressSearchWebService.asmx","http://WebXml.com.cn/",false, SOAPVersion.SOAP_11);

            soap.setMeta(soapMeta);


//        SoapHeader header = new SoapHeader();
//        header.setHeader("Auth");
//        header.addKeyValue("UserName","张三");
//        header.addKeyValue("Password","123456");
//
//        soap.setHeader(header);

            SoapBody body = new SoapBody();
            body.setMethod("getCountryCityByIp");
            body.addKeyValue("theIpAddress", "192.168.1.1");

            soap.setBody(body);

            soap.execute();
            System.out.println(SoapExecutor.prettyFormat(soap.getResponsedXML(),2));
        }



    }
}
