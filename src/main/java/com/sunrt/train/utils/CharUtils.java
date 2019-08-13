package com.sunrt.train.utils;

public class CharUtils {
    public static boolean isLetter(char c){
        return (c>='a'&&c<='z') || (c>='A'&&c<='Z');
    }

    public static char getTrainCodeFirstLetter(char c){
        if(CharUtils.isLetter(c)){
            return c;
        }else{
            return 'P';
        }
    }
}
