package com.example.swaggerdemo.json;

public class info_util {
    private int status;
    private String msg;
    private String data;
 
    public int getStatus() {
        return status;
    }
 
    public void setStatus(int status) {
        this.status = status;
    }
 
    public String getMsg() {
        return msg;
    }
 
    public void setMsg(String msg) {
        this.msg = msg;
    }
 
    public String getData() {
        return data;
    }
 
    public void setData(String data) {
        this.data = data;
    }
 
    @Override
    public String toString() {
        return "info_util [status=" + status + ", msg=" + msg + ", data="
                + data + "]";
    }
 
}