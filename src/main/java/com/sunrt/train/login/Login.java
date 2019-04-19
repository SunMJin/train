package com.sunrt.train.login;

import com.sunrt.train.login.API;
import com.sunrt.train.login.Captcha;
import com.sunrt.train.utils.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class Login {

    private String userName = "1036524012@qq.com";
    private String password = "lol9403J";

    public boolean validate(int pointsCount){

        String mobile="/^(13[0-9])|(14[0-9])|(15[0-9])|(18[0-9])|(17[0-9])\\d{8}$/";
        String tel = "/^[A-Za-z]{1}([A-Za-z0-9]|[_]) {0,29}$/";
        String tel_other = "/^((([a-z]|\\d|[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])+(\\.([a-z]|\\d|[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])+)*)|((\\x22)((((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)?(([\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x7f]|\\x21|[\\x23-\\x5b]|[\\x5d-\\x7e]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(\\\\([\\x01-\\x09\\x0b\\x0c\\x0d-\\x7f]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF]))))*(((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)?(\\x22)))@((([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])))\\.)+(([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])))$/i";
        if(StringUtils.isEmpty(userName)){
            System.out.println("请输入用户名！");
            return false;
        }
        if (StringUtils.isEmpty(password)) {
            System.out.println("请输入密码！");
            return false;
        }
        if (password.length() < 6) {
            System.out.println("密码长度不能少于6位！");
            return false;
        }

        if (pointsCount == 0) {
            System.out.println("请选择验证码！");
            return false;
        }
        return true;
    }

    public void start(int pointsCount,String randCode){
        if(validate(pointsCount)){
            loginForUam(randCode);
        }
    }

    public void loginForUam(String randCode){
        List<NameValuePair> listParams= Form.form()
                .add("username",userName)
                .add("password",password)
                .add("appid",API.popup_passport_appId)
                .add("answer",randCode)
                .build();
        try{
            HttpPost post=new HttpPost(API.popup_passport_login);
            post.setEntity(new UrlEncodedFormEntity(listParams));
            String cookies=
                    "RAIL_DEVICEID=KrOb6A_-X2fC1a3TsoJFAe8R2gwBQT8kwSvo_pdYn9cNZMsHM8boHn-RnSmruWb33hccmidp4bl5enkX9ko8GF0dAk4RyJrUhj8KODFQRDQQ1KXTbtiRkzmxTZedAbciw9wO5t3gkPR_AS7tX6UlrqQvvwytuc2r;" +
                            "RAIL_EXPIRATION=1555930365856;";
            post.addHeader("Cookie", cookies);
            String content= HttpUtils.PostCus(post);
            JSONObject json=new JSONObject(content);
            System.out.println(json);
            int result_code=json.getInt("result_code");
            if(result_code==0){
                HttpUtils.Post("https://kyfw.12306.cn/otn/login/userLogin");
                CloseableHttpResponse response=HttpUtils.getResponse();
                int StatusCode=response.getStatusLine().getStatusCode();
                if(StatusCode==302){
                    String location=response.getFirstHeader("Location").getValue();
                    HttpUtils.Get(location);
                    JSONObject tkJson=new JSONObject(HttpUtils.Post(API.popup_passport_uamtk,Form.form().add("appid", "otn").build()));
                    if(tkJson.getInt("result_code")==0){
                        System.out.println(tkJson.getString("result_message"));
                        String tk=tkJson.getString("newapptk");
                        String uamauthclientJson=HttpUtils.Post("https://kyfw.12306.cn/otn/uamauthclient",Form.form().add("tk", tk).build());
                        System.out.println(uamauthclientJson);

                        String c=HttpUtils.Post("https://kyfw.12306.cn/otn/passengers/query", Form.form().add("pageIndex","1").add("pageSize","10").build());
                        System.out.println(c);
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            return;
        }
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        Captcha captcha=new Captcha();
        captcha.createPassCode();
    }

}
