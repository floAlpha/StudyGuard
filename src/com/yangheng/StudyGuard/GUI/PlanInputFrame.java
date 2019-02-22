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
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.yangheng.StudyGuard.Object.StudyPlan;
import com.yangheng.StudyGuard.Utils.Utils;

public class PlanInputFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField hour;
	private JLabel label;
	private JTextField min;
	private JLabel label_1;
	private JLabel label_2;
	private JLabel label_3;
	private JLabel label_4;
	private JTextField task;
	private JTextField detail;
	private JTextField args;
	private JButton submit;
	private JButton cancel;
	private JTextField year;
	private JTextField mon;
	private JTextField day;

	private void planInput() {
		if (Integer.parseInt(hour.getText()) >= 0 && Integer.parseInt(min.getText()) >= 0
				&& !task.getText().equals(null) && !task.getText().equals("")) {
			// ArrayList<String> planList = new ArrayList<String>();
			StudyPlan studyPlan = new StudyPlan(
					"[time]" + hour.getText() + ":" + min.getText() + "[time] [task]" + task.getText()
							+ "[task] [detail]" + detail.getText() + "[detail] [args]" + args.getText() + "[args]");
			System.out.println("[time]" + hour.getText() + ":" + min.getText() + "[time] [task]" + task.getText()
					+ "[task] [detail]" + detail.getText() + "[detail] [args]" + args.getText()
					+ "[args] [finish][finish]");
			
			try {
				ArrayList<String> newplan = new ArrayList<String>();
				newplan.add(studyPlan.toString());
				Utils.writeObjectsToFile(newplan, MainGuardFrame.filePath + "\\plan\\" + year.getText() + "年"
						+ mon.getText() + "月" + day.getText() + "日" + ".txt");
			} catch (IOException e1) {
				MainGuardFrame.showToast("错误", "时间格式错误！", MessageType.ERROR);
				e1.printStackTrace();
			}
			dispose();
		}

	}

	/**
	 * Create the frame.
	 */
	public PlanInputFrame() {
		setTitle("每日计划录入(" + Utils.getTime().substring(0, 11) + ")");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 465, 366);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		final Toolkit toolkit = Toolkit.getDefaultToolkit();
		toolkit.addAWTEventListener(new AWTEventListener() {
			@Override
			public void eventDispatched(AWTEvent e) {
				if (e.getID() == KeyEvent.KEY_PRESSED) {
					KeyEvent evt = (KeyEvent) e;
					if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
						dispose();
					}
					if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
						planInput();
					}
				}
			}
		}, AWTEvent.KEY_EVENT_MASK);

		JLabel lblNewLabel = new JLabel("\u65F6\u95F4");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("楷体", Font.BOLD, 16));
		lblNewLabel.setBounds(21, 36, 56, 31);
		contentPane.add(lblNewLabel);

		hour = new JTextField();
		hour.setHorizontalAlignment(SwingConstants.CENTER);
		hour.setFont(new Font("楷体", Font.PLAIN, 16));
		hour.setBounds(288, 42, 29, 21);
		contentPane.add(hour);
		hour.setColumns(10);

		label = new JLabel("\u65F6");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("楷体", Font.PLAIN, 16));
		label.setBounds(319, 36, 29, 31);
		contentPane.add(label);

		min = new JTextField();
		min.setHorizontalAlignment(SwingConstants.CENTER);
		min.setFont(new Font("楷体", Font.PLAIN, 16));
		min.setColumns(10);
		min.setBounds(349, 42, 29, 21);
		contentPane.add(min);

		label_1 = new JLabel("\u5206");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setFont(new Font("楷体", Font.PLAIN, 16));
		label_1.setBounds(381, 36, 29, 31);
		contentPane.add(label_1);

		label_2 = new JLabel("\u540D\u79F0");
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		label_2.setFont(new Font("楷体", Font.BOLD, 16));
		label_2.setBounds(21, 94, 56, 31);
		contentPane.add(label_2);

		label_3 = new JLabel("\u5907\u6CE8");
		label_3.setHorizontalAlignment(SwingConstants.CENTER);
		label_3.setFont(new Font("楷体", Font.BOLD, 16));
		label_3.setBounds(21, 148, 56, 31);
		contentPane.add(label_3);

		label_4 = new JLabel("\u53C2\u6570");
		label_4.setHorizontalAlignment(SwingConstants.CENTER);
		label_4.setFont(new Font("楷体", Font.BOLD, 16));
		label_4.setBounds(21, 208, 56, 31);
		contentPane.add(label_4);

		task = new JTextField();
		task.setFont(new Font("楷体", Font.PLAIN, 16));
		task.setColumns(10);
		task.setBounds(87, 97, 323, 21);
		contentPane.add(task);

		detail = new JTextField();
		detail.setFont(new Font("楷体", Font.PLAIN, 16));
		detail.setColumns(10);
		detail.setBounds(87, 152, 323, 21);
		contentPane.add(detail);

		args = new JTextField();
		args.setFont(new Font("楷体", Font.PLAIN, 16));
		args.setColumns(10);
		args.setBounds(87, 212, 323, 21);
		contentPane.add(args);

		submit = new JButton("\u6DFB\u52A0");
		submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				planInput();
			}
		});
		submit.setFont(new Font("楷体", Font.PLAIN, 16));
		submit.setBounds(94, 267, 93, 23);
		contentPane.add(submit);

		cancel = new JButton("\u53D6\u6D88");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		cancel.setFont(new Font("楷体", Font.PLAIN, 16));
		cancel.setBounds(266, 267, 93, 23);
		contentPane.add(cancel);

		year = new JTextField();
		year.setHorizontalAlignment(SwingConstants.CENTER);
		year.setFont(new Font("楷体", Font.PLAIN, 16));
		year.setColumns(10);
		year.setBounds(87, 42, 45, 21);
		contentPane.add(year);

		mon = new JTextField();
		mon.setHorizontalAlignment(SwingConstants.CENTER);
		mon.setFont(new Font("楷体", Font.PLAIN, 16));
		mon.setColumns(10);
		mon.setBounds(160, 42, 29, 21);
		contentPane.add(mon);

		JLabel label_5 = new JLabel("\u5E74");
		label_5.setHorizontalAlignment(SwingConstants.CENTER);
		label_5.setFont(new Font("楷体", Font.PLAIN, 16));
		label_5.setBounds(133, 37, 29, 31);
		contentPane.add(label_5);

		day = new JTextField();
		day.setHorizontalAlignment(SwingConstants.CENTER);
		day.setFont(new Font("楷体", Font.PLAIN, 16));
		day.setColumns(10);
		day.setBounds(219, 42, 29, 21);
		contentPane.add(day);

		JLabel label_6 = new JLabel("\u6708");
		label_6.setHorizontalAlignment(SwingConstants.CENTER);
		label_6.setFont(new Font("楷体", Font.PLAIN, 16));
		label_6.setBounds(189, 37, 29, 31);
		contentPane.add(label_6);

		JLabel label_7 = new JLabel("\u65E5");
		label_7.setHorizontalAlignment(SwingConstants.CENTER);
		label_7.setFont(new Font("楷体", Font.PLAIN, 16));
		label_7.setBounds(249, 36, 29, 31);
		contentPane.add(label_7);

		String time = Utils.getTime();
		year.setText(time.substring(0, 4));
		mon.setText(time.substring(5, 7));
		day.setText(time.substring(8, 10));
		hour.setText(time.substring(12, 14));
		min.setText(time.substring(15, 17));

		hour.requestFocus();
	}
}
