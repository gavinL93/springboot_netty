package org.my.netty.handler;

import org.my.annotation.Module;
import org.my.netty.bean.Message;
import org.my.netty.bean.Result;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import io.netty.channel.ChannelHandlerContext;

@Module(module = 99)
@Component
public class EchoHandler extends BaseHandler {

    @Override
    public Result process(ChannelHandlerContext ctx, Message message) {
        return new Result(1, "success...", JSON.toJSONString(message));
    }

}
