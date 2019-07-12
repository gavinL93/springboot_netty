package org.my.netty.bean;

@org.msgpack.annotation.Message
public class Message {

    // 用户id
    private int uid;

    // 模块id: 0-心跳包
    private int module;

    // json格式数据
    private String data;

    public Message() {
        super();
    }

    public Message(int uid, int module, String data) {
        this.uid = uid;
        this.module = module;
        this.data = data;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getModule() {
        return module;
    }

    public void setModule(int module) {
        this.module = module;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "uid:" + uid + " module:" + module + " data:" + data;
    }

}
