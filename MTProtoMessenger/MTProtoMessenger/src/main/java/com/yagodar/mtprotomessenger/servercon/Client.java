package com.yagodar.mtprotomessenger.servercon;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Yagodar on 19.07.13.
 */
public class Client {
    public Client(String serverIP, int serverPort, MessageListener messageListener) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.messageListener = messageListener;
    }

    public void startListening() {
        if(!connect()) {
            return;
        }

        isListenEnabled = true;

        byte buffer[] = new byte[64*1024];//TODO разобраться с размером

        try {
            while (isListenEnabled) {
                //TODO test
                int numberOfBytes = serverInputStream.read(buffer);
                //test
                messageListener.onMessageReceived(numberOfBytes, buffer);
            }
        }
        catch (Exception e) {
            //TODO
            isListenEnabled = false;
        }
    }

    public boolean sendMessage(byte buffer[]) {
        try {
            serverOutputStream.write(buffer);
            serverOutputStream.flush();
        }
        catch (Exception e) {
            //TODO
            return false;
        }

        return true;
    }

    public void stopListening(){
        isListenEnabled = false;

        try {
            serverSocket.close();
        }
        catch (Exception e) {
            //TODO
        }
        finally {
            serverSocket = null;
        }

        try {
            serverOutputStream.close();
        }
        catch (Exception e) {
            //TODO
        }
        finally {
            serverOutputStream = null;
        }

        try {
            serverInputStream.close();
        }
        catch (Exception e) {
            //TODO
        }
        finally {
            serverInputStream = null;
        }
    }

    public boolean isListening() {
        return isListenEnabled;
    }

    private boolean connect() {
        try {
            serverSocket = new Socket(serverIP, serverPort);
            serverOutputStream = serverSocket.getOutputStream();
            serverInputStream = serverSocket.getInputStream();
        }
        catch (Exception e) {
            //TODO
            return false;
        }

        return true;
    }

    private String serverIP;
    private int serverPort;

    private MessageListener messageListener = null;

    private Socket serverSocket;
    private OutputStream serverOutputStream;
    private InputStream serverInputStream;

    private boolean isListenEnabled = false;
}