package com.yangheng.StudyGuard.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.TrayIcon.MessageType;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import com.yangheng.StudyGuard.Utils.Utils;

public class TipFrame extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	// private JLabel label;

	public static TipFrame instance;
	JTextArea tip = new JTextArea("");
	JTextArea proverb = new JTextArea("pro");

	// 实现阻止程序多次启动
	public static TipFrame getInstance() {

		if (instance == null) {
			synchronized (TipFrame.class) {
				if (instance == null) {
					instance = new TipFrame();
				}
			}
		} else {
			// instance.dispose();
			instance = new TipFrame();
		}
		return instance;
	}

	static int mouseAtX;
	static int mouseAtY;

	public TipFrame() {

		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension sc = kit.getScreenSize();
		setSize(600, 200);
		setLocation(sc.width / 3, sc.height / 9);
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
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		contentPane.setOpaque(false);

		tip.setFont(new Font("华文行楷", Font.PLAIN, 36));
		tip.setOpaque(false);
		tip.setWrapStyleWord(true);
		tip.setLineWrap(true);
		tip.setAlignmentX(JTextArea.CENTER_ALIGNMENT);
		tip.setAlignmentY(JTextArea.CENTER_ALIGNMENT);
		tip.setEditable(false);
		tip.setEnabled(false);
		tip.setDisabledTextColor(Color.RED);
	
		tip.addMouseListener(new MouseAdapter() // 设置窗口可拖动
		{
			public void mousePressed(MouseEvent e) {
				mouseAtX = e.getPoint().x;
				mouseAtY = e.getPoint().y;
			}
		});
		tip.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				setLocation((e.getXOnScreen() - mouseAtX), (e.getYOnScreen() - mouseAtY));// 设置拖拽后，窗口的位置
			}
		});
		proverb.addMouseListener(new MouseAdapter() // 设置窗口可拖动
		{
			public void mousePressed(MouseEvent e) {
				mouseAtX = e.getPoint().x;
				mouseAtY = e.getPoint().y;
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				saveImgFromClipboard(MainFrame.filePath + "\\idea\\pic\\" + Utils.getTime() + "DISPLAY.png");
				String tmpString = proverb.getText();
				MainFrame.showToast("提示", "已保存剪切板图片到速记",MessageType.INFO);

				updateTip(null, "保存图片笔记成功");
				tip.invalidate();
				try {
					Thread.sleep(1000);

				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				updateTip(null, tmpString);
			}
		});
		proverb.addMouseMotionListener(new MouseMotionAdapter() {
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
		tip.setBounds(10, 10, 580, 89);

		contentPane.add(tip);

		proverb.setText("");
		proverb.setWrapStyleWord(true);
		proverb.setOpaque(false);
		proverb.setLineWrap(true);
		proverb.setForeground(Color.BLUE);
		proverb.setFont(new Font("华文行楷", Font.PLAIN, 28));
		proverb.setEditable(false);
		proverb.setBounds(10, 109, 580, 78);
		proverb.setAlignmentX(JTextArea.CENTER_ALIGNMENT);
		proverb.setAlignmentY(JTextArea.CENTER_ALIGNMENT);
		contentPane.add(proverb);
		setContentPane(contentPane);
		

	}

	public void updateTip(String task, String pro) {
		
		if (task != (null)) {
			tip.setText(task);
			tip.setEnabled(false);
		}
		if (pro != null) {
			proverb.setText(pro);
			proverb.setEnabled(false);
		}
		tip.setDisabledTextColor(Color.red);
		proverb.setDisabledTextColor(Color.BLUE);
		invalidate();
	}

	public static boolean saveImgFromClipboard(String path) {
		if (path != null) {
			path = path.replace(":", "-");
			try {
				// 获取粘贴板图片
				Image image = getImageFromClipboard();
				File file = new File(path);
				// 转成jpg
				// BufferedImage bufferedImage = new
				// BufferedImage(image.getWidth(null), image.getHeight(null),
				// BufferedImage.TYPE_INT_RGB);
				// 转成png
				BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
						BufferedImage.TYPE_INT_ARGB);
				Graphics2D g = bufferedImage.createGraphics();
				g.drawImage(image, null, null);
				// ImageIO.write((RenderedImage)bufferedImage, "jpg", file);

				ImageIO.write((RenderedImage) bufferedImage, "png", file);

				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 从剪切板获得图片。
	 */
	public static Image getImageFromClipboard() throws Exception {
		Clipboard sysc = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable cc = sysc.getContents(null);
		if (cc == null)
			return null;
		else if (cc.isDataFlavorSupported(DataFlavor.imageFlavor))
			return (Image) cc.getTransferData(DataFlavor.imageFlavor);
		return null;
	}

	@Override
	public void run() {

		while (true) {

			if (Utils.getTime().substring(15, 17).equals("00")) {
				updateTip(null, "复制图片后点击此处保存为图片笔记");
			}
			ArrayList<String> proverbs = MainFrame.ioUtils.getProverblist();
			ArrayList<String> planList = MainFrame.ioUtils.getPlanlist();
			updateTip(null, proverbs.get((int) (Math.random() * proverbs.size())).toString());

			for (String string : planList) {
				if (Utils.getValueOfElementByTag(string, "[time]").equals(Utils.getTime().substring(12, 17))) {
					String tipString = "当前时段任务 :  " + Utils.getValueOfElementByTag(string, "[task]");
					updateTip(tipString, null);
				}
			}
			try {
				Thread.sleep(60000 * 3);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
