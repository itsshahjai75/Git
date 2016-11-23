package com.iodapp.activities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import com.firstdata.globalgatewaye4.Response;

import android.content.Context;
import android.widget.Toast;

 public class SoapRequestBuilder{
	  // The following section sets the connection requirements.

	  String Host = "https://demo.globalgatewaye4.firstdata.com";  //"api.globalgatewaye4.firstdata.com";
	  String WebServicePath = "https://api.demo.globalgatewaye4.firstdata.com/transaction/v12/wsdl";
	  String SoapAction = "http://secure2.e-xact.com/vplug-in/transaction/rpc-enc/SendAndCommit";
	  String MethodName = "SendAndCommit";
	  String XmlNamespace = "http://secure2.e-xact.com/vplug-in/transaction/rpc-enc/Request";
	  String XmlResponse = "http://secure2.e-xact.com/vplug-in/transaction/rpc-enc/Response";
	  
	  public String sendRequest(SendAndCommitSource source) {
	    String retval = "";

	    SSLSocket socket = null;
	    try {
	      SSLSocketFactory factory = (SSLSocketFactory)SSLSocketFactory.getDefault();
	      socket = (SSLSocket)factory.createSocket(Host, 443);
	    }
	    catch (Exception ex1) {
	      return ("Error: "+ex1.getMessage());
	    }

	    try {
	      int length = 2330;      // Original xml packet size without the variable data.

	      // Sum up all the lengths of field values.
	      length += (MethodName.length() * 2) + XmlNamespace.length();
	      Enumeration<?> e = source.hMap.elements();
	      while ( e.hasMoreElements() ) {
		String value = (String)e.nextElement();
		length += value.length();
	      }

	      StringBuffer outBuffer = new StringBuffer();

	      // post header
	      outBuffer.append("POST " + WebServicePath + " HTTP/1.0\r\n");
	      outBuffer.append("Host: " + Host + "\r\n");
	      outBuffer.append("Content-Type:text/xml;charset=utf-8\r\n");
	      outBuffer.append("Content-Length:" + String.valueOf(length) + "\r\n");
	      outBuffer.append("SOAPAction:\"" + SoapAction + "\"" + "\r\n");
	      outBuffer.append("\r\n");

	      // XML packet to process transactions.
	      outBuffer.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>" + "\n");
	      outBuffer.append("<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:tns=\"http://secure2.e-xact.com/vplug-in/transaction/rpc-enc/\" xmlns:types=\"http://secure2.e-xact.com/vplug-in/transaction/rpc-enc/encodedTypes\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" + "\n");
	      outBuffer.append("<soap:Body soap:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">" + "\n");
	      outBuffer.append("<q1:" + MethodName + " xmlns:q1=\"" + XmlNamespace + "\">" + "\n");
	      outBuffer.append("<SendAndCommitSource href=\"#id1\" />" + "\n");
	      outBuffer.append("</q1:" + MethodName + ">" + "\n");
	      outBuffer.append("<types:Transaction id=\"id1\" xsi:type=\"types:Transaction\">" + "\n");

	      // Populate the fields in the xml packet.
	      e = source.hMap.keys();
	      while ( e.hasMoreElements() ) {
		String key = (String)e.nextElement();
		String value = (String)source.hMap.get(key);
		outBuffer.append("<" + key + " xsi:type=\"xsd:string\">" + value + "</" + key + ">\n");
	      }

	      outBuffer.append("</types:Transaction>" + "\n");
	      outBuffer.append("</soap:Body>" + "\n");
	      outBuffer.append("</soap:Envelope>");
	      // end of XML packet

	      // Print out the packet to stdout for your convenience.
	      String sOutBuffer = outBuffer.toString();
	      System.out.println(sOutBuffer);

	      OutputStream os = socket.getOutputStream();
	      boolean autoflush = true;
	      PrintWriter out = new PrintWriter(socket.getOutputStream(), autoflush);
	      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

	      // send an HTTPS request to the web service
	      out.println(sOutBuffer);

	      StringBuffer sb = new StringBuffer();     // Result buffer.
	      char[] cb = new char[1024];
	      int r = 0;
	      while ( (r = in.read(cb, 0, 1024) ) != -1 ) {
		sb.append(cb, 0, r);
	      }
	      // Close read buffer and close socket connnection.
	      in.close();
	      socket.close();

	      // The StringBuffer result now contains the complete result from the
	      // webservice in XML format.  You can parse this XML if you want to
	      // get more complex results than a single value.

	      return sb.toString();
	     
	    }
	    catch (Exception ex) {
	      System.out.println(ex.getMessage());
	      return ex.getMessage();
	    }
	  }
	  
	  
	 
}
