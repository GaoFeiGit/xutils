/**
 *
 */
package org.xdemo.app.xutils.ext.webservice.request;

import java.util.Map.Entry;

import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

/**
 *
 * @author Goofy 2019年8月12日 下午2:19:16
 */

public class SoapXmlConverter implements Converter<SoapRequest> {


    public SoapXmlConverter() {
        super();
    }

    /**
     * @author Goofy 2019年7月4日 上午10:09:59
     */
    public SoapRequest read(InputNode node) throws Exception {
        return null;
    }

    /**
     * @author Goofy 2019年7月4日 上午10:09:59
     */
    public void write(OutputNode node, SoapRequest request) throws Exception {

        node.setAttribute("xmlns:xsi", request.xsi);
        node.setAttribute("xmlns:xsd", request.xsd);
        node.setAttribute("xmlns:soap", request.soap);

        OutputNode header=node.getChild("soap:Header");

        if (request.getHeader() != null) {
            OutputNode method = header.getChild(request.getHeader().getHeader());
            method.setAttribute("xmlns", SoapRequest.xmlns);
            for (Entry<String, String> entry : request.getHeader().getKeyValuePair().entrySet()) {
                OutputNode child = method.getChild(entry.getKey());
                child.setValue(entry.getValue());
            }
        }

        OutputNode body = node.getChild("soap:Body");//创建空的header

        OutputNode method = body.getChild(request.getBody().getMethod());
        method.setAttribute("xmlns", SoapRequest.xmlns);


        for (Entry<String, String> entry : request.getBody().getKeyValuePair().entrySet()) {
            OutputNode child = method.getChild(entry.getKey());
            child.setValue(entry.getValue());
        }
    }

}
