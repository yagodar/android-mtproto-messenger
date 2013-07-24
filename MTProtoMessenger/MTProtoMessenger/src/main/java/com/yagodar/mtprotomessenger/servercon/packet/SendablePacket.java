package com.yagodar.mtprotomessenger.servercon.packet;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by Yagodar on 24.07.13.
 */
public abstract class SendablePacket implements Packet {
    @Override
    public byte[] getPayload() {
        return payload.array();
    }

    abstract public void writePayload();

    protected SendablePacket(int payloadLength) {
        payload = ByteBuffer.allocate(payloadLength);
        payload.order(ByteOrder.LITTLE_ENDIAN);
        writePayload();
    }

    protected void writeInt(int value) {
        payload.putInt(value);
    }

    protected void writeInt(int index, int value) {
        payload.putInt(index, value);
    }

    protected void writeLong(long value) {
        payload.putLong(value);
    }

    protected void writeLong(int index, long value) {
        payload.putLong(index, value);
    }

    private ByteBuffer payload;
}
