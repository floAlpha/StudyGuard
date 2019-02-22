package com.yangheng.StudyGuard.GUI;

import java.awt.AWTEvent;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class IdeaInputFrame extends JFrame {


	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public IdeaInputFrame() {
		setTitle("\u901F\u8BB0");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 540, 400);
		contentPane = new JPanel();
		contentPane.setToolTipText("\u901F\u8BB0");
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
				}
			}
		}, AWTEvent.KEY_EVENT_MASK);
		
		JScrollPane scrollPane = new JScrollPane();
//		scrollPane.setViewportBorder(null);
		scrollPane.setBounds(10, 10, 504, 304);
		contentPane.add(scrollPane);
		
		JTextArea textArea = new JTextArea();
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		scrollPane.setViewportView(textArea);
		
		JButton submit = new JButton("\u4FDD\u5B58");

		submit.setFont(new Font("¿¬Ìå", Font.PLAIN, 16));
		submit.setBounds(150, 328, 93, 23);
		contentPane.add(submit);
		
		JButton cancel = new JButton("\u53D6\u6D88");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		cancel.setFont(new Font("¿¬Ìå", Font.PLAIN, 16));
		cancel.setBounds(281, 328, 93, 23);
		contentPane.add(cancel);
		
		submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				IdeaFrame.storeIdea(textArea.getText());
				dispose();
			}
		});
	}
}
