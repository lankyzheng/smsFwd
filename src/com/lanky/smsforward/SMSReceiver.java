package com.lanky.smsforward;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;

public class SMSReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		SmsMessage[] sms_messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
		//Log.e("smsfwd", "SMS length: " + sms_messages.length);
		String message_sender = "";
		String sms_message_body = "";

		for (SmsMessage message : sms_messages) {
			message_sender = message.getOriginatingAddress();
			// String message_body = message.getMessageBody();
			sms_message_body = sms_message_body + message.getMessageBody();
		}

		if (sms_message_body != null) {
			sms_message_body = "[" + message_sender + "]" + sms_message_body;
			//Log.e("smsfwd", sms_message_body);
			MainActivity.onReceivedSMS(context, sms_message_body);

		}
	}
}
