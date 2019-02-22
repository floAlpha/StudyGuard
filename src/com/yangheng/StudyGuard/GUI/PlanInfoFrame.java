package com.yangheng.StudyGuard.GUI;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.yangheng.StudyGuard.Notifier;
import com.yangheng.StudyGuard.Object.StudyPlan;
import com.yangheng.StudyGuard.Utils.Utils;

public class PlanInfoFrame extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;
	public static String studyplanpath;

	public static boolean exist = false;

	private JPanel contentPane;

	ArrayList<JTextField> tasks = new ArrayList<JTextField>();
	ArrayList<JLabel> labels = new ArrayList<JLabel>();
	ArrayList<JLabel> labels_finish = new ArrayList<JLabel>();

	private JTextField task1;
	private JTextField task2;
	private JTextField task3;
	private JTextField task4;
	private JTextField task5;
	private JTextField task6;
	private JTextField task7;
	private JTextField task8;
	private JTextField task9;
	private JTextField task10;
	private JTextField task11;
	private JTextField task12;

	private void initTextFields() {
		tasks.add(task1);
		tasks.add(task2);
		tasks.add(task3);
		tasks.add(task4);
		tasks.add(task5);
		tasks.add(task6);
		tasks.add(task7);
		tasks.add(task8);
		tasks.add(task9);
		tasks.add(task10);
		tasks.add(task11);
		tasks.add(task12);
	}

	static PlanInfoFrame instance = null;

	// 实现阻止程序多次启动
	public static PlanInfoFrame getInstance() {

		if (instance == null) {
			synchronized (PlanInfoFrame.class) {
				if (instance == null) {
					instance = new PlanInfoFrame(new ArrayList<String>(Notifier.planlist));
					instance.setVisible(true);
					instance.setAlwaysOnTop(true);
				}
			}
		}
		return instance;
	}

	/**
	 * Create the frame.
	 */
	public PlanInfoFrame(ArrayList<String> planlist) {

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.setAlwaysOnTop(true);
		this.setLocationRelativeTo(null);

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

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					File file = new File(
							MainGuardFrame.filePath + "\\plan\\" + Utils.getTime().substring(0, 11) + ".txt");
					if (file.exists()) {
						file.delete();
					}
					Utils.writeObjectsToFile(planlist,
							MainGuardFrame.filePath + "\\plan\\" + Utils.getTime().substring(0, 11) + ".txt");
					exist = false;
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		if (exist) {
			this.dispose();
		} else {
			exist = true;
		}

		setTitle("\u5B66\u4E60\u8BA1\u5212");
		setBounds(100, 100, 525, 481);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel label = new JLabel("");
		label.setFont(new Font("楷体", Font.PLAIN, 16));
		label.setBounds(21, 22, 53, 24);
		contentPane.add(label);

		JLabel label_1 = new JLabel("");
		label_1.setFont(new Font("楷体", Font.PLAIN, 16));
		label_1.setBounds(21, 56, 53, 24);
		contentPane.add(label_1);

		JLabel label_2 = new JLabel("");
		label_2.setFont(new Font("楷体", Font.PLAIN, 16));
		label_2.setBounds(21, 90, 53, 24);
		contentPane.add(label_2);

		JLabel label_3 = new JLabel("");
		label_3.setFont(new Font("楷体", Font.PLAIN, 16));
		label_3.setBounds(21, 124, 53, 24);
		contentPane.add(label_3);

		JLabel label_4 = new JLabel("");
		label_4.setFont(new Font("楷体", Font.PLAIN, 16));
		label_4.setBounds(21, 158, 53, 24);
		contentPane.add(label_4);

		JLabel label_5 = new JLabel("");
		label_5.setFont(new Font("楷体", Font.PLAIN, 16));
		label_5.setBounds(21, 192, 53, 24);
		contentPane.add(label_5);

		JLabel label_6 = new JLabel("");
		label_6.setFont(new Font("楷体", Font.PLAIN, 16));
		label_6.setBounds(21, 226, 53, 24);
		contentPane.add(label_6);

		JLabel label_7 = new JLabel("");
		label_7.setFont(new Font("楷体", Font.PLAIN, 16));
		label_7.setBounds(21, 260, 53, 24);
		contentPane.add(label_7);

		JLabel label_8 = new JLabel("");
		label_8.setFont(new Font("楷体", Font.PLAIN, 16));
		label_8.setBounds(21, 294, 53, 24);
		contentPane.add(label_8);

		JLabel label_9 = new JLabel("");
		label_9.setFont(new Font("楷体", Font.PLAIN, 16));
		label_9.setBounds(21, 328, 53, 24);
		contentPane.add(label_9);

		JLabel label_10 = new JLabel("");
		label_10.setFont(new Font("楷体", Font.PLAIN, 16));
		label_10.setBounds(21, 362, 53, 24);
		contentPane.add(label_10);

		JLabel label_11 = new JLabel("");
		label_11.setFont(new Font("楷体", Font.PLAIN, 16));
		label_11.setBounds(21, 396, 53, 24);
		contentPane.add(label_11);

		task1 = new JTextField();
		task1.setFont(new Font("楷体", Font.PLAIN, 16));
		task1.setBounds(84, 24, 341, 21);
		contentPane.add(task1);
		task1.setColumns(10);

		task2 = new JTextField();
		task2.setFont(new Font("楷体", Font.PLAIN, 16));
		task2.setColumns(10);
		task2.setBounds(84, 59, 341, 21);
		contentPane.add(task2);

		task3 = new JTextField();
		task3.setFont(new Font("楷体", Font.PLAIN, 16));
		task3.setColumns(10);
		task3.setBounds(84, 93, 341, 21);
		contentPane.add(task3);

		task4 = new JTextField();
		task4.setFont(new Font("楷体", Font.PLAIN, 16));
		task4.setColumns(10);
		task4.setBounds(84, 127, 341, 21);
		contentPane.add(task4);

		task5 = new JTextField();
		task5.setFont(new Font("楷体", Font.PLAIN, 16));
		task5.setColumns(10);
		task5.setBounds(84, 161, 341, 21);
		contentPane.add(task5);

		task6 = new JTextField();
		task6.setFont(new Font("楷体", Font.PLAIN, 16));
		task6.setColumns(10);
		task6.setBounds(84, 195, 341, 21);
		contentPane.add(task6);

		task7 = new JTextField();
		task7.setFont(new Font("楷体", Font.PLAIN, 16));
		task7.setColumns(10);
		task7.setBounds(84, 229, 341, 21);
		contentPane.add(task7);

		task8 = new JTextField();
		task8.setFont(new Font("楷体", Font.PLAIN, 16));
		task8.setColumns(10);
		task8.setBounds(84, 263, 341, 21);
		contentPane.add(task8);

		task9 = new JTextField();
		task9.setFont(new Font("楷体", Font.PLAIN, 16));
		task9.setColumns(10);
		task9.setBounds(84, 297, 341, 21);
		contentPane.add(task9);

		task10 = new JTextField();
		task10.setFont(new Font("楷体", Font.PLAIN, 16));
		task10.setColumns(10);
		task10.setBounds(84, 331, 341, 21);
		contentPane.add(task10);

		task11 = new JTextField();
		task11.setFont(new Font("楷体", Font.PLAIN, 16));
		task11.setColumns(10);
		task11.setBounds(84, 365, 341, 21);
		contentPane.add(task11);

		task12 = new JTextField();
		task12.setFont(new Font("楷体", Font.PLAIN, 16));
		task12.setColumns(10);
		task12.setBounds(84, 399, 341, 21);
		contentPane.add(task12);

		JLabel l1 = new JLabel();
		l1.setHorizontalAlignment(SwingConstants.CENTER);
		l1.setFont(new Font("楷体", Font.PLAIN, 16));
		l1.setBounds(433, 25, 72, 18);
		contentPane.add(l1);

		JLabel l2 = new JLabel();
		l2.setHorizontalAlignment(SwingConstants.CENTER);
		l2.setFont(new Font("楷体", Font.PLAIN, 16));
		l2.setBounds(433, 60, 72, 18);
		contentPane.add(l2);

		JLabel l3 = new JLabel();
		l3.setHorizontalAlignment(SwingConstants.CENTER);
		l3.setFont(new Font("楷体", Font.PLAIN, 16));
		l3.setBounds(433, 94, 72, 18);
		contentPane.add(l3);

		JLabel l4 = new JLabel();
		l4.setHorizontalAlignment(SwingConstants.CENTER);
		l4.setFont(new Font("楷体", Font.PLAIN, 16));
		l4.setBounds(433, 128, 72, 18);
		contentPane.add(l4);

		JLabel l5 = new JLabel();
		l5.setHorizontalAlignment(SwingConstants.CENTER);
		l5.setFont(new Font("楷体", Font.PLAIN, 16));
		l5.setBounds(433, 162, 72, 18);
		contentPane.add(l5);

		JLabel l6 = new JLabel();
		l6.setHorizontalAlignment(SwingConstants.CENTER);
		l6.setFont(new Font("楷体", Font.PLAIN, 16));
		l6.setBounds(433, 197, 72, 18);
		contentPane.add(l6);

		JLabel l7 = new JLabel();
		l7.setHorizontalAlignment(SwingConstants.CENTER);
		l7.setFont(new Font("楷体", Font.PLAIN, 16));
		l7.setBounds(433, 231, 72, 18);
		contentPane.add(l7);

		JLabel l8 = new JLabel();
		l8.setHorizontalAlignment(SwingConstants.CENTER);
		l8.setFont(new Font("楷体", Font.PLAIN, 16));
		l8.setBounds(433, 265, 72, 18);
		contentPane.add(l8);

		JLabel l9 = new JLabel();
		l9.setHorizontalAlignment(SwingConstants.CENTER);
		l9.setFont(new Font("楷体", Font.PLAIN, 16));
		l9.setBounds(433, 299, 72, 18);
		contentPane.add(l9);

		JLabel l10 = new JLabel();
		l10.setHorizontalAlignment(SwingConstants.CENTER);
		l10.setFont(new Font("楷体", Font.PLAIN, 16));
		l10.setBounds(433, 332, 72, 18);
		contentPane.add(l10);

		JLabel l11 = new JLabel();
		l11.setHorizontalAlignment(SwingConstants.CENTER);
		l11.setFont(new Font("楷体", Font.PLAIN, 16));
		l11.setBounds(433, 367, 72, 18);
		contentPane.add(l11);

		JLabel l12 = new JLabel();
		l12.setHorizontalAlignment(SwingConstants.CENTER);
		l12.setFont(new Font("楷体", Font.PLAIN, 16));
		l12.setBounds(433, 401, 72, 18);
		contentPane.add(l12);

		initTextFields();
		labels.add(label);
		labels.add(label_1);
		labels.add(label_2);
		labels.add(label_3);
		labels.add(label_4);
		labels.add(label_5);
		labels.add(label_6);
		labels.add(label_7);
		labels.add(label_8);
		labels.add(label_9);
		labels.add(label_10);
		labels.add(label_11);
		labels_finish.add(l1);
		labels_finish.add(l2);
		labels_finish.add(l3);
		labels_finish.add(l4);
		labels_finish.add(l5);
		labels_finish.add(l6);
		labels_finish.add(l7);
		labels_finish.add(l8);
		labels_finish.add(l9);
		labels_finish.add(l10);
		labels_finish.add(l11);
		labels_finish.add(l12);

	}

	@Override
	public void run() {
		
		while (true) {
			
			for (int i = 0; i < Notifier.planlist.size(); i++) {
				StudyPlan sp = new StudyPlan(Notifier.planlist.get(i));
				tasks.get(i).setText(sp.getTask());
				labels.get(i).setText(sp.getTime());
				labels_finish.get(i).setText(sp.getFinish());
				if (labels_finish.get(i).getText().equals("完成")) {
					labels_finish.get(i).setForeground(Color.GREEN);
				} else if (labels_finish.get(i).getText().equals("未完成")) {
					labels_finish.get(i).setForeground(Color.RED);
				} else {
					labels_finish.get(i).setForeground(Color.GRAY);
				}
			}

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

	}
}
