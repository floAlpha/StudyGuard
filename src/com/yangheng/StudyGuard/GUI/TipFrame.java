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
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.yangheng.StudyGuard.Utils.IOUtils;
import com.yangheng.StudyGuard.Utils.Utils;

public class TipFrame extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	// private JLabel label;

	public static TipFrame instance;
	JTextField tip = new JTextField("");
	JTextField proverb = new JTextField("pro");

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
		setSize(839, 85);
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
		tip.setHorizontalAlignment(SwingConstants.CENTER);

		tip.setFont(new Font("等线", Font.PLAIN, 26));
		tip.setOpaque(false);
		tip.setBorder(BorderFactory.createLineBorder(Color.BLACK, 0));
		// tip.setWrapStyleWord(true);
		// tip.setLineWrap(true);
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
		proverb.setHorizontalAlignment(SwingConstants.CENTER);
		proverb.setEnabled(false);
		proverb.addMouseListener(new MouseAdapter() // 设置窗口可拖动
		{
			public void mousePressed(MouseEvent e) {
				mouseAtX = e.getPoint().x;
				mouseAtY = e.getPoint().y;
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				saveImgFromClipboard(
						MainFrame.filePath + "\\idea\\pic\\" + Utils.getTime() + "DISPLAY.png");
				String tmpString = proverb.getText();
				MainFrame.showToast("提示", "已保存剪切板截图到速记", MessageType.INFO);

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
		tip.setBounds(10, 10, 819, 30);

		contentPane.add(tip);

		proverb.setText("");
		proverb.setOpaque(false);
		proverb.setForeground(Color.ORANGE);
		proverb.setFont(new Font("华文行楷", Font.PLAIN, 22));
		proverb.setEditable(false);
		proverb.setBounds(10, 45, 819, 30);
		proverb.setAlignmentX(JTextArea.CENTER_ALIGNMENT);
		proverb.setAlignmentY(JTextArea.CENTER_ALIGNMENT);
		contentPane.add(proverb);
		proverb.setBorder(BorderFactory.createLineBorder(Color.BLACK, 0));

		setContentPane(contentPane);
		setVisible(true);

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

	/**
	 * @param rate
	 *            压缩的比例
	 */
	public static BufferedImage reduceImg(BufferedImage img, Float rate) {
		try {
			
			int widthdist = 0, heightdist = 0;
			// 如果比例不为空则说明是按比例压缩
			if (rate != null && rate > 0) {
				// 获得源图片的宽高存入数组中
				int[] results = { img.getWidth(), img.getHeight() };
			
				if (results == null || results[0] == 0 || results[1] == 0) {
					return null;
				} else {
					// 按比例缩放或扩大图片大小，将浮点型转为整型
					widthdist = (int) (results[0] * rate);
					heightdist = (int) (results[1] * rate);
				}
			}
			// 开始读取文件并进行压缩
			// Image src = ImageIO.read(srcfile);
			// // 构造一个类型为预定义图像类型之一的 BufferedImage
//			System.out.println(widthdist);
//			System.err.println(heightdist);
			BufferedImage tag = new BufferedImage((int) widthdist, (int) heightdist, BufferedImage.TYPE_INT_RGB);

			// 绘制图像
			// getScaledInstance表示创建此图像的缩放版本，返回一个新的缩放版本Image,按指定的width,height呈现图像
			// Image.SCALE_SMOOTH,选择图像平滑度比缩放速度具有更高优先级的图像缩放算法。
			tag.getGraphics().drawImage(((Image) img).getScaledInstance(widthdist, heightdist, Image.SCALE_SMOOTH), 0,
					0, null);
			return tag;

		} catch (Exception ef) {
			ef.printStackTrace();
		}
		return null;
	}

	// /**
	// * 获取图片宽度和高度
	// *
	// * @param 图片路径
	// * @return 返回图片的宽度
	// */
	// public static int[] getImgWidthHeight(File file) {
	// InputStream is = null;
	// BufferedImage src = null;
	// int result[] = { 0, 0 };
	// try {
	// // 获得文件输入流
	// is = new FileInputStream(file);
	// // 从流里将图片写入缓冲图片区
	// src = ImageIO.read(is);
	// result[0] =src.getWidth(null); // 得到源图片宽
	// result[1] =src.getHeight(null);// 得到源图片高
	// is.close(); //关闭输入流
	// } catch (Exception ef) {
	// ef.printStackTrace();
	// }
	//
	// return result;
	// }

	public static boolean saveImgFromClipboard(String path) {
	
		if (path != null) {
			path = path.replace(":", "-");
			try {
				Image image = getImageFromClipboard();
				if (image != null) {
					BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
							BufferedImage.TYPE_INT_ARGB);
					Graphics2D g = bufferedImage.createGraphics();
					g.drawImage(image, null, null);
					File file = new File(path);
					ImageIO.write((RenderedImage) bufferedImage, "png", file);
				} else {
					List<File> files = getFileFromClipboard();
					for (int i = 0; i < files.size(); i++) {
						image = ImageIO.read(files.get(i));
						BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
								BufferedImage.TYPE_INT_ARGB);
						Graphics2D g = bufferedImage.createGraphics();
						g.drawImage(image, null, null);
						BufferedImage img = reduceImg(bufferedImage, Float.valueOf((float) (800.0 / bufferedImage.getWidth())));
						ImageIO.write((RenderedImage) img, "png", new File(path.replace(".png", " " + i + "th.png")));
					}

				}
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

	/**
	 * 从剪切板获得图片。
	 */
	@SuppressWarnings("unchecked")
	public static List<File> getFileFromClipboard() throws Exception {
		Clipboard sysc = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable cc = sysc.getContents(null);
		if (cc == null)
			return null;
		else if (cc.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
			return (List<File>) cc.getTransferData(DataFlavor.javaFileListFlavor);
		return null;
	}

	@Override
	public void run() {

		while (true) {
			try {
				updateTip("双击速记显示浮窗可屏蔽该条速记", "截图后点击红色字体可保存截图到速记");
				Thread.sleep(6000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			ArrayList<String> planList = IOUtils.planlist;

			if (Utils.poems.equals("true")) {
				String json = IOUtils.sendGet("https://api.gushi.ci/all.json", null);
				if (json != null) {
					String[] items = json.split(",");
					for (String string : items) {
						if (string.contains("content")) {
							updateTip(null, string.split("\"")[string.split("\"").length - 1].replace("。", ""));
							break;
						}
					}
				} else {
					updateTip(null, "请求内容失败");
				}
			} else {
				ArrayList<String> proverbs = IOUtils.proverblist;
				updateTip(null, proverbs.get((int) (Math.random() * proverbs.size())).toString());
			}

			boolean b = true;
			for (String string : planList) {
				if (Utils.getValueOfElementByTag(string, "[time]").compareTo(Utils.getTime().substring(12, 17)) < 0) {
					String tipString = "当前任务 :  " + Utils.getValueOfElementByTag(string, "[task]");
					updateTip(tipString, null);
					b = false;
				}
			}
			if (b) {
				updateTip("暂无任务，请添加", null);
			}
			try {
				Thread.sleep(60000 * 3);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
