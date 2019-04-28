package com.sunrt.train.utils;

import com.sunrt.train.exception.HttpException;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class HttpUtils {

    public static final String COOKIESFILEPATH="d:"+File.separator+"obj";
    
    private CookieStore cookieStore;
    public CookieStore getCookieStore() {
        return cookieStore;
    }

    private CloseableHttpClient httpclient;
    private CloseableHttpResponse response;
    public CloseableHttpResponse getResponse() {
        return response;
    }

    private boolean isProxyByFiddle;
    private boolean isReadLocalCookies;

    public HttpUtils(boolean isProxyByFiddle,boolean isReadLocalCookies){
        this.isProxyByFiddle=isProxyByFiddle;
        this.isReadLocalCookies=isReadLocalCookies;
        buildHttpClient(isProxyByFiddle,isReadLocalCookies);
    }

    private void resetCookieStore(){
        cookieStore=new BasicCookieStore();
    }

    private CloseableHttpClient buildHttpClient(boolean isProxyByFiddle,boolean isReadLocalCookies) {
        if(isReadLocalCookies){
            File cookiesFile=new File(COOKIESFILEPATH);
            if(cookiesFile.exists()){
                try {
                    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(cookiesFile));
                    cookieStore=(CookieStore)ois.readObject();
                    ois.close();
                } catch (IOException e) {
                    throw new RuntimeException();
                } catch (ClassNotFoundException e){
                    throw new RuntimeException();
                }
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

    public void resetHttp(){
        resetCookieStore();
        httpclient= buildHttpClient(isProxyByFiddle,isReadLocalCookies);
    }

    public void PRCookies() {
        List<Cookie> cookies=cookieStore.getCookies();
        for(Cookie c:cookies){
            System.out.println(c.getName()+":"+c.getValue());
        }
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

    public JSONObject Get(String uri) throws HttpException {
        return Get(uri,null);
    }

    public JSONObject Get(String uri, List<NameValuePair> listParams) throws HttpException {
        return new JSONObject(GetHtml(uri,listParams));
    }

    public String GetHtml(String uri, List<NameValuePair> listParams) throws HttpException {
        HttpGet httpGet = new HttpGet(buildURI(uri,listParams));
        try {
            response = httpclient.execute(httpGet);
            HttpEntity httpEntity=response.getEntity();
            String content=EntityUtils.toString(httpEntity);
            EntityUtils.consume(httpEntity);
            return content;
        } catch (IOException e) {
            throw new HttpException(uri);
        }
    }

    public String GetHtml(String uri) throws HttpException {
        return GetHtml(uri,null);
    }

    public JSONObject Post(String uri) throws HttpException {
        return Post(uri,null);
    }

    public JSONObject PostCus(HttpPost httpPost) throws HttpException {
        try {
            response = httpclient.execute(httpPost);
            HttpEntity httpEntity=response.getEntity();
            String content=EntityUtils.toString(httpEntity);
            EntityUtils.consume(httpEntity);
            return new JSONObject(content);
        } catch (IOException e) {
            throw new HttpException(httpPost.getURI().toString());
        }
    }

    public JSONObject Post(String uri, List<NameValuePair> listParams) throws HttpException {
        return new JSONObject(PostHtml(uri,listParams));
    }

    public String PostHtml(String uri, List<NameValuePair> listParams) throws HttpException {
        if(uri==null){
            throw new NullPointerException();
        }
        HttpPost httpPost = new HttpPost(uri);
        try{
            if(listParams!=null){
                httpPost.setEntity(new UrlEncodedFormEntity(listParams));
            }
            response = httpclient.execute(httpPost);
            HttpEntity httpEntity=response.getEntity();
            String content=EntityUtils.toString(httpEntity);
            EntityUtils.consume(httpEntity);
            return content;
        }catch(IOException e){
            throw new HttpException(uri);
        }
    }

}
