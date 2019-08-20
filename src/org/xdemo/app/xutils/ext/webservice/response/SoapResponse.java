/**
 *
 */
package com.wxzd.fts.project.webservice.response;

import com.wxzd.fts.project.webservice.parser.SoapResultParser;
import com.wxzd.fts.project.webservice.request.SoapRequest;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import javax.xml.ws.WebServiceException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.FutureTask;

/**
 * Soap响应
 * @author Goofy 2019年8月12日 下午3:58:43
 */
public class SoapResponse {

    /**
     * 请求对象
     */
    private SoapRequest request;

    /**
     * 是否开启Debug，开启后，输出请求的XML内容和响应的XML内容
     */
    private boolean debug = false;

    /**
     * 请求开始时间
     */
    private long startTime;

    /**
     * 请求结束时间
     */
    private long endTime;

    /**
     * 请求超时
     */
    private int timeout = 5000;

    /**
     * 响应的XML
     */
    private String responsedXML;

    /**
     * 响应的结果
     */
    private Object responsedResult;

    /**
     * 结果解析器
     */
    private SoapResultParser parser;

    /**
     * 执行异步操作获取线程执行的结果
     */
    private FutureTask<String> task;

    /**
     * 是否执行异步操作
     */
    private boolean async = false;

    /**
     * 请求成功
     */
    private boolean success = true;

    /**
     * 异常信息
     */
    private Exception ex;

    private CloseableHttpClient client;
    private CloseableHttpResponse httpResponse;

    public SoapResponse setAsync(boolean async) {
        this.async = async;
        return this;
    }

    public SoapResponse() {
    }

    public SoapResponse(SoapResultParser parser) {
        this.parser = parser;
    }

    public SoapResponse(SoapRequest request) {
        this.request = request;
    }

    public SoapResponse(SoapRequest request, SoapResultParser parser, boolean debug) {
        this.request = request;
        this.debug = debug;
        this.parser = parser;
    }

    public boolean isDebug() {
        return debug;
    }

    public SoapResponse setDebug(boolean debug) {
        this.debug = debug;
        return this;
    }

    public int getTimeout() {
        return timeout;
    }

    public SoapResponse setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public SoapRequest getRequest() {
        return request;
    }

    public SoapResponse setRequest(SoapRequest request) {
        this.request = request;
        return this;
    }

    /**
     * 执行SOAP请求
     * @return
     * @throws Exception
     */
    public SoapResponse execute() {

        StringBuffer sb = new StringBuffer();

        String xml = null;
        try {
            if(request==null){
                throw new WebServiceException("SOAP请求为空，请设置SoapRequest对象");
            }
            xml = request.xml();
        } catch (Exception e) {
            ex=e;
            e.printStackTrace();
            success = false;
            return this;
        }

        startTime = System.currentTimeMillis();
        if (debug) {
            sb.append("目标：\n\t" + request.getWsdl() + "\n");
            sb.append("SOAPAction:\n\t" + request.getXmlns() + request.getBody().getMethod() + "\n");
            sb.append("请求内容:\n" + xml + "\n");
            System.out.println(sb.toString());
        }

        try {
            //发送Http请求并获取响应
            getServerResponse(xml);
        } catch (Exception e) {
            ex=e;
            e.printStackTrace();
            success = false;
            return this;
        }

        if (debug) {
            sb = new StringBuffer();
            if (!async) {
                sb.append("响应:\n" + responsedXML + "\n");
                endTime = System.currentTimeMillis();
                sb.append("总耗时:\t" + (endTime - startTime) + "ms");
            } else {
                sb.append("异步请求，暂时无法获取响应内容和请求耗时，请自己附加代码进行调试");
            }
            System.out.println(sb.toString() + "\n\n");
        }
        return this;
    }

    /**
     * 获取服务器的响应
     * @param requestXML
     * @return
     * @throws Exception
     */
    private void getServerResponse(String requestXML) throws Exception {

        client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(request.getWsdl());
        try {

            post.setConfig(config());
            post.setHeader("Content-Type", "text/xml;charset=UTF-8");
            post.setHeader("SOAPAction", request.getXmlns() + request.getBody().getMethod());
            StringEntity entity = new StringEntity(requestXML, StandardCharsets.UTF_8);

            post.setEntity(entity);

            if (async) {
                task = new FutureTask<>(() -> httpPost(client, post));
                SoapThreadPool.getInstance().submit(task);
            } else {
                responsedXML = httpPost(client, post);
                if (parser != null) {
                    responsedResult = parser.parse(request.getBody().getMethod(), responsedXML);
                }
            }
        } finally {
            //非异步的时候才能关闭，如果是异步，链接会提前关闭，无法获取结果
            if (!async && client != null) {
                client.close();
            }
        }
    }

    private String httpPost(CloseableHttpClient client, HttpPost post) throws ClientProtocolException, IOException {
        httpResponse = null;
        HttpEntity httpEntity = null;
        try {
            httpResponse = client.execute(post);
            httpEntity = httpResponse.getEntity();
            return IOUtils.toString(httpEntity.getContent());
        } finally {
            //非异步的时候才能关闭，如果是异步，链接会提前关闭，无法获取结果
            if (!async && httpResponse != null) {
                httpResponse.close();
            }
        }
    }


    /**
     * 获取HTTP客户端对象
     * @return
     */
    private CloseableHttpClient client() {
        return HttpClients.custom().setDefaultRequestConfig(config()).build();
    }

    /**
     * 获取Request配置
     * @return
     */
    private RequestConfig config() {
        return RequestConfig.custom().setSocketTimeout(timeout).setConnectionRequestTimeout(timeout).setConnectionRequestTimeout(timeout).build();
    }

    /**
     * 获取耗时
     * @return
     */
    public long getTimeConsuming() {
        return endTime - startTime;
    }

    /**
     * 获取响应的XML
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
                    responsedResult = parser.parse(request.getBody().getMethod(), responsedXML);
                }else{
                    if(debug){
                        System.out.println("未设置结果解析器，结果返回空，可以调用SoapResponse.getResponsedXML()获取返回的未解析的内容");
                    }
                }
            } catch (Exception e) {
                ex=e;
                success=false;
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
     * @return
     */
    public <T> T getResponsedResult() {
        asyncGet();
        return (T)responsedResult;
    }

    /**
     * 是否同步
     * @return
     */
    public boolean isAsync() {
        return async;
    }

    /**
     * 是否执行成功
     * @return
     */
    public boolean isSuccess() {
        asyncGet();
        return success;
    }

    /**
     * 设置结果解析器
     * @param parser
     */
    public SoapResponse setParser(SoapResultParser parser) {
        this.parser = parser;
        return this;
    }

    /**
     * 获取异常信息
     * @return
     */
    public Exception getException(){
        return this.ex;
    }
}
