package org.my.netty.config;

import java.util.concurrent.ConcurrentHashMap;

import org.my.netty.service.MyRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

@Configuration
public class ChannelCache {

    @Autowired
    private MyRedisService myRedisService;

    // 存储所有Channel
    private ChannelGroup channelGroup = new DefaultChannelGroup("channelGroups", GlobalEventExecutor.INSTANCE);

    // 存储Channel.id().asLongText()和用户id对应关系
    private ConcurrentHashMap<String, Integer> channelIdUid = new ConcurrentHashMap<String, Integer>();

    public ChannelGroup getChannelGroup() {
        return channelGroup;
    }

    public ConcurrentHashMap<String, Integer> getChannelIdUid() {
        return channelIdUid;
    }

    /**
     * 退出时删除redis数据库中缓存
     *
     */
    public void flushDb() {
        myRedisService.flushDb();
    }

    /**
     * 获取Channel 
     * @return
     */
    public Channel getChannel(Channel channel) {
        Channel channel_ = channelGroup.find(channel.id());
        if (channel_ != null) {
            return channel_;
        }
        return null;
    }

    /**
     * 添加Channel到ChannelGroup
     * @param uid
     * @param channel
     */
    public void addChannel(Channel channel, int uid) {
        Channel channel_ = channelGroup.find(channel.id());
        if (channel_ == null) {
            channelGroup.add(channel);
        }

        // redis添加对应用户和channelId之前的关系
        Integer userId = channelIdUid.get(channel.id().asLongText());
        if (userId != null && userId.intValue() != uid) {
            // 和本次用户数据对不上，直接删除对应channel的老数据
            redisDelete(userId, channel);
        }
        channelIdUid.put(channel.id().asLongText(), userId);
        // redis添加对应channelId
        redisAdd(uid, channel);
    }

    /**
     * 删除Channel
     * @param channel
     */
    public void removeChannel(Channel channel) {
        Channel channel_ = channelGroup.find(channel.id());
        if (channel_ != null) {
            channelGroup.remove(channel_);
        }
        Integer userId = channelIdUid.get(channel.id().asLongText());
        if (userId != null) {
            channelIdUid.remove(channel.id().asLongText());
            redisDelete(userId, channel);
        }
    }

    private void redisDelete(int uid, Channel channel) {
        redisDelete(uid, channel.id());
    }

    private void redisDelete(int uid, ChannelId channelId) {
        myRedisService.setRemove(myRedisService.getUserKeyPrefix() + uid, channelId.asLongText());
    }

    private void redisAdd(int uid, Channel channel) {
        redisAdd(uid, channel.id());
    }

    private void redisAdd(int uid, ChannelId channelId) {
        myRedisService.sSetAndTime(myRedisService.getUserKeyPrefix() + uid, myRedisService.getExpireSeconds(),
                channelId.asLongText());
    }

}
