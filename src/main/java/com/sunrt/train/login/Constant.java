package com.sunrt.train.login;

public interface Constant {
    String popup_passport_appId = "otn";
    String popup_passport_baseUrl = "https://kyfw.12306.cn/passport/";
    String popup_passport_login =  popup_passport_baseUrl + "web/login";
    String popup_passport_captcha = popup_passport_baseUrl + "captcha/captcha-image64?login_site=E&module=login&rand=sjrand&";
    String popup_passport_captcha_check = popup_passport_baseUrl + "captcha/captcha-check";
    String popup_passport_uamtk = popup_passport_baseUrl + "web/auth/uamtk";
    String checkUser="https://kyfw.12306.cn/otn/login/checkUser";
    String uamauthclient="https://kyfw.12306.cn/otn/uamauthclient";
    String userLogin="https://kyfw.12306.cn/otn/login/userLogin";


    int markWidth=26;
    int markHeight=26;
    int CodeHeight=188;
    int CodeWidth=300;

    String rerushText="刷新";
    String commitText="提交";
    String noCodeText="请选择验证码！";
    String codeErrText="验证码错误";
    String codeFormat="JPG";
    String checkSuccessCode="4";
    String codeGetFail="验证码拉取失败！";
    String codeCheckFail="验证码验证失败！";
    String loginFailText="登陆失败！";
    String markFailText="图片读取失败";
    String cookiesReadFail="cookies读取失败";

    String title="验证码";
    String markPath="/pic/mark.png";
}
