package com.yangheng.StudyGuard.Utils;

import java.awt.TrayIcon.MessageType;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.yangheng.StudyGuard.GUI.IdeaFrame;
import com.yangheng.StudyGuard.GUI.MainFrame;
import com.yangheng.StudyGuard.Object.Idea;
import com.yangheng.StudyGuard.Object.Mind;



public class IOUtils extends Thread {
	public static ArrayList<String> planlist;
	public static ArrayList<String> ideaslist;
	public static ArrayList<String> mindlist;
	public static ArrayList<String> memolist;
	public static ArrayList<String> proverblist;

	public static String filepath;

	public IOUtils(String filepath) {
		IOUtils.filepath = filepath;
	}

	public ArrayList<String> load_study_plan() {
		try {
			Utils.loadConfig();
			planlist = Utils
					.readTxtFileIntoStringArrList(filepath + "\\plan\\" + Utils.getTime().substring(0, 11) + ".txt");
//			if (!PlanNotifier.isalive) {
//				new PlanNotifier().start();
//				PlanNotifier.isalive = true;
//			}
		} catch (Exception e) {

		}
		return planlist;
	}

	public static void writeStudyPlan() {
		try {
			File file = new File(filepath + "\\plan\\" + Utils.getTime().substring(0, 11) + ".txt");
			if (file.exists()) {
				file.delete();
			}
//			System.err.println("write");
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
		IdeaFrame ideaFrame = IdeaFrame.getInstance();
		ideaFrame.setVisible(true);
		Mind mind = new Mind(Utils.getTime().substring(0, 17), idea.replace("\n", "[����]"), "DISPLAY");
		if (!idea.trim().equals(null)) {
			IOUtils.ideaslist.add(mind.toString());
			IdeaFrame.instance.dispose();
			IdeaFrame.instance = IdeaFrame.getInstance();
			IdeaFrame.instance.setVisible(true);
		}
	}

	@Override
	public void run() {
		while (true) {

			try {

				load_study_plan();// ��ȡѧϰ�ƻ�
				ideaslist = Utils.readTxtFileIntoStringArrList(filepath + "\\idea\\idea.txt");// ��ȡideas
				proverblist = Utils.readTxtFileIntoStringArrList(MainFrame.filePath + "\\proverb\\proverb.txt");// ��ȡproverb
				memolist = Utils.readTxtFileIntoStringArrList(MainFrame.filePath + "\\memorandum\\memorandum.txt");// ��ȡmeomorandum
//				System.out.println(planlist);
	
				if (IdeaFrame.currentmind==null) {
					new Thread(IdeaFrame.getInstance()).start();
				}
				
				sleep(1000 * 60 * 3);// 3����IOһ��
//				writeStudyPlan();
				File file1 = new File(MainFrame.filePath + "\\idea\\idea.txt");
				if (file1.exists()) {
					file1.delete();
					try {
						Utils.writeObjectsToFile(ideaslist, MainFrame.filePath + "\\idea\\idea.txt");
					} catch (Exception e) {
						MainFrame.showToast("����", "д��idea�ļ�ʧ��", MessageType.ERROR);
					}
				}
				sleep(1000* 60 * 3);// 3����IOһ��
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	/**
     * ��ָ��URL����GET����������
     * 
     * @param url
     *            ���������URL
     * @param param
     *            ����������������Ӧ���� name1=value1&name2=value2 ����ʽ��
     * @return URL ������Զ����Դ����Ӧ���
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // �򿪺�URL֮�������
            URLConnection connection = realUrl.openConnection();
            // ����ͨ�õ���������
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            
            // ����ʵ�ʵ�����
            connection.connect();
            // ��ȡ������Ӧͷ�ֶ�
            Map<String, List<String>> map = connection.getHeaderFields();
            // �������е���Ӧͷ�ֶ�
            for (String key : map.keySet()) {
                map.get(key);
            }
            // ���� BufferedReader����������ȡURL����Ӧ
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(),"UTF-8"));

            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("����GET��������쳣��" + e);
            e.printStackTrace();
        }
        // ʹ��finally�����ر�������
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }


}
