package com.company;

import java.net.MalformedURLException;
import java.net.URL;
import java.io.*;
import org.json.JSONObject;
import javax.net.ssl.HttpsURLConnection;
import java.util.Scanner;

public class JSONTest {
	public static void main(String[] args) {
		OutputStream os = null;
		InputStream is = null;
		HttpsURLConnection conn = null;
		try {
			//constants
			URL url = new URL("https://lablet.vega.uberspace.de/scon/db.cgi");
			String message = new JSONObject().toString();
			//String message = "{\"1\":1}".toString();
			conn = (HttpsURLConnection) url.openConnection();
			conn.setReadTimeout(10000 /*milliseconds*/);
			conn.setConnectTimeout(15000 /* milliseconds */);
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setFixedLengthStreamingMode(message.getBytes().length);

			//make some HTTP header nicety
			conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");

			//open
			conn.connect();

			//setup send
			os = new BufferedOutputStream(conn.getOutputStream());
			os.write(message.getBytes());
			//clean up
			os.flush();

			//do somehting with response
			is = conn.getInputStream();
			String contentAsString = new Scanner(is,"UTF-8").useDelimiter("\\A").next();
			System.out.println(contentAsString);
		}catch (MalformedURLException e){
			System.out.println("Malformed Url");
		}catch (IOException e){
			System.out.println("IOException");
		}finally {
			//clean up
			//os.close();
			//is.close();
			//conn.disconnect();
		}
	};
}
