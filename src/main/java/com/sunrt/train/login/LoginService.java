package com.sunrt.train.login;

import com.sunrt.train.TrainHttp;
import com.sunrt.train.constant.LoginConst;
import com.sunrt.train.conf.TrainConf;
import com.sunrt.train.utils.HttpUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONObject;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Set;

public class LoginService {
    private static LoginService loginService;
    private LoginService(){};
    public static LoginService getInstance(){
        if(loginService==null){
            loginService=new LoginService();
        }
        return loginService;
    }
    private HttpUtils httpUtils = TrainHttp.getInstance();
    private CaptchaService captchaService = CaptchaService.getInstance();
    private String username;
    private String password;
    private String successInfo;

    public static String getDeviceId() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("--no-sandbox");
        WebDriver driver = new ChromeDriver(chromeOptions);
        driver.get("https://www.12306.cn/index/");
        while(true){
            Set<Cookie> cookies=driver.manage().getCookies();
            for(Cookie cookie:cookies){
                if("RAIL_DEVICEID".equals(cookie.getName())){
                    System.out.println(cookie);
                    driver.quit();
                    return cookie.getValue();
                }
            }
        }
    }
    //判断当前是否登录
    public boolean checkUser() {
        JSONObject json = httpUtils.postJson(LoginConst.checkUser, Form.form().add("_json_att", "").build());
        return json.getJSONObject("data").getBoolean("flag");
    }

    public String login(String username, String password) {
        this.username = username;
        this.password = password;
        while (true) {
            String ranCode = captchaService.getcode();
            if (captchaService.checkPassCode(ranCode)) {
                if (loginForUam(ranCode)) {
                    httpUtils.saveCookies();
                    return successInfo;
                }
            }
        }
    }

    public boolean loginForUam(String randCode) {
        List<NameValuePair> listParams = Form.form()
                .add("username", username)
                .add("password", password)
                .add("appid", LoginConst.popup_passport_appId)
                .add("answer", randCode)
                .build();
        HttpPost post = new HttpPost(LoginConst.popup_passport_login);
        try {
            post.setEntity(new UrlEncodedFormEntity(listParams));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        post.addHeader("Cookie", "RAIL_DEVICEID=" + getDeviceId());
        JSONObject json = httpUtils.PostCus(post);
        int result_code = json.getInt("result_code");
        if (result_code == 0) {
            httpUtils.postHtml(LoginConst.userLogin, null);
            CloseableHttpResponse response = httpUtils.getResponse();
            int StatusCode = response.getStatusLine().getStatusCode();
            if (StatusCode == 302) {
                String location = response.getFirstHeader("Location").getValue();
                httpUtils.GetHtml(location);
                JSONObject tkJson = httpUtils.postJson(LoginConst.popup_passport_uamtk, Form.form().add("appid", "otn").build());
                if (tkJson.getInt("result_code") == 0) {
                    JSONObject ucJson = httpUtils.postJson(LoginConst.uamauthclient, Form.form().add("tk", tkJson.getString("newapptk")).build());
                    if (ucJson.getInt("result_code") == 0) {
                        successInfo = ucJson.toString();
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
