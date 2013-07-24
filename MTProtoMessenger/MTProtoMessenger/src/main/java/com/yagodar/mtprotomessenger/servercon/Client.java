package com.yagodar.mtprotomessenger.servercon;

import android.util.Log;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Yagodar on 19.07.13.
 */
public class Client {
    public Client(String serverIP, int serverPort, MessageListener messageListener, ConnectListener connectListener) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.messageListener = messageListener;
        this.connectListener = connectListener;
    }

    public void startListening() {
        String locLogTag = LOG_TAG + ".startListening";

        Log.i(locLogTag, "starting...");

        if(isListening()) {
            Log.i(locLogTag, "client already listening. (serverSocket='" + serverSocket + "';serverSocket.InetAddress='" + serverSocket.getInetAddress() + "'serverOutputStream='" + serverOutputStream + "';serverInputStream='" + serverInputStream + "'). pause listening...");
            pauseListening();
        }

        if(!isConnected()) {
            Log.i(locLogTag, "client not connected before. (serverSocket='" + serverSocket + "';serverSocket.isConnected='" + (serverSocket == null ? null : serverSocket.isConnected()) + "'). connecting...");

            if(!connect()) {
                Log.e(locLogTag, "error while connecting. break.");
                return;
            }
            else {
                Log.i(locLogTag, "resume listening...");
                resumeListening();

                connectListener.onConnected();
            }
        }
        else {
            Log.i(locLogTag, "client already connected. (serverSocket='" + serverSocket + "';serverSocket.InetAddress='" + serverSocket.getInetAddress() + "').");
        }

        if(!isListenEnabled()) {
            Log.i(locLogTag, "resume listening...");
            resumeListening();
        }

        Log.i(locLogTag, "creating read buffer[]... (size='" + BUFFER_SIZE + "')");

        byte buffer[] = new byte[BUFFER_SIZE];

        try {
            Log.i(locLogTag, "begin listening...");

            String bufferStr = null;
            while (isListenEnabled()) {
                int numberOfBytes = serverInputStream.read(buffer);

                bufferStr = "";
                for (int i = 0; i < numberOfBytes; i++) {
                    bufferStr += buffer[i] + " ";
                }

                Log.i(locLogTag, "new message! buffer[]='" + bufferStr + "'");

                messageListener.onMessageReceived(numberOfBytes, buffer);
            }
        }
        catch (Exception e) {
            Log.e(locLogTag, "error. (serverSocket='" + serverSocket + "';serverOutputStream='" + serverOutputStream + "';serverInputStream='" + serverInputStream + "'). stopping listening...", e);
            stopListening();
        }
    }

    public boolean sendBuffer(byte buffer[]) {
        String locLogTag = LOG_TAG + ".sendBuffer";

        Log.i(locLogTag, "sending buffer '" + new String(buffer) + "'...");

        try {
            serverOutputStream.write(buffer);
            serverOutputStream.flush();
        }
        catch (Exception e) {
            Log.e(locLogTag, "error. (serverOutputStream='" + serverOutputStream + "').", e);
            return false;
        }

        Log.i(locLogTag, "buffer  '" + new String(buffer) + "' sent.");

        return true;
    }

    public int getSendablePacketSN() {
        return sendablePacketSN;
    }

    public void incSendablePacketSN() {
        sendablePacketSN++;
    }

    public void pauseListening() {
        isListenEnabled = false;
    }

    public void resumeListening() {
        isListenEnabled = true;
    }

    public void stopListening() {
        String locLogTag = LOG_TAG + ".stopListening";

        Log.i(locLogTag, "pause listening...");
        pauseListening();

        Log.i(locLogTag, "close serverSocket='" + serverSocket + "'...");
        if (serverSocket != null) {
            try {
                serverSocket.close();
            }
            catch (Exception e) {
                Log.e(locLogTag, "error. (serverSocket='" + serverSocket + "'.", e);
            }
            finally {
                serverSocket = null;
            }
        }

        Log.i(locLogTag, "close serverOutputStream='" + serverOutputStream + "'...");
        if(serverOutputStream != null) {
            try {
                serverOutputStream.close();
            }
            catch (Exception e) {
                Log.e(locLogTag, "error. (serverOutputStream='" + serverOutputStream + "'.", e);
            }
            finally {
                serverOutputStream = null;
            }
        }

        Log.i(locLogTag, "close serverInputStream='" + serverInputStream + "'...");
        if(serverInputStream != null) {
            try {
                serverInputStream.close();
            }
            catch (Exception e) {
                Log.e(locLogTag, "error. (serverInputStream='" + serverInputStream + "').", e);
            }
            finally {
                serverInputStream = null;
            }
        }

        Log.i(locLogTag, "listening stopped.");
    }

    public boolean isListening() {
        return  isConnected() && isListenEnabled();
    }

    public boolean isListenEnabled() {
        return  isListenEnabled;
    }

    public boolean isConnected() {
        return serverSocket != null && serverSocket.isConnected();
    }

    private boolean connect() {
        String locLogTag = LOG_TAG + ".connect";

        Log.i(locLogTag, "connecting... (serverIP='" + serverIP + "';serverPort='" + serverPort + "').");

        try {
            serverSocket = new Socket(serverIP, serverPort);
            serverOutputStream = serverSocket.getOutputStream();
            serverInputStream = serverSocket.getInputStream();
        }
        catch (Exception e) {
            Log.e(locLogTag, "error. (serverSocket='" + serverSocket + "';serverOutputStream='" + serverOutputStream + "';serverInputStream='" + serverInputStream + "').", e);
            return false;
        }

        Log.i(locLogTag, "connected.");

        return true;
    }

    private String serverIP;
    private int serverPort;

    private MessageListener messageListener;
    private ConnectListener connectListener;

    private Socket serverSocket;
    private OutputStream serverOutputStream;
    private InputStream serverInputStream;

    private boolean isListenEnabled = false;

    private int sendablePacketSN;

    private static final int BUFFER_SIZE = 65536; //TODO разобраться с размером. сейчас 64*1024
    private static final String LOG_TAG = Client.class.getSimpleName();
}