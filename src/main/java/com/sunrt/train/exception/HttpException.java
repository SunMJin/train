package com.sunrt.train.exception;

public class HttpException extends Exception{
    private String URL;

    public HttpException(String message){
        super(message);
    }
    public HttpException(String URL, String message){
        super(message);
        this.URL=URL;
    }
    public String getURL() {
        return URL;
    }

}
