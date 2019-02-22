package com.yangheng.StudyGuard.GUI;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.TrayIcon.MessageType;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

import com.yangheng.StudyGuard.Notifier;
import com.yangheng.StudyGuard.Object.Idea;
import com.yangheng.StudyGuard.Object.Mind;
import com.yangheng.StudyGuard.Utils.Utils;

public class IdeaFrame extends JFrame implements Runnable {


	private static final long serialVersionUID = 1L;
	static String mindpath = MainGuardFrame.filePath + "\\idea";
	static IdeaFrame instance = null;
	static ArrayList<Idea> ideas = getIdeas(mindpath);
	public static Idea currentmind = null;
	
	// 实现阻止程序多次启动
	public static IdeaFrame getInstance() {

		if (instance == null) {
			synchronized (IdeaFrame.class) {
				if (instance == null) {
					instance = new IdeaFrame();
				}
			}
		} else {
			instance.dispose();
			instance = new IdeaFrame();
		}
		return instance;
	}


	public static void storeIdea(String idea) {
		System.out.println(idea);
		IdeaFrame mindFrame = IdeaFrame.getInstance();
		mindFrame.setVisible(true);
		Mind mind = new Mind(Utils.getTime().substring(0, 17), idea.replace("\n", "*#&"), "DISPLAY");
		System.out.println(mind.toString());
		if (!idea.trim().equals(null)) {
			try {
				ArrayList<String> arrayList = new ArrayList<String>();
				arrayList.add(mind.toString());
				Utils.writeObjectsToFile(arrayList,
						MainGuardFrame.filePath + "\\idea\\idea.txt");
				instance.dispose();
				instance = new IdeaFrame();
				instance.setVisible(true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static ArrayList<Idea> getIdeas(String mindpath) {
		ArrayList<String> minddatabase = Utils.getFiles(mindpath);
		ArrayList<String> mindlist = Utils
				.readTxtFileIntoStringArrList(minddatabase.get((int) (Math.random() * minddatabase.size())));
		ArrayList<Idea> ideas = new ArrayList<Idea>();
		for (int i = 0; i < mindlist.size(); i++) {
			ideas.add(new Idea(mindlist.get(i)));
		}
		return ideas;
	}

	public void showIdea() {

		ArrayList<Idea> ideas = getIdeas(mindpath);
//		System.out.println(ideas);
		int count = 0;
		if (ideas.size()==0) {
			return;
		}
		while ( count < ideas.size()) {
			int i = ((int) (Math.random() * ideas.size()));
//			System.out.println(ideas.get(i).getTag());
			System.out.println(ideas.get(i));
			if (ideas.get(i).getTag().equals("DISPLAY")){
				MainGuardFrame.showToast("使用CTRL+SHIFT+X热键查看详情", ideas.get(i).getContent().replace("*#&", "\n"), MessageType.INFO);
				currentmind=ideas.get(i);
				return;
			}
			count++;
		}
	}

	JPanel contentPane;


	private IdeaFrame() {

		setTitle("速记列表");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 876, 675);

		final Toolkit toolkit = Toolkit.getDefaultToolkit();
		toolkit.addAWTEventListener(new AWTEventListener() {
			@Override
			public void eventDispatched(AWTEvent e) {
				if (e.getID() == KeyEvent.KEY_PRESSED) {
					KeyEvent evt = (KeyEvent) e;
					if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
						dispose();
					}
				}
			}
		}, AWTEvent.KEY_EVENT_MASK);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout());
		// System.out.println(MainGuardFrame.filePath + "\\" + "idea");
		try {
			remove(contentPane);
		} catch (Exception e) {

		}
		ArrayList<String> ideafiles = Utils.getFiles(MainGuardFrame.filePath + "\\" + "idea");

		ArrayList<Mind> minds = new ArrayList<Mind>();
		for (String ideafile : ideafiles) {
			for (String idea : Utils.readTxtFileIntoStringArrList(ideafile)) {
				minds.add(new Mind(idea.replace("*#&", "\n")));
			}
		}

		Vector<Vector<String>> tabledata = new Vector<Vector<String>>();
		for (Mind mind : minds) {
			Vector<String> row = new Vector<String>();
			try {

				row.add(mind.getTime());
				row.add(mind.getContent());

				tabledata.add(row);
			} catch (Exception e) {

			}

		}
		// 定义一维数据作为列标题
		Vector<String> columnTitle = new Vector<String>();
		columnTitle.add("时间");
		columnTitle.add("Idea");
		JTable table = new JTable(tabledata, columnTitle);
		table.setRowSelectionAllowed(false);
		table.setLayout(new BorderLayout());
		table.setRowHeight(26);
		table.setForeground(Color.blue);
		table.setRowHeight(24);
		table.setFont(new Font("楷体", Font.PLAIN, 16));
		table.getTableHeader().setPreferredSize(new Dimension(1, 30));
		table.getTableHeader().setFont(new Font("楷体", Font.BOLD, 16));

		DefaultTableCellRenderer r = new DefaultTableCellRenderer();
		r.setHorizontalAlignment(JLabel.CENTER);
		table.setDefaultRenderer(Object.class, r);
		table.getTableHeader().setDefaultRenderer(r);
		r.setForeground(Color.RED);
		table.getTableHeader().setPreferredSize(new Dimension(1, 30));
		table.getTableHeader().setFont(new Font("楷体", Font.BOLD, 16));
		table.getTableHeader().setBackground(Color.green);
		// table.setCellSelectionEnabled(true);
		table.setRowSelectionAllowed(true);
		table.setSelectionBackground(Color.GREEN);
		// table.setColumnSelectionAllowed(false);

		JScrollPane ideascrollPane = new JScrollPane(table);
		ideascrollPane.setBounds(10, 10, 840, 616);
		contentPane.add(ideascrollPane);
		this.setContentPane(contentPane);
		this.invalidate();

	}

	@Override
	public void run() {

		while (true) {
			if (Utils.randomideashow.equals("true")) {
				while (Notifier.iswatching) {
					ideas = getIdeas(mindpath);
					try {
						if (Integer.parseInt(Utils.nextideashow) > 0) {
							Thread.sleep(Integer.parseInt(Utils.nextideashow) * 60000);
						} else {
							Thread.sleep((long) (((Math.random() * 0.5 + 0.1) * 6000000)));
						}
						showIdea();
					} catch (Exception e) {
						try {
							Thread.sleep(60000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						 MainGuardFrame.showToast("警告",
						 "请检查控制idea回顾的参数是否合法",MessageType.ERROR);
					}
				}
			}

			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

	}
}
