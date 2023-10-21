package com.ascklrt.infrastructure.framework.netty.im.protocol.command.request;

import com.ascklrt.infrastructure.framework.netty.im.protocol.Packet;
import com.ascklrt.infrastructure.framework.netty.im.protocol.command.Command;
import lombok.Data;

@Data
public class MessageRequestPacket extends Packet {

    private String toUserId;

    private String message;

    @Override
    public Command getCommand() {
        return Command.MESSAGE_REQUEST;
    }
}