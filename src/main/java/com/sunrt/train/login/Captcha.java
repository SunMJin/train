package com.sunrt.train.login;

import com.sunrt.train.utils.HttpUtils;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Captcha {
    private Map<JLabel,String> points=new HashMap();
    private JFrame jFrame;
    private JLabel jLabel;

    private ImageIcon imageIcon=null;
    public Captcha(){
        try {
            InputStream in=this.getClass().getResourceAsStream("/pic/mark.png");
            byte buf[]=new byte[in.available()];
            in.read(buf);
            imageIcon=new ImageIcon(buf);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        jFrame=new JFrame();
        jFrame.setIconImage(imageIcon.getImage());
        jFrame.setTitle("验证码");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLayout(null);
        jFrame.setLocationRelativeTo(null);
        jFrame.setSize((int)(Constant.CodeWidth*1.5),(int)(Constant.CodeHeight*1.5));
        addButton();
    }

    public void setVisible(boolean flag){
        jFrame.setVisible(flag);
    }
    public ImageIcon getCode(){
        long temp = new Date().getTime();
        try {
            JSONObject json=new JSONObject(HttpUtils.Get(Constant.popup_passport_captcha + temp));
            return new ImageIcon(Base64.decodeBase64(json.getString("image")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addButton(){
        JButton submit=new JButton("提交");
        submit.setSize(70,30);
        submit.setLocation(0,200);
        submit.addMouseListener(new MouseListener() {
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
            jLabel.setSize(Constant.CodeWidth,Constant.CodeHeight);
            jLabel.setLocation(0,0);
            jFrame.add(jLabel);
            jFrame.repaint();
        }
    }

    public boolean checkPassCode(){
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
            // 登录
            jFrame.dispose();
            Login.loginForUam(getRandCode());
            return true;

        }else{
            passCodeError();
        }
        return false;
    }

    public void refreshPassCode(){
        HttpUtils.clearCookies();
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

}
