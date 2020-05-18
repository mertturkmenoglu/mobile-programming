package ce.yildiz.android.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

import ce.yildiz.android.R;

public class PhoneCallReceiver extends BroadcastReceiver {
    private static final String PHONE_CALL_ACTION = "android.intent.action.PHONE_STATE";
    private static final String TAG = PhoneCallReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action == null) {
            Toast.makeText(context,
                    R.string.invalid_intent_error_message, Toast.LENGTH_LONG).show();
            return;
        }

        if (action.equals(PHONE_CALL_ACTION)) {
            try {
                Bundle bundle = intent.getExtras();

                if (bundle == null) return;

                String phoneNumber = bundle.getString("incoming_number");
                String state = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);

                if (phoneNumber == null || state == null) return;

                if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                    Calendar c = Calendar.getInstance();
                    String dateFormatted = DateFormat
                            .getDateInstance(DateFormat.DEFAULT).format(c.getTime());
                    String toastMessage = "Phone Number: " + phoneNumber;
                    toastMessage += "Date: " + dateFormatted;

                    Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show();
                    Log.d(TAG, "onReceive: " + toastMessage);
                }

                return;
            } catch (Exception e) {
                Toast.makeText(context, R.string.exception, Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }

        Log.e(TAG, "onReceive: Phone call toast message failed");
    }
}
