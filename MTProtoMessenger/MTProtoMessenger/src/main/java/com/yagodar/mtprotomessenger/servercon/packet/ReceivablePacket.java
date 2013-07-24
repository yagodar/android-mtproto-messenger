package com.yagodar.mtprotomessenger.servercon.packet;

import com.yagodar.mtprotomessenger.servercon.Client;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by Yagodar on 25.07.13.
 */
public abstract class ReceivablePacket implements Packet {
    @Override
    public byte[] getPayload() {
        return null;//TODO не используется
    }

    abstract public void readPayload(ByteBuffer payload);

    protected ReceivablePacket(Client client, byte[] packetBuffer) {
        this.client = client;

        //TODO криво конечно! хотя.. хз)

        ByteBuffer packetLengthBuffer = ByteBuffer.allocate(PacketTransceiver.PACKET_LENGTH_SIZE);
        packetLengthBuffer.put(packetBuffer, 0, PacketTransceiver.PACKET_LENGTH_SIZE);
        String packetLengthHexString = PacketTransceiver.getInstance().getHexString(packetLengthBuffer.array());

        int payloadLength = Integer.decode(packetLengthHexString) - PacketTransceiver.PACKET_CONST_LENGTH_SNUMBER_CRC32_SIZE;

        ByteBuffer payload = ByteBuffer.allocate(payloadLength);
        payload.put(packetBuffer, PacketTransceiver.PACKET_LENGTH_SIZE + PacketTransceiver.PACKET_SNUMBER_SIZE, payloadLength);

        readPayload(payload);
    }

    protected Client getClient() {
        return client;
    }

    private Client client;
}
