package com.company;

public class Project implements Comparable<Project>{
	private Integer id;
	private String name;
	private String description;
	//private Dictionary experiments = new Hashtable();

	public Project(Integer id, String name, String description){
		this.id = id;
		this.name = name;
		this.description = description;
	};

	@Override
	public int compareTo(Project other_project) {
		return this.id.compareTo(other_project.get_id());
	};

	public Integer get_id() {
		return this.id;
	};

	public String get_name(){
		return this.name;
	};

	public String get_description(){
		return this.description;
	};

}