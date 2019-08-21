package org.xdemo.app.xutils.ext.webservice;

public interface SoapResultParser {
    Object parse(String method, String xml) throws Exception;
}
