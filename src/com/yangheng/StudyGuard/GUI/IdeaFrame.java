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
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

import com.yangheng.StudyGuard.Object.Idea;
import com.yangheng.StudyGuard.Utils.IOUtils;
import com.yangheng.StudyGuard.Utils.Utils;

public class IdeaFrame extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;
	public static IdeaFrame instance = null;
	static ArrayList<String> ideas;
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

	public void showIdea() {

		ArrayList<Idea> ideas = new ArrayList<Idea>();
		for (String string : IOUtils.ideaslist) {
			ideas.add(new Idea(string));
		}
		ArrayList<String> idea_pic = Utils.getFiles(MainFrame.filePath + "\\idea\\pic");

		for (int i = 0; i < idea_pic.size(); i++) {
			if (idea_pic.get(i).contains("NODISPLAY")) {
				idea_pic.remove(idea_pic.get(i));
			}
		}
		for (Idea i : ideas) {
			if (i.getTag().equals("DISPLAY")) {
				idea_pic.add(i.getContent());
			}
		}
		int tmp = (int) (Math.random() * idea_pic.size());

		if (!idea_pic.get(tmp).endsWith(".png")) {
			for (Idea idea : ideas) {
				if (idea.toString().contains(idea_pic.get(tmp))) {
					currentmind = idea;
					break;
				}
			}
		}

		if (!Utils.tiponwindow.equals("true")) {
			MainFrame.showToast("使用CTRL+SHIFT+X热键查看详情", idea_pic.get(tmp).replace("[换行]", "\n"), MessageType.INFO);
			MainFrame.ideaInfoFrame.updateView(new Idea(null, idea_pic.get(tmp), null));
			return;
		} else {
			MainFrame.ideaFloatFrame.updateTip(idea_pic.get(tmp));
		}
	}

	JPanel contentPane;

	private IdeaFrame() {

		setTitle("速记列表");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 876, 675);
		Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
		Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
		int screenWidth = screenSize.width / 2; // 获取屏幕的宽
		int screenHeight = screenSize.height / 2; // 获取屏幕的高

		int height = this.getHeight();
		int width = this.getWidth();
		setLocation(screenWidth - width / 2, screenHeight - height / 2);

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

		ArrayList<Idea> ideas = new ArrayList<Idea>();
		for (String idea : IOUtils.ideaslist) {
			ideas.add(new Idea(idea));
		}
		Vector<Vector<String>> tabledata = new Vector<Vector<String>>();
		for (Idea idea : ideas) {
			Vector<String> row = new Vector<String>();
			try {

				row.add(idea.getTime());
				row.add(idea.getContent());

				tabledata.add(row);
			} catch (Exception e) {
				e.printStackTrace();
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
				ideas = IOUtils.ideaslist;

				try {
					showIdea();
					if (Integer.parseInt(Utils.nextideashow) > 0) {
						Thread.sleep(Integer.parseInt(Utils.nextideashow) * 60000);
					} else {
						Thread.sleep((long) (((Math.random() * 0.5 + 0.1) * 6000000)));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
