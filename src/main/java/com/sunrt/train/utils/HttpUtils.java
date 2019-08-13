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

    private String host;
    private Integer port;

    private String encoding="UTF-8";

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    private CloseableHttpClient httpclient;
    private CloseableHttpResponse response;
    public CloseableHttpResponse getResponse() {
        return response;
    }
    private CookieStore cookieStore;
    private boolean isProxyByFiddle;
    public final static File cookiesFile= new File("cookies");

    public HttpUtils(){
        httpclient=buildHttpClient();
    }

    public HttpUtils(boolean isProxyByFiddle,String host,Integer port){
        this.isProxyByFiddle=isProxyByFiddle;
        this.host=host;
        this.port=port;
        httpclient=buildHttpClient();
    }

    public void saveCookies(){
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(cookiesFile));){
            objectOutputStream.writeObject(cookieStore);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCookies(){
        if(!cookiesFile.exists()){
            cookieStore=new BasicCookieStore();
            return;
        }
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(cookiesFile));){
            cookieStore=(CookieStore)inputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private CloseableHttpClient buildHttpClient() {
        loadCookies();
        HttpClientBuilder hb =HttpClients.custom().setDefaultCookieStore(cookieStore);
        if(isProxyByFiddle){
            hb.setSSLSocketFactory(SSLUtils.trustAllHttpsCertificates())
                    .setProxy(new HttpHost(host,port));
        }
        return hb.build();
    }

    public void reset(){
        httpclient= buildHttpClient();
    }

    private URI buildURI(String uri, List<NameValuePair> listParams) {
        if(uri==null){
            throw new NullPointerException();
        }
        try{
            URIBuilder uriBuilder = new URIBuilder(uri);
            if(listParams!=null){
                uriBuilder.addParameters(listParams);
            }
            return uriBuilder.build();
        }catch(URISyntaxException e){
            throw new RuntimeException();
        }
    }

    public JSONObject Get(String uri) {
        return Get(uri,null);
    }

    public JSONObject Get(String uri, List<NameValuePair> listParams) {
        return new JSONObject(GetHtml(uri,listParams));
    }

    public String GetHtml(String uri, List<NameValuePair> listParams) {
        HttpGet httpGet = new HttpGet(buildURI(uri,listParams));
        try {
            response = httpclient.execute(httpGet);
            HttpEntity httpEntity=response.getEntity();
            String content=EntityUtils.toString(httpEntity);
            EntityUtils.consume(httpEntity);
            return content;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String GetHtml(String uri) {
        return GetHtml(uri,null);
    }

    public JSONObject PostJson(String uri) {
        return postJson(uri,null);
    }

    public JSONObject PostCus(HttpPost httpPost) {
        try {
            response = httpclient.execute(httpPost);
            HttpEntity httpEntity=response.getEntity();
            String content=EntityUtils.toString(httpEntity);
            EntityUtils.consume(httpEntity);
            return new JSONObject(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject postJson(String uri, List<NameValuePair> listParams) {
        return new JSONObject(postHtml(uri,listParams));
    }

    public String postHtml(String uri, List<NameValuePair> listParams) {
        if(uri==null){
            throw new NullPointerException();
        }
        HttpPost httpPost = new HttpPost(uri);
        try {
            if(listParams!=null){
                httpPost.setEntity(new UrlEncodedFormEntity(listParams,encoding));
            }
            response = httpclient.execute(httpPost);
            HttpEntity httpEntity=response.getEntity();
            String content=EntityUtils.toString(httpEntity);
            EntityUtils.consume(httpEntity);
            return content;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
