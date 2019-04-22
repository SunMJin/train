package com.sunrt.train.utils;

import org.apache.http.HttpEntity;
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
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class HttpUtils {

    private static CookieStore cookieStore;
    private static CloseableHttpClient httpclient;
    private static CloseableHttpResponse response = null;

    static {
        cookieStore=new BasicCookieStore();
        httpclient=HttpClients.custom().setDefaultCookieStore(cookieStore).build();
    }

    public static void clearCookies(){
        cookieStore.clear();
        httpclient=HttpClients.custom().setDefaultCookieStore(cookieStore).build();
    }

    public static CloseableHttpResponse getResponse() {
        return response;
    }


    public static String getCookie(String name) {
        List<Cookie> cookies=cookieStore.getCookies();
        for(Cookie c:cookies){
            if(c.getName().equals(name)){
                return c.getValue();
            }
        }
        return null;
    }

    public static void PRCookies() {
        List<Cookie> cookies=cookieStore.getCookies();
        for(Cookie c:cookies){
            System.out.println(c.getName()+":"+c.getValue());
        }
    }


    private static URI buildURI(String uri, List<NameValuePair> listParams){
        try {
            URIBuilder uriBuilder = new URIBuilder(uri);
            if(listParams!=null){
                uriBuilder.addParameters(listParams);
            }
            return uriBuilder.build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String Get(String uri){
        return Get(uri,null);
    }

    public static String Get(String uri, List<NameValuePair> listParams){
        String content=null;
        try {
            HttpGet httpGet = new HttpGet(buildURI(uri,listParams));
            response = httpclient.execute(httpGet);
            HttpEntity httpEntity=response.getEntity();
            content=EntityUtils.toString(httpEntity);
            EntityUtils.consume(httpEntity);
        } catch (Exception e) {
            return null;
        }
        return content;
    }

    public static String Post(String uri){
        return Post(uri,null);
    }

    public static String PostCus(HttpPost httpPost){
        String content=null;
        try {
            response = httpclient.execute(httpPost);
            HttpEntity httpEntity=response.getEntity();
            content=EntityUtils.toString(httpEntity);
            EntityUtils.consume(httpEntity);
        } catch (Exception e) {
            return null;
        }
        return content;
    }

    public static String Post(String uri, List<NameValuePair> listParams){
        String content=null;
        try {
            HttpPost httpPost = new HttpPost(uri);
            if(listParams!=null){
                httpPost.setEntity(new UrlEncodedFormEntity(listParams));
            }
            response = httpclient.execute(httpPost);
            HttpEntity httpEntity=response.getEntity();
            content=EntityUtils.toString(httpEntity);
            EntityUtils.consume(httpEntity);
        } catch (Exception e) {
            return null;
        }
        return content;
    }

}
