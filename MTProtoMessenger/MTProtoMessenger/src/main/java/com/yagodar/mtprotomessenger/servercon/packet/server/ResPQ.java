package com.yagodar.mtprotomessenger.servercon.packet.server;

import com.yagodar.mtprotomessenger.servercon.Client;
import com.yagodar.mtprotomessenger.servercon.packet.PacketTransceiver;
import com.yagodar.mtprotomessenger.servercon.packet.ReceivablePacket;
import com.yagodar.mtprotomessenger.servercon.packet.client.PQInnerData;

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
        String payloadString = PacketTransceiver.getInstance().getHexString(payload.array());

        ByteBuffer pqValueBuffer = ByteBuffer.allocate(8);
        pqValueBuffer.put(payload.array(), 57, 8);

        String pqHexString = PacketTransceiver.getInstance().getHexString(pqValueBuffer.array(), true);

        long pq = Long.decode(pqHexString);

        long sqrtPq = (long) Math.sqrt(pq);

        long p;
        long q = 0L;

        if(sqrtPq % 2L == 0) {
            sqrtPq--;
        }

        for(p = sqrtPq; p >= 3L; p -= 2L) {
            if(pq % p == 0) {
                q = pq / p;
                break;
            }
        }

        if(pq == p * q) {
            ByteBuffer pqFullBuffer = ByteBuffer.allocate(12);
            pqFullBuffer.put(payload.array(), 56, 12);

            ByteBuffer nonce = ByteBuffer.allocate(16);
            nonce.put(payload.array(), 24, 16);

            ByteBuffer serverNonce = ByteBuffer.allocate(16);
            serverNonce.put(payload.array(), 40, 16);

            PQInnerData pqInnerDataPacket = new PQInnerData(pqFullBuffer.array(), p, q, nonce.array(), serverNonce.array());
            pqInnerDataPacket.writePayload();

            String serializedPQInnerData = PacketTransceiver.getInstance().getHexString(pqInnerDataPacket.getPayload());

            //TODO норм! херачим дальше!
            //http://dev.stel.com/mtproto/samples-auth_key#zapros-nachala-obmena-klyuchey-po-diffi-hellmanu
            //http://dev.stel.com/mtproto/auth_key

            System.out.println();
        }
    }
}
