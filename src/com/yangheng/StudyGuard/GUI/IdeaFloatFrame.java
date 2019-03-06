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
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.yangheng.StudyGuard.Utils.FileUtils;

public class IdeaFloatFrame extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel label;

	public String content;

	public static IdeaFloatFrame instance;
	JTextArea tip = new JTextArea();

	// 实现阻止程序多次启动
	public static IdeaFloatFrame getInstance() {

		if (instance == null) {
			synchronized (IdeaFloatFrame.class) {
				if (instance == null) {
					instance = new IdeaFloatFrame();
				}
			}
		} else {

			return instance;
		}
		instance.setVisible(true);
		return instance;
	}

	static int mouseAtX;
	static int mouseAtY;

	private IdeaFloatFrame() {

		setResizable(false);
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension sc = kit.getScreenSize();
		setSize(sc.width / 4, sc.height - 100);

		setLocation(sc.width / 3 * 2, sc.height / 9);

		setUndecorated(true); // 窗口去边框
		setAlwaysOnTop(true); // 设置窗口总在最前
		setBackground(new Color(0, 0, 0, 0)); // 设置窗口背景为透明色
		setType(JFrame.Type.UTILITY);// 隐藏任务栏图标
		addMouseListener(new MouseAdapter() // 设置窗口可拖动
		{
			public void mousePressed(MouseEvent e) {
				mouseAtX = e.getPoint().x;
				mouseAtY = e.getPoint().y;
			}

		});
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				setLocation((e.getXOnScreen() - mouseAtX), (e.getYOnScreen() - mouseAtY));// 设置拖拽后，窗口的位置
			}
		});

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		contentPane.setLayout(null);
		contentPane.setOpaque(false);
		setContentPane(contentPane);
		tip.setEnabled(false);

		tip.setFont(new Font("等线", Font.BOLD, 18));
		tip.setOpaque(false);
		tip.setWrapStyleWord(true);
		tip.setLineWrap(true);
		tip.setText("");
		tip.setEditable(false);
		tip.setDisabledTextColor(Color.DARK_GRAY);
		

		tip.addMouseListener(new MouseAdapter() // 设置窗口可拖动
		{
			public void mousePressed(MouseEvent e) {
				mouseAtX = e.getPoint().x;
				mouseAtY = e.getPoint().y;
			}

//			@Override
//			public void mouseEntered(MouseEvent e) {
//				tip.setEnabled(true);
//				tip.setForeground(Color.green);
//			}
//
//			@Override
//			public void mouseExited(MouseEvent e) {
//				tip.setEnabled(false);
//				tip.setDisabledTextColor(Color.LIGHT_GRAY);
//			}

			@Override
			public void mouseDragged(MouseEvent e) {
				tip.setEnabled(false);
				tip.setForeground(Color.green);
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {

					IdeaInfoFrame.disableDisplay(content);
					MainFrame.showToast("提示", "已设置该速记不再显示", MessageType.INFO);

				}
				if (e.getButton() == MouseEvent.BUTTON1) {
					IdeaInfoFrame ideaInfoFrame = IdeaInfoFrame.getInstance(IdeaFrame.currentmind);
					ideaInfoFrame.updateView(IdeaFrame.currentmind);
					if (ideaInfoFrame != null) {
						ideaInfoFrame.setAlwaysOnTop(true);
						ideaInfoFrame.setVisible(true);
					}
				}

			}
		});
		tip.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				setLocation((e.getXOnScreen() - mouseAtX), (e.getYOnScreen() - mouseAtY));// 设置拖拽后，窗口的位置
			}
		});
		contentPane.addMouseListener(new MouseAdapter() // 设置窗口可拖动
		{
			public void mousePressed(MouseEvent e) {
				mouseAtX = e.getPoint().x;
				mouseAtY = e.getPoint().y;
			}
		});
		contentPane.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				setLocation((e.getXOnScreen() - mouseAtX), (e.getYOnScreen() - mouseAtY));// 设置拖拽后，窗口的位置
			}
		});
		tip.setBounds(0, 0, (int) getSize().getWidth(), (int) getSize().getWidth());
		
		contentPane.add(tip);
		new Thread(this).start();

	}

	/**
	 * 改变png图片的透明度
	 * 
	 * @param srcImageFile
	 *            源图像地址
	 * @param descImageDir
	 *            输出图片的路径和名称
	 * @param alpha
	 *            输出图片的透明度1-10
	 */
	private static BufferedImage transparentImage(String srcImageFile, int alpha) {

		try {
			// 读取图片
			FileInputStream stream = new FileInputStream(new File(srcImageFile));// 指定要读取的图片
			// 定义一个字节数组输出流，用于转换数组
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			byte[] data = new byte[1024];// 定义一个1K大小的数组
			while (stream.read(data) != -1) {
				os.write(data);
			}
			ImageIcon imageIcon = new ImageIcon(os.toByteArray());
			BufferedImage bufferedImage = new BufferedImage(imageIcon.getIconWidth(), imageIcon.getIconHeight(),
					BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D g2D = (Graphics2D) bufferedImage.getGraphics();
			g2D.drawImage(imageIcon.getImage(), 0, 0, imageIcon.getImageObserver());
			g2D.dispose();
			// 判读透明度是否越界
			if (alpha < 0) {
				alpha = 0;
			} else if (alpha > 10) {
				alpha = 10;
			}
			// 循环每一个像素点，改变像素点的Alpha值
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

	public void updateTip(final String s) {
		// System.out.println(Utils.getTime()+s);
		getContentPane().setVisible(false);
		try {
			content = s;
			File file = new File(s);
			if (file.exists()) {
				tip.setText(null);
				tip.setVisible(false);
				try {
					MouseListener[] ml = label.getMouseListeners();
					for (int i = 0; i < ml.length; i++) {
						label.removeMouseListener(ml[i]);
					}
					MouseMotionListener[] mml = label.getMouseMotionListeners();
					for (int i = 0; i < mml.length; i++) {
						label.removeMouseMotionListener(mml[i]);
					}
					getContentPane().remove(label);
				} catch (Exception e) {
				}
				label = new JLabel() {
					private static final long serialVersionUID = 1L;

					public void paintComponent(Graphics g) {
						Image img = transparentImage(s, 7);
						g.drawImage(img, 0, 0, this.getSize().width, this.getSize().height, this);
					}
				};

				label.addMouseMotionListener(new MouseMotionAdapter() {
					public void mouseDragged(MouseEvent e) {
						setLocation((e.getXOnScreen() - mouseAtX), (e.getYOnScreen() - mouseAtY));// 设置拖拽后，窗口的位置
					}
				});
				label.addMouseListener(new MouseAdapter() // 设置窗口可拖动
				{
					public void mousePressed(MouseEvent e) {
						mouseAtX = e.getPoint().x;
						mouseAtY = e.getPoint().y;
					}

					@Override
					public void mouseClicked(MouseEvent e) {
						if (e.getButton() == MouseEvent.BUTTON1) {
							try {
								Runtime.getRuntime().exec("explorer " + s);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
						if (e.getButton() == MouseEvent.BUTTON3) {
							File file = new File(s);
							if (file.exists()) {
								String s1 = s.replace("DISPLAY", "NODISPLAY");
								file.renameTo(new File(s1));
								FileUtils.moveFile(s1, s1.replace("pic", "pic_NODISPLAY"));
							}
							MainFrame.showToast("提示", "已设置该速记不再显示", MessageType.INFO);
						}
					}
				});
				label.setHorizontalAlignment(SwingConstants.CENTER);
				ImageIcon img = new ImageIcon(s);

				label.setBounds(0, 0, (int) getSize().getWidth(),
						(int) getSize().getWidth() * img.getIconHeight() / img.getIconWidth());

				contentPane.add(label);
				label.setVisible(true);

			} else {
				if (label != null) {
					getContentPane().remove(label);
				}
				tip.setText(s.replace("[换行]", "\n"));
				tip.setVisible(true);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		getContentPane().setVisible(true);

	}

	@Override
	public void run() {
		while (true) {
			try {
				System.gc();
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}