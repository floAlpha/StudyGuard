package com.yangheng.StudyGuard.GUI;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

import com.yangheng.StudyGuard.Object.Mind;
import com.yangheng.StudyGuard.Utils.Utils;

public class MindFrame extends JFrame implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String mindpath = MainFrame.filePath + "\\idea";
	static MindFrame instance = null;

	public static Mind currentmind = null;
	
	// 实现阻止程序多次启动
	public static MindFrame getInstance() {

		if (instance == null) {
			synchronized (MindFrame.class) {
				if (instance == null) {
					instance = new MindFrame();
				}
			}
		} else {
			instance.dispose();
			instance = new MindFrame();
		}
		return instance;
	}

	public static void storeMind() {
		MindFrame mindFrame = MindFrame.getInstance();
		mindFrame.setVisible(true);
		String string = JOptionPane.showInputDialog(null, "请输入需要储存的记忆（不会显示在通知中）:", "IDEA录入",
				JOptionPane.OK_CANCEL_OPTION);
		Mind mind = new Mind(Utils.getTime().substring(0, 17), string, "NODISPLAY");
		if (!string.equals(null)) {
			try {
				ArrayList<String> arrayList = new ArrayList<String>();
				arrayList.add(mind.toString());
				Utils.writeObjectsToFile(arrayList,
						MainFrame.filePath + "\\mind\\mind.txt");
				instance.dispose();
				instance = new MindFrame();
				instance.setVisible(true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
//
//	public static void storeIdea() {
//		MindFrame mindFrame = MindFrame.getInstance();
//		mindFrame.setVisible(true);
//		String string = JOptionPane.showInputDialog(null, "请输入你的想法:", "IDEA录入", JOptionPane.OK_CANCEL_OPTION);
//		Mind mind = new Mind(Utils.getTime().substring(0, 17), string, "DISPLAY");
//		if (!string.equals(null)) {
//			try {
//				ArrayList<String> arrayList = new ArrayList<String>();
//				arrayList.add(mind.toString());
//				Utils.writeObjectsToFile(arrayList,
//						MainGuardFrame.filePath + "\\idea\\" + Utils.getTime().substring(0, 11) + ".txt");
//				instance.dispose();
//				instance = new MindFrame();
//				instance.setVisible(true);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//
//	}

//	private ArrayList<Mind> getMinds(String mindpath) {
//		ArrayList<String> minddatabase = Utils.getFiles(mindpath);
//		ArrayList<String> mindlist = Utils
//				.readTxtFileIntoStringArrList(minddatabase.get((int) (Math.random() * minddatabase.size())));
//		ArrayList<Mind> ideas = new ArrayList<Mind>();
//		for (int i = 0; i < mindlist.size(); i++) {
//			ideas.add(new Mind(mindlist.get(i)));
//		}
//		return ideas;
//	}
//
//	public void showIdea() {
//		ArrayList<Mind> minds = getMinds(mindpath);
//		System.out.println(minds);
//		int count = 0;
//		if (minds.size()==0) {
//			return;
//		}
//		while ( count < minds.size()) {
//			int i = ((int) (Math.random() * minds.size()));
//			if (minds.get(i).getTag().equals("DISPLAY")){
//				MainGuardFrame.showToast("idea回顾", minds.get(i).getContent(), MessageType.INFO);
//				currentmind=minds.get(i);
//				return;
//			}
//			count++;
//		}
////		showIdea();
//	}

	JPanel contentPane;


	private MindFrame() {

		setTitle("Minds");
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
		
//		addWindowListener(new WindowAdapter() {
//			@Override
//			public void windowClosing(WindowEvent e) {
//				try {
//					File file = new File(MainGuardFrame.studyPlanPath);
//					if (file.exists()) {
//						file.delete();
//					}
//				} catch (IOException e1) {
//					e1.printStackTrace();
//				}
//			}
//		});

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout());
		// System.out.println(MainGuardFrame.filePath + "\\" + "idea");
		try {
			remove(contentPane);
		} catch (Exception e) {

		}
		ArrayList<String> ideafiles = Utils.getFiles(MainFrame.filePath + "\\" + "mind");

		ArrayList<Mind> minds = new ArrayList<Mind>();
		for (String ideafile : ideafiles) {
			// System.out.println(Utils.readTxtFileIntoStringArrList(ideafile));
			for (String idea : Utils.readTxtFileIntoStringArrList(ideafile)) {
				// System.out.println("idea"+idea);
				minds.add(new Mind(idea));
			}
		}
		// System.out.println(minds);

		Vector<Vector<String>> tabledata = new Vector<Vector<String>>();
		for (Mind mind : minds) {
			// String[] tmp = idea.split(" ");
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
		columnTitle.add("Mind");
		JTable table = new JTable(tabledata, columnTitle);
		// System.out.println(columnTitle );
		// System.out.println(tabledata);
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

		
	}
}
