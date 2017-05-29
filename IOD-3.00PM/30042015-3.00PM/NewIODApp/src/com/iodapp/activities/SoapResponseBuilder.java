package com.iodapp.activities;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Random;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.apache.http.client.HttpResponseException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class SoapResponseBuilder extends Activity{

	 private static final String SOAP_ACTION = "http://secure2.e-xact.com/vplug-in/transaction/rpc-enc/TransactionInfo";
	    private static final String METHOD_NAME = "TransactionInfo";
	    private static final String NAMESPACE = "http://secure2.e-xact.com/vplug-in/transaction/rpc-enc/";
	    private static final String URL = "https://api.demo.globalgatewaye4.firstdata.com/transaction/v12/wsdl";
	    private TextView tv;
	    private String response;
//	String Host = "https://demo.globalgatewaye4.firstdata.com";  //"api.globalgatewaye4.firstdata.com";
//	  String WebServicePath = "https://api.demo.globalgatewaye4.firstdata.com/transaction/v12/wsdl";
//	  String SoapAction = "http://secure2.e-xact.com/vplug-in/transaction/rpc-enc/SendAndCommit";
//	  String MethodName = "SendAndCommit";
//	  String XmlNamespace = "http://secure2.e-xact.com/vplug-in/transaction/rpc-enc/Response";
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.payment_getway_activity);
	 
	        tv= (TextView)findViewById(R.id.textView1);
	 
	        myAsyncTask myRequest = new myAsyncTask();
	        myRequest.execute();
	 
	    } 
	    private class myAsyncTask extends AsyncTask<Void, Void, Void>    {
	    	 
	    	 
	        @Override
	        protected void onPostExecute(Void result) {
	            super.onPostExecute(result);
	            tv.setText(response);
	        }
	 
	        @Override
	        protected void onPreExecute() {
	            super.onPreExecute();      
	        }
	 
	        @Override
	        protected Void doInBackground(Void... arg0) {
	 
	            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
	            request.addProperty("ExactID", "AE1534-05");
	            request.addProperty("Password", "0mkw6u5h");
	            request.addProperty("KeyId", "110312");
	            request.addProperty("HmacKey", hmackey());
	            request.addProperty("Transaction_Type", "00");
	            request.addProperty("DollarAmount", "5.00");
	            request.addProperty("Card_Number", "4111111111111111");
	            request.addProperty("Expiry_Date", "0520");
	            request.addProperty("CardHoldersName", "PG Patel");
	            request.addProperty("CAVV", "111");
	            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	            envelope.setOutputSoapObject(request); 
	 
	            HttpTransportSE httpTransport = new HttpTransportSE(URL);
	 
	            httpTransport.debug = true; 
	            try {
	                httpTransport.call(SOAP_ACTION, envelope);
	            } catch (HttpResponseException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            } catch (IOException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            } catch (XmlPullParserException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            } //send request
	            SoapObject result = null;
	            try {
	                result = (SoapObject)envelope.getResponse();
	            } catch (SoapFault e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	 
	            Log.d("App",""+result.getProperty(1).toString());
	            response = result.getProperty(1).toString();
	            return null;
	        }  
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
	
//	  public String sendRequest(SendAndCommitSource source) {
//	    String retval = "";
//
//	    SSLSocket socket = null;
//	    try {
//	      SSLSocketFactory factory = (SSLSocketFactory)SSLSocketFactory.getDefault();
//	      socket = (SSLSocket)factory.createSocket(Host, 443);
//	    }
//	    catch (Exception ex1) {
//	      return ("Error: "+ex1.getMessage());
//	    }
//
//	    try {
//	      int length = 2330;      // Original xml packet size without the variable data.
//
//	      // Sum up all the lengths of field values.
//	      length += (MethodName.length() * 2) + XmlNamespace.length();
//	      Enumeration<?> e = source.hMap.elements();
//	      while ( e.hasMoreElements() ) {
//		String value = (String)e.nextElement();
//		length += value.length();
//	      }
//
//	      StringBuffer outBuffer = new StringBuffer();
//
//	      // get header
//	      outBuffer.append("GET " + WebServicePath + " HTTP/1.0\r\n");
//	      outBuffer.append("Host: " + Host + "\r\n");
//	      outBuffer.append("Content-Type:text/xml;charset=utf-8\r\n");
//	      outBuffer.append("Content-Length:" + String.valueOf(length) + "\r\n");
//	      outBuffer.append("SOAPAction:\"" + SoapAction + "\"" + "\r\n");
//	      outBuffer.append("\r\n");
//
//	      // XML packet to process transactions.
//	      outBuffer.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>" + "\n");
//	      outBuffer.append("<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:tns=\"http://secure2.e-xact.com/vplug-in/transaction/rpc-enc/\" xmlns:types=\"http://secure2.e-xact.com/vplug-in/transaction/rpc-enc/encodedTypes\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" + "\n");
//	      outBuffer.append("<soap:Body soap:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">" + "\n");
//	      outBuffer.append("<q1:" + MethodName + " xmlns:q1=\"" + XmlNamespace + "\">" + "\n");
//	      outBuffer.append("<SendAndCommitSource href=\"#id1\" />" + "\n");
//	      outBuffer.append("</q1:" + MethodName + ">" + "\n");
//	      outBuffer.append("<types:Transaction id=\"id1\" xsi:type=\"types:Transaction\">" + "\n");
//
//	      // Populate the fields in the xml packet.
//	      e = source.hMap.keys();
//	      while ( e.hasMoreElements() ) {
//		String key = (String)e.nextElement();
//		String value = (String)source.hMap.get(key);
//		outBuffer.append("<" + key + " xsi:type=\"xsd:string\">" + value + "</" + key + ">\n");
//	      }
//
//	      outBuffer.append("</types:Transaction>" + "\n");
//	      outBuffer.append("</soap:Body>" + "\n");
//	      outBuffer.append("</soap:Envelope>");
//	      // end of XML packet
//
//	      // Print out the packet to stdout for your convenience.
//	      String sOutBuffer = outBuffer.toString();
//	      System.out.println(sOutBuffer);
//
//	      OutputStream os = socket.getOutputStream();
//	      boolean autoflush = true;
//	      PrintWriter out = new PrintWriter(socket.getOutputStream(), autoflush);
//	      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//
//	      // send an HTTPS request to the web service
//	      out.println(sOutBuffer);
//
//	      StringBuffer sb = new StringBuffer();     // Result buffer.
//	      char[] cb = new char[1024];
//	      int r = 0;
//	      while ( (r = in.read(cb, 0, 1024) ) != -1 ) {
//		sb.append(cb, 0, r);
//	      }
//	      // Close read buffer and close socket connnection.
//	      in.close();
//	      socket.close();
//
//	      // The StringBuffer result now contains the complete result from the
//	      // webservice in XML format.  You can parse this XML if you want to
//	      // get more complex results than a single value.
//
//	      return sb.toString();
//	     
//	    }
//	    catch (Exception ex) {
//	      System.out.println(ex.getMessage());
//	      return ex.getMessage();
//	    }
//	  }
	  
	 
//}

