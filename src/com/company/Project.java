package com.company;

import java.util.Dictionary;
import java.util.Hashtable;

public class Project implements Comparable<Project>{
	private Integer local_id;
	private Integer remote_id;
	private String name;
	private String description;
	private Dictionary experiments = new Hashtable();

	public Project(Integer id, String name, String description){
		this.local_id = id;
		this.name = name;
		this.description = description;
	};

	public Project(Integer id, String name){
		this.local_id = id;
		this.name = name;
	};

	@Override
	public int compareTo(Project other_project) {
		return this.local_id.compareTo(other_project.get_local_id());
	};

	public Integer get_local_id(){
		return this.local_id;
	};

	public Integer get_remote_id(){
		return this.remote_id;
	};

	public void set_remote_id(Integer id){
		this.remote_id = id;
	};


	public String get_name(){
		return this.name;
	};

	public void set_name(String new_name){
		this.name = new_name;
	};

	public String get_description(){
		return this.description;
	};

	public void set_description(String new_description){
		this.description = new_description;
	};
}