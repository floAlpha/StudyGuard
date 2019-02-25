package com.yangheng.StudyGuard.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.TrayIcon.MessageType;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class IdeaFloatFrame extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel label;

	public String content;

	public static IdeaFloatFrame instance;
	JTextArea tip = new JTextArea();

	// ʵ����ֹ����������
	public static IdeaFloatFrame getInstance() {

		if (instance == null) {
			synchronized (IdeaFloatFrame.class) {
				if (instance == null) {
					instance = new IdeaFloatFrame();
				}
			}
		} else {
			//instance = new IdeaFloatFrame();
			return instance;
		}
		return instance;
	}

	static int mouseAtX;
	static int mouseAtY;

	private IdeaFloatFrame() {

		setResizable(false);
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension sc = kit.getScreenSize();
		if (sc.width>=1920) {
			setSize(sc.width / 3, sc.height / 2);
		}else {
			setSize(sc.width / 4, sc.height / 2);
		}
		
		setLocation(sc.width / 3 * 2, sc.height / 9);

		setUndecorated(true); // ����ȥ�߿�
		setAlwaysOnTop(true); // ���ô���������ǰ
		setBackground(new Color(0, 0, 0, 0)); // ���ô��ڱ���Ϊ͸��ɫ
		setType(JFrame.Type.UTILITY);// ����������ͼ��
		addMouseListener(new MouseAdapter() // ���ô��ڿ��϶�
		{
			public void mousePressed(MouseEvent e) {
				mouseAtX = e.getPoint().x;
				mouseAtY = e.getPoint().y;
			}

		});
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				setLocation((e.getXOnScreen() - mouseAtX), (e.getYOnScreen() - mouseAtY));// ������ק�󣬴��ڵ�λ��
			}
		});

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		contentPane.setLayout(null);
		contentPane.setOpaque(false);
		setContentPane(contentPane);
		tip.setEnabled(false);

		tip.setFont(new Font("����", Font.BOLD, 18));
		tip.setOpaque(false);
		tip.setWrapStyleWord(true);
		tip.setLineWrap(true);
		tip.setText("");
		tip.setEditable(false);
		tip.setForeground(Color.green);

		tip.addMouseListener(new MouseAdapter() // ���ô��ڿ��϶�
		{
			public void mousePressed(MouseEvent e) {
				mouseAtX = e.getPoint().x;
				mouseAtY = e.getPoint().y;
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				tip.setEnabled(true);
				tip.setForeground(Color.green);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				tip.setEnabled(false);
				tip.setDisabledTextColor(Color.LIGHT_GRAY);
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				tip.setEnabled(false);
				tip.setForeground(Color.green);
			}

			long end = new Date().getTime();

			@Override
			public void mouseClicked(MouseEvent e) {
				if (new Date().getTime() - end > 500) {
					end = new Date().getTime();
				} else {
					if (new Date().getTime() - end < 500) {
						IdeaInfoFrame.disableDisplay(content);
						MainFrame.showToast("��ʾ", "�����ø��ټǲ�����ʾ", MessageType.INFO);
					}
					end = new Date().getTime();
				}
			}
		});
		tip.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				setLocation((e.getXOnScreen() - mouseAtX), (e.getYOnScreen() - mouseAtY));// ������ק�󣬴��ڵ�λ��
			}
		});
		contentPane.addMouseListener(new MouseAdapter() // ���ô��ڿ��϶�
		{
			public void mousePressed(MouseEvent e) {
				mouseAtX = e.getPoint().x;
				mouseAtY = e.getPoint().y;
			}
		});
		contentPane.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				setLocation((e.getXOnScreen() - mouseAtX), (e.getYOnScreen() - mouseAtY));// ������ק�󣬴��ڵ�λ��
			}
		});
		tip.setBounds(0, 0, (int) getSize().getWidth(), (int) getSize().getWidth());

		contentPane.add(tip);

	}

	/**
	 * �ı�pngͼƬ��͸����
	 * 
	 * @param srcImageFile
	 *            Դͼ���ַ
	 * @param descImageDir
	 *            ���ͼƬ��·��������
	 * @param alpha
	 *            ���ͼƬ��͸����1-10
	 */
	private static BufferedImage transparentImage(String srcImageFile, int alpha) {

		try {
			// ��ȡͼƬ
			FileInputStream stream = new FileInputStream(new File(srcImageFile));// ָ��Ҫ��ȡ��ͼƬ
			// ����һ���ֽ����������������ת������
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			byte[] data = new byte[1024];// ����һ��1K��С������
			while (stream.read(data) != -1) {
				os.write(data);
			}
			ImageIcon imageIcon = new ImageIcon(os.toByteArray());
			BufferedImage bufferedImage = new BufferedImage(imageIcon.getIconWidth(), imageIcon.getIconHeight(),
					BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D g2D = (Graphics2D) bufferedImage.getGraphics();
			g2D.drawImage(imageIcon.getImage(), 0, 0, imageIcon.getImageObserver());

			// �ж�͸�����Ƿ�Խ��
			if (alpha < 0) {
				alpha = 0;
			} else if (alpha > 10) {
				alpha = 10;
			}
			// ѭ��ÿһ�����ص㣬�ı����ص��Alphaֵ
			for (int j1 = bufferedImage.getMinY(); j1 < bufferedImage.getHeight(); j1++) {
				for (int j2 = bufferedImage.getMinX(); j2 < bufferedImage.getWidth(); j2++) {
					int rgb = bufferedImage.getRGB(j2, j1);
					rgb = ((alpha * 255 / 10) << 24) | (rgb & 0x00ffffff);
					bufferedImage.setRGB(j2, j1, rgb);
				}
			}
			g2D.drawImage(bufferedImage, 0, 0, imageIcon.getImageObserver());
			stream.close();
			return bufferedImage;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// public void resizeWindow(int sizelevel) {
	// if (sizelevel == 1) {
	// Toolkit kit = Toolkit.getDefaultToolkit();
	// Dimension sc = kit.getScreenSize();
	// setSize(sc.width / 4, sc.height / 2);
	// setLocation(sc.width / 3 * 2, sc.height / 9);
	// }
	// if (sizelevel == 2) {
	// Toolkit kit = Toolkit.getDefaultToolkit();
	// Dimension sc = kit.getScreenSize();
	// setSize(sc.width / 3, sc.height / 2);
	// setLocation(sc.width / 3, sc.height / 9);
	//
	// }
	// if (sizelevel == 3) {
	// Toolkit kit = Toolkit.getDefaultToolkit();
	// Dimension sc = kit.getScreenSize();
	// setSize(sc.width / 2, sc.height / 2);
	// setLocation(sc.width / 2, sc.height / 9);
	//
	// }
	//
	// }

	public void updateTip(String s) {

		try {
			content = s;
			File file = new File(s);

			if (file.exists()) {

				tip.setVisible(false);
				try {
					if (label != null) {

					}
					remove(label);
				} catch (Exception e) {

					e.printStackTrace();
				}
				label = new JLabel() {
					private static final long serialVersionUID = 1L;

					public void paintComponent(Graphics g) {
						Image img = transparentImage(s, 3);
						g.drawImage(img, 0, 0, this.getSize().width, this.getSize().height, this);
					}
				};
				label.addMouseListener(new MouseAdapter() // ���ô��ڿ��϶�
				{
					public void mousePressed(MouseEvent e) {
						mouseAtX = e.getPoint().x;
						mouseAtY = e.getPoint().y;
					}

				});
				label.addMouseMotionListener(new MouseMotionAdapter() {
					public void mouseDragged(MouseEvent e) {
						setLocation((e.getXOnScreen() - mouseAtX), (e.getYOnScreen() - mouseAtY));// ������ק�󣬴��ڵ�λ��
					}

				});

				label.addMouseListener(new MouseAdapter() // ���ô��ڿ��϶�
				{
					public void mousePressed(MouseEvent e) {
						mouseAtX = e.getPoint().x;
						mouseAtY = e.getPoint().y;
					}

					long end = new Date().getTime();

					@Override
					public void mouseClicked(MouseEvent e) {
						if (new Date().getTime() - end > 500) {
							end = new Date().getTime();
							try {
								Runtime.getRuntime().exec("explorer " + content);
							} catch (IOException e1) {

								e1.printStackTrace();
							}
						} else {

							if (new Date().getTime() - end < 500) {
								File file = new File(content);
								if (file.exists()) {

									content = content.replace("DISPLAY", "NODISPLAY");
									file.renameTo(new File(content));
								}
								MainFrame.showToast("��ʾ", "�����ø��ټǲ�����ʾ", MessageType.INFO);
							}
							end = new Date().getTime();
						}
					}
				});
				label.setHorizontalAlignment(SwingConstants.CENTER);
				label.setBounds(0, 0, (int) getSize().getWidth(), (int) getSize().getWidth() * 2 / 3);

				contentPane.add(label);

				invalidate();
			} else {
				try {
					if (label != null) {
						remove(label);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				tip.setText(s.replace("[����]", "\n"));
				tip.setVisible(true);
				invalidate();
			}

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	@Override
	public void run() {
//		while (true) {
//
//			try {
//
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}

	}
}
