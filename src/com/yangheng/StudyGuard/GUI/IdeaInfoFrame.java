package com.yangheng.StudyGuard.GUI;

import java.awt.AWTEvent;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.TrayIcon.MessageType;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import com.yangheng.StudyGuard.Object.Idea;
import com.yangheng.StudyGuard.Utils.Utils;

public class IdeaInfoFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextArea textArea;
	public static IdeaInfoFrame instance = null; // 实现阻止程序多次启动
	private JScrollPane scrollPane_1;

	public static IdeaInfoFrame getInstance(Idea idea) {

		if (instance == null) {
			synchronized (IdeaInfoFrame.class) {
				if (instance == null) {
					if (!(idea == null)) {
						instance = new IdeaInfoFrame(idea);
					}
				}
			}
		}
		return instance;
	}

	private IdeaInfoFrame(Idea idea) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 555, 420);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				ArrayList<String> ideas = Utils
						.readTxtFileIntoStringArrList(MainGuardFrame.filePath + "\\idea\\idea.txt");
				for (int i = 0; i < ideas.size(); i++) {
					if (Utils.getValueOfElementByTag(ideas.get(i), "[content]").equals(idea.getContent())) {
						idea.setContent(textArea.getText());
						ideas.set(ideas.indexOf(ideas.get(i)), idea.toString());
						try {
							File file = new File(MainGuardFrame.filePath + "\\idea\\idea.txt");
							if (file.exists()) {
								file.delete();
							}
							Utils.writeObjectsToFile(ideas, MainGuardFrame.filePath + "\\idea\\idea.txt");
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						dispose();
					}
				}

			}
		});

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

		setResizable(false);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(45, 108, 303, 111);

		textArea = new JTextArea();
		// textArea.setBounds(45, 108, 303, 111);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setFont(new Font("楷体", Font.PLAIN, 16));
		scrollPane_1 = new JScrollPane(textArea);
		scrollPane_1.setBounds(24, 10, 500, 308);
		textArea.setColumns(10);
		textArea.setText(idea.getContent());
		// setTitle("Idea - <dynamic>
		// (\u6240\u505A\u7684\u4FEE\u6539\u5C06\u4F1A\u88AB\u4FDD\u5B58)");
		contentPane.setLayout(null);
		contentPane.add(scrollPane_1);

		JButton btnNewButton = new JButton("\u4E0D\u518D\u663E\u793A\u6B64Idea");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				ArrayList<String> newidea = new ArrayList<String>();
				idea.setTag("NODISPLAY");
				// System.out.println(IdeaFrame.ideas);
				for (Idea i : IdeaFrame.ideas) {
					if (idea.getTime().equals(i.getTime())) {
						i.setTag(idea.getTag());
					}
					newidea.add(i.toString());
				}

				try {
					File file = new File(MainGuardFrame.filePath + "\\idea\\idea.txt");
					if (file.exists()) {
						file.delete();
					}
					Utils.writeObjectsToFile(newidea, MainGuardFrame.filePath + "\\idea\\idea.txt");
					dispose();
				} catch (IOException e1) {
					MainGuardFrame.showToast("错误", "标记不再显示出错", MessageType.ERROR);
					e1.printStackTrace();
				}
			}
		});
		btnNewButton.setFont(new Font("楷体", Font.PLAIN, 16));
		btnNewButton.setBounds(188, 342, 148, 23);
		contentPane.add(btnNewButton);
		invalidate();
	}

	public void updateView(Idea idea) {
		textArea.setText(idea.getContent());
		invalidate();
	}



}
