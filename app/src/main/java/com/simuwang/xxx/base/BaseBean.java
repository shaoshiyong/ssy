package com.simuwang.xxx.base;

/**
 * function: 基础响应状态类
 *
 * <p></p>
 * Created by Leo on 2017/12/20.
 */
@SuppressWarnings("ALL")
public class BaseBean {
    protected int status;

    protected String msg;

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

    @Override
    public String toString() {
        return "BaseBean{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                '}';
    }
}
