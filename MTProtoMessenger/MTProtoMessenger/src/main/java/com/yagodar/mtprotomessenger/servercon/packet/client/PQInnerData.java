package com.yagodar.mtprotomessenger.servercon.packet.client;

import com.yagodar.mtprotomessenger.Util;
import com.yagodar.mtprotomessenger.servercon.packet.PacketTransceiver;
import com.yagodar.mtprotomessenger.servercon.packet.SendablePacket;

/**
 * Created by Yagodar on 26.07.13.
 */
public class PQInnerData extends SendablePacket {
    public PQInnerData(byte[] pq, long p, long q, byte[] nonce, byte[] serverNonce) {
        super(96, false);

        this.pq = pq;
        this.p = p;
        this.q = q;
        this.nonce = nonce;
        this.serverNonce = serverNonce;
    }

    @Override
    public void writePayload() {
        writeInt(0x83c95aec);//%(p_q_inner_data)
        writeData(pq);//pq
        writeData(Util.decodeMTProtoHexString(Long.toHexString(p)));//p
        writeData(Util.decodeMTProtoHexString(Long.toHexString(q)));//q
        writeData(nonce);
        writeData(serverNonce);
        writeInt(0x311C85DB);//new_nonce 311C85DB 234AA264 0AFC4A76 A735CF5B
        writeInt(0x234AA264);//new_nonce
        writeInt(0x0AFC4A76);//new_nonce
        writeInt(0xA735CF5B);//new_nonce
        writeInt(0x1F0FD68B);//new_nonce 1F0FD68B D17FA181 E1229AD8 67CC024D
        writeInt(0xD17FA181);//new_nonce
        writeInt(0xE1229AD8);//new_nonce
        writeInt(0x67CC024D);//new_nonce

        String payloadStr = Util.getHexStringForView(getPayload());
        System.out.println();
    }

    private byte[] nonce;
    private byte[] serverNonce;
    private byte[] pq;
    private long p;
    private long q;
}
