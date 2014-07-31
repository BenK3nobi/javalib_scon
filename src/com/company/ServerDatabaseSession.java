package com.company;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Scanner;
import java.util.UUID;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;
import android.util.Base64;

public class ServerDatabaseSession {
	private String session_id;
	private Boolean session_id_set;
	private String username;
	private String password_hash;
	private URL database_url;



//De and Encode a Base64 Wrapped, ASCII Encoded String
//they already arrive as a String because of the utf-8 transport
//not working on a normal pc as we need the andoid stubs
	private byte[] uni2bin(String uni) throws SBSBaseException{
		try {
			byte[] ascii_encoded_bytes = uni.getBytes("ASCII");
			return Base64.decode(ascii_encoded_bytes, Base64.DEFAULT);
		} catch (UnsupportedEncodingException e) {
			throw new SBSBaseException();
		}
	}

	private String bin2uni(byte[] bin) throws SBSBaseException{
		byte[] base64_encoded_bytes = Base64.encode(bin, Base64.DEFAULT);
		try {
			return new String(base64_encoded_bytes, "ascii");
		}catch (UnsupportedEncodingException e) {
			throw new SBSBaseException();
		}
	}

	public ServerDatabaseSession(URL database_url, String username, String password_hash){
		this.database_url = database_url;
		this.username = username;
		this.password_hash = password_hash;
		this.session_id_set = Boolean.FALSE;
	}

	private JSONObject send_json(JSONObject message) throws IOException, JSONException {
		System.out.println("Start Sending Data");
		//FIXME we need to check the https certificate!!!
		//FIXME make Timeouts variable
		//FIXME Exception Wrapping needs to be done
		HttpsURLConnection conn = null;
		byte[] message_bytes = message.toString().getBytes("UTF-8");
		conn = (HttpsURLConnection) this.database_url.openConnection();
		conn.setReadTimeout(10000 /*milliseconds*/);
		conn.setConnectTimeout(15000 /* milliseconds */);
		conn.setRequestMethod("POST");
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setFixedLengthStreamingMode(message_bytes.length);
		conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
		conn.connect();
		//setup send
		OutputStream os = new BufferedOutputStream(conn.getOutputStream());
		os.write(message_bytes);
		os.flush();
		//do something with response
		InputStream is = conn.getInputStream();
		String contentAsString = new Scanner(is,"UTF-8").useDelimiter("\\A").next();
		System.out.println(contentAsString);
		JSONObject final_object = new JSONObject(contentAsString);
		os.close();
		is.close();
		conn.disconnect();
		System.out.println("Start Sending Data finished");
		return final_object;
	};

	private byte[] calculate_response(byte[] salt, byte[] challenge)
	{
		return "a".getBytes(Charset.forName("UTF-8"));
	};

	public byte[] get_challenge() throws SBSBaseException {
		JSONObject request = new JSONObject();
		try {
			request.put("action", "get_challenge");
			request.put("username", this.username);
		} catch(JSONException e){
			//should be impossible as we add a valid parameter to the json
			throw new AssertionError(e);
		}
		JSONObject result = null;
		try {
			result = this.send_json(request);
		}catch (IOException e){
			throw new SBSBaseException();
		}catch (JSONException e){
			throw new SBSBaseException();
		}
		System.out.println("Response is here");
		System.out.println(result.toString());
		try {
			this.session_id = result.getString("session_id");
			this.session_id_set = Boolean.TRUE;
		}catch (JSONException e){
			throw new SBSBaseException();
		}
		System.out.println("Got Data from Response");
		try{
			return this.uni2bin(result.getString("challenge"));
		}catch (JSONException e){
			throw new SBSBaseException();
		}
	};

	public Boolean auth_session(byte[] response){return Boolean.TRUE;};
	public Project[] get_projects() throws SBSBaseException{
		JSONObject request = new JSONObject();
		if(!this.session_id_set){
			//we need a session id before we try to get projects
			throw new SBSBaseException();
		};
		try {
			request.put("action", "get_projects");
			request.put("session_id", this.session_id);
		} catch (JSONException e) {
			//should be impossible as we add a valid parameter to the json
			throw new AssertionError(e);
		}
		JSONObject result = null;
		try {
			result = this.send_json(request);
		} catch (IOException e) {
			throw new SBSBaseException();
		} catch (JSONException e) {
			throw new SBSBaseException();
		}
		System.out.println("Response is here");
		System.out.println(result.toString());
		Project[] project_array = null;
		return project_array;
	};


	public Experiment[] get_experiments(Integer project_id){return null;};
	public Integer[] get_last_entry_ids(Integer session_id, Integer experiment_id, Integer entry_count){return null;};
	public Entry get_entry(Integer entry_id){return null;};
}
