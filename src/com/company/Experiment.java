package com.company;

import java.util.SortedSet;
import java.util.TreeSet;

public class Experiment implements Comparable<Experiment> {
	private Integer local_id;
	private Integer remote_id;
	private String name;
	private String description;
	private SortedSet<Entry> entrys = new TreeSet(); //we need to add an comparator here

	public Experiment(Integer id, String name, String description) {
		this.local_id = id;
		this.name = name;
		this.description = description;
	};

	public Experiment(Integer id, String name) {
		this.local_id = id;
		this.name = name;
	};

	@Override
	public int compareTo(Experiment other_experiment) {
		return this.local_id.compareTo(other_experiment.get_local_id());
	};

	public Integer get_local_id() {
		return this.local_id;
	};

	public Integer get_remote_id() {
		return this.remote_id;
	};

	public void set_remote_id(Integer id) {
		this.remote_id = id;
	};

	public String get_name() {
		return this.name;
	};

	public void set_name(String new_name) {
		this.name = new_name;
	};

	public String get_description() {
		return this.description;
	};

	public void set_description(String new_description) {
		this.description = new_description;
	};
}