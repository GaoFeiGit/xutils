package org.xdemo.app.xutils.ext.webservice;

import com.sun.xml.internal.ws.api.SOAPVersion;
import org.xdemo.app.xutils.j2se.StringUtils;

/**
 * Soap元信息
 */
public class SoapMeta {


    private String url;

    private String namespaceAbbreviation = "ws";

    private String namespace;

    private SOAPVersion version;

    private Boolean targetIsJavaServer = true;


    public static final String xsi = "http://www.w3.org/2001/XMLSchema-instance";

    public static final String xsd = "http://www.w3.org/2001/XMLSchema";

    public SoapMeta(String url, String namespace, Boolean targetIsJavaServer, SOAPVersion version) {
        this.url = StringUtils.trim(url);
        this.namespace = StringUtils.trim(namespace);
        this.version = version;
        this.targetIsJavaServer = targetIsJavaServer;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public SOAPVersion getVersion() {
        return version;
    }

    public void setVersion(SOAPVersion version) {
        this.version = version;
    }

    public String getNamespaceAbbreviation() {
        return namespaceAbbreviation;
    }

    public void setNamespaceAbbreviation(String namespaceAbbreviation) {
        this.namespaceAbbreviation = namespaceAbbreviation;
    }

    public Boolean getTargetIsJavaServer() {
        return targetIsJavaServer;
    }

    public void setTargetIsJavaServer(Boolean targetIsJavaServer) {
        this.targetIsJavaServer = targetIsJavaServer;
    }
}
