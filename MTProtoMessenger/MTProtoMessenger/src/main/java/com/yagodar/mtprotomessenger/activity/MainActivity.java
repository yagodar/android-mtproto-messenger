package com.yagodar.mtprotomessenger.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.yagodar.mtprotomessenger.R;
import com.yagodar.mtprotomessenger.servercon.Client;
import com.yagodar.mtprotomessenger.servercon.ConnectListener;
import com.yagodar.mtprotomessenger.servercon.MessageListener;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_llv);

        ServerListener serverListener = new ServerListener();
        client = new Client(getResources().getString(R.string.server_ip), getResources().getInteger(R.integer.server_port), serverListener, serverListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        client.stopListening();

        if(clientConnector != null) {
            clientConnector.cancel(true);
            clientConnector = null;
        }
    }

    public void onButtonClick(View button) {
        switch(button.getId()) {
            case R.id.btn_connect_disconnect:
                if(!client.isListening()) {
                    if(clientConnector != null) {
                        clientConnector.cancel(true);
                        clientConnector = null;
                    }

                    clientConnector = new ClientConnector();
                    clientConnector.execute(null);
                }
                else {
                    client.stopListening();

                    if(clientConnector != null) {
                        clientConnector.cancel(true);
                        clientConnector = null;
                    }

                    onClientDisconnected();
                }

                break;
            case R.id.btn_send_1_nonce:
                byte buffer[] = new byte[] { 60469778,  };
                onSendMessage(buffer, client.sendMessage(buffer), R.id.tv_send_1_nonce_result);
                break;
            default:
                break;
        }
    }

    private void onClientConnected() {
        String locLogTag = LOG_TAG + ".onClientConnected";

        if(client.isListening()) {
            //((TextView) findViewById(R.id.tv_connect_disconnect_result)).setText(R.string.connected);//TODO adapter
            Log.i(locLogTag, getResources().getString(R.string.connected));
        }
        else {
            //((TextView) findViewById(R.id.tv_connect_disconnect_result)).setText(R.string.error);//TODO adapter
            Log.i(locLogTag, getResources().getString(R.string.error));
        }
    }

    private void onClientDisconnected() {
        String locLogTag = LOG_TAG + ".onClientDisconnected";

        if(client.isListening()) {
            ((TextView) findViewById(R.id.tv_connect_disconnect_result)).setText(R.string.error);
            Log.i(locLogTag, getResources().getString(R.string.error));
        }
        else {
            ((TextView) findViewById(R.id.tv_connect_disconnect_result)).setText(R.string.disconnected);
            Log.i(locLogTag, getResources().getString(R.string.disconnected));
        }
    }

    private void onClientMessageReceived(int numberOfBytes, byte[] buffer) {
        String locLogTag = LOG_TAG + ".onClientMessageReceived";

        //((TextView) findViewById(R.id.tv_response)).setText(new String(buffer, 0, numberOfBytes));//TODO позже разбор //TODO adapter
        Log.i(locLogTag, new String(buffer, 0, numberOfBytes));
    }

    private void onSendMessage(byte[] buffer, boolean success, int resTvId) {
        String locLogTag = LOG_TAG + ".onSendMessage";

        if(success) {
            ((TextView) findViewById(resTvId)).setText(R.string.ok);
            Log.i(locLogTag, "send: '" + new String(buffer) + "'.");
        }
        else {
            ((TextView) findViewById(resTvId)).setText(R.string.error);
            Log.i(locLogTag, "error on send: '" + new String(buffer) + "'.");
        }
    }

    private class ServerListener implements MessageListener, ConnectListener {
        @Override
        public void onMessageReceived(int numberOfBytes, byte[] buffer) {
            onClientMessageReceived(numberOfBytes, buffer);
        }

        @Override
        public void onConnected() {
            onClientConnected();
        }
    }

    private class ClientConnector extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            client.startListening();
            return null;
        }
    }

    private Client client;
    private ClientConnector clientConnector;

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
}
