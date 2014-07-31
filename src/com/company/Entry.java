package com.company;

public class Entry implements Comparable<Entry>{
	private Integer id;
	private String title;
	private String attachment; //no access yet
	private String attachment_type; //no access yet
	private String sync_time;
	private String entry_time;
	private String user;
	private Integer experiment_id;

	public Entry(Integer id, String title, String attachment, String attachment_type, String sync_time, String entry_time, String user, Integer experiment_id){
		this.id = id;
		this.title = title;
		this.attachment = attachment;
		this.attachment_type = attachment_type;
		this.sync_time = sync_time;
		this.entry_time = entry_time;
		this.user = user;
		this.experiment_id = experiment_id;
	};

	@Override
	public int compareTo(Entry other_entry) {
		return this.entry_time.compareTo(other_entry.get_entry_time());
	};

	public Integer get_id(){
		return this.id;
	};

	public String get_title(){
		return this.title;
	};

	public String get_sync_time(){
		return this.sync_time;
	};

	public String get_entry_time(){
		return this.entry_time;
	};

	public Integer get_experiment_id(){
		return this.experiment_id;
	};

	public String get_user(){
		return this.user;
	};
}