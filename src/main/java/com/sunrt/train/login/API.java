package com.sunrt.train.login;

public class API {
    public static final String popup_passport_appId = "otn";
    public static final String popup_passport_baseUrl = "https://kyfw.12306.cn/passport/";
    public static final String popup_passport_login =  popup_passport_baseUrl + "web/login";
    public static final String popup_passport_captcha = popup_passport_baseUrl + "captcha/captcha-image64?login_site=E&module=login&rand=sjrand&";
    public static final String popup_passport_captcha_check = popup_passport_baseUrl + "captcha/captcha-check";
    public static final String popup_passport_uamtk = popup_passport_baseUrl + "web/auth/uamtk";


    public static final int markWidth=26;
    public static final int markHeight=26;
}
