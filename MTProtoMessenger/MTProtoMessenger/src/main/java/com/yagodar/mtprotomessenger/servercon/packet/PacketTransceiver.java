package com.yagodar.mtprotomessenger.servercon.packet;

import com.yagodar.mtprotomessenger.servercon.Client;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.zip.CRC32;

/**
 * Created by Yagodar on 24.07.13.
 */
public class PacketTransceiver {
    public static PacketTransceiver getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new PacketTransceiver();
        }

        return INSTANCE;
    }

    public boolean sendPacket(Client client, SendablePacket sendablePacket, boolean fullProtocol) {
        try {
            if(fullProtocol) {
                int packetLength = PACKET_CONST_LENGTH_SNUMBER_CRC32_SIZE + sendablePacket.getPayload().length;

                ByteBuffer packetBuffer = ByteBuffer.allocate(packetLength);
                packetBuffer.order(ByteOrder.LITTLE_ENDIAN);

                packetBuffer.putInt(packetLength);
                packetBuffer.putInt(client.getSendablePacketSN());
                packetBuffer.put(sendablePacket.getPayload());


                CRC32 packetBufferCRC32 = new CRC32();
                //TODO проверить packetBuffer.arrayOffset вместо счета
                packetBufferCRC32.update(packetBuffer.array(), 0, packetLength - PACKET_CRC32_SIZE);

                packetBuffer.putInt((int) packetBufferCRC32.getValue());

                String bufferStr = "";
                for(byte bufferByte : packetBuffer.array()) {
                    bufferStr += Integer.toHexString(bufferByte) + " ";
                }

                if(client.sendBuffer(packetBuffer.array())) {
                    client.incSendablePacketSN();
                }
            }
            else {
                //TODO
            }
        }
        catch (Exception e) {
            //TODO
            return false;
        }

        return true;
    }

    private static int PACKET_LENGTH_SIZE = 4;
    private static int PACKET_SNUMBER_SIZE = 4;
    private static int PACKET_CRC32_SIZE = 4;
    private static int PACKET_CONST_LENGTH_SNUMBER_CRC32_SIZE = PACKET_LENGTH_SIZE + PACKET_SNUMBER_SIZE + PACKET_CRC32_SIZE;

    private static PacketTransceiver INSTANCE;
}
