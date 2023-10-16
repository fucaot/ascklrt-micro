package com.ascklrt.infrastructure.framework.netty.im.protocol.command.response;

import com.ascklrt.infrastructure.framework.netty.im.protocol.Packet;
import com.ascklrt.infrastructure.framework.netty.im.protocol.command.Command;
import lombok.Data;

@Data
public class MessageResponsePacket extends Packet {

    private String message;

    @Override
    public Command getCommand() {
        return Command.MESSAGE_RESPONSE;
    }
}
