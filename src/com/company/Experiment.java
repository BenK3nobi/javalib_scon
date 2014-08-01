package com.company;

public class Experiment implements Comparable<Experiment> {
	private Integer project_id;
	private Integer id;
	private String name;
	private String description;
	//private SortedSet<Entry> entrys = new TreeSet(); //we need to add an comparator here

	public Experiment(Integer project_id, Integer id, String name, String description) {
		this.project_id = project_id;
		this.id = id;
		this.name = name;
		this.description = description;
	};

	@Override
	public int compareTo(Experiment other_experiment) {
		return this.id.compareTo(other_experiment.get_id());
	};

	public Integer get_id() {
		return this.id;
	};

	public Integer get_project_id() {
		return this.project_id;
	};

	public String get_name() {
		return this.name;
	};

	public String get_description() {
		return this.description;
	};
}