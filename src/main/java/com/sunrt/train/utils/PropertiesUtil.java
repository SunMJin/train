package com.sunrt.train.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {
    private static Properties properties=new Properties();


    public static String getValueByResource(String filename,String key){
        try {
            properties.load(PropertiesUtil.class.getClassLoader().getResourceAsStream(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties.getProperty(key);
    }

    public static String getValueByFile(String filename,String key){
        try {
            properties.load(new FileInputStream(new File(filename)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties.getProperty(key);
    }

    public static void writeValueByFile(String filename,String key,String value){
        try {
            properties.load(new FileInputStream(new File(filename)));
            properties.setProperty(key, value);
            properties.store(new FileOutputStream(filename), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
