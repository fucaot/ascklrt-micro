package com.ascklrt.infrastructure.framework.netty.im.protocol.command.response;

import com.ascklrt.infrastructure.framework.netty.im.protocol.Packet;
import com.ascklrt.infrastructure.framework.netty.im.protocol.command.Command;
import lombok.Data;

@Data
public class LoginResponsePacket extends Packet {

    private String userId;

    private String reason;

    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public boolean getSuccess() {
        return success;
    }

    @Override
    public Command getCommand() {
        return Command.LOGIN_RESPONSE;
    }
}
