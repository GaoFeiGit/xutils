package org.xdemo.app.xutils.ext.webservice.usage;

import com.sun.xml.internal.ws.api.SOAPVersion;
import org.xdemo.app.xutils.ext.webservice.SoapBody;
import org.xdemo.app.xutils.ext.webservice.SoapExecutor;
import org.xdemo.app.xutils.ext.webservice.SoapMeta;

public class Test2 {

    public static void main(String[] args) {

        {
            SoapExecutor soap=new SoapExecutor();
            soap.setAsync(false).setDebug(true);
            SoapMeta soapMeta=new SoapMeta("https://soap.aspsms.com/aspsmsx2.asmx?WSDL","https://webservice.aspsms.com/aspsmsx2.asmx",false, SOAPVersion.SOAP_11);
            soap.setMeta(soapMeta);
            SoapBody body = new SoapBody();
            body.setMethod("VersionInfo");
            soap.setBody(body);
            soap.execute();
        }

        {
            SoapExecutor soap=new SoapExecutor();
            soap.setAsync(false).setDebug(true);
            SoapMeta soapMeta=new SoapMeta("http://www.dneonline.com/calculator.asmx?wsdl","http://tempuri.org/",false, SOAPVersion.SOAP_12);
            soap.setMeta(soapMeta);
            SoapBody body = new SoapBody();
            body.setMethod("Add");
            body.addKeyValue("intA", "1");
            body.addKeyValue("intB","2");
            soap.setBody(body);
            soap.execute();
        }

        {
            SoapExecutor soap=new SoapExecutor();
            soap.setAsync(false).setDebug(true);
            SoapMeta soapMeta=new SoapMeta("http://www.webxml.com.cn/WebServices/ChinaZipSearchWebService.asmx?wsdl","http://WebXml.com.cn/",false, SOAPVersion.SOAP_11);
            soap.setMeta(soapMeta);
//            SoapHeader header = new SoapHeader();
//            header.setHeader("Auth");
//            header.addKeyValue("UserName","张三");
//            header.addKeyValue("Password","123456");
//            soap.setHeader(header);
            SoapBody body = new SoapBody();
            body.setMethod("getAddressByZipCode");
            body.addKeyValue("theZipCode", "223600");
            body.addKeyValue("userID","");
            soap.setBody(body);
            soap.execute();
        }

        {
            SoapExecutor soap=new SoapExecutor();
            soap.setAsync(false).setDebug(true);
            SoapMeta soapMeta=new SoapMeta("http://www.webxml.com.cn/WebServices/IpAddressSearchWebService.asmx","http://WebXml.com.cn/",false, SOAPVersion.SOAP_11);
            soap.setMeta(soapMeta);
            SoapBody body = new SoapBody();
            body.setMethod("getCountryCityByIp");
            body.addKeyValue("theIpAddress", "192.168.1.1");
            soap.setBody(body);
            soap.execute();
        }

        {
            SoapExecutor soap=new SoapExecutor();
            soap.setAsync(false).setDebug(true);
            SoapMeta soapMeta=new SoapMeta("http://www.webxml.com.cn/WebServices/TranslatorWebService.asmx?wsdl","http://WebXml.com.cn/",false, SOAPVersion.SOAP_11);
            soap.setMeta(soapMeta);
            SoapBody body = new SoapBody();
            body.setMethod("getEnCnTwoWayTranslator");
            body.addKeyValue("Word", "Good");
            soap.setBody(body);
            soap.execute();
        }
    }
}
