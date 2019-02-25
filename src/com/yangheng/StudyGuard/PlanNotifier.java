package com.yangheng.StudyGuard;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import com.yangheng.StudyGuard.GUI.MainFrame;
import com.yangheng.StudyGuard.Object.StudyPlan;
import com.yangheng.StudyGuard.Utils.Utils;

public class PlanNotifier extends Thread {

	public static ArrayList<String> planlist;
	public static boolean isalive = true;
	public static boolean iswatching = true;

	public static void dailyPlanSum() {
		if (Utils.getTime().substring(12, 17).equals(Utils.dailysumtime)) {
			ArrayList<String> dailyitems = Utils.readTxtFileIntoStringArrList(
					MainFrame.filePath + "\\plan\\" + Utils.getTime().substring(0, 11) + ".txt");
			double finished = 0;
			try {
				for (String s : dailyitems) {
					if (Utils.getValueOfElementByTag(s, "[finish]") != null
							&& Utils.getValueOfElementByTag(s, "[finish]").equals("完成")) {
						finished++;
					}

					Utils.writeToFileByRow(s,
							MainFrame.filePath + "\\dailyplan\\" + Utils.getTime().substring(0, 11) + ".txt");
				}
				String dailysum = JOptionPane.showInputDialog(null, "请输入今日工作总结：");
				if (((String) dailysum).equals("")) {
					return;
				}
				Utils.writeToFileByRow("[全天任务完成度（不包括已取消任务）]" + (finished / dailyitems.size()) * 100 + "%",
						MainFrame.filePath + "\\dailyplan\\" + Utils.getTime().substring(0, 11) + ".txt");
				Utils.writeToFileByRow("[今日任务总结]" + dailysum,
						MainFrame.filePath + "\\dailyplan\\" + Utils.getTime().substring(0, 11) + ".txt");
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

				planlist = MainFrame.ioUtils.getPlanlist();
				String time = Utils.getTime();
				for (int i = 0; i < planlist.size(); i++) {
					String string = planlist.get(i);
					StudyPlan sp = new StudyPlan(string);
					if (time.substring(12, 17).equals(sp.getTime())) {
						for (int j = 0; j < i; j++) {
							System.out.println(sp);
							StudyPlan sp1 = new StudyPlan(planlist.get(j));
							if (sp1.getFinish().equals("")) {
								// java.awt.Toolkit.getDefaultToolkit().beep();
								int answer = JOptionPane.showConfirmDialog(null,
										sp1.getTime() + " 任务[ " + (sp1.getTask()) + " ]是否完成？", "警告",
										JOptionPane.YES_NO_OPTION);
								if (answer == 0) {
									sp1.setFinish("完成");
									planlist.set(j, sp1.toString());
								} else {
									sp1.setFinish("未完成");
									planlist.set(j, sp1.toString());
								}
							}
						}

						Object[] options = { "接受任务", "推迟10分钟", "推迟20分钟", "推迟30分钟", "取消任务" };

						Object selectedItem = JOptionPane.showInputDialog(null, "此刻的任务:\n" + sp.getTask(), "学习计划",
								JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

						switch (selectedItem.toString()) {

						case "推迟10分钟":

							planlist.set(planlist.indexOf(string), updateStudyPlan(sp, 10).toString());

							if (Utils.cascadedelay.equals("true")) {
								updateSchdule(planlist, sp, 10);
							}
							break;

						case "推迟20分钟":
							planlist.set(planlist.indexOf(string), updateStudyPlan(sp, 20).toString());
							if (Utils.cascadedelay.equals("true")) {
								updateSchdule(planlist, sp, 20);
							}
							break;

						case "推迟30分钟":
							planlist.set(planlist.indexOf(string), updateStudyPlan(sp, 30).toString());
							if (Utils.cascadedelay.equals("true")) {
								updateSchdule(planlist, sp, 30);
							}
							break;
						case "取消任务":
							sp.setFinish("取消任务");
							planlist.set(planlist.indexOf(string), sp.toString());
							break;
						default:
							try {
								if (!sp.getArgs().equals(null) && !sp.getArgs().equals("")) {
									// System.out.println("explorer " +
									// sp.getArgs());
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

						try {
							sleep(60000);// 提醒一次休眠
						} catch (InterruptedException e) {

							e.printStackTrace();
						}
					}

				}
				if (time.substring(12, 17).equals("00:00")) {
					for (int j = 0; j < planlist.size(); j++) {
						StudyPlan sp1 = new StudyPlan(planlist.get(j));
						if (sp1.getFinish().equals("")) {
							// java.awt.Toolkit.getDefaultToolkit().beep();
							int answer = JOptionPane.showConfirmDialog(null,
									sp1.getTime() + " 任务[ " + (sp1.getTask()) + " ]是否完成？", "警告",
									JOptionPane.YES_NO_OPTION);
							if (answer == 0) {
								sp1.setFinish("完成");
								planlist.set(j, sp1.toString());
							} else {
								sp1.setFinish("未完成");
								planlist.set(j, sp1.toString());
							}
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

					dailyPlanSum();// 生成每日学习计划总结

				}
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
