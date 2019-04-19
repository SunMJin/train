package com.sunrt.train.login;

import com.sunrt.train.utils.HttpUtils;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Captcha {

    private Map<JLabel,String> points=new HashMap();
    private JFrame jFrame;
    private JLabel jLabel;

    public Captcha(){
        jFrame=new JFrame();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLayout(null);
        jFrame.setLocationRelativeTo(null);
        jFrame.setSize(900,400);
        jFrame.setVisible(true);
        addSubmit();
    }

    public ImageIcon getCode(){
        long temp = new Date().getTime();
        try {
            JSONObject json=new JSONObject(HttpUtils.Get(API.popup_passport_captcha + temp));
            return new ImageIcon(Base64.decodeBase64(json.getString("image")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addSubmit(){
        JButton jButton=new JButton("提交");
        jButton.setSize(70,30);
        jButton.setLocation(0,200);
        jButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                checkPassCode();
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
        jFrame.add(jButton);
        jFrame.repaint();
    }

    public void createPassCode(){
        ImageIcon code=getCode();
        if(code!=null){
            jLabel=new JLabel(code);
            jLabel.addMouseListener(new MouseListener() {
                public void mouseClicked(MouseEvent e) {

                }
                public void mousePressed(MouseEvent e) {
                    int x=e.getX();
                    int y=e.getY();
                    byte buf[]=null;
                    try {
                        InputStream in=this.getClass().getResourceAsStream("/pic/mark.png");
                        buf=new byte[in.available()];
                        in.read(buf);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    JLabel markLabel=new JLabel(new ImageIcon(buf));
                    markLabel.setSize(API.markWidth,API.markHeight);
                    markLabel.setLocation(x-API.markWidth/2,y-API.markHeight/2);
                    markLabel.addMouseListener(new MouseListener() {
                        @Override
                        public void mouseClicked(MouseEvent e) {

                        }

                        @Override
                        public void mousePressed(MouseEvent e) {
                            points.remove(markLabel);
                            jLabel.remove(markLabel);
                            jLabel.repaint();
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
                    jLabel.add(markLabel);
                    jLabel.repaint();
                }

                public void mouseReleased(MouseEvent e) {

                }

                public void mouseEntered(MouseEvent e) {

                }

                public void mouseExited(MouseEvent e) {

                }
            });
            jLabel.setSize(300,188);
            jLabel.setLocation(0,0);
            jFrame.add(jLabel);
            jFrame.repaint();
        }
    }

    public boolean checkPassCode(){
        String randCode=getRandCode();
        String result_code=null;
        try {
            JSONObject json=new JSONObject(HttpUtils.Get(API.popup_passport_captcha_check+"?answer="+randCode+"&&rand=sjrand&&login_site=E"));
            result_code=json.getString("result_code");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        if("4".equalsIgnoreCase(result_code)){
            // 登录
            System.out.println("验证码成功");
            Login login=new Login();
            login.start(points.size(),getRandCode());
            return true;

        }else{
            passCodeError();
        }
        return false;
    }

    public void refreshPassCode(){
        jFrame.remove(jLabel);
        points.clear();
        createPassCode();
    }


    public void passCodeError(){
        System.out.println("验证码错误");
        refreshPassCode();
    }


    public String getRandCode(){
        String randCode = "";
        //遍历点位
        for (String value : points.values()) {
            randCode += value + ",";
        }
        randCode = randCode.substring(0, randCode.length() - 1);
        return randCode;
    }

    public int getPointCount(){
        return points.size();
    }


}
