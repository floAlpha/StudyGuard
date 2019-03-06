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

	// ʵ����ֹ����������
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
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		contentPane.setOpaque(false);
		tip.setHorizontalAlignment(SwingConstants.CENTER);

		tip.setFont(new Font("����", Font.PLAIN, 26));
		tip.setOpaque(false);
		tip.setBorder(BorderFactory.createLineBorder(Color.BLACK, 0));
		// tip.setWrapStyleWord(true);
		// tip.setLineWrap(true);
		tip.setAlignmentX(JTextArea.CENTER_ALIGNMENT);
		tip.setAlignmentY(JTextArea.CENTER_ALIGNMENT);
		tip.setEditable(false);
		tip.setEnabled(false);
		tip.setDisabledTextColor(Color.RED);

		tip.addMouseListener(new MouseAdapter() // ���ô��ڿ��϶�
		{
			public void mousePressed(MouseEvent e) {
				mouseAtX = e.getPoint().x;
				mouseAtY = e.getPoint().y;
			}
		});
		tip.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				setLocation((e.getXOnScreen() - mouseAtX), (e.getYOnScreen() - mouseAtY));// ������ק�󣬴��ڵ�λ��
			}
		});
		proverb.setHorizontalAlignment(SwingConstants.CENTER);
		proverb.setEnabled(false);
		proverb.addMouseListener(new MouseAdapter() // ���ô��ڿ��϶�
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
				MainFrame.showToast("��ʾ", "�ѱ�����а��ͼ���ټ�", MessageType.INFO);

				updateTip(null, "����ͼƬ�ʼǳɹ�");
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
		tip.setBounds(10, 10, 819, 30);

		contentPane.add(tip);

		proverb.setText("");
		proverb.setOpaque(false);
		proverb.setForeground(Color.ORANGE);
		proverb.setFont(new Font("�����п�", Font.PLAIN, 22));
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
	 *            ѹ���ı���
	 */
	public static BufferedImage reduceImg(BufferedImage img, Float rate) {
		try {
			
			int widthdist = 0, heightdist = 0;
			// ���������Ϊ����˵���ǰ�����ѹ��
			if (rate != null && rate > 0) {
				// ���ԴͼƬ�Ŀ�ߴ���������
				int[] results = { img.getWidth(), img.getHeight() };
			
				if (results == null || results[0] == 0 || results[1] == 0) {
					return null;
				} else {
					// ���������Ż�����ͼƬ��С����������תΪ����
					widthdist = (int) (results[0] * rate);
					heightdist = (int) (results[1] * rate);
				}
			}
			// ��ʼ��ȡ�ļ�������ѹ��
			// Image src = ImageIO.read(srcfile);
			// // ����һ������ΪԤ����ͼ������֮һ�� BufferedImage
//			System.out.println(widthdist);
//			System.err.println(heightdist);
			BufferedImage tag = new BufferedImage((int) widthdist, (int) heightdist, BufferedImage.TYPE_INT_RGB);

			// ����ͼ��
			// getScaledInstance��ʾ������ͼ������Ű汾������һ���µ����Ű汾Image,��ָ����width,height����ͼ��
			// Image.SCALE_SMOOTH,ѡ��ͼ��ƽ���ȱ������ٶȾ��и������ȼ���ͼ�������㷨��
			tag.getGraphics().drawImage(((Image) img).getScaledInstance(widthdist, heightdist, Image.SCALE_SMOOTH), 0,
					0, null);
			return tag;

		} catch (Exception ef) {
			ef.printStackTrace();
		}
		return null;
	}

	// /**
	// * ��ȡͼƬ��Ⱥ͸߶�
	// *
	// * @param ͼƬ·��
	// * @return ����ͼƬ�Ŀ��
	// */
	// public static int[] getImgWidthHeight(File file) {
	// InputStream is = null;
	// BufferedImage src = null;
	// int result[] = { 0, 0 };
	// try {
	// // ����ļ�������
	// is = new FileInputStream(file);
	// // �����ｫͼƬд�뻺��ͼƬ��
	// src = ImageIO.read(is);
	// result[0] =src.getWidth(null); // �õ�ԴͼƬ��
	// result[1] =src.getHeight(null);// �õ�ԴͼƬ��
	// is.close(); //�ر�������
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
	 * �Ӽ��а���ͼƬ��
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
	 * �Ӽ��а���ͼƬ��
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
				updateTip("˫���ټ���ʾ���������θ����ټ�", "��ͼ������ɫ����ɱ����ͼ���ټ�");
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
							updateTip(null, string.split("\"")[string.split("\"").length - 1].replace("��", ""));
							break;
						}
					}
				} else {
					updateTip(null, "��������ʧ��");
				}
			} else {
				ArrayList<String> proverbs = IOUtils.proverblist;
				updateTip(null, proverbs.get((int) (Math.random() * proverbs.size())).toString());
			}

			boolean b = true;
			for (String string : planList) {
				if (Utils.getValueOfElementByTag(string, "[time]").compareTo(Utils.getTime().substring(12, 17)) < 0) {
					String tipString = "��ǰ���� :  " + Utils.getValueOfElementByTag(string, "[task]");
					updateTip(tipString, null);
					b = false;
				}
			}
			if (b) {
				updateTip("�������������", null);
			}
			try {
				Thread.sleep(60000 * 3);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
