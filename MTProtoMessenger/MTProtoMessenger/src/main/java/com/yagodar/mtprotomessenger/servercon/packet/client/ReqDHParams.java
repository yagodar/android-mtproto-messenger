package com.yagodar.mtprotomessenger.servercon.packet.client;

import com.yagodar.mtprotomessenger.servercon.packet.SendablePacket;

/**
 * Created by Yagodar on 26.07.13.
 */
public class ReqDHParams extends SendablePacket {
    public ReqDHParams() {
        super(340, false);

        //TODO параметры
    }

    @Override
    public void writePayload() {
        //TODO херачим!
        System.out.println();
    }
}
