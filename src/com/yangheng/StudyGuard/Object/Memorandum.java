package com.yangheng.StudyGuard.Object;

import com.yangheng.StudyGuard.Utils.Utils;

public class Memorandum {
	private String time;
	private String content;
	
	public Memorandum(String item) {
		time=Utils.getValueOfElementByTag(item, "[time]");
		content = Utils.getValueOfElementByTag(item, "[content]");
	}
	
	
	public Memorandum(String time, String content) {
		this.time = time;
		this.content = content;
	}
	public String getTime() {
		return time;
	}

	public String getContent() {

		return content;
	}

	@Override
	public String toString() {
		return "[time]" + time + "[time] [content]" + content+"[content]";
	}
	
	
}
