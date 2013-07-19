package com.yagodar.mtprotomessenger.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.yagodar.mtprotomessenger.R;
import com.yagodar.mtprotomessenger.servercon.TCPClient;

import java.util.ArrayList;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_llv);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void onButtonClick(View button) {
        switch(button.getId()) {
            case R.id.btn_test_con:
                // connect to the server
                new connectTask().execute("");
                //TODO look http://myandroidsolutions.blogspot.ru/2012/07/android-tcp-connection-tutorial.html
                break;
            default:
                break;
        }
    }

    public class connectTask extends AsyncTask<String,String,TCPClient> {

        @Override
        protected TCPClient doInBackground(String... message) {

            //we create a TCPClient object and
            mTcpClient = new TCPClient(new TCPClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                }
            });
            mTcpClient.run();

            return null;
        }
    }

    private TCPClient mTcpClient;
    private ArrayList<String> arrayList;
}
