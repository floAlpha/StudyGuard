package com.yangheng.StudyGuard;

import java.awt.TrayIcon.MessageType;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import com.yangheng.StudyGuard.GUI.MainGuardFrame;
import com.yangheng.StudyGuard.GUI.PlanInfoFrame;
import com.yangheng.StudyGuard.Object.StudyPlan;
import com.yangheng.StudyGuard.Utils.Utils;

public class Notifier extends Thread {

	public static ArrayList<String> planlist;
	public static boolean isalive = false;
	public static boolean iswatching = true;

	public Notifier(ArrayList<String> planlist) {
		super();
		isalive = true;
		Notifier.planlist = planlist;
	}

	public static void dailySum() {
		if (Utils.getTime().substring(12, 17).equals(Utils.dailysumtime)) {
			ArrayList<String> dailyitems = Utils.readTxtFileIntoStringArrList(
					MainGuardFrame.filePath + "\\plan\\" + Utils.getTime().substring(0, 11) + ".txt");
			double finished = 0;
			try {
				for (String s : dailyitems) {
					if (Utils.getValueOfElementByTag(s, "[finish]") != null
							&& Utils.getValueOfElementByTag(s, "[finish]").equals("���")) {
						finished++;
					}
					// System.out.println(s);
					Utils.writeToFileByRow(s,
							MainGuardFrame.filePath + "\\dailyplan\\" + Utils.getTime().substring(0, 11) + ".txt");
				}
				String dailysum = JOptionPane.showInputDialog(null, "��������չ����ܽ᣺");
				if (((String) dailysum).equals("")) {
					return;
				}
				Utils.writeToFileByRow("[ȫ��������ɶȣ���������ȡ������]" + (finished / dailyitems.size()) * 100 + "%",
						MainGuardFrame.filePath + "\\dailyplan\\" + Utils.getTime().substring(0, 11) + ".txt");
				Utils.writeToFileByRow("[���������ܽ�]" + dailysum,
						MainGuardFrame.filePath + "\\dailyplan\\" + Utils.getTime().substring(0, 11) + ".txt");
				java.awt.Toolkit.getDefaultToolkit().beep();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	@Override
	public void run() {
		while (true) {

			while (iswatching) {
				Utils.loadConfig();
				if (Utils.getTime().substring(12, 17).equals(Utils.pausequery)) {

					try {
						SimpleDateFormat simpleFormat = new SimpleDateFormat("hh:mm");
						Date pauseTime = simpleFormat.parse(Utils.pausequery);
						Date startTime = simpleFormat.parse(Utils.startquery);
						Watcher.iswatching = false;
						MainGuardFrame.showToast("֪ͨ", "�ر�ѧϰ���ѯ��", MessageType.INFO);

						sleep(Math.abs((startTime.getTime() - pauseTime.getTime())));
						Watcher.iswatching = true;
						MainGuardFrame.showToast("֪ͨ", "�ر�ѧϰ���ѯ��", MessageType.INFO);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				Watcher.load_study_plan();

				for (int i = 0; i < planlist.size(); i++) {
					String string = planlist.get(i);
					StudyPlan sp = new StudyPlan(string);
					if (Utils.getTime().substring(12, 17).equals(sp.getTime())) {
						StudyPlan sp1 = new StudyPlan(planlist.get(i - 1));
						if (i > 0) {
							int answer = JOptionPane.showConfirmDialog(null, "��һ����[ " + (sp1.getTask()) + " ]�Ƿ���ɣ�",
									"����", JOptionPane.YES_NO_OPTION);
							if (answer == 0) {
								sp1.setFinish("���");
								planlist.set(i - 1, sp1.toString());
							}else {
								sp1.setFinish("δ���");
								planlist.set(i - 1, sp1.toString());
							}

						}
						java.awt.Toolkit.getDefaultToolkit().beep();
						Object[] options = { "��������", "�Ƴ�10����", "�Ƴ�20����", "�Ƴ�30����", "ȡ������" };

						Object selectedItem = JOptionPane.showInputDialog(null, "ѧϰ�ƻ�:\n" + sp.getTask(), "������������!",
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
									Runtime.getRuntime().exec("explorer " + sp.getArgs());
									Runtime.getRuntime().exec(sp.getArgs());
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}

						PlanInfoFrame planInfoFrame = PlanInfoFrame.getInstance();
						planInfoFrame.setVisible(true);
						new Thread(planInfoFrame).start();
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

				dailySum();// ����ÿ��ѧϰ�ƻ��ܽ�
				try {
					sleep(55000);
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
			}
			try {
				sleep(50000);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}

		}

	}

	private StudyPlan updateStudyPlan(StudyPlan sp, int delay) {

		try {

			SimpleDateFormat simpleFormat = new SimpleDateFormat("HH:mm");
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
