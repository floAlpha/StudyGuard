package com.yangheng.StudyGuard.Object;

import com.yangheng.StudyGuard.Utils.Utils;

public class Memorandum {
	private String time;
	private String content;
	
	public Memorandum(String item) {
		super();
		
		time=Utils.getValueOfElementByTag(item, "[time]");
		content = Utils.getValueOfElementByTag(item, "[content]");
	}
	
	
	public Memorandum(String time, String content) {
		super();
		this.time = time;
		this.content = content;
	}


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

	@Override
	public String toString() {
		return "[time]" + time + "[time] [content]" + content+"[content]";
	}
	
	
}
