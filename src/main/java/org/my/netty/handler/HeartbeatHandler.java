package org.my.netty.handler;

import org.my.annotation.Module;
import org.my.netty.bean.Message;
import org.my.netty.bean.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.netty.channel.ChannelHandlerContext;

@Module(module = 0)
@Component
public class HeartbeatHandler extends BaseHandler {
    
    private static final Logger log = LoggerFactory.getLogger(HeartbeatHandler.class);
    
    @Override
    public Result process(ChannelHandlerContext ctx, Message message) {
        log.info("heartbeat...");
        return new Result(1, "heartbeat success...");
    }

}
