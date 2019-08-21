package org.xdemo.app.xutils.ext.webservice;

import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

import java.util.Map.Entry;

/**
 * 自定义XML转换器，将SoapRequest转换为xml
 */
public class SoapXmlConverter implements Converter<SoapExecutor> {

    public SoapExecutor read(InputNode node) {
        return null;
    }


    /**
     * 对象生成XML
     *
     * @author Goofy 2019年7月4日 上午10:09:59
     */
    public void write(OutputNode node, SoapExecutor request) throws Exception {

        String HEADER = "soap:Header";

        String BODY = "soap:Body";

        String XMLNS = "xmlns";

        String XMLNS_XSI = "xmlns:xsi";

        String XMLNS_XSD = "xmlns:xsd";

        String XMLNS_SOAP = "xmlns:soap";

        SoapMeta meta = request.getMeta();

        node.setAttribute(XMLNS_XSI, SoapMeta.xsi);
        node.setAttribute(XMLNS_XSD, SoapMeta.xsd);
        node.setAttribute(XMLNS_SOAP, meta.getVersion().nsUri);

        node.setAttribute(XMLNS + ":" + meta.getNamespaceAbbreviation(), meta.getNamespace());

        OutputNode header = node.getChild(HEADER);

        if (request.getHeader() != null) {
            OutputNode headerInfo;
            if (meta.getTargetIsJavaServer()) {
                headerInfo = header.getChild(meta.getNamespaceAbbreviation() + ":" + request.getHeader().getHeader());
            } else {
                headerInfo = header.getChild(request.getHeader().getHeader());
            }
            for (Entry<String, String> entry : request.getHeader().getKeyValuePair().entrySet()) {
                OutputNode child = headerInfo.getChild(entry.getKey());
                child.setValue(entry.getValue());
            }
        }

        OutputNode body = node.getChild(BODY);
        OutputNode method;
        if (meta.getTargetIsJavaServer()) {
            method = body.getChild(meta.getNamespaceAbbreviation() + ":" + request.getBody().getMethod());
        } else {
            method = body.getChild(request.getBody().getMethod());
            method.setAttribute(XMLNS, meta.getNamespace());
        }
        for (Entry<String, String> entry : request.getBody().getKeyValuePair().entrySet()) {
            OutputNode child = method.getChild(entry.getKey());
            child.setValue(entry.getValue());
        }
    }

}
