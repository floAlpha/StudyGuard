package com.yangheng.StudyGuard;

import java.awt.TrayIcon.MessageType;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import javax.swing.JOptionPane;

import com.yangheng.StudyGuard.GUI.MainFrame;
import com.yangheng.StudyGuard.Object.StudyPlan;
import com.yangheng.StudyGuard.Utils.IOUtils;
import com.yangheng.StudyGuard.Utils.Utils;

public class PlanNotifier extends TimerTask {

	public static ArrayList<String> planlist;
	public static boolean isalive = true;
	public static boolean iswatching = true;

	SimpleDateFormat simpleFormat = new SimpleDateFormat("HH:mm");

	public static void dailyPlanSum() {
		String time = Utils.getTime().substring(0,11);
		if (Utils.getTime().substring(12, 17).equals(Utils.dailysumtime)) {
			IOUtils.writeStudyPlan();
			updateTaskStatus(planlist.size());
			IOUtils.writeStudyPlan();
			ArrayList<String> dailyitems = Utils.readTxtFileIntoStringArrList(
					MainFrame.filePath + "\\plan\\" + Utils.getTime().substring(0, 11) + ".txt");
			double finished = 0;
			try {
				for (String s : dailyitems) {
					if (Utils.getValueOfElementByTag(s, "[finish]") != null
							&& Utils.getValueOfElementByTag(s, "[finish]").equals("���")) {
						finished++;
					}
				}

				String dailysum = JOptionPane.showInputDialog(null, "��������չ����ܽ᣺");
				if (((String) dailysum).equals("")) {
					return;
				}
				Utils.writeToFileByRow("[ȫ��������ɶȣ���������ȡ������]" + (finished / dailyitems.size()) * 100 + "%",
						MainFrame.filePath + "\\plan\\" + time + ".txt");
				Utils.writeToFileByRow("[���������ܽ�]" + dailysum,
						MainFrame.filePath + "\\plan\\" + time + ".txt");
				java.awt.Toolkit.getDefaultToolkit().beep();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	public static void updateTaskStatus(int i) {
		for (int j = 0; j < i; j++) {

			StudyPlan sp1 = new StudyPlan(planlist.get(j));
			if (sp1.getFinish().equals("")) {
				// java.awt.Toolkit.getDefaultToolkit().beep();
				int answer = JOptionPane.showConfirmDialog(null, sp1.getTime() + " ����[ " + (sp1.getTask()) + " ]�Ƿ���ɣ�",
						"����", JOptionPane.YES_NO_OPTION);
				if (answer == 0) {
					sp1.setFinish("���");
					planlist.set(j, sp1.toString());
				} else {
					sp1.setFinish("δ���");
					planlist.set(j, sp1.toString());
				}
			}
		}
	}

	public void arrangeForTask(StudyPlan sp) {
		String string = sp.toString();
		Object[] options = { "��������", "�Ƴ�10����", "�Ƴ�20����", "�Ƴ�30����", "ȡ������" };

		Object selectedItem = JOptionPane.showInputDialog(null, "�˿̵�����:\n" + sp.getTask(), "ѧϰ�ƻ�",
				JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

		switch (selectedItem.toString()) {

		case "�Ƴ�10����":

			planlist.set(planlist.indexOf(string), updateStudyPlan(sp, 10).toString());

			if (Utils.cascadedelay.equals("true")) {
				updateSchdule(planlist, sp, 10);
			}
			break;

		case "�Ƴ�20����":
			planlist.set(planlist.indexOf(string), updateStudyPlan(sp, 20).toString());
			if (Utils.cascadedelay.equals("true")) {
				updateSchdule(planlist, sp, 20);
			}
			break;

		case "�Ƴ�30����":
			planlist.set(planlist.indexOf(string), updateStudyPlan(sp, 30).toString());
			if (Utils.cascadedelay.equals("true")) {
				updateSchdule(planlist, sp, 30);
			}
			break;
		case "ȡ������":
			sp.setFinish("ȡ������");
			planlist.set(planlist.indexOf(string), sp.toString());
			break;
		default:
			try {
				if (!sp.getArgs().equals(null) && !sp.getArgs().equals("")) {
					if (sp.getArgs().startsWith("http") || sp.getArgs().startsWith("www")) {
						Runtime.getRuntime().exec("cmd /c start " + sp.getArgs());
					} else if (sp.getArgs().startsWith("C:") || sp.getArgs().startsWith("D:")
							|| sp.getArgs().startsWith("E:") || sp.getArgs().startsWith("F:")
							|| sp.getArgs().startsWith("G:") || sp.getArgs().startsWith("H:")
							|| sp.getArgs().startsWith("I:") || sp.getArgs().startsWith("J:")) {
						Runtime.getRuntime().exec("explorer " + sp.getArgs());
						Runtime.getRuntime().exec(sp.getArgs());
					}

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		// while (true) {
		try {
			if (Utils.getTime().endsWith("09:00")) {
				String date = Utils.getTime().substring(0, 11).replace("��", "-").replace("��", "-").replace("��", "");
				String json = IOUtils.sendGet("http://timor.tech/api/holiday/info/" + date, null);
				if (!json.contains("null")) {
					String[] items = json.split(",");
					for (String string : items) {
						if (string.contains("name")) {
							MainFrame.showToast(null,"����(" + Utils.getTime().substring(0, 11) + ")��"
							+ string.split("\"")[string.split("\"").length - 1],MessageType.INFO);
							break;
						}
					}
				}
			}

			planlist = IOUtils.planlist;
			String time = Utils.getTime();
			for (int i = 0; i < planlist.size(); i++) {
				String string = planlist.get(i);
				StudyPlan sp = new StudyPlan(string);
				if (time.substring(12, 17).equals(sp.getTime())) {

					updateTaskStatus(i);
					arrangeForTask(sp);
				}
			}

			Collections.sort((List<String>) planlist, new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					if (o1.compareTo(o2) < 0) {
						return -1;
					} else if (o1.compareTo(o2) == 0) {
						return 0;
					} else {
						return 1;
					}

				}
			});
			dailyPlanSum();// ����ÿ��ѧϰ�ƻ��ܽ�

		
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private StudyPlan updateStudyPlan(StudyPlan sp, int delay) {

		try {
			Date t1 = simpleFormat.parse(sp.getTime());
			sp.setTime(simpleFormat.format(t1.getTime() + delay * 1000 * 60));
		} catch (Exception e) {

		}
		return sp;
	}

	private void updateSchdule(ArrayList<String> planlist, StudyPlan studyPlan, int delay) {

		for (String string : planlist) {
			if (string.contains(studyPlan.getTask())) {
				for (int i = planlist.indexOf(string) + 1; i < planlist.size(); i++) {
					StudyPlan sp = new StudyPlan(planlist.get(i));
					updateStudyPlan(sp, delay);
					planlist.set(i, sp.toString());
				}
			}
		}
	}
}
