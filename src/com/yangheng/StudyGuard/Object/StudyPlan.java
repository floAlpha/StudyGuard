package com.yangheng.StudyGuard.Object;

import com.yangheng.StudyGuard.Utils.Utils;

public class StudyPlan {
	private String time="";
	private String task="";
	private String detail="";
	private String args="";
	private String finish="";

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDetail() {
		return detail.replace("##", " ");
	}

	public void setDetail(String detail) {
		this.detail = detail.replace(" ", "##");
	}

	public String getTask() {
		return task.replace("##", " ");
	}

	public void setTask(String task) {
		this.task = task.replace(" ", "##");
	}

	public String getFinish() {
		return finish.replace("##", " ");
	}

	public void setFinish(String finish) {
		this.finish = finish.replace(" ", "##");
	}

	public String getArgs() {
		return args.replace("##", " ");
	}

	public void setArgs(String args) {
		this.args = args.replace(" ", "##");
	}

	public StudyPlan(String item) {
		
		super();
		time = Utils.getValueOfElementByTag(item, "[time]");
		task = Utils.getValueOfElementByTag(item, "[task]");
		detail = Utils.getValueOfElementByTag(item, "[detail]");
		finish = Utils.getValueOfElementByTag(item, "[finish]");
		args = Utils.getValueOfElementByTag(item, "[args]");

	}
	
	

	public StudyPlan(String time, String task, String detail, String args, String finish) {
		super();
		this.time = time;
		this.task = task.replace(" ", "##");
		this.detail = detail.replace(" ", "##");
		this.args = args.replace(" ", "##");
		this.finish = finish.replace(" ", "##");
	}

	@Override
	public String toString() {
		return "[time]" + time + "[time] [task]" + task + "[task] [detail]" + detail + "[detail] [args]" + args + "[args] [finish]"
				+ finish+"[finish]";
	}

}
