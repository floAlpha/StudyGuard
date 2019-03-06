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

public class MindFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String mindpath = MainFrame.filePath + "\\idea";
	static MindFrame instance = null;

	public static Mind currentmind = null;
	
	// ʵ����ֹ����������
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
		String string = JOptionPane.showInputDialog(null, "��������Ҫ����ļ��䣨������ʾ��֪ͨ�У�:", "IDEA¼��",
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
		

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout());
		try {
			remove(contentPane);
		} catch (Exception e) {

		}
		ArrayList<String> ideafiles = Utils.getFiles(MainFrame.filePath + "\\" + "mind");

		ArrayList<Mind> minds = new ArrayList<Mind>();
		for (String ideafile : ideafiles) {
			for (String idea : Utils.readTxtFileIntoStringArrList(ideafile)) {
				minds.add(new Mind(idea));
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
		// ����һά������Ϊ�б���
		Vector<String> columnTitle = new Vector<String>();
		columnTitle.add("ʱ��");
		columnTitle.add("Mind");
		JTable table = new JTable(tabledata, columnTitle);
		table.setRowSelectionAllowed(false);
		table.setLayout(new BorderLayout());
		table.setRowHeight(26);
		table.setForeground(Color.blue);
		table.setRowHeight(24);
		table.setFont(new Font("����", Font.PLAIN, 16));
		table.getTableHeader().setPreferredSize(new Dimension(1, 30));
		table.getTableHeader().setFont(new Font("����", Font.BOLD, 16));

		DefaultTableCellRenderer r = new DefaultTableCellRenderer();
		r.setHorizontalAlignment(JLabel.CENTER);
		table.setDefaultRenderer(Object.class, r);
		table.getTableHeader().setDefaultRenderer(r);
		r.setForeground(Color.RED);
		table.getTableHeader().setPreferredSize(new Dimension(1, 30));
		table.getTableHeader().setFont(new Font("����", Font.BOLD, 16));
		table.getTableHeader().setBackground(Color.green);
		table.setRowSelectionAllowed(true);
		table.setSelectionBackground(Color.GREEN);

		JScrollPane ideascrollPane = new JScrollPane(table);
		ideascrollPane.setBounds(10, 10, 840, 616);
		contentPane.add(ideascrollPane);
		this.setContentPane(contentPane);
		this.invalidate();

	}


}
