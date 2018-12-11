package com.tools;

/**
 * 此类是用于保存服务器发来的信息到变量
 * Created by 29924 on 2018/8/12.
 */

public class App {
    private boolean result;
    private int code;
    private String codeGet;
    public App(){
        code=1;//0代表服务器炸了，所以随便选一个数初始化，免得默认初始化为0，这里初始化为1
        result=false;
    }

    public String getCodeGet() {
        return codeGet;
    }

    public void setCodeGet(String codeGet) {
        this.codeGet = codeGet;
    }

    public boolean getResult(){
        return  result;
    }
    public int getCode(){
        return code;
    }
    public void setResult(boolean result){
        this.result=result;
    }
    public void setCode(int code){
        this.code=code;
    }
}
