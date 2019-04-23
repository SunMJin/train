package com.sunrt.train.login;

import com.sunrt.train.utils.HttpUtils;
import org.apache.commons.codec.binary.Base64;
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

    private static Map<JLabel,String> points=new HashMap();
    private static JFrame jFrame;
    private static JLabel codeLabel;

    private static ImageIcon imageIcon=null;
    static{
        try {
            InputStream in=Captcha.class.getResourceAsStream(Constant.markPath);
            byte buf[]=new byte[in.available()];
            in.read(buf);
            imageIcon=new ImageIcon(buf);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        jFrame=new JFrame();
        jFrame.setIconImage(imageIcon.getImage());
        jFrame.setTitle(Constant.title);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLayout(null);
        jFrame.setLocationRelativeTo(null);
        jFrame.setSize((int)(Constant.CodeWidth*1.5),(int)(Constant.CodeHeight*1.5));
        addButton();
    }

    public static void setVisible(boolean flag){
        jFrame.setVisible(flag);
    }

    public static ImageIcon getCode(){
        long temp = new Date().getTime();
        ByteArrayInputStream in=null;
        ByteArrayOutputStream baos=null;
        try {
            JSONObject json=new JSONObject(HttpUtils.Get(Constant.popup_passport_captcha + temp));
            in=new ByteArrayInputStream(Base64.decodeBase64(json.getString("image")));
            BufferedImage src = ImageIO.read(in);
            baos=new ByteArrayOutputStream(1024);
            ImageIO.write(src, "JPG", baos);
            return new ImageIcon(baos.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(in!=null){
                    in.close();
                }
                if(baos!=null){
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void addButton(){
        JButton submit=new JButton("提交");
        submit.setSize(70,30);
        submit.setLocation(0,200);
        submit.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }
            @Override
            public void mousePressed(MouseEvent e) {
                if(checkPassCode()){
                    Login.loginForUam(getRandCode());
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
        jFrame.add(submit);

        JButton rerush=new JButton("刷新");
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
        jFrame.add(rerush);

        jFrame.repaint();
    }

    public static void createPassCode(){
        ImageIcon code=getCode();
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
            jFrame.add(codeLabel);
            jFrame.repaint();
        }
    }

    public static boolean checkPassCode(){
        if(points.size()==0){
            System.out.println("请选择验证码！");
            return false;
        }
        String randCode=getRandCode();
        String result_code=null;
        try {
            JSONObject json=new JSONObject(HttpUtils.Get(Constant.popup_passport_captcha_check+"?answer="+randCode+"&&rand=sjrand&&login_site=E"));
            result_code=json.getString("result_code");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        if("4".equalsIgnoreCase(result_code)){
            jFrame.dispose();
            return true;

        }else{
            passCodeError();
        }
        return false;
    }

    public static void refreshPassCode(){
        HttpUtils.clearCookies();
        jFrame.remove(codeLabel);
        points.clear();
        createPassCode();
    }

    public static void passCodeError(){
        System.out.println("验证码错误");
        refreshPassCode();
    }

    public static String getRandCode(){
        String randCode = "";
        //遍历点位
        for (String value : points.values()) {
            randCode += value + ",";
        }
        randCode = randCode.substring(0, randCode.length() - 1);
        return randCode;
    }

}
