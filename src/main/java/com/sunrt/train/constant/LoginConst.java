package com.sunrt.train.constant;

public interface LoginConst {
    String popup_passport_appId = "otn";
    String popup_passport_baseUrl = "https://kyfw.12306.cn/passport/";
    String popup_passport_login = popup_passport_baseUrl + "web/login";
    String popup_passport_captcha = popup_passport_baseUrl + "captcha/captcha-image64?login_site=E&module=login&rand=sjrand&";
    String popup_passport_captcha_check = popup_passport_baseUrl + "captcha/captcha-check";
    String popup_passport_uamtk = popup_passport_baseUrl + "web/auth/uamtk";
    String checkUser = "https://kyfw.12306.cn/otn/login/checkUser";
    String uamauthclient = "https://kyfw.12306.cn/otn/uamauthclient";
    String userLogin = "https://kyfw.12306.cn/otn/login/userLogin";
    String checkSuccessCode = "4";
}
