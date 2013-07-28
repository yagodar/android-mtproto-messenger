package com.yagodar.mtprotomessenger;

import java.io.UnsupportedEncodingException;

/**
 * Created by Yagodar on 26.07.13.
 */
public class Util {
    /**
     * Является ли число простым?
     */
    public static boolean isPrime(long N) {
        if (N < 2L) return false;
        for (long i = 2L; i*i <= N; i++)
            if (N % i == 0L) return false;
        return true;
    }

    public static String getHexStringForView(byte[] data) {
        return getHexStringForView(data, false);
    }

    public static String getHexStringForView(byte[] data, boolean bigEndian) {
        return convertHexStringForView(getHexString(data, bigEndian));
    }

    public static String convertHexStringForView(String hexString) {
        if(hexString != null && hexString.length() != 0) {

            if(hexString.startsWith("0x")) {
                hexString = hexString.substring(2);
            }

            if(hexString.startsWith("#")) {
                hexString = hexString.substring(1);
            }

            if(hexString.length() != 0) {
                String hexStringForView = "";

                int intentCounter8 = 0;
                int intentCounter2 = 0;

                for(int i = 0; i < hexString.length(); i++) {
                    hexStringForView += hexString.charAt(i);
                    intentCounter2++;
                    intentCounter8++;

                    if(intentCounter2 == 2) {
                        hexStringForView += " ";
                        intentCounter2 = 0;
                    }

                    if(intentCounter8 == 8) {
                        hexStringForView += " | ";
                        intentCounter8 = 0;
                    }
                }

                return hexStringForView;
            }
        }

        return null;
    }

    public static String getHexString(byte[] data) {
        return getHexString(data, false);
    }

    public static String getHexString(byte[] data, boolean bigEndian) {
        String dataHexString = null;
        if(data != null && data.length > 0) {
            dataHexString = "#";
            if(bigEndian) {
                for(int i = 0; i < data.length; i++) {
                    String hexString = Integer.toHexString(data[i]);
                    if(hexString.length() > 2) {
                        dataHexString += hexString.substring(hexString.length() - 2, hexString.length());
                    }
                    else if(hexString.length() == 1) {
                        dataHexString += "0" + hexString;
                    }
                    else {
                        dataHexString += hexString;
                    }
                }
            }
            else {
                for(int i = data.length - 1; i >= 0; i--) {
                    String hexString = Integer.toHexString(data[i]);
                    if(hexString.length() > 2) {
                        dataHexString += hexString.substring(hexString.length() - 2, hexString.length());
                    }
                    else if(hexString.length() == 1) {
                        dataHexString += "0" + hexString;
                    }
                    else {
                        dataHexString += hexString;
                    }
                }
            }
        }

        return dataHexString;
    }

    public static byte[] decodeMTProtoHexString(String dataHexStr) {
        if(dataHexStr != null ) {
            if(dataHexStr.startsWith("0x")) {
                dataHexStr = dataHexStr.substring(2);
            }

            if(dataHexStr.startsWith("#")) {
                dataHexStr = dataHexStr.substring(1);
            }

            if(dataHexStr.length() != 0) {

                if(dataHexStr.length() % 2 != 0) {
                    return null;
                }

                int length = dataHexStr.length() / 2;

                String lengthStr = Integer.toHexString(length);

                int lengthBytesSymbolCount;
                if(length <= 253) {
                    lengthBytesSymbolCount = 1 * 2;
                }
                else {
                    lengthBytesSymbolCount = 3 * 2;
                }

                for (int i = lengthStr.length(); i < lengthBytesSymbolCount; i++) {
                    lengthStr = "0" + lengthStr;
                }

                if(length > 253) {
                    lengthStr = Integer.toHexString(254) + lengthStr;
                }

                dataHexStr = lengthStr + dataHexStr;

                int modulo = dataHexStr.length() % 8;

                if(modulo > 0) {
                    int alignmentCount = 8 - modulo;

                    if(alignmentCount > 0) {

                        String alignmentStr = "";
                        if(alignmentCount % 2 == 0) {
                            for(int i = 0; i < alignmentCount / 2; i++) {
                                alignmentStr += "00";
                            }
                        }
                        else {
                            //TODO
                            return null;
                        }

                        dataHexStr += alignmentStr;
                    }
                }

                return getBytesFromHexString(dataHexStr);
            }

        }

        return null;
    }

    public static byte[] getBytesFromHexString(String dataStr) {
        if(dataStr != null ) {
            if(dataStr.startsWith("0x")) {
                dataStr = dataStr.substring(2);
            }

            if(dataStr.startsWith("#")) {
                dataStr = dataStr.substring(1);
            }

            if(dataStr.length() != 0) {

                if(dataStr.length() % 2 != 0) {
                    return null;
                }

                byte[] result = new byte[dataStr.length() / 2];

                for(int i = 0; i < result.length; i++) {
                    result[i] = Byte.parseByte(dataStr.substring(i * 2, i * 2 + 2), 16);
                }

                return result;
            }
        }

        return null;
    }
}
