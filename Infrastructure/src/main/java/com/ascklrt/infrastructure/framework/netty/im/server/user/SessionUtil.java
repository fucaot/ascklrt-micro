package com.ascklrt.infrastructure.framework.netty.im.server.user;

import com.ascklrt.infrastructure.framework.netty.im.server.Attributes;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class SessionUtil {

    private static final Map<String, Channel> userIdChannelMap = new ConcurrentHashMap<>();

    public static void bindSession(Session session, Channel channel) {
        userIdChannelMap.put(session.getUserId(), channel);
        channel.attr(Attributes.SESSION).set(session);
    }

    public static void unBindSession(Channel channel) {
        if (hasLogin(channel)) {
            userIdChannelMap.remove(getSession(channel).getUserId());
            channel.attr(Attributes.SESSION).set(null);
        }
    }

    public static boolean hasLogin(Channel channel) {
        return channel.attr(Attributes.LOGIN).get();
    }

    public static boolean hasLogin(String userId) {
        Channel channel = getChannel(userId);
        if (Objects.nonNull(channel)) {
            return channel.attr(Attributes.LOGIN).get();
        }
        return false;
    }

    public static Session getSession(Channel channel) {
        return channel.attr(Attributes.SESSION).get();
    }

    public static Channel getChannel(String userId) {
        return userIdChannelMap.get(userId);
    }
}
