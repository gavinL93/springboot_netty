package org.my.netty.codec;

import java.util.List;

import org.msgpack.MessagePack;
import org.my.netty.bean.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class MsgDecoder extends ByteToMessageDecoder {

    private static final Logger log = LoggerFactory.getLogger(MsgDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

//        log.info("thread name: " + Thread.currentThread().getName());

        long start = System.currentTimeMillis();

        if (in.readableBytes() < 4) {
            return;
        }

        in.markReaderIndex();

        int length = in.readInt();
        if (length <= 0) {
            log.info("length: " + length);
            ctx.close();
            return;
        }

        if (in.readableBytes() < length) {
            log.info("return");
            in.resetReaderIndex();
            return;
        }

        byte[] b = new byte[length];
        in.readBytes(b);

        Message message = new Message();
        MessagePack msgpack = new MessagePack();
        try {
           message = msgpack.read(b, Message.class);
           out.add(message);
        } catch (Exception e) {
            log.error("MessagePack read error");
            ctx.close();
        }
        log.info(" ====== decode succeed: " + message.toString());

        long time = System.currentTimeMillis() - start;
        log.info("decode time: " + time + " ms");
    }

}
