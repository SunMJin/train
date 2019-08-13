package com.sunrt.train.utils;

import java.io.*;

public class FileUtils {

    public static void writeText(String filename,String text){
        BufferedOutputStream bos=null;
        try {
            bos=new BufferedOutputStream(new FileOutputStream(filename));
            bos.write(text.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(bos!=null){
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getText(String filename){
        BufferedInputStream bis=null;
        try {
            bis=new BufferedInputStream(new FileInputStream(filename));
            byte buff[]=new byte[bis.available()];
            bis.read(buff);
            return new String(buff,"utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(bis!=null){
                    bis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void noExistsCopyResource(String filename){
        if(!new File(filename).exists()){
            FileUtils.copy(FileUtils.class.getClassLoader().getResourceAsStream(filename),filename);
        }
    }

    public static void noExistsCopyResource(String ... filenames){
        for(String filename:filenames){
            if(!new File(filename).exists()){
                FileUtils.copy(FileUtils.class.getClassLoader().getResourceAsStream(filename),filename);
            }
        }
    }

    public static void copy(InputStream in,String name) {
        BufferedInputStream bis=new BufferedInputStream(in);
        BufferedOutputStream bos = null;
        try {
            byte buff[] = new byte[bis.available()];
            int i=bis.read(buff);
            bos=new BufferedOutputStream(new FileOutputStream(name));
            bos.write(buff, 0, i);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(bis!=null){
                    bis.close();
                }
                if(bos!=null){
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
