package org.my.netty.bean;

import org.msgpack.annotation.Message;

@Message
public class Result {

    private int resultCode;
    private String resultMsg;
    private String data;

    public Result() {
        this(1, "success");
    }

    public Result(int resultCode, String resultMsg) {
        this(resultCode, resultMsg, null);
    }

    public Result(int resultCode, String resultMsg, String data) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
        this.data = data;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "code:" + resultCode + " msg:" + resultMsg + " data:" + data;
    }

}
