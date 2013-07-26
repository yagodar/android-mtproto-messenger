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
        this(payloadLength, true);
    }

    protected SendablePacket(int payloadLength, boolean writePayload) {
        payload = ByteBuffer.allocate(payloadLength);
        payload.order(ByteOrder.LITTLE_ENDIAN);
        if(writePayload) {
            writePayload();
        }
    }

    protected void writeInt(int value) {
        payload.putInt(value);
    }

    protected void writeLong(long value) {
        payload.putLong(value);
    }

    protected void writeByte(byte value) {
        payload.put(value);
    }

    protected void writeData(byte[] data) {
        payload.put(data);
    }

    private ByteBuffer payload;
}
