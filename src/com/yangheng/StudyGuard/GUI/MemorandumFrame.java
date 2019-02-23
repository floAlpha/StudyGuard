package com.yangheng.StudyGuard.GUI;

import java.awt.AWTEvent;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.TrayIcon.MessageType;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.yangheng.StudyGuard.PlanNotifier;
import com.yangheng.StudyGuard.Object.Memorandum;
import com.yangheng.StudyGuard.Utils.Utils;

public class MemorandumFrame extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private static JTextField year;
	private static JTextField mon;
	private static JTextField day;
	private static JTextField hour;
	private static JTextField min;
	private static JTextArea task;

	static MemorandumFrame instance = null;

	// ʵ����ֹ����������
	public static MemorandumFrame getInstance() {

		if (instance == null) {
			synchronized (MemorandumFrame.class) {
				if (instance == null) {
					instance = new MemorandumFrame();
					instance.setVisible(true);
					instance.setAlwaysOnTop(true);
				}
			}
		}
		task.setText("");
		return instance;
	}

	private void storeMemo() {

		try {
			if (Integer.parseInt(year.getText()) > 0) {
				if (Integer.parseInt(mon.getText()) > 0 && Integer.parseInt(mon.getText()) < 13) {
					if (Integer.parseInt(day.getText()) > 0 && Integer.parseInt(day.getText()) < 32) {
						if (Integer.parseInt(hour.getText()) >= 0 && Integer.parseInt(hour.getText()) < 24) {
							if (Integer.parseInt(min.getText()) >= 0 && Integer.parseInt(mon.getText()) < 60) {
								if (!task.getText().equals("") && !task.getText().equals(null)) {

								}
								takeMemorandum(year.getText() + "��" + mon.getText() + "��" + day.getText() + "��" + " "
										+ hour.getText() + ":" + min.getText(), task.getText());
								dispose();
							}
						}
					}
				}
			}
		} catch (Exception formatException) {
			MainFrame.showToast("����", "����¼ʱ���ʽ����", MessageType.ERROR);
		}

	}

	/**
	 * Create the frame.
	 */
	public MemorandumFrame() {
		setTitle("\u65B0\u5EFA\u5907\u5FD8\u5F55");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setAlwaysOnTop(true);

		setBounds(100, 100, 406, 336);
		contentPane = new JPanel();
		final Toolkit toolkit = Toolkit.getDefaultToolkit();
		toolkit.addAWTEventListener(new AWTEventListener() {
			@Override
			public void eventDispatched(AWTEvent e) {
				if (e.getID() == KeyEvent.KEY_PRESSED) {
					KeyEvent evt = (KeyEvent) e;
					if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
						dispose();
					}
					if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
						storeMemo();
						dispose();
					}

				}
			}

		}, AWTEvent.KEY_EVENT_MASK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("\u65E5\u671F");
		lblNewLabel.setFont(new Font("����", Font.PLAIN, 16));
		lblNewLabel.setBounds(58, 23, 39, 19);
		contentPane.add(lblNewLabel);

		year = new JTextField();
		year.setFont(new Font("Times New Roman", Font.BOLD, 16));
		year.setHorizontalAlignment(SwingConstants.CENTER);
		year.setBounds(101, 22, 66, 21);
		contentPane.add(year);
		year.setColumns(10);

		JLabel label = new JLabel("\u5E74");
		label.setFont(new Font("����", Font.PLAIN, 16));
		label.setBounds(177, 23, 23, 19);
		contentPane.add(label);

		mon = new JTextField();
		mon.setHorizontalAlignment(SwingConstants.CENTER);
		mon.setFont(new Font("Times New Roman", Font.BOLD, 16));
		mon.setColumns(10);
		mon.setBounds(202, 22, 32, 21);
		contentPane.add(mon);

		JLabel label_1 = new JLabel("\u6708");
		label_1.setFont(new Font("����", Font.PLAIN, 16));
		label_1.setBounds(244, 24, 23, 19);
		contentPane.add(label_1);

		day = new JTextField();
		day.setHorizontalAlignment(SwingConstants.CENTER);
		day.setFont(new Font("Times New Roman", Font.BOLD, 16));
		day.setColumns(10);
		day.setBounds(265, 22, 32, 21);
		contentPane.add(day);

		JLabel label_2 = new JLabel("\u65E5");
		label_2.setFont(new Font("����", Font.PLAIN, 16));
		label_2.setBounds(307, 24, 23, 19);
		contentPane.add(label_2);

		JLabel label_3 = new JLabel("\u65F6\u95F4");
		label_3.setFont(new Font("����", Font.PLAIN, 16));
		label_3.setBounds(58, 63, 39, 19);
		contentPane.add(label_3);

		hour = new JTextField();
		hour.setHorizontalAlignment(SwingConstants.CENTER);
		hour.setFont(new Font("Times New Roman", Font.BOLD, 16));
		hour.setColumns(10);
		hour.setBounds(102, 63, 32, 21);
		contentPane.add(hour);

		JLabel label_4 = new JLabel("\u65F6");
		label_4.setFont(new Font("����", Font.PLAIN, 16));
		label_4.setBounds(144, 64, 23, 19);
		contentPane.add(label_4);

		min = new JTextField();
		min.setHorizontalAlignment(SwingConstants.CENTER);
		min.setFont(new Font("Times New Roman", Font.BOLD, 16));
		min.setColumns(10);
		min.setBounds(171, 63, 32, 21);
		contentPane.add(min);

		JLabel label_5 = new JLabel("\u5206");
		label_5.setFont(new Font("����", Font.PLAIN, 16));
		label_5.setBounds(214, 64, 23, 19);
		contentPane.add(label_5);

		task = new JTextArea();
		task.setWrapStyleWord(true);
		task.setLineWrap(true);
		task.setFont(new Font("����", Font.PLAIN, 16));
		task.setBounds(45, 108, 303, 111);
		contentPane.add(task);
		task.setColumns(10);

		JButton submit = new JButton("\u63D0\u4EA4");
		submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				storeMemo();
				dispose();
			}
		});
		submit.setFont(new Font("����", Font.PLAIN, 16));
		submit.setBounds(69, 245, 93, 23);
		contentPane.add(submit);

		JButton cancel = new JButton("\u53D6\u6D88");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		cancel.setFont(new Font("����", Font.PLAIN, 16));
		cancel.setBounds(235, 246, 93, 23);
		contentPane.add(cancel);

		String time = Utils.getTime();
		year.setText(time.substring(0, 4));
		mon.setText(time.substring(5, 7));
		day.setText(time.substring(8, 10));
		hour.setText(time.substring(12, 14));
		min.setText(time.substring(15, 17));

		JButton openmemorandum = new JButton("\u6253\u5F00\u5907\u5FD8\u5F55");
		openmemorandum.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Runtime.getRuntime()
							.exec(("explorer " + MainFrame.filePath + "/memorandum/memorandum.txt").replace('/', '\\'));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		openmemorandum.setFont(new Font("����", Font.PLAIN, 12));
		openmemorandum.setBounds(237, 62, 93, 23);
		contentPane.add(openmemorandum);

		setLocationRelativeTo(null);
		setAlwaysOnTop(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	public static void showMemorandum() {
		try {
			
			ArrayList<String> memorandums = MainFrame.ioUtils.getMemolist();
			for (String string : memorandums) {
		
				Memorandum memorandum = new Memorandum(string);
				System.out.println(Utils.getTime());
				System.out.println(memorandum.getTime());
				
				System.out.println(Utils.getTime().contains(memorandum.getTime()));
				System.out.println();
				if (Utils.getTime().contains(memorandum.getTime())) {
		
					MainFrame.showToast("[����¼]" + memorandum.getTime(), memorandum.getContent(), MessageType.INFO);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("memo err");
		}

	}

	public static void takeMemorandum(String time, String content) {
		if (!time.equals("") && !content.equals("")) {
			Memorandum memorandum = new Memorandum(time, content);
			ArrayList<String> arrayList = new ArrayList<String>();
			arrayList.add(memorandum.toString());
			try {
				Utils.writeObjectsToFile(arrayList, MainFrame.filePath + "\\memorandum\\memorandum.txt");
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	@Override
	public void run() {

		while (true) {
			while (PlanNotifier.iswatching) {

				try {
					showMemorandum();
					Thread.sleep(55000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(55000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
