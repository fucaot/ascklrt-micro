package com.ascklrt.infrastructure.framework.netty.im.server.user;

import lombok.Data;

@Data
public class Session {

    private String userId;
    private String userName;

    public Session() {}

    public Session(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }
}
