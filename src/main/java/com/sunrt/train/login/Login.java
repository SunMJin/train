package com.sunrt.train.login;

import com.sunrt.train.TrainHttp;
import com.sunrt.train.exception.HttpException;
import com.sunrt.train.utils.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONObject;

import java.io.*;
import java.util.List;
import java.util.Properties;

public class Login {
    private static HttpUtils httpUtils = TrainHttp.getHttp();
    private static String username;
    private static char[] password;
    private static String cookies;
    public static void setUsername(String username) {
        Login.username = username;
    }
    public static void setPassword(char[] password) {
        Login.password = password;
    }

    public static boolean checkUser() {
        try {
            JSONObject json = httpUtils.Post(Constant.checkUser, Form.form().add("_json_att", "").build());
            return json.getJSONObject("data").getBoolean("flag");
        } catch (HttpException e) { }
        return false;
    }


    static{
        Properties properties = new Properties();
        try {
            properties.load(Login.class.getClassLoader().getResourceAsStream("cookies.properties"));
            cookies="RAIL_DEVICEID="+properties.getProperty("RAIL_DEVICEID")+";RAIL_EXPIRATION="+properties.getProperty("RAIL_EXPIRATION");;
        } catch (IOException e) {
            System.out.println(Constant.cookiesReadFail);
        }
    }

    public static boolean unvalid(String username){
        if(StringUtils.isEmpty(username)){
            return false;
        }
        return true;
    }

    public static boolean pwValid(char pw[]){
        if (pw.length<6 ) {
            System.out.println("密码长度不能少于6位！");
            return false;
        }
        return true;
    }

    public static void testmain(){
        System.out.println("欢迎使用12306购票系统");
        Login login=new Login();
        Captcha.createPassCode();
        login.setUsername("1036524012@qq.com");
        login.setPassword("lol9403J".toCharArray());
        Captcha.setVisible(true);
    }

    public static void main(){
        System.out.println("欢迎使用12306购票系统");
        Login login=new Login();
        Captcha.createPassCode();
        Console console=System.console();
        while(true){
            System.out.println("用户名：");
            String username=console.readLine();
            if(login.unvalid(username)){
                login.setUsername(username);
                break;
            }else{
                System.out.println("用户名格式错误");
            }
        }
        while(true){
            System.out.println("密码：");
            char pw[]=console.readPassword();
            if(login.pwValid(pw)){
                login.setPassword(pw);
                Captcha.setVisible(true);
                break;
            }
        }
    }

    public static JSONObject loginForUam(String randCode){
        List<NameValuePair> listParams= Form.form()
                .add("username",username)
                .add("password",new String(password))
                .add("appid", Constant.popup_passport_appId)
                .add("answer",randCode)
                .build();
        try{
            HttpPost post=new HttpPost(Constant.popup_passport_login);
            post.setEntity(new UrlEncodedFormEntity(listParams));
            post.addHeader("Cookie", cookies);
            JSONObject json=httpUtils.PostCus(post);
            int result_code=json.getInt("result_code");
            if(result_code==0){
                httpUtils.Post(Constant.userLogin);
                CloseableHttpResponse response=httpUtils.getResponse();
                int StatusCode=response.getStatusLine().getStatusCode();
                if(StatusCode==302){
                    String location=response.getFirstHeader("Location").getValue();
                    httpUtils.Get(location);
                    JSONObject tkJson=httpUtils.Post(Constant.popup_passport_uamtk,Form.form().add("appid", "otn").build());
                    if(tkJson.getInt("result_code")==0){
                        JSONObject ucJson=httpUtils.Post(Constant.uamauthclient,Form.form().add("tk", tkJson.getString("newapptk")).build());
                        if(ucJson.getInt("result_code")==0){
                            ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(new File(HttpUtils.COOKIESFILEPATH)));
                            oos.writeObject(httpUtils.getCookieStore());
                            oos.close();
                            return ucJson;
                        }
                    }
                }
            }
        }catch (HttpException e){
        }catch (IOException e){}
        return null;
    }
}
