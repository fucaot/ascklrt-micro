package com.ascklrt.infrastructure.framework.netty.im.protocol.command.request;

import com.ascklrt.infrastructure.framework.netty.im.protocol.Packet;
import com.ascklrt.infrastructure.framework.netty.im.protocol.command.Command;
import lombok.Data;

@Data
public class LoginRequestPacket extends Packet {

    private String userId;

    private String username;

    private String password;
    @Override
    public Command getCommand() {
        return Command.LOGIN_REQUEST;
    }
}
