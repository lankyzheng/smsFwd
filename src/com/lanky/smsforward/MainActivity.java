package com.lanky.smsforward;

import java.util.ArrayList;

import com.lanky.smsforward.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.EditText;

public class MainActivity extends Activity {

	private static String preference_string = "default_setting";
	private static String key_to = "key_to";
	private static String key_keyword = "key_keyword";
	private static String forward_prefix = "[auto]";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		restoreSetting();
	}

	@Override
	protected void onPause() {
		saveSetting();
		super.onPause();
	}

	private static SharedPreferences getSetting(Context context) {
		return context.getSharedPreferences(preference_string, Context.MODE_PRIVATE);
	}

	public static void onReceivedSMS(Context context, String message) {
		if (message == null) {
			return;
		}
		if (message.startsWith(forward_prefix)) {
			return;
		}
		String to = getSetting(context).getString(key_to, null);
		if (to == null) {
			return;
		}

		if (isMessageForForwarding(context, message)) {
			// String to_be_sent_message = forward_prefix.concat(message);

			// SmsManager.getDefault().sendTextMessage(to, null,
			// to_be_sent_message, null, null);
			SmsManager sm = SmsManager.getDefault();
			ArrayList<String> parts = sm.divideMessage(message);
			sm.sendMultipartTextMessage(to, null, parts, null, null);
			// sendSMS(to, message);
		}
	}

	public static boolean isMessageForForwarding(Context context, String message) {
		SharedPreferences setting = getSetting(context);

		String raw_keywords = setting.getString(key_keyword, null);
		if (raw_keywords == null) {
			return false;
		}

		String[] keywords = raw_keywords.split(",");
		if (keywords == null || keywords.length <= 0) {
			return false;
		}
		for (String keyword : keywords) {
			if (message.contains(keyword)) {
				return true;
			}
		}
		return false;
	}

	private void saveSetting() {
		final SharedPreferences preference = getSetting(this);
		SharedPreferences.Editor editor = preference.edit();
		editor.putString(key_to, getTextString(R.id.editText_to));
		editor.putString(key_keyword, getTextString(R.id.editText_keywords));
		editor.commit();
	}

	private void restoreSetting() {
		final SharedPreferences preference = getSetting(this);

		String to = preference.getString(key_to, null);
		if (to != null) {
			setText(R.id.editText_to, to);
		}
		String keyword = preference.getString(key_keyword, null);
		if (keyword != null) {
			setText(R.id.editText_keywords, keyword);
		}
	}

	private void setText(int id, String text) {
		final EditText edit_text = (EditText) findViewById(id);
		assert (edit_text != null);
		edit_text.setText(text);
	}

	private String getTextString(int id) {
		final EditText edit_text = (EditText) findViewById(id);
		assert (edit_text != null);
		return edit_text.getText().toString();
	}
	/*
	 * private static void sendSMS(String send_to, String send_message) {
	 * SmsManager sm = SmsManager.getDefault(); ArrayList<String> parts =
	 * sm.divideMessage(send_message);
	 * 
	 * sm.sendMultipartTextMessage(send_to, null, parts, null, null);
	 * 
	 * int numParts = parts.size(); String sms_message_send = ""; int j;
	 * Log.e("smsfwd", "divided into parts: " + numParts); //
	 * ArrayList<PendingIntent> sentIntents = new // ArrayList<PendingIntent>();
	 * // ArrayList<PendingIntent> deliveryIntents = new //
	 * ArrayList<PendingIntent>();
	 * 
	 * String prefixSMS = ""; // prefixSMS = prefixSMS + (char) 5 + (char) 0 +
	 * (char) 3 + (char) 0; prefixSMS = prefixSMS + (char) 6 + (char) 8 + (char)
	 * 4 + (char) 0 + (char) 77;
	 * 
	 * for (int i = 0; i < numParts; i++) { //
	 * sentIntents.add(PendingIntent.getBroadcast(getContext(), 0, //
	 * mSendIntent, 0)); //
	 * deliveryIntents.add(PendingIntent.getBroadcast(getContext(), // 0,
	 * mDeliveryIntent, 0)); // if (numParts > 1) { j = i + 1; //
	 * sms_message_send = prefixSMS + (char) numParts + (char) j + //
	 * parts.get(i); // } else { sms_message_send = parts.get(i); // }
	 * sm.sendTextMessage(send_to, null, sms_message_send, null, null);
	 * Log.e("smsfwd", sms_message_send); }
	 * 
	 * }
	 */
}
