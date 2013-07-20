package com.yagodar.mtprotomessenger.servercon;

/**
 * Created by Yagodar on 20.07.13.
 */
public interface MessageListener {
    public void onMessageReceived(int numberOfBytes, byte buffer[]);
}
