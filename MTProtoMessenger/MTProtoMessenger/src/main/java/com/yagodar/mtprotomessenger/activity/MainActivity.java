package com.yagodar.mtprotomessenger.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yagodar.mtprotomessenger.R;
import com.yagodar.mtprotomessenger.servercon.Client;
import com.yagodar.mtprotomessenger.servercon.MessageListener;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_llv);

        client = new Client(getResources().getString(R.string.server_ip), getResources().getInteger(R.integer.server_port), new ServerMessageListener());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        client.stopListening();
    }

    public void onButtonClick(View button) {
        switch(button.getId()) {
            case R.id.btn_connect_disconnect:
                if(!client.isListening()) {
                    client.startListening();//TODO ошибка ! нобходимо отдельным потоком!!!

                    if(client.isListening()) {
                        ((TextView) findViewById(R.id.tv_connect_disconnect_result)).setText(R.string.connected);
                    }
                    else {
                        ((TextView) findViewById(R.id.tv_connect_disconnect_result)).setText(R.string.error);
                    }
                }
                else {
                    client.stopListening();

                    if(client.isListening()) {
                        ((TextView) findViewById(R.id.tv_connect_disconnect_result)).setText(R.string.error);
                    }
                    else {
                        ((TextView) findViewById(R.id.tv_connect_disconnect_result)).setText(R.string.disconnected);
                    }
                }

                break;
            case 2:
                byte buffer[] = "hello".getBytes();//TODO запрос другой естественно

                if(client.sendMessage(buffer)) {
                    //TODO
                }
                else {
                    //TODO
                }

                break;
            default:
                break;
        }
    }

    private class ServerMessageListener implements MessageListener {
        @Override
        public void onMessageReceived(int numberOfBytes, byte[] buffer) {
            ((TextView) findViewById(R.id.tv_response)).setText(new String(buffer));//TODO позже разбор
        }
    }

    private Client client;
}
