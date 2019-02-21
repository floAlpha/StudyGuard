package com.yangheng.StudyGuard;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.yangheng.StudyGuard.GUI.MainGuardFrame;
import com.yangheng.StudyGuard.Utils.Utils;

public class Watcher extends Thread {

	public static String filePath;// 工作路径
	public static String studyPlanPath;

	public static ArrayList<String> planlist;

	public static boolean iswatching = true;

	Notifier informer;

	public Watcher(String filePath, String studyPlanPath) {

		Watcher.filePath = filePath;
		Watcher.studyPlanPath = studyPlanPath;

	}

	public static boolean load_study_plan() {

		try {
			Utils.loadConfig();
			Notifier.planlist = Utils.readTxtFileIntoStringArrList(
					MainGuardFrame.filePath + "\\plan\\" + Utils.getTime().substring(0, 11) + ".txt");

			if (!Notifier.isalive) {
				new Notifier(planlist).start();
				Notifier.isalive = true;
			}

		} catch (Exception e) {

		}

		return true;
	}

	private String ask_what_is_doing() {
		java.awt.Toolkit.getDefaultToolkit().beep();
		String doing = JOptionPane.showInputDialog(null, "学习情况记录，输入你正在做的事：\n", "你知道自己在做什么吗？",
				JOptionPane.PLAIN_MESSAGE);
		try {
			if (!doing.equals(null) && !doing.equals("")) {
				Utils.writeToFileByRow(Utils.getTime().substring(0, 17) + doing,
						filePath + "\\监控日志\\" + Utils.getTime().substring(0, 11) + ".txt");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doing;

	}

	@Override
	public void run() {
		while (true) {
			while (iswatching && Utils.askfordoing.equals("true")) {
				try {
					sleep((long) (((Math.random() * 0.5 + 0.1) * 6000000 * 3)));
				} catch (InterruptedException e) {
				}
				ask_what_is_doing();
			}
			try {
				sleep(3000);
			} catch (InterruptedException e) {

			}
		}
	}

}
