package com.yangheng.StudyGuard.GUI;

import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.yangheng.StudyGuard.Object.StudyPlan;
import com.yangheng.StudyGuard.Utils.IOUtils;
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
			IOUtils.planlist.add(studyPlan.toString());

			IOUtils.writeStudyPlan();
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
//					if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
//						planInput();
//						dispose();
//					}
				}
			}
		}, AWTEvent.KEY_EVENT_MASK);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("\u65E5\u671F");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("楷体", Font.BOLD, 16));
		lblNewLabel.setBounds(21, 25, 34, 19);
		contentPane.add(lblNewLabel);

		hour = new JTextField();
		hour.setHorizontalAlignment(SwingConstants.CENTER);
		hour.setFont(new Font("楷体", Font.PLAIN, 16));
		hour.setBounds(70, 69, 86, 25);
		contentPane.add(hour);
		hour.setColumns(10);

		label = new JLabel("\u65F6");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("楷体", Font.PLAIN, 16));
		label.setBounds(160, 72, 16, 19);
		contentPane.add(label);

		min = new JTextField();
		min.setHorizontalAlignment(SwingConstants.CENTER);
		min.setFont(new Font("楷体", Font.PLAIN, 16));
		min.setColumns(10);
		min.setBounds(178, 69, 86, 25);
		contentPane.add(min);

		label_1 = new JLabel("\u5206");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setFont(new Font("楷体", Font.PLAIN, 16));
		label_1.setBounds(266, 72, 16, 19);
		contentPane.add(label_1);

		label_2 = new JLabel("\u540D\u79F0");
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		label_2.setFont(new Font("楷体", Font.BOLD, 16));
		label_2.setBounds(21, 121, 34, 19);
		contentPane.add(label_2);

		label_3 = new JLabel("\u5907\u6CE8");
		label_3.setHorizontalAlignment(SwingConstants.CENTER);
		label_3.setFont(new Font("楷体", Font.BOLD, 16));
		label_3.setBounds(21, 224, 34, 19);
		contentPane.add(label_3);

		label_4 = new JLabel("\u53C2\u6570");
		label_4.setToolTipText("");
		label_4.setHorizontalAlignment(SwingConstants.CENTER);
		label_4.setFont(new Font("楷体", Font.BOLD, 16));
		label_4.setBounds(21, 172, 34, 19);
		contentPane.add(label_4);

		task = new JTextField();
		task.setFont(new Font("楷体", Font.PLAIN, 16));
		task.setColumns(10);
		task.setBounds(70, 118, 321, 25);
		contentPane.add(task);

		detail = new JTextField();
		detail.setFont(new Font("楷体", Font.PLAIN, 16));
		detail.setColumns(10);
		detail.setBounds(70, 221, 321, 25);
		contentPane.add(detail);

		args = new JTextField();
		args.setToolTipText(
				"\u4EFB\u52A1\u9700\u8981\u6D4F\u89C8\u7684\u7F51\u5740/\u6587\u4EF6\uFF0C\u548C\u5176\u4ED6EXE\uFF0Cbat\u53EF\u6267\u884C\u6587\u4EF6\u7B49\u7B49");
		args.setFont(new Font("楷体", Font.PLAIN, 16));
		args.setColumns(10);
		args.setBounds(70, 169, 321, 25);
		contentPane.add(args);

		submit = new JButton("\u6DFB\u52A0");
		submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				planInput();
				dispose();
			}
		});
		submit.setFont(new Font("楷体", Font.PLAIN, 16));
		submit.setBounds(94, 275, 103, 27);
		contentPane.add(submit);

		cancel = new JButton("\u53D6\u6D88");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		cancel.setFont(new Font("楷体", Font.PLAIN, 16));
		cancel.setBounds(262, 275, 93, 27);
		contentPane.add(cancel);

		year = new JTextField();
		year.setHorizontalAlignment(SwingConstants.CENTER);
		year.setFont(new Font("楷体", Font.PLAIN, 16));
		year.setColumns(10);
		year.setBounds(71, 22, 86, 25);
		contentPane.add(year);

		mon = new JTextField();
		mon.setHorizontalAlignment(SwingConstants.CENTER);
		mon.setFont(new Font("楷体", Font.PLAIN, 16));
		mon.setColumns(10);
		mon.setBounds(177, 22, 86, 25);
		contentPane.add(mon);

		JLabel label_5 = new JLabel("\u5E74");
		label_5.setHorizontalAlignment(SwingConstants.CENTER);
		label_5.setFont(new Font("楷体", Font.PLAIN, 16));
		label_5.setBounds(159, 25, 16, 19);
		contentPane.add(label_5);

		day = new JTextField();
		day.setHorizontalAlignment(SwingConstants.CENTER);
		day.setFont(new Font("楷体", Font.PLAIN, 16));
		day.setColumns(10);
		day.setBounds(279, 21, 86, 25);
		contentPane.add(day);

		JLabel label_6 = new JLabel("\u6708");
		label_6.setHorizontalAlignment(SwingConstants.CENTER);
		label_6.setFont(new Font("楷体", Font.PLAIN, 16));
		label_6.setBounds(262, 25, 16, 19);
		contentPane.add(label_6);

		JLabel label_7 = new JLabel("\u65E5");
		label_7.setHorizontalAlignment(SwingConstants.CENTER);
		label_7.setFont(new Font("楷体", Font.PLAIN, 16));
		label_7.setBounds(375, 24, 16, 19);
		contentPane.add(label_7);

		String time = Utils.getTime();
		year.setText(time.substring(0, 4));
		mon.setText(time.substring(5, 7));
		day.setText(time.substring(8, 10));
		hour.setText(time.substring(12, 14));
		min.setText(time.substring(15, 17));

		JLabel label_8 = new JLabel("\u65F6\u95F4");
		label_8.setHorizontalAlignment(SwingConstants.CENTER);
		label_8.setFont(new Font("楷体", Font.BOLD, 16));
		label_8.setBounds(21, 72, 34, 19);
		contentPane.add(label_8);

		new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, new DropTargetAdapter() {
			@Override
			public void drop(DropTargetDropEvent dtde) {
				try {
					dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
					@SuppressWarnings("unchecked")
					List<File> list = (List<File>) (dtde.getTransferable()
							.getTransferData(DataFlavor.javaFileListFlavor));
					args.setText("");
					for (File file : list) {
						args.setText(file.getAbsolutePath());
					}
					dtde.dropComplete(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		hour.requestFocus();
	}
}
