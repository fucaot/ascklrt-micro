package com.ascklrt.infrastructure.framework.netty.im.protocol.command;

import com.ascklrt.infrastructure.framework.netty.im.protocol.Packet;

import java.util.Arrays;
import java.util.Objects;

public enum Command {

    LOGIN_REQUEST((byte)1, LoginRequestPacket.class);

    private final byte code;
    private final Class<? extends Packet> clz;

    Command(byte code, Class<? extends Packet> clz) {
        this.code = code;
        this.clz = clz;
    }

    public byte getCode() {
        return code;
    }

    public Class<? extends Packet> getClz() {
        return clz;
    }

    public static Command byCode(byte code) {
        return Arrays.stream(Command.values())
                .filter(v -> Objects.equals(v.getCode(), code)).findFirst().get();
    }
}
