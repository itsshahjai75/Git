package com.iodapp.activities;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Random;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import android.app.Activity;

public class SendAndCommitSource {
	  Hashtable<String, String> hMap;

	  SendAndCommitSource() {
	    hMap = new Hashtable<String, String>();

//	    hMap.put("ExactID", "AE1534-05");
//	    hMap.put("Password", "0mkw6u5h");
//	    hMap.put("KeyID", "110312");
//	    hMap.put("HmacKey", hmackey());
//	    hMap.put("Transaction_Type", "00");
//	    hMap.put("DollarAmount", "1.23");
//	    hMap.put("SurchargeAmount", "0.0");
//	    hMap.put("Card_Number", "4111111111111111");
////	    hMap.put("Expiry_Date", "01" + (Calendar.getInstance().get(Calendar.YEAR)  + 1 - 2000)); // Use expiry date of January, one year from "now"
//	    hMap.put("Expiry_Date", "0120"); 
//	    hMap.put("CardHoldersName", "test");
////	    hMap.put("Secure_AuthRequired", "");
////	    hMap.put("Secure_AuthResult", "");
//	    hMap.put("CAVV", "111");
////	    hMap.put("CAVV_Algorithm", "111");
	    hMap.put("ExactID", "AE1534-05");
	    hMap.put("Password", "0mkw6u5h");
	    hMap.put("Transaction_Type", "00");
	    hMap.put("DollarAmount", "1.23");
	    hMap.put("SurchargeAmount", "0.0");
	    hMap.put("Card_Number", "4111111111111111");
	    hMap.put("Transaction_Tag", "");
	    hMap.put("Track1", "");
	    hMap.put("Track2", "");
	    hMap.put("PAN", "");
	    hMap.put("Authorization_Num", "");
	    hMap.put("Expiry_Date", "0520"); // Use expiry date of January, one year from "now"
	    hMap.put("CardHoldersName", "Java Webservice Test");
	    hMap.put("VerificationStr1", "");
	    hMap.put("VerificationStr2", "");
	    hMap.put("CVD_Presence_Ind", "");
	    hMap.put("ZipCode", "");
	    hMap.put("Tax1Amount", "");
	    hMap.put("Tax1Number", "");
	    hMap.put("Tax2Amount", "");
	    hMap.put("Tax2Number", "");
	    hMap.put("Secure_AuthRequired", "");
	    hMap.put("Secure_AuthResult", hmackey());
	    hMap.put("Ecommerce_Flag", "");
	    hMap.put("XID", "110312");
	    hMap.put("CAVV", "111");
	    hMap.put("CAVV_Algorithm", "");
	    hMap.put("Reference_No", "");
	    hMap.put("Customer_Ref", "");
	    hMap.put("Reference_3", "");
	    hMap.put("Language", "en");
	    hMap.put("Bank_Message", "");
	    hMap.put("Client_Email", "youremail@gmail.com");
	  
	  }
	  
	  protected static String hmackey()
	  {
		  String str = new String();

			String x_TransactionKey = "eTv~5z0qmugbCIaxAF_Q";
			String x_Login = "WSP-IOD-D-XoZPfgAzuQ";
			String x_randomno = GetRandomNo();
			String x_timestamp = getTimeStamp();
			String x_amount = "50";
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
			return HMAC_KEY;
			
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