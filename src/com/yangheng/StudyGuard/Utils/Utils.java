package com.yangheng.StudyGuard.Utils;

import java.awt.Component;
import java.awt.Font;
import java.awt.TrayIcon.MessageType;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

import com.yangheng.StudyGuard.GUI.MainFrame;

public class Utils {
	// public static String tipwinsizelevel;
	public static String dailysumtime;
	public static String cascadedelay;
	public static String nomainwindow;
	public static String nextideashow;
	public static String randomideashow;
	public static String tiponwindow;
	public static String poems;
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// 设置日期格式;

	public static void runAsTimer(TimerTask task) {
		Timer timer = new Timer();
		Date date = new Date();
		long time = (date.getTime() / 1000 / 60) * 60 * 1000 + 60 * 1000;
		timer.schedule(task, new Date(time), 60 * 1000);
		// timer.schedule(task, new Date(), 1000 );
	}

	public static void loadConfig() {
		ArrayList<String> configs = Utils.readTxtFileIntoStringArrList(MainFrame.filePath + "/conf/conf.txt");
		try {

			for (String string : configs) {
				for (String s : string.split(" ")) {

					if (Utils.getValueOfElementByTag(s, "[dailysumtime]") != null
							&& !Utils.getValueOfElementByTag(s, "[dailysumtime]").equals("")) {
						if ((dailysumtime = Utils.getValueOfElementByTag(s, "[dailysumtime]")).equals("")) {
							MainFrame.showToast("错误", "[dailysumtime]配置参数错误:" + dailysumtime, MessageType.ERROR);
						}
					}
					if (Utils.getValueOfElementByTag(s, "[cascadedelay]") != null
							&& !Utils.getValueOfElementByTag(s, "[cascadedelay]").equals("")) {
						if ((cascadedelay = Utils.getValueOfElementByTag(s, "[cascadedelay]")).equals("")) {
							MainFrame.showToast("错误", "[startquery]配置参数错误:" + cascadedelay, MessageType.ERROR);
						}
					}
					if (Utils.getValueOfElementByTag(s, "[nomainwindow]") != null
							&& !Utils.getValueOfElementByTag(s, "[nomainwindow]").equals("")) {
						if ((nomainwindow = Utils.getValueOfElementByTag(s, "[nomainwindow]")).equals("")) {
							MainFrame.showToast("错误", "[nomainwindow]配置参数错误:" + nomainwindow, MessageType.ERROR);
						}
					}
					if (Utils.getValueOfElementByTag(s, "[randomideashow]") != null
							&& !Utils.getValueOfElementByTag(s, "[randomideashow]").equals("")) {
						if ((randomideashow = Utils.getValueOfElementByTag(s, "[randomideashow]")).equals("")) {
							MainFrame.showToast("错误", "[randomideashow]配置参数错误:" + randomideashow, MessageType.ERROR);
						}
					}
					if (Utils.getValueOfElementByTag(s, "[nextideashow]") != null
							&& !Utils.getValueOfElementByTag(s, "[nextideashow]").equals("")) {
						if ((nextideashow = Utils.getValueOfElementByTag(s, "[nextideashow]")).equals("")) {
							MainFrame.showToast("错误", "[nextideashow]配置参数错误:" + nextideashow, MessageType.ERROR);
						}
					}
					if (Utils.getValueOfElementByTag(s, "[tiponwindow]") != null
							&& !Utils.getValueOfElementByTag(s, "[tiponwindow]").equals("")) {
						if ((tiponwindow = Utils.getValueOfElementByTag(s, "[tiponwindow]")).equals("")) {
							MainFrame.showToast("错误", "[tiponwindow]配置参数错误:" + tiponwindow, MessageType.ERROR);
						}
					}
					if (Utils.getValueOfElementByTag(s, "[poems]") != null
							&& !Utils.getValueOfElementByTag(s, "[poems]").equals("")) {
						if ((poems = Utils.getValueOfElementByTag(s, "[poems]")).equals("")) {
							MainFrame.showToast("错误", "[poems]配置参数错误:" + poems, MessageType.ERROR);
						}
					}

				}

			}

		} catch (Exception e) {

		}

	}

	/**
	 * 功能：Java读取txt文件的内容 步骤：1：先获得文件句柄 2：获得文件句柄当做是输入一个字节码流，需要对这个输入流进行读取
	 * 3：读取到输入流后，需要读取生成字节流 4：一行一行的输出。readline()。 备注：需要考虑的是异常情况
	 * 
	 * @param filePath
	 *            文件路径[到达文件:如： D:\aa.txt]
	 * @return 将这个文件按照每一行切割成数组存放到list中。
	 */
	public static ArrayList<String> readTxtFileIntoStringArrList(String filePath) {
		// System.out.println(filePath);
		ArrayList<String> list = new ArrayList<String>();
		try {

			String encoding = "GBK";
			File file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
			}
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;

				while ((lineTxt = bufferedReader.readLine()) != null) {
					if (lineTxt.equals("")) {
						continue;
					}
					list.add(lineTxt.replace('：', ':').replace("[换行]", "\n"));
				}
				bufferedReader.close();
				read.close();
			} else {
				System.out.println("找不到指定的文件" + filePath);
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错" + filePath);
			e.printStackTrace();
		}
		Collections.sort((List<String>) list, new Comparator<String>() {
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

		return list;
	}

	public static void writeToFileByRow(String string, String path) throws IOException {
		// System.err.println(path+" "+string);
		File file = new File(path);
		// 如果没有文件就创建
		if (!file.isFile()) {
			file.createNewFile();
		}
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path, true)));
			out.write(string.replace("[换行]", "\n") + "\r\n");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void writeObjectsToFile(ArrayList<String> objects, String path) throws IOException {
		// System.out.println(path);
		File file = new File(path);
		// 如果没有文件就创建
		if (!file.isFile()) {
			file.createNewFile();
		}
		Collections.sort((List<String>) objects, new Comparator<String>() {
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
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path, true)));
			for (String item : objects) {
				out.write(item.replace("\n", "[换行]") + "\r\n\r\n");
			}

		} catch (Exception e) {
			MainFrame.showToast("错误", "写入文件" + path + "失败，请检查文件是否存在", MessageType.ERROR);
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String getTime() {

		String currentTime = sdf.format(new Date());// new Date()为获取当前系统时间
		return currentTime;

	}

	public static ArrayList<String> getFiles(String path) {
		// System.out.println(path);
		ArrayList<String> files = new ArrayList<String>();
		File file = new File(path);
		if (!file.isDirectory()) {
			file.mkdir();
		}
		File[] tempList = file.listFiles();

		for (int i = 0; i < tempList.length; i++) {
			if (tempList[i].isFile()) {
				// System.out.println("文 件：" + tempList[i]);
				files.add(tempList[i].toString());
			}
			if (tempList[i].isDirectory()) {
				// System.out.println("文件夹：" + tempList[i]);
			}
		}
		return files;

	}

	public static String getValueOfElementByTag(String item, String name) {

		try {
			int begin = item.indexOf(name);
			int end = item.substring(begin + 1).indexOf(name);
			return item.substring(begin + name.length(), begin + end + 1);
		} catch (Exception e) {

			return "";
		}
	}

	/**
	 * frame中的控件自适应frame大小：改变大小位置和字体
	 * 
	 * @param frame
	 *            要控制的窗体
	 * @param proportion
	 *            当前和原始的比例
	 */
	public static void modifyComponentSize(JFrame frame, float proportionW, float proportionH) {

		try {
			Component[] components = frame.getRootPane().getContentPane().getComponents();
			for (Component co : components) {
				// String a = co.getClass().getName();//获取类型名称
				// if(a.equals("javax.swing.JLabel"))
				// {
				// }
				float locX = co.getX() * proportionW;
				float locY = co.getY() * proportionH;
				float width = co.getWidth() * proportionW;
				float height = co.getHeight() * proportionH;
				co.setLocation((int) locX, (int) locY);
				co.setSize((int) width, (int) height);
				int size = (int) (co.getFont().getSize() * proportionH);
				Font font = new Font(co.getFont().getFontName(), co.getFont().getStyle(), size);
				co.setFont(font);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
