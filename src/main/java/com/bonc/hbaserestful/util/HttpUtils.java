package com.bonc.hbaserestful.util;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/**
 * httpClient Helper
 *
 * @author yangdx
 * @version 1.0.0
 */
public class HttpUtils {

    // 连接池对象
    private static PoolingHttpClientConnectionManager httpClientPool = null;

    static {
        LayeredConnectionSocketFactory sslsf = null;

        try {
            sslsf = new SSLConnectionSocketFactory(SSLContext.getDefault());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("https", sslsf)
                .register("http", new PlainConnectionSocketFactory())
                .build();

        // 构建连接池
        httpClientPool = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        httpClientPool.setMaxTotal(500);
        httpClientPool.setDefaultMaxPerRoute(20);
    }

    /**
     * 获取HttpClient对象
     *
     * @return HttpClient
     */
    private static HttpClient getHttpClient() {
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(httpClientPool)
                .setUserAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.112 Safari/537.36")//浏览器版本
                .setRetryHandler(new DefaultHttpRequestRetryHandler(2, true))//请求超时后重发2次
                .build();

        return httpClient;
    }

    /**
     * 执行HTTP请求
     *
     * @param method 请求对象
     * @throws IOException
     */
    public static String doMethod(HttpRequestBase method) throws IOException {
        try {
            HttpResponse response = getHttpClient().execute(method);

            //return EntityUtils.toString(response.getEntity());
            return EntityUtils.toString(response.getEntity(),"utf-8");
        } finally {
            method.releaseConnection();
        }
    }

    /**
     * 执行 POST请求
     *
     * @param uri    请求url
     * @param params 参数集合
     * @return 执行结果字符串
     */
    public static String doPost(String uri, Map<String, String> params) throws IOException {
        return doPost(uri, new UrlEncodedFormEntity(map2Pairs(params), "UTF-8"));
    }

    /**
     * 执行 POST请求
     *
     * @param uri       请求url
     * @param jsonPrams json参数
     * @return 执行结果字符串
     */
    public static String doPost(String uri, String jsonPrams) throws IOException {
        return doPost(uri, new StringEntity(jsonPrams, "UTF-8"));
    }

    /**
     * 执行POST请求
     *
     * @param uri    url地址
     * @param entity StringEntity 对象
     * @return 执行结果字符串
     */
    public static String doPost(String uri, StringEntity entity) throws IOException {
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setEntity(entity);

        return doMethod(httpPost);
    }

    /**
     * 执行GET请求
     *
     * @param uri url地址
     * @return 返回字符串
     */
    public static String doGet(String uri) throws IOException {
        HttpGet httpGet = new HttpGet(uri);

        return doMethod(httpGet);
    }

    /**
     * 参数集合转换成 NameValuePair
     *
     * @param params 参数集合
     * @return NameValuePair集合
     */
    private static List<NameValuePair> map2Pairs(Map<String, String> params) {
        List<NameValuePair> listPairs = new ArrayList<>();

        if (null != params) {
            Iterator<Map.Entry<String, String>> iter = params.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, String> entry = iter.next();
                listPairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }

        return listPairs;
    }
}
