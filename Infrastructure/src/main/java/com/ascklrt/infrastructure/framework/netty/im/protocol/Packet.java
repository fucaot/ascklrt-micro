package com.ascklrt.infrastructure.framework.netty.im.protocol;

import com.ascklrt.infrastructure.framework.netty.im.protocol.command.Command;
import lombok.Data;

@Data
public abstract class Packet {

    /**
     * 协议版本
     */
    private Byte version = 1;

    /**
     * 指令
     */
    public abstract Command getCommand();
}
