package com.company;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

public class Main {
	public static void main(String[] args) {
		System.out.println("Starting Test!");
		URL url = null;
		try {
			url = new URL("https://lablet.vega.uberspace.de/scon/db.cgi");
			//url = new URL("https://lablet.vega.uberspace.de/scon/json_bounce.cgi");
		} catch (MalformedURLException e) {
			System.exit(1);
		};
		String user = "fredi@uni-siegen.de";
		String pw_h = "sad";
		ServerDatabaseSession SDS = new ServerDatabaseSession(url, user, pw_h);
		try {
			System.out.println(SDS.get_challenge());
		} catch (SBSBaseException e) {
			System.out.println(e);
		};
		try {
			LinkedList<Project> project_list = SDS.get_projects();
			LinkedList<Experiment> experiment_list = SDS.get_experiments();
			System.out.println(project_list);
			System.out.println(experiment_list);
		} catch (SBSBaseException e) {
			System.out.println(e);
		};
	};
};
