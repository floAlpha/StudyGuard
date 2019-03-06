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
		Mind mind = new Mind(Utils.getTime().substring(0, 17), idea.replace("\n", "[换行]"), "DISPLAY");
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

				load_study_plan();// 读取学习计划
				ideaslist = Utils.readTxtFileIntoStringArrList(filepath + "\\idea\\idea.txt");// 读取ideas
				proverblist = Utils.readTxtFileIntoStringArrList(MainFrame.filePath + "\\proverb\\proverb.txt");// 读取proverb
				memolist = Utils.readTxtFileIntoStringArrList(MainFrame.filePath + "\\memorandum\\memorandum.txt");// 读取meomorandum
//				System.out.println(planlist);
	
				if (IdeaFrame.currentmind==null) {
					new Thread(IdeaFrame.getInstance()).start();
				}
				
				sleep(1000 * 60 * 3);// 3分钟IO一次
//				writeStudyPlan();
				File file1 = new File(MainFrame.filePath + "\\idea\\idea.txt");
				if (file1.exists()) {
					file1.delete();
					try {
						Utils.writeObjectsToFile(ideaslist, MainFrame.filePath + "\\idea\\idea.txt");
					} catch (Exception e) {
						MainFrame.showToast("错误", "写入idea文件失败", MessageType.ERROR);
					}
				}
				sleep(1000* 60 * 3);// 3分钟IO一次
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	/**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                map.get(key);
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(),"UTF-8"));

            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
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
