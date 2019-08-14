package com.sunrt.train.login;

import com.sunrt.train.TrainHttp;
import com.sunrt.train.data.TrainConf;
import com.sunrt.train.utils.HttpUtils;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;


public class CaptchaService {

    private CaptchaService(){}
    private static CaptchaService captchaService;

    public static CaptchaService getInstance(){
        if(captchaService==null){
            captchaService=new CaptchaService();
        }
        return captchaService;
    }

    private HttpUtils httpUtils=TrainHttp.getInstance();

    private Socket socket;
    private BufferedWriter bw;
    private BufferedReader br;


    public boolean conn(String host,int port) {
        try {
            socket = new Socket(host,port);
            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void close(){
        try {
            if(br!=null){
                br.close();
            }
            if(bw!=null){
                bw.close();
            }
            if(socket!=null){
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public String getPoints(String file) {
        try {
            bw.write(file);
            bw.flush();
            char[] buff=new char[1024];
            int len=br.read(buff);
            return new String(buff, 0,len);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getcode(){
        String file=downloadCode();
        return getPoints(file);
    }

    private String downloadCode() {
        long temp = new Date().getTime();
        try {
            JSONObject json = httpUtils.Get(LoginConst.popup_passport_captcha + temp);
            ByteArrayInputStream in=new ByteArrayInputStream(Base64.decodeBase64(json.getString("image")));
            Path path= Paths.get(TrainConf.captchaDownPath);
            if(!Files.exists(path)){
                Files.createDirectories(path);
            }
            Path file= Paths.get(TrainConf.captchaDownPath+ File.separator+System.currentTimeMillis()+".jpg");
            Files.copy(in, file);
            in.close();
            return file.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public boolean checkPassCode(String randCode){
        JSONObject json = httpUtils.Get(LoginConst.popup_passport_captcha_check+"?answer="+randCode+"&&rand=sjrand&&login_site=E");
        String result_code=json.getString("result_code");
        if(LoginConst.checkSuccessCode.equalsIgnoreCase(result_code)){
            return true;
        }else{
            return false;
        }
    }

}
