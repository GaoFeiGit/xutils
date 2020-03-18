package org.xdemo.app.xutils.ext.webservice;


import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.convert.Convert;
import org.simpleframework.xml.core.Persister;
import org.xdemo.app.xutils.j2se.StringUtils;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.FutureTask;

/**
 * Soap请求
 */
@Root(name = "soap:Envelope")
@Convert(SoapXmlConverter.class)
public class SoapExecutor {

    /**
     * 元信息
     */
    private SoapMeta meta;

    /**
     * 头部信息
     */
    private SoapHeader header;

    /**
     * 请求体内容
     */
    private SoapBody body;

    /**
     * Debug模式
     */
    private boolean debug = false;

    /**
     * 是否异步调用
     */
    private boolean async = false;

    /**
     * 是否成功
     */
    private boolean success = true;

    /**
     * 请求编码
     */
    private Charset charset = StandardCharsets.UTF_8;

    /**
     * 请求开始时间
     */
    private Long startTime;

    /**
     * 请求结束时间
     */
    private Long endTime;

    /**
     * 异常信息
     */
    private Exception ex;

    /**
     * 获取响应的XML
     */
    private String responsedXML;

    /**
     * 响应的结果
     */
    private Object responsedResult;

    /**
     * 请求超时，毫秒数
     */
    private int timeout;

    /**
     * 结果解析器
     */
    private SoapResultParser parser;


    public SoapMeta getMeta() {
        return meta;
    }

    public SoapExecutor setMeta(SoapMeta meta) {
        this.meta = meta;
        return this;
    }

    public SoapHeader getHeader() {
        return header;
    }

    public SoapExecutor setHeader(SoapHeader header) {
        this.header = header;
        return this;
    }

    public SoapBody getBody() {
        return body;
    }

    public SoapExecutor setBody(SoapBody body) {
        this.body = body;
        return this;
    }

    public boolean isDebug() {
        return debug;
    }

    public SoapExecutor setDebug(boolean debug) {
        this.debug = debug;
        return this;
    }

    public boolean isAsync() {
        return async;
    }

    public SoapExecutor setAsync(boolean async) {
        this.async = async;
        return this;
    }

    public Long getStartTime() {
        return startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public Exception getEx() {
        return ex;
    }


    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * 生成SOAP请求的XML字符串
     *
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

    public static String prettyFormat(String input, int indent) {
        try {
            Source xmlInput = new StreamSource(new StringReader(input));
            StringWriter stringWriter = new StringWriter();
            StreamResult xmlOutput = new StreamResult(stringWriter);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", indent);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(xmlInput, xmlOutput);
            return xmlOutput.getWriter().toString();
        } catch (Exception e) {
            throw new RuntimeException(e); // simple exception handling, please review it
        }
    }


    private CloseableHttpClient client;
    private CloseableHttpResponse httpResponse;

    /**
     * 执行异步操作获取线程执行的结果
     */
    private FutureTask<String> task;


    public void execute() {

        StringBuffer sb = new StringBuffer();

        String xml = null;

        try {
            xml = this.xml();
        } catch (Exception e) {
            ex = e;
            success = false;
            e.printStackTrace();
        }

        startTime = System.currentTimeMillis();
        if (debug) {
            sb.append("目标：\n\t" + this.getMeta().getUrl() + "\n");
            sb.append("SOAPAction:\n\t" + this.getMeta().getNamespace() + (this.getMeta().getNamespace().endsWith("/") ? "" : "/") + this.getBody().getMethod() + "\n");
            sb.append("请求内容:\n" + xml + "\n");
            System.out.println(sb.toString());
        }

        try {
            //发送Http请求并获取响应
            request(xml);
        } catch (Exception e) {
            ex = e;
            success = false;
            e.printStackTrace();
        }

        if (debug) {
            sb = new StringBuffer();
            if (!async) {
                sb.append("响应:\n" + prettyFormat(responsedXML, 4) + "\n");
                endTime = System.currentTimeMillis();
                sb.append("总耗时:\t" + (endTime - startTime) + "ms");
            } else {
                sb.append("异步请求，暂时无法获取响应内容和请求耗时，请自己附加代码进行调试");
            }
            System.out.println(sb.toString() + "\n\n");
        }
    }

    /**
     * 获取服务器的响应
     *
     * @param requestXML
     * @return
     * @throws Exception
     */
    private void request(String requestXML) throws IOException {
        client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(this.getMeta().getUrl());
        try {

            post.setConfig(config());
            post.setHeader("Content-Type", this.getMeta().getVersion().contentType + ";charset=" + charset.name());
            if (!this.getMeta().getTargetIsJavaServer()) {
                //Java发布的webservice不需要这个header
                post.setHeader("SOAPAction", this.getMeta().getNamespace() + (this.getMeta().getNamespace().endsWith("/") ? "" : "/") + this.getBody().getMethod());
            }
            StringEntity entity = new StringEntity(requestXML, charset);

            post.setEntity(entity);

            if (async) {
                task = new FutureTask<>(() -> httpPost(client, post));
                SoapThreadPool.getInstance().submit(task);
            } else {
                responsedXML = httpPost(client, post);
            }
        } finally {
            //非异步的时候才能关闭，如果是异步，链接会提前关闭，无法获取结果
            if (!async && client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取响应的XML
     *
     * @return
     */
    public String getResponsedXML() {
        asyncGet();
        return responsedXML;
    }

    /**
     * 获取同步或异步线程执行的结果
     */
    private void asyncGet() {
        if (async && responsedXML == null) {
            try {
                responsedXML = task.get();
                if (parser != null) {
                    responsedResult = parser.parse(this.getBody().getMethod(), responsedXML);
                } else {
                    if (debug) {
                        System.out.println("未设置结果解析器，调用SoapResponse.getResponsedXML()获取返回的内容,自己解析");
                    }
                }
            } catch (Exception e) {
                ex = e;
                success = false;
                e.printStackTrace();
            } finally {
                try {
                    if (httpResponse != null) {
                        httpResponse.close();
                    }
                } catch (Exception e) {
                }
                try {
                    if (client != null) {
                        client.close();
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * 获取响应的解析后的结果
     *
     * @return
     */
    public <T> T getResponsedResult() {
        asyncGet();
        return (T) responsedResult;
    }


    /**
     * 是否执行成功
     *
     * @return
     */
    public boolean isSuccess() {
        asyncGet();
        return success;
    }

    /**
     * 设置结果解析器
     *
     * @param parser
     */
    public SoapExecutor setParser(SoapResultParser parser) {
        this.parser = parser;
        return this;
    }

    /**
     * 发送http post请求
     *
     * @param client
     * @param post
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    private String httpPost(CloseableHttpClient client, HttpPost post) throws ClientProtocolException, IOException {
        httpResponse = null;
        HttpEntity httpEntity = null;
        try {
            httpResponse = client.execute(post);
            httpEntity = httpResponse.getEntity();
            return StringUtils.readFromInputStream(httpEntity.getContent());
        } finally {
            //非异步的时候才能关闭，如果是异步，链接会提前关闭，无法获取结果
            if (!async && httpResponse != null) {
                httpResponse.close();
            }
        }
    }


    /**
     * 获取HTTP客户端对象
     *
     * @return
     */
    private CloseableHttpClient client() {
        return HttpClients.custom().setDefaultRequestConfig(config()).build();
    }

    /**
     * 获取Request配置
     *
     * @return
     */
    private RequestConfig config() {
        return RequestConfig.custom().setSocketTimeout(timeout).setConnectionRequestTimeout(timeout).setConnectionRequestTimeout(timeout).build();
    }
}
