package br.com.heiderlopes.broadcastreceiversms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SMSBroadcastReceiver extends BroadcastReceiver {

    final SmsManager sms = SmsManager.getDefault();

    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();

        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        String format = intent.getStringExtra("format");
                        currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i], format);
                    } else {
                        currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    }

                    String numeroTelefone = currentMessage.getDisplayOriginatingAddress();

                    String mensagem = currentMessage.getDisplayMessageBody();
                    Log.i("SmsReceiver", "senderNum: "+ numeroTelefone + "; message: " + mensagem);

                    Intent i2= new Intent("android.intent.action.SMSRECEBIDO")
                            .putExtra("remetente", numeroTelefone)
                            .putExtra("mensagem", mensagem);
                    context.sendBroadcast(i2);
                }
            }
        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);
        }
    }

    private void showToast(Context context, String numeroTelefone, String mensagem) {
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context,
                "senderNum: "+ numeroTelefone + ", message: " + mensagem, duration);
        toast.show();
    }
}
