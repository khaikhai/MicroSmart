package com.microsmart.tv.server;

import com.xuhao.didi.core.iocore.interfaces.ISendable;

public interface IAdminCtrl {

    void sendToAdmin(ISendable sendable);

    void sendToClient(ISendable sendable);

    void restart(int port);

}