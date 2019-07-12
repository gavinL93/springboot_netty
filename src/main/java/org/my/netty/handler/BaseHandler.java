package org.my.netty.handler;

import org.my.netty.bean.Message;
import org.my.netty.bean.Result;
import org.my.netty.service.LoginRedisService;
import org.my.netty.service.MyRedisService;
import org.springframework.beans.factory.annotation.Autowired;

import io.netty.channel.ChannelHandlerContext;

public abstract class BaseHandler {
    
    @Autowired
    protected MyRedisService jxRedisService;
    
    @Autowired
    protected LoginRedisService loginRedisService;
    
    public abstract Result process(ChannelHandlerContext ctx, Message message);
}
