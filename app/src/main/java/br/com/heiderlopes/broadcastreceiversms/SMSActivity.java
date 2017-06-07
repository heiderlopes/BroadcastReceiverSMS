package br.com.heiderlopes.broadcastreceiversms;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class SMSActivity extends AppCompatActivity {

    private BroadcastReceiver mReceiver;

    private TextView tvMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        tvMessage = (TextView) findViewById(R.id.tvMessage);

        if(getIntent() != null) {
            Intent i = getIntent();
            String remetente = i.getStringExtra("remetente");
            String mensagem = i.getStringExtra("mensagem");
            tvMessage.setText(remetente == null && mensagem ==null ? "" : remetente + " : " + mensagem);
        }
        requestSmsPermission();
    }

    private void requestSmsPermission() {
        String permission = Manifest.permission.RECEIVE_SMS;
        int grant = ContextCompat.checkSelfPermission(this, permission);
        if ( grant != PackageManager.PERMISSION_GRANTED) {
            String[] permission_list = new String[1];
            permission_list[0] = permission;
            ActivityCompat.requestPermissions(this, permission_list, 1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter(
                "android.intent.action.SMSRECEBIDO");

        mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                //extract our message from intent
                String remetente = intent.getStringExtra("remetente");
                String mensagem = intent.getStringExtra("mensagem");
                tvMessage.setText(remetente + " : " + mensagem);

            }
        };
        //registering our receiver
        this.registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //unregister our receiver
        this.unregisterReceiver(this.mReceiver);
    }
}
