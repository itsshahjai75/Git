package com.iodapp.activities;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Random;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import android.app.Activity;
import android.widget.Toast;

import com.firstdata.globalgatewaye4.*;

public class PaymentGetawatApi{

	public static void main(String[] args) {

		String str = new String();

		String x_TransactionKey = "eTv~5z0qmugbCIaxAF_Q";
		String x_Login = "WSP-IOD-D-XoZPfgAzuQ";
		String x_randomno = GetRandomNo();
		String x_timestamp = getTimeStamp();
		String x_amount = "1.00";
		String x_currency = ""; // default empty

		// str += x_TransactionKey;
		str += x_Login;
		str += "^";
		str += x_randomno;
		str += "^";
		str += x_timestamp;
		str += "^";
		str += x_amount;
		str += "^";
		str += x_currency;
		// msg3.setText(str);
		// msg1.setText("No"+x_randomno);
		// msg2.setText("time"+x_timestamp);

		// msg.setText(sStringToHMACMD5(x_TransactionKey,str));
		String HMAC_KEY = sStringToHMACMD5(x_TransactionKey, str);
		BigDecimal sB = new BigDecimal("1.00");
		GlobalGatewayE4 e4 = new GlobalGatewayE4(Environment.DEMO, "AE1534-05",
				"0mkw6u5h", "110312", HMAC_KEY);
		Request request = e4.getRequest();
		request.cardholder_name("E4 Java Tester").cc_number("4111111111111111")
				.cc_expiry("0420").cavv("111")
				.transaction_type(TransactionType.Purchase).amount(sB);
		try {
			Response response = request.submit();
			System.out.println("Transaction Approved: "
					+ response.transaction_approved());
			System.out.println("Bank Message:" + response.bank_message());
			System.out.println(response.ctr());
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected static String GetRandomNo() {
		Random random = new Random();
		return String.valueOf(random.nextInt(1000));
	}

	protected static String getTimeStamp() {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		long mil = calendar.getTimeInMillis();
		// msg.setText(String.valueOf(mil));
		return String.valueOf(mil);
	}

	public static String sStringToHMACMD5(String sKey, String sData) {
		SecretKeySpec key;
		byte[] bytes;
		String sEncodedString = null;
		try {
			key = new SecretKeySpec((sKey).getBytes(), "ASCII");
			Mac mac = Mac.getInstance("HMACMD5");
			mac.init(key);
			// mac.update(sKey.getBytes());
			bytes = mac.doFinal(sData.getBytes("ASCII"));

			StringBuffer hash = new StringBuffer();

			for (int i = 0; i < bytes.length; i++) {
				String hex = Integer.toHexString(0xFF & bytes[i]);
				if (hex.length() == 1) {
					hash.append('0');
				}
				hash.append(hex);
			}
			sEncodedString = hash.toString();
		} catch (UnsupportedEncodingException e) {
		} catch (InvalidKeyException e) {
		} catch (NoSuchAlgorithmException e) {
		}
		return sEncodedString;
	}
}