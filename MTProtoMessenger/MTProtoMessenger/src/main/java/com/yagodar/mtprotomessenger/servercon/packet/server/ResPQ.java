package com.yagodar.mtprotomessenger.servercon.packet.server;

import com.yagodar.mtprotomessenger.Util;
import com.yagodar.mtprotomessenger.servercon.Client;
import com.yagodar.mtprotomessenger.servercon.packet.PacketTransceiver;
import com.yagodar.mtprotomessenger.servercon.packet.ReceivablePacket;
import com.yagodar.mtprotomessenger.servercon.packet.client.PQInnerData;
import com.yagodar.mtprotomessenger.servercon.packet.client.ReqDHParams;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

/**
 * Created by Yagodar on 25.07.13.
 */
public class ResPQ extends ReceivablePacket {
    public ResPQ(Client client, byte[] packetBuffer) {
        super(client, packetBuffer);
    }

    @Override
    public void readPayload(ByteBuffer payload) {
        String payloadString = Util.getHexStringForView(payload.array());

        ByteBuffer pqValueBuffer = ByteBuffer.allocate(8);
        pqValueBuffer.put(payload.array(), 57, 8);

        String pqHexString = Util.getHexString(pqValueBuffer.array(), true);

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

            byte[] dataBytes = pqInnerDataPacket.getPayload();
            String dataStr = Util.getHexString(dataBytes);
            String dataStrForView = Util.convertHexStringForView(dataStr);

            MessageDigest md = null;

            try {
                //SHA1

                md = MessageDigest.getInstance("SHA-1");

                byte[] sha1DataBytes = md.digest(dataBytes);
                byte[] sha1DataBytesCheck = md.digest(dataStr.getBytes("UTF-8"));
                String sha1DataStr = Util.getHexString(sha1DataBytes);

                ByteBuffer dataWithHashBuffer = ByteBuffer.allocate(255);
                dataWithHashBuffer.put(sha1DataBytes);
                dataWithHashBuffer.put(dataBytes);
                dataWithHashBuffer.put(new byte[255 - sha1DataBytes.length - dataBytes.length]);

                byte[] dataWithHashBytes = dataWithHashBuffer.array();
                String dataWithHashBytesStr = Util.getHexString(dataWithHashBytes);

                //RSA

                final byte[] rsaPublicKeyBytes = Util.getBytesFromHexString(  "MIIBCgKCAQEAwVACPi9w23mF3tBkdZz+zwrzKOaaQdr01vAbU4E1pvkfj4sqDsm6"
                                                                            + "lyDONS789sVoD/xCS9Y0hkkC3gtL1tSfTlgCMOOul9lcixlEKzwKENj1Yz/s7daS"
                                                                            + "an9tqw3bfUV/nqgbhGX81v/+7RFAEd+RwFnK7a+XYl9sluzHRyVVaTTveB2GazTw"
                                                                            + "Efzk2DWgkBluml8OREmvfraX3bkHZJTKX4EQSjBbbdJ2ZXIsRrYOXfaA+xayEGB+"
                                                                            + "8hdlLmAjbCVfaigxX0CDqWeR1yFL9kwd9P0NsZRPsmoqVwMbMu7mStFai6aIhc3n"
                                                                            + "Slv8kg9qv1m6XHVQY3PnEw+QQtqSIXklHwIDAQAB");

                Cipher cipher = Cipher.getInstance("RSA"); // create conversion processing object
                cipher.init(Cipher.ENCRYPT_MODE, new PublicKey() {
                    @Override
                    public String getAlgorithm() {
                        return null;
                    }

                    @Override
                    public String getFormat() {
                        return null;
                    }

                    @Override
                    public byte[] getEncoded() {
                        return rsaPublicKeyBytes;
                    }
                }); // initialize object's mode and key

                byte[] encryptedDataBytes = cipher.doFinal(dataWithHashBytes); // use object for encryption
                String encryptedDataBytesStr = Util.getHexString(encryptedDataBytes);
            }
            catch(Exception e) {
                e.printStackTrace();
            }

            PacketTransceiver.getInstance().sendPacket(getClient(), new ReqDHParams(), true);

            System.out.println();
        }
    }
}
