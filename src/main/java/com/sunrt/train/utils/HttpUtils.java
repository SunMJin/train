package com.sunrt.train.utils;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
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
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

    public static JSONObject Get(String uri){
        return Get(uri,null);
    }

    public static JSONObject Get(String uri, List<NameValuePair> listParams){
        try {
            HttpGet httpGet = new HttpGet(buildURI(uri,listParams));
            response = httpclient.execute(httpGet);
            HttpEntity httpEntity=response.getEntity();
            String content=EntityUtils.toString(httpEntity);
            EntityUtils.consume(httpEntity);
            return new JSONObject(content);
        }catch (Exception e){}
        return null;
    }

    public static String GetStr(String uri){
        try {
            HttpGet httpGet = new HttpGet(buildURI(uri,null));
            response = httpclient.execute(httpGet);
            HttpEntity httpEntity=response.getEntity();
            String content=EntityUtils.toString(httpEntity);
            EntityUtils.consume(httpEntity);
            return content;
        }catch (Exception e){}
        return null;
    }

    public static JSONObject Post(String uri){
        return Post(uri,null);
    }

    public static JSONObject PostCus(HttpPost httpPost){
        try {
            response = httpclient.execute(httpPost);
            HttpEntity httpEntity=response.getEntity();
            String content=EntityUtils.toString(httpEntity);
            EntityUtils.consume(httpEntity);
            return new JSONObject(content);
        } catch (Exception e) {}
        return null;
    }

    public static JSONObject Post(String uri, List<NameValuePair> listParams){
        try {
            HttpPost httpPost = new HttpPost(uri);
            if(listParams!=null){
                httpPost.setEntity(new UrlEncodedFormEntity(listParams));
            }
            response = httpclient.execute(httpPost);
            HttpEntity httpEntity=response.getEntity();
            String content=EntityUtils.toString(httpEntity);
            EntityUtils.consume(httpEntity);
            return new JSONObject(content);
        } catch (Exception e) {}
        return null;
    }

    public static String PostStr(String uri, List<NameValuePair> listParams){
        try {
            HttpPost httpPost = new HttpPost(uri);
            if(listParams!=null){
                httpPost.setEntity(new UrlEncodedFormEntity(listParams));
            }
            response = httpclient.execute(httpPost);
            HttpEntity httpEntity=response.getEntity();
            String content=EntityUtils.toString(httpEntity);
            EntityUtils.consume(httpEntity);
            return content;
        } catch (Exception e) {}
        return null;
    }

}
