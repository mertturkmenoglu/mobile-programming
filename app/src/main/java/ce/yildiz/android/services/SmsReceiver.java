package ce.yildiz.android.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import ce.yildiz.android.R;

public class SmsReceiver extends BroadcastReceiver {
    private static final String SMS_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = SmsReceiver.class.getSimpleName();

    @SuppressWarnings("deprecation")
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action == null) {
            Toast.makeText(context,
                    R.string.invalid_intent_error_message, Toast.LENGTH_LONG).show();
            return;
        }

        if (action.equals(SMS_ACTION)) {
            Bundle bundle = intent.getExtras();
            Log.d(TAG, context.getString(R.string.sms_receive_ok_message));

            if (bundle == null) return;

            Object[] pduArr = (Object[]) bundle.get("pdus");

            if (pduArr == null) return;

            ArrayList<SmsMessage> smsMessages = new ArrayList<>(pduArr.length);

            for (int i = 0; i < pduArr.length; i++) {
                smsMessages.set(i, SmsMessage.createFromPdu((byte[]) pduArr[i]));
                Calendar c = Calendar.getInstance();
                String dateStr = DateFormat.getDateInstance(DateFormat.DEFAULT).format(c.getTime());
                String toastMessage = "Phone Number: "
                        + smsMessages.get(i).getDisplayOriginatingAddress();
                toastMessage += "Message: " + smsMessages.get(i).getMessageBody();
                toastMessage += "Date: " + dateStr;

                Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show();
                Log.d(TAG, "onReceive: " + toastMessage);
            }

            return;
        }

        Log.e(TAG, "onReceive: Sms receive toast message failed");
    }
}
