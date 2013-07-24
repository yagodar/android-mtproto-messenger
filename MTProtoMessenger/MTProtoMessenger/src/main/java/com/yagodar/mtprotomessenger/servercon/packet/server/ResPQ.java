package com.yagodar.mtprotomessenger.servercon.packet.server;

import com.yagodar.mtprotomessenger.servercon.Client;
import com.yagodar.mtprotomessenger.servercon.packet.PacketTransceiver;
import com.yagodar.mtprotomessenger.servercon.packet.ReceivablePacket;

import java.nio.ByteBuffer;

/**
 * Created by Yagodar on 25.07.13.
 */
public class ResPQ extends ReceivablePacket {
    public ResPQ(Client client, byte[] packetBuffer) {
        super(client, packetBuffer);
    }

    @Override
    public void readPayload(ByteBuffer payload) {
        String payloadHexString = PacketTransceiver.getInstance().getHexString(payload.array());//TODO
        //всё ок! теперь выборка pq и расчет p и q.
        //http://dev.stel.com/mtproto/samples-auth_key#2-poluchen-otvet-servera-so-sleduyushim-soderzhimym
        //http://dev.stel.com/mtproto/auth_key
        System.out.println();
    }
}
