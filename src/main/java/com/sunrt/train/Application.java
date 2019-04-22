package com.sunrt.train;

import com.sunrt.train.login.Captcha;
import com.sunrt.train.login.Login;

import java.io.Console;

public class Application {
    public static void main(String[] args) {
        Login login=new Login();
        Captcha captcha=new Captcha();
        captcha.createPassCode();
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
                captcha.setVisible(true);
                break;
            }
        }
    }
}
