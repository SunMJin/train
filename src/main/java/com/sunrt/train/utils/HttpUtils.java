package com.sunrt.train.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class HttpUtils {
    public static final String COOKIESFILEPATH="d:"+File.separator+"obj";
    
    private static CookieStore cookieStore;
    public static CookieStore getCookieStore() {
        return cookieStore;
    }
    private static CloseableHttpClient httpclient;
    private static CloseableHttpResponse response = null;
    public static CloseableHttpResponse getResponse() {
        return response;
    }

    static {
        try {
            buildHttpClient();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static CloseableHttpClient buildHttpClient() throws IOException, ClassNotFoundException {
        return buildHttpClient(false,true);
    }

    private static void resetCookieStore(){
        cookieStore=new BasicCookieStore();
    }

    private static CloseableHttpClient buildHttpClient(boolean isProxyByFiddle,boolean isReadLocalCookies) throws IOException, ClassNotFoundException {
        if(isReadLocalCookies){
            File cookiesFile=new File(COOKIESFILEPATH);
            if(cookiesFile.exists()){
                ObjectInputStream ois = null;
                ois = new ObjectInputStream(new FileInputStream(cookiesFile));
                cookieStore=(CookieStore)ois.readObject();
                ois.close();
            }else{
                resetCookieStore();
            }
        }else{
            resetCookieStore();
        }
        HttpClientBuilder hb =HttpClients.custom().setDefaultCookieStore(cookieStore);
        if(isProxyByFiddle){
            hb.setSSLSocketFactory(SSLUtils.trustAllHttpsCertificates())
                    .setProxy(new HttpHost("127.0.0.1",8888));
        }
        return hb.build();
    }

    public static void clearCookies() throws IOException, ClassNotFoundException {
        resetCookieStore();
        httpclient=buildHttpClient();
    }

    public static void PRCookies() {
        List<Cookie> cookies=cookieStore.getCookies();
        for(Cookie c:cookies){
            System.out.println(c.getName()+":"+c.getValue());
        }
    }

    private static URI buildURI(String uri, List<NameValuePair> listParams) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(uri);
        if(listParams!=null){
            uriBuilder.addParameters(listParams);
        }
        return uriBuilder.build();
    }

    public static JSONObject Get(String uri) throws IOException, URISyntaxException {
        if(uri==null){
            throw new NullPointerException();
        }
        return Get(uri,null);
    }

    public static JSONObject Get(String uri, List<NameValuePair> listParams) throws URISyntaxException, IOException  {
        return new JSONObject(GetStr(uri,listParams));
    }

    public static String GetStr(String uri,List<NameValuePair> listParams) throws URISyntaxException, IOException {
        if(uri==null){
            throw new NullPointerException();
        }
        HttpGet httpGet = new HttpGet(buildURI(uri,listParams));
        response = httpclient.execute(httpGet);
        HttpEntity httpEntity=response.getEntity();
        String content=EntityUtils.toString(httpEntity);
        EntityUtils.consume(httpEntity);
        return content;
    }

    public static String GetStr(String uri) throws IOException, URISyntaxException {
        return GetStr(uri,null);
    }

    public static JSONObject Post(String uri) throws IOException {
        return Post(uri,null);
    }

    public static JSONObject PostCus(HttpPost httpPost) throws IOException {
        response = httpclient.execute(httpPost);
        HttpEntity httpEntity=response.getEntity();
        String content=EntityUtils.toString(httpEntity);
        EntityUtils.consume(httpEntity);
        return new JSONObject(content);
    }

    public static JSONObject Post(String uri, List<NameValuePair> listParams) throws IOException {
        return new JSONObject(PostStr(uri,listParams));
    }

    public static String PostStr(String uri, List<NameValuePair> listParams) throws IOException {
        if(uri==null){
            throw new NullPointerException();
        }
        HttpPost httpPost = new HttpPost(uri);
        if(listParams!=null){
            httpPost.setEntity(new UrlEncodedFormEntity(listParams));
        }
        response = httpclient.execute(httpPost);
        HttpEntity httpEntity=response.getEntity();
        String content=EntityUtils.toString(httpEntity);
        EntityUtils.consume(httpEntity);
        return content;
    }

}
