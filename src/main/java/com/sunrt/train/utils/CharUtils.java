package com.sunrt.train.utils;

public class CharUtils {
    public static boolean isLetter(char c){
        return (c>='a'&&c<='z') || (c>='A'&&c<='Z');
    }
    public static String rmRepeated(String s){
        int len = s.length();
        int k = 0;
        int count = 0;
        String str = "";
        char[] c = new char[len];
        for(int i=0;i<len;i++){
            c[i] = s.charAt(i);
        }
        for(int i=0;i<len;i++){
            k=i+1;
            while(k<len-count){
                if(c[i]==c[k]){
                    for(int j=k;j<len-1;j++){
                        c[j] = c[j+1];
                    }
                    count++;
                    k--;
                }
                k++;
            }
        }
        for(int i=0;i<len-count;i++){
            str+=String.valueOf(c[i]);
        }
        return str;
    }
}
