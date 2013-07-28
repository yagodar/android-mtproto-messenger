package com.yagodar.mtprotomessenger.servercon.packet;

import com.yagodar.mtprotomessenger.Util;
import com.yagodar.mtprotomessenger.servercon.Client;
import com.yagodar.mtprotomessenger.servercon.packet.server.ResPQ;

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

                if(client.sendBuffer(packetBuffer.array())) {
                    client.incSendablePacketSN();
                }
            }
            else {
                //TODO
                return false;
            }
        }
        catch (Exception e) {
            //TODO
            return false;
        }

        return true;
    }

    public boolean receivePacketBuffer(Client client, byte[] packetBuffer, boolean fullProtocol) {
        try {
            if(fullProtocol) {

                ByteBuffer schemerNameBuffer = ByteBuffer.allocate(4);
                schemerNameBuffer.put(packetBuffer, PACKET_LENGTH_SIZE + PACKET_SNUMBER_SIZE + 8 + 8 + 4, 4);

                String schemerNameHexStr = Util.getHexString(schemerNameBuffer.array());
                long schemerNameHex = Long.decode(schemerNameHexStr);

                if(schemerNameHex == 0x05162463) {//resPQ
                    new ResPQ(client, packetBuffer);
                }
                else {
                    return false;
                }
            }
            else {
                //TODO
                return false;
            }
        }
        catch (Exception e) {
            //TODO
            return false;
        }

        return true;
    }

    public static int PACKET_LENGTH_SIZE = 4;
    public static int PACKET_SNUMBER_SIZE = 4;
    public static int PACKET_CRC32_SIZE = 4;
    public static int PACKET_CONST_LENGTH_SNUMBER_CRC32_SIZE = PACKET_LENGTH_SIZE + PACKET_SNUMBER_SIZE + PACKET_CRC32_SIZE;

    private static PacketTransceiver INSTANCE;
}
