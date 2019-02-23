package com.yangheng.StudyGuard.Utils;

import java.awt.TrayIcon.MessageType;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.yangheng.StudyGuard.PlanNotifier;
import com.yangheng.StudyGuard.GUI.IdeaFrame;
import com.yangheng.StudyGuard.GUI.MainFrame;
import com.yangheng.StudyGuard.Object.Idea;
import com.yangheng.StudyGuard.Object.Mind;

public class IOUtils extends Thread {
	public ArrayList<String> planlist;
	public ArrayList<String> ideaslist;
	public ArrayList<String> mindlist;
	public ArrayList<String> memolist;
	public ArrayList<String> proverblist;

	private String filepath;

	public IOUtils(String filepath) {
		this.filepath = filepath;
	}

	public ArrayList<String> getPlanlist() {
		return planlist;
	}

	public void setPlanlist(ArrayList<String> planlist) {
		this.planlist = planlist;
	}

	public ArrayList<String> getIdeaslist() {
		return ideaslist;
	}

	public void setIdeaslist(ArrayList<String> ideaslist) {
		this.ideaslist = ideaslist;
	}

	public ArrayList<String> getMindlist() {
		return mindlist;
	}

	public void setMindlist(ArrayList<String> mindlist) {
		this.mindlist = mindlist;
	}

	public ArrayList<String> getMemolist() {
		return memolist;
	}

	public void setMemolist(ArrayList<String> memolist) {
		this.memolist = memolist;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public ArrayList<String> load_study_plan() {
		try {
			Utils.loadConfig();
			planlist = Utils
					.readTxtFileIntoStringArrList(filepath + "\\plan\\" + Utils.getTime().substring(0, 11) + ".txt");
			if (!PlanNotifier.isalive) {
				new PlanNotifier().start();
				PlanNotifier.isalive = true;
			}
		} catch (Exception e) {

		}
		return planlist;
	}

	public void updateStudyPlan() {
		try {
			File file = new File(filepath + "\\plan\\" + Utils.getTime().substring(0, 11) + ".txt");
			if (file.exists()) {
				file.delete();
			}
			Utils.writeObjectsToFile(planlist, filepath + "\\plan\\" + Utils.getTime().substring(0, 11) + ".txt");

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public ArrayList<Idea> getIdeas(String mindpath) {
		ArrayList<String> minddatabase = Utils.getFiles(mindpath);
		ArrayList<String> mindlist = Utils
				.readTxtFileIntoStringArrList(minddatabase.get((int) (Math.random() * minddatabase.size())));
		ArrayList<Idea> ideas = new ArrayList<Idea>();
		for (int i = 0; i < mindlist.size(); i++) {
			ideas.add(new Idea(mindlist.get(i)));
		}
		return ideas;
	}

	public void storeIdea(String idea) {
		IdeaFrame mindFrame = IdeaFrame.getInstance();
		mindFrame.setVisible(true);
		Mind mind = new Mind(Utils.getTime().substring(0, 17), idea.replace("\n", "*#&"), "DISPLAY");
		if (!idea.trim().equals(null)) {
			try {
				ArrayList<String> arrayList = new ArrayList<String>();
				arrayList.add(mind.toString());
				Utils.writeObjectsToFile(arrayList, MainFrame.filePath + "\\idea\\idea.txt");
				IdeaFrame.instance.dispose();
				IdeaFrame.instance = IdeaFrame.getInstance();
				IdeaFrame.instance.setVisible(true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public ArrayList<String> getProverblist() {
		return proverblist;
	}

	public void setProverb(ArrayList<String> proverb) {
		this.proverblist = proverb;
	}

	@Override
	public void run() {
		while (true) {

			try {
				load_study_plan();// 读取学习计划
				
				ideaslist = Utils.readTxtFileIntoStringArrList(filepath + "\\idea\\idea.txt");//读取ideas
//				System.out.println(ideaslist);
				proverblist = Utils.readTxtFileIntoStringArrList(MainFrame.filePath + "\\proverb\\proverb.txt");//读取proverb

				memolist =  Utils.readTxtFileIntoStringArrList(MainFrame.filePath + "\\memorandum\\memorandum.txt");//读取meomorandum

				sleep(1000*60*10);// 3分钟IO一次
				
				File file = new File(MainFrame.filePath + "\\plan\\" + Utils.getTime().substring(0, 11));
				
				if (file.exists()) {
					file.delete();
					try {
						Utils.writeObjectsToFile(planlist,
								MainFrame.filePath + "\\plan\\" + Utils.getTime().substring(0, 11));
					} catch (Exception e) {
						MainFrame.showToast("错误", "写入计划文件失败", MessageType.ERROR);
					}
				}
				File file1 = new File(MainFrame.filePath + "\\idea\\idea.txt");
				if (file1.exists()) {
					file1.delete();
					try {
						Utils.writeObjectsToFile(ideaslist, MainFrame.filePath + "\\idea\\idea.txt");
					} catch (Exception e) {
						MainFrame.showToast("错误", "写入idea文件失败", MessageType.ERROR);
					}
				}
			} catch (Exception e) {

			}
		}

	}
}
