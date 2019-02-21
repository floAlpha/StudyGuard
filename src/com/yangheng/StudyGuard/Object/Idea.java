package com.yangheng.StudyGuard.Object;

import com.yangheng.StudyGuard.Utils.Utils;

public class Idea {
	private String time;
	private String content;
	private String tag;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Idea(String item) {
		super();
		time = Utils.getValueOfElementByTag(item, "[time]");
		content = Utils.getValueOfElementByTag(item, "[content]");
		tag = Utils.getValueOfElementByTag(item, "[tag]");
	}

	public Idea(String time, String content, String tag) {
		super();
		this.time = time;
		this.content = content;
		this.tag = tag;
	}

	@Override
	public String toString() {
		return "[time]" + time + "[time] [content]" + content + "[content] [tag]" + tag+"[tag]";
	}

}
