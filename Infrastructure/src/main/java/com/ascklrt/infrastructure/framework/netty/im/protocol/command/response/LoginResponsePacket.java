package com.ascklrt.infrastructure.framework.netty.im.protocol.command.response;

import com.ascklrt.infrastructure.framework.netty.im.protocol.Packet;
import lombok.Data;

@Data
public class LoginResponsePacket extends Packet {

    private String reason;

    private boolean success;

    public boolean getSuccess() {
        return success;
    }

    @Override
    public Byte getCommand() {
        return null;
    }
}
