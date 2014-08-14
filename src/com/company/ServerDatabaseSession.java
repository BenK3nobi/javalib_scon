package com.company;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLHandshakeException;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.Scanner;
//import android.util.Base64;

public class ServerDatabaseSession {
	private String session_id;
	private Boolean session_id_set;
	private String username;
	private String password_hash;
	private URL database_url;


	public ServerDatabaseSession(URL database_url, String username, String password_hash) {
		this.database_url = database_url;
		this.username = username;
		this.password_hash = password_hash;
		this.session_id_set = Boolean.FALSE;
	}

	//De and Encode a Base64 Wrapped, ASCII Encoded String
//they already arrive as a String because of the utf-8 transport
//not working on a normal pc as we need the andoid stubs
	private byte[] uni2bin(String uni) throws SBSBaseException {
		try {
			byte[] ascii_encoded_bytes = uni.getBytes("ASCII");
			return Base64.decode(ascii_encoded_bytes);//  Base64.DEFAULT);
		} catch (UnsupportedEncodingException e) {
			throw new SBSBaseException();
		} catch (IOException e) { //needed for Base64
			throw new SBSBaseException();
		}
	}

	private String bin2uni(byte[] bin) throws SBSBaseException {
		//some changes are needed if I go back to the andoid version
		byte[] base64_encoded_bytes = Base64.encodeBytesToBytes(bin); //, Base64.DEFAULT);
		try {
			return new String(base64_encoded_bytes, "ascii");
		} catch (UnsupportedEncodingException e) {
			throw new SBSBaseException();
		}
	}

	private JSONObject send_json(JSONObject message) throws SBSBaseException {
		return this.send_json(message, 10000, 15000);
	}

	;

	private JSONObject send_json(JSONObject message, Integer ReadTimeout, Integer ConnectTimeout) throws SBSBaseException {
		//transform JSONObject to a byte string
		byte[] message_bytes = null;
		try {
			message_bytes = message.toString().getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UTF-8 encoding not available, this should really not happen");
		}

		String response_string = null;
		try {
			HttpsURLConnection conn = (HttpsURLConnection) this.database_url.openConnection();
			conn.setReadTimeout(ReadTimeout);
			conn.setConnectTimeout(ConnectTimeout);
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setFixedLengthStreamingMode(message_bytes.length);
			conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
			conn.connect();
			OutputStream os = new BufferedOutputStream(conn.getOutputStream());
			os.write(message_bytes);
			os.flush();
			os.close();
			InputStream is = conn.getInputStream();
			response_string = new Scanner(is, "UTF-8").useDelimiter("\\A").next().trim();
			is.close();
			conn.disconnect();
		} catch (SSLHandshakeException e) {
			throw new NoValidSSLCert();
		} catch (IOException e) {
			System.out.println(e);
			throw new NoServerConnectionException();
		}

		//maybe we should return a JSONObject with success:failed here and add the string as a parameter
		if (response_string.startsWith("<html>")) {
			throw new ServerSideException();
		}
		;

		try {
			JSONObject final_object = new JSONObject(response_string);
			return final_object;
		} catch (JSONException e) {
			System.out.println(response_string);
			System.out.println(e);
			throw new ErroneousResponse();
		}
	};

	private byte[] calculate_response(byte[] salt, byte[] challenge)
	{
		return "a".getBytes(Charset.forName("UTF-8"));
	};

	private void check_for_session() throws SBSBaseException {
		if (!this.session_id_set) {
			//we need a session id before we try to get projects
			throw new SBSBaseException();
		}
		;
	}

	;

	private void check_for_success(JSONObject result) throws SBSBaseException {
		//check if we succeeded
		try {
			if (!result.getString("status").toLowerCase().equals("success")) {
				throw new SBSBaseException();
			}
			;
		} catch (JSONException e) {
			throw new SBSBaseException();
		}
	}

	;

	private JSONObject send_action_after_auth_and_get_result(String action) throws SBSBaseException {
		this.check_for_session();
		JSONObject request = new JSONObject();
		try {
			request.put("action", action);
			request.put("session_id", this.session_id);
		} catch (JSONException e) {
			//should be impossible as we add a valid parameter to the json
			throw new SBSBaseException();
		}
		JSONObject result = this.send_json(request);
		this.check_for_success(result);
		return result;
	}

	;

	public byte[] get_challenge() throws SBSBaseException {
		JSONObject request = new JSONObject();
		try {
			request.put("action", "get_challenge");
			request.put("username", this.username);
		} catch(JSONException e){
			//should be impossible as we add a valid parameter to the json
			throw new SBSBaseException();
		}
		JSONObject result = null;
		result = this.send_json(request);
		try {
			this.session_id = result.getString("session_id");
			this.session_id_set = Boolean.TRUE;
		}catch (JSONException e){
			throw new SBSBaseException();
		}
		try{
			return this.uni2bin(result.getString("challenge"));
		}catch (JSONException e){
			throw new SBSBaseException();
		}
	};

	public Boolean auth_session(byte[] response){return Boolean.TRUE;};

	public LinkedList<RemoteProject> get_projects() throws SBSBaseException {
		this.check_for_session();
		JSONObject request = new JSONObject();
		try {
			request.put("action", "get_projects");
			request.put("session_id", this.session_id);
		} catch (JSONException e) {
			//should be impossible as we add a valid parameter to the json
			throw new SBSBaseException();
		}
		JSONObject result = this.send_json(request);
		this.check_for_success(result);

		JSONArray project_json_array = null;
		try {
			project_json_array = result.getJSONArray("projects");
		} catch (JSONException e) {
			throw new SBSBaseException();
		}

		LinkedList<RemoteProject> remoteProject_list = new LinkedList<RemoteProject>();
		for (int i = 0; i < project_json_array.length(); i++) {
			JSONArray project_json = null;
			Integer id = null;
			String name = null;
			String description = null;
			try {
				project_json = project_json_array.getJSONArray(i);
				id = project_json.getInt(0);
				name = project_json.getString(1);
				description = project_json.getString(2);
				remoteProject_list.add(new RemoteProject(id, name, description));
			} catch (JSONException e) {
				//some project did not decode correctly
				throw new SBSBaseException();
			}
			;
		}
		return remoteProject_list;
	}

	;


	public LinkedList<RemoteExperiment> get_experiments() throws SBSBaseException {
		this.check_for_session();
		JSONObject request = new JSONObject();
		System.out.println("Success0");
		try {
			request.put("action", "get_experiments");
			request.put("session_id", this.session_id);
			//request.put("project_id", project_id);
		} catch (JSONException e) {
			//should be impossible as we add a valid parameter to the json
			throw new SBSBaseException();
		}
		JSONObject result = this.send_json(request);
		this.check_for_success(result);

		JSONArray experiment_json_array = null;
		try {
			experiment_json_array = result.getJSONArray("experiments");
		} catch (JSONException e) {
			throw new SBSBaseException();
		}
		LinkedList<RemoteExperiment> remoteExperiment_list = new LinkedList<RemoteExperiment>();
		for (int i = 0; i < experiment_json_array.length(); i++) {
			JSONArray experiment_json = null;
			Integer project_id = null;
			Integer id = null;
			String name = null;
			String description = null;
			try {
				experiment_json = experiment_json_array.getJSONArray(i);
				project_id = experiment_json.getInt(0);
				id = experiment_json.getInt(1);
				name = experiment_json.getString(2);
				description = experiment_json.getString(3);
				remoteExperiment_list.add(new RemoteExperiment(project_id, id, name, description));
			} catch (JSONException e) {
				//some project did not decode correctly
				throw new SBSBaseException();
			}
			;
		}
		return remoteExperiment_list;
	};

	public Integer[] get_last_entry_ids(Integer session_id, Integer experiment_id, Integer entry_count){return null;};

	public RemoteEntry get_entry(Integer entry_id) {
		return null;
	}

	;
}
