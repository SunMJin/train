package com.sunrt.train.login;

import com.sunrt.train.TrainHttp;
import com.sunrt.train.exception.HttpException;
import com.sunrt.train.exception.VerifcationException;
import com.sunrt.train.ticket.BuyTicketHandle;
import com.sunrt.train.ticket.Param;
import com.sunrt.train.ticket.Stum;
import com.sunrt.train.utils.HttpUtils;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Captcha {
    private static HttpUtils httpUtils = TrainHttp.getHttp();
    private static Map<JLabel,String> points;
    private static JFrame window;
    private static JLabel codeLabel;
    private static ImageIcon imageIcon;

    private static void markFail(){
        System.out.println(Constant.markFailText);
    }

    static{
        points=new HashMap();
        InputStream in=Captcha.class.getResourceAsStream(Constant.markPath);
        byte buf[]=null;
        try {
            buf = new byte[in.available()];
            in.read(buf);
        } catch (IOException e) {
            markFail();
        }
        imageIcon=new ImageIcon(buf);
        window =new JFrame();
        window.setIconImage(imageIcon.getImage());
        window.setTitle(Constant.title);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(null);
        window.setLocationRelativeTo(null);
        window.setSize((int)(Constant.CodeWidth*1.5),(int)(Constant.CodeHeight*1.5));
        addButton();
    }

    public static void setVisible(boolean flag){
        window.setVisible(flag);
    }

    public static ImageIcon getCode() throws VerifcationException {
        long temp = new Date().getTime();
        ByteArrayInputStream in=null;
        ByteArrayOutputStream baos=null;
        try {
            JSONObject json = httpUtils.Get(Constant.popup_passport_captcha + temp);
            in=new ByteArrayInputStream(Base64.decodeBase64(json.getString("image")));
            BufferedImage src = ImageIO.read(in);
            baos=new ByteArrayOutputStream(1024);
            ImageIO.write(src, Constant.codeFormat, baos);
            return new ImageIcon(baos.toByteArray());
        }catch (Exception e){
            throw new VerifcationException(e.getMessage());
        }finally {
            try {
                if(in!=null){
                    in.close();
                }
                if(baos!=null){
                    baos.close();
                }
            } catch (IOException e) {
                throw new VerifcationException(e.getMessage());
            }
        }
    }

    public static void codeCheckFail(){
        System.out.println(Constant.codeCheckFail);
    }
    public static void addButton(){
        JButton submit=new JButton(Constant.commitText);
        submit.setSize(70,30);
        submit.setLocation(0,200);
        submit.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }
            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    if(checkPassCode()){
                        JSONObject loginInfo=Login.loginForUam(getRandCode());
                        if(loginInfo!=null){
                            window.dispose();
                            System.out.println(loginInfo);
                            try{
                                BuyTicketHandle.start(new Param(new Stum[]{Stum.yz,Stum.ze},"2019-04-30","WXH","SHH","ADULT",null,null,null,"无锡","上海","dc"));
                            }catch (HttpException e2){
                                System.out.println("任务失败！");
                            }

                        }else{
                            System.out.println(Constant.loginFailText);
                        }
                    }else{
                        codeCheckFail();
                    }
                } catch (HttpException e1) {
                    codeCheckFail();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        window.add(submit);

        JButton rerush=new JButton(Constant.rerushText);
        rerush.setSize(70,30);
        rerush.setLocation(90,200);
        rerush.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                refreshPassCode();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        window.add(rerush);

        window.repaint();
    }

    public static void createPassCode() {
        ImageIcon code= null;
        try {
            code = getCode();
        } catch (VerifcationException e) {
            codeGetFail();
        }
        if(code!=null){
            codeLabel=new JLabel(code);
            codeLabel.addMouseListener(new MouseListener() {
                public void mouseClicked(MouseEvent e) {

                }
                public void mousePressed(MouseEvent e) {
                    int x=e.getX();
                    int y=e.getY();
                    JLabel markLabel=new JLabel(imageIcon);
                    markLabel.setSize(Constant.markWidth, Constant.markHeight);
                    markLabel.setLocation(x- Constant.markWidth/2,y- Constant.markHeight/2);
                    markLabel.addMouseListener(new MouseListener() {
                        @Override
                        public void mouseClicked(MouseEvent e) {

                        }

                        @Override
                        public void mousePressed(MouseEvent e) {
                            points.remove(markLabel);
                            codeLabel.remove(markLabel);
                            codeLabel.repaint();
                        }

                        @Override
                        public void mouseReleased(MouseEvent e) {

                        }

                        @Override
                        public void mouseEntered(MouseEvent e) {

                        }

                        @Override
                        public void mouseExited(MouseEvent e) {

                        }
                    });
                    points.put(markLabel,x+","+y);
                    codeLabel.add(markLabel);
                    codeLabel.repaint();
                }

                public void mouseReleased(MouseEvent e) {

                }

                public void mouseEntered(MouseEvent e) {

                }

                public void mouseExited(MouseEvent e) {

                }
            });
            codeLabel.setSize(Constant.CodeWidth,Constant.CodeHeight);
            codeLabel.setLocation(0,0);
            window.add(codeLabel);
            window.repaint();
        }
    }

    public static boolean checkPassCode() throws HttpException {
        if(points.size()==0){
            System.out.println(Constant.noCodeText);
            return false;
        }
        String randCode=getRandCode();
        JSONObject json = httpUtils.Get(Constant.popup_passport_captcha_check+"?answer="+randCode+"&&rand=sjrand&&login_site=E");
        String result_code;
        result_code=json.getString("result_code");
        if(Constant.checkSuccessCode.equalsIgnoreCase(result_code)){
            return true;
        }else{
            passCodeError();
            return false;
        }
    }

    public static void refreshPassCode() {
        httpUtils.resetHttp();
        window.remove(codeLabel);
        points.clear();
        createPassCode();
    }

    public static void codeGetFail() {
        System.out.println(Constant.codeGetFail);
    }

    public static void passCodeError() {
        System.out.println(Constant.codeErrText);
        refreshPassCode();
    }

    public static String getRandCode(){
        String randCode = "";
        for (String value : points.values()) {
            randCode += value + ",";
        }
        randCode = randCode.substring(0, randCode.length() - 1);
        return randCode;
    }

}
