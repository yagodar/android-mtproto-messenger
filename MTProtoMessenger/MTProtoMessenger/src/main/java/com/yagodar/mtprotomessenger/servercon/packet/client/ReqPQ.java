package com.yagodar.mtprotomessenger.servercon.packet.client;

import android.content.SyncStatusObserver;

import com.yagodar.mtprotomessenger.servercon.packet.SendablePacket;

/**
 * Created by Yagodar on 24.07.13.
 */
public class ReqPQ extends SendablePacket {
    public ReqPQ() {
        //TODO сериализация и прочее. а пока что вручную, грубо.
        super(40); //http://dev.stel.com/mtproto/samples-auth_key#1-zapros-p-q-avtorizatsii
    }

    @Override
    public void writePayload() {
        writeLong(0);//auth_key_id
        writeLong(System.currentTimeMillis() << 32);//message_id TODO что значит <<
        writeInt(20);//message_length
        writeInt(0x60469778);//%(req_pq)
        writeInt(0x3E054982);//nonce 3E054982 8CCA27E9 66B301A4 8FECE2FC
        writeInt(0x8CCA27E9);//nonce
        writeInt(0x66B301A4);//nonce
        writeInt(0x8FECE2FC);//nonce

        String payloadStr = "";
        for(byte payloadByte : getPayload()) {
            payloadStr += Integer.toHexString(payloadByte) + " ";
        }
        System.out.println(payloadStr);
    }
}
