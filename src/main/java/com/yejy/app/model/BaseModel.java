package com.yejy.app.model;

/**
 * 基本类型
 */
public class BaseModel {
    private Integer status;
    private String msg;
    private Object result;

    public BaseModel() {
        //empty
    }

    public BaseModel(Integer status, String msg, Object result) {
        this.status = status;
        this.msg = msg;
        this.result = result;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
