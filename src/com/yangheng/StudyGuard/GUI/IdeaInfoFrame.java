package com.yangheng.StudyGuard.GUI;

import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
	private static JTextArea textArea;
	public static IdeaInfoFrame instance = null; // 实现阻止程序多次启动
	private JScrollPane scrollPane_1;
	static public Idea idea;
	
	public static IdeaInfoFrame getInstance(Idea idea) {
		IdeaInfoFrame.idea=idea;
		if (instance == null) {
			synchronized (IdeaInfoFrame.class) {
				if (instance == null) {
					if (!(IdeaInfoFrame.idea == null)) {
						instance = new IdeaInfoFrame();
						IdeaInfoFrame.idea = idea;
					}
				}
			}
		}
		return instance;
	}

	private static void updateIdea(Idea idea) {
		ArrayList<String> ideas = MainFrame.ioUtils.getIdeaslist();

		for (int i = 0; i < ideas.size(); i++)

		{
			if (Utils.getValueOfElementByTag(ideas.get(i), "[content]").equals(idea.getContent())) {
				idea.setContent(textArea.getText().replace("\n", "hh"));
				ideas.set(ideas.indexOf(ideas.get(i)), idea.toString());

			}
		}

	}
	
	public static void disableDisplay(String content) {
		
		for (int i = 0; i < MainFrame.ioUtils.getIdeaslist().size(); i++) {
			if (Utils.getValueOfElementByTag(MainFrame.ioUtils.getIdeaslist().get(i), "[content]").equals(content)) {
				
				MainFrame.ioUtils.getIdeaslist().set(i, MainFrame.ioUtils.getIdeaslist().get(i).replace("DISPLAY", "NODISPLAY"));
				// ideas.remove(ideas.get(i));
				// ideas.add(ideas.get(i).replace("DISPLAY", "NODISPLAY"));
			}
		}
//		updateIdea(idea);
	}

	private IdeaInfoFrame() {
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 555, 420);
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

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {

				updateIdea(idea);
				dispose();
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
		contentPane.setLayout(null);
		contentPane.add(scrollPane_1);

		JButton btnNewButton = new JButton("\u4E0D\u518D\u663E\u793A\u6B64Idea");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				disableDisplay(idea.getContent());
				updateIdea(new Idea(idea.toString().replace("DISPLAY", "NODISPLAY")));
				dispose();
			}
		});
		btnNewButton.setFont(new Font("楷体", Font.PLAIN, 16));
		btnNewButton.setBounds(188, 342, 148, 23);
		contentPane.add(btnNewButton);
		// invalidate();
	}

	public void updateView(Idea idea) {
		IdeaInfoFrame.idea=idea;
		setTitle("速记回顾 " + idea.getTime() + " 任何修改都会被保存");
		textArea.setText(idea.getContent().replace("hh", "\n"));
//		content = idea.getContent();
		invalidate();
	}

}
