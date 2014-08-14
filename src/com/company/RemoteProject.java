package com.company;

public class RemoteProject implements Comparable<RemoteProject> {
	private Integer id;
	private String name;
	private String description;
	//private Dictionary experiments = new Hashtable();

	public RemoteProject(Integer id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	};

	@Override
	public int compareTo(RemoteProject other_Remote_project) {
		return this.id.compareTo(other_Remote_project.get_id());
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