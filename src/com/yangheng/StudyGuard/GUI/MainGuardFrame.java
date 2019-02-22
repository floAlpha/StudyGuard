package com.yangheng.StudyGuard.GUI;

import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;
import com.yangheng.StudyGuard.Notifier;
import com.yangheng.StudyGuard.SingleInstance;
import com.yangheng.StudyGuard.Watcher;
import com.yangheng.StudyGuard.Utils.Utils;

/**
 * @author chuan
 *
 */
public class MainGuardFrame extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public static String filePath;
	// public static String studyPlanPath;
	Watcher watcher;

	public static SystemTray systemTray;// ϵͳ����
	public static TrayIcon trayIcon;// ����ͼ��

	static String realPath = "./";

	public static String getAppPath() {
		// ��ȡjar�ļ���ǰִ��·��
		String apppath = MainGuardFrame.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		String realPath = "./";
		try {
			realPath = java.net.URLDecoder.decode(apppath, "utf-8");
			realPath = (realPath.substring(0, realPath.substring(0, realPath.length() - 1).lastIndexOf('/')) + "/");
			realPath = realPath.substring(1, realPath.length());

		} catch (Exception e) {

		}
		return realPath;
	}

	/**
	 * Launch the application.
	 */

	public static void showToast(String title, String message, MessageType messageType) {

		trayIcon.displayMessage(title, message, messageType);

	}

	public static void main(String[] args) {

		MainGuardFrame mainframe = MainGuardFrame.getInstance();
		new Thread(mainframe).start();
	}

	private static MainGuardFrame instance = null;

	// ʵ����ֹ����������
	public static MainGuardFrame getInstance() {
		MainGuardFrame.filePath = getAppPath() + "/Data/";
		try {
			systemTray = SystemTray.getSystemTray();
			Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
			trayIcon = new TrayIcon(image, "��������");
			trayIcon.setImageAutoSize(true);
			systemTray.add(trayIcon);

		} catch (AWTException e1) {

			e1.printStackTrace();
		}
		SingleInstance singleInstance = new SingleInstance();
		singleInstance.start();
		Utils.loadConfig();
		// System.out.println("��һ�μ���");
		if (instance == null) {
			synchronized (MainGuardFrame.class) {
				if (instance == null) {

					instance = new MainGuardFrame();
					MainGuardFrame.showToast("��ʾ", "������ں�̨����", MessageType.INFO);
					try {
						UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					} catch (Exception e) {
						e.printStackTrace();
					}
					// System.out.println(Utils.nomainwindow);
					if (Utils.nomainwindow.equals("true")) {
						instance.setVisible(false);
						instance.setAlwaysOnTop(false);
					} else {
						instance.setVisible(true);
						instance.setAlwaysOnTop(true);
					}

				}
			}
		}

		return instance;
	}

	private MainGuardFrame() {

		trayIcon.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2)// ������̴�������
				{
					instance.setExtendedState(Frame.NORMAL);
					instance.setVisible(true);
				}
				if (e.getButton() == MouseEvent.BUTTON3) {
					IdeaInfoFrame ideaInfo = IdeaInfoFrame.getInstance(IdeaFrame.currentmind);
					if (ideaInfo != null) {
						ideaInfo.setAlwaysOnTop(true);
						ideaInfo.setVisible(true);
					}

				}

			}

		});

		this.addWindowListener(new WindowAdapter() {
			public void windowIconified(WindowEvent e) {
				dispose();// ������С��ʱdispose�ô���
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				dispose();// ������С��ʱdispose�ô���
			}

			@Override
			public void windowOpened(WindowEvent e) {
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

		// this.setExtendedState(JFrame.ICONIFIED);

		Toolkit kit = Toolkit.getDefaultToolkit(); // ���幤�߰�
		Dimension screenSize = kit.getScreenSize(); // ��ȡ��Ļ�ĳߴ�
		int screenWidth = screenSize.width / 2; // ��ȡ��Ļ�Ŀ�
		int screenHeight = screenSize.height / 2; // ��ȡ��Ļ�ĸ�

		int height = this.getHeight();
		int width = this.getWidth();
		setLocation(screenWidth - width / 2, screenHeight - height / 2);

		setTitle("ѧϰ���� V1.0" + "(�����ػ�)");
		Toolkit tool = getToolkit(); // �õ�һ��Toolkit����
		Image image = tool.getImage(realPath + "src/icon.png");
		setIconImage(image);

		setFont(new Font("����", Font.BOLD, 16));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setResizable(false);
		setBounds(100, 100, 489, 255);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton pausewatch = new JButton("\u6682\u505C\u76D1\u63A7(F3)");
		pausewatch.setFont(new Font("����", Font.BOLD, 18));
		pausewatch.setBounds(35, 33, 173, 37);
		contentPane.add(pausewatch);

		JButton satrtwatch = new JButton("\u6062\u590D\u76D1\u63A7(F4)");

		satrtwatch.setFont(new Font("����", Font.BOLD, 18));
		satrtwatch.setBounds(277, 33, 173, 37);
		contentPane.add(satrtwatch);

		JButton openplanfile = new JButton("\u6253\u5F00\u8BA1\u5212(F5)");
		openplanfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showToast("��ʾ", "ʹ��CTRL+SHIFT+Z�ȼ����������¼ƻ���Ŀ", MessageType.INFO);
				Runtime runtime = Runtime.getRuntime();
				try {
					runtime.exec(("explorer " + filePath + "\\plan\\" + Utils.getTime().substring(0, 11) + ".txt")
							.replace('/', '\\'));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		openplanfile.setFont(new Font("����", Font.BOLD, 18));
		openplanfile.setBounds(35, 80, 173, 38);
		contentPane.add(openplanfile);

		JSeparator separator = new JSeparator();
		separator.setBounds(-2, 144, 485, 2);
		contentPane.add(separator);

		JButton takeidea = new JButton("\u65B0\u5EFAIDEA (F1)");
		takeidea.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// IdeaFrame.storeIdea();
				new IdeaInputFrame().setVisible(true);
			}
		});
		takeidea.setForeground(Color.BLUE);
		takeidea.setFont(new Font("����", Font.BOLD, 16));
		takeidea.setBounds(35, 167, 173, 39);
		contentPane.add(takeidea);

		JButton takeMemorandumFrame = new JButton("\u5907\u5FD8\u5F55 (F2)");
		takeMemorandumFrame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MemorandumFrame memorandumFrame = MemorandumFrame.getInstance();
				memorandumFrame.setVisible(true);
				new Thread(memorandumFrame).start();
			}
		});
		takeMemorandumFrame.setForeground(Color.BLUE);
		takeMemorandumFrame.setFont(new Font("����", Font.BOLD, 18));
		takeMemorandumFrame.setBounds(277, 167, 173, 39);
		contentPane.add(takeMemorandumFrame);

		pausewatch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// MindFrame.storeMind();
				Notifier.iswatching = false;
				Watcher.iswatching = false;
				setTitle("ѧϰ���� V1.0" + "(��ͣ����)");
				showToast("֪ͨ", "��������ͣ���", MessageType.INFO);
			}
		});

		satrtwatch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Notifier.iswatching = true;
				Watcher.iswatching = true;
				setTitle("ѧϰ���� V1.0" + "(�����ػ�)");
				showToast("֪ͨ", "������ָ����", MessageType.INFO);

			}
		});

		filePath = realPath + "Data";
		// studyPlanPath = filePath + "/plan/ѧϰ�ƻ�.txt";

		watcher = new Watcher(filePath,
				MainGuardFrame.filePath + "\\plan\\" + Utils.getTime().substring(0, 11) + ".txt");
		Watcher.load_study_plan();
		watcher.start();

		JButton btnf = new JButton("\u914D\u7F6E\u6587\u4EF6(F6)");
		btnf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Runtime runtime = Runtime.getRuntime();
				try {
					runtime.exec(("explorer " + filePath + "\\conf\\conf.txt").replace('/', '\\'));
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		});
		btnf.setFont(new Font("����", Font.BOLD, 18));
		btnf.setBounds(277, 80, 173, 38);
		contentPane.add(btnf);
		new Thread(IdeaFrame.getInstance()).start();
		new Thread(new MemorandumFrame()).start();

		try {
			JIntellitype.setLibraryLocation(realPath + "\\JIntellitype64.dll");
		} catch (Exception e) {

			JOptionPane.showConfirmDialog(null, "�����ʧ��");
			dispose();
			JIntellitype.setLibraryLocation(realPath + "\\JIntellitype.dll");
		}

		// �ȼ���ʶ

		final int GLOBAL_HOT_KEY_1 = 1;
		final int GLOBAL_HOT_KEY_2 = 2;
		final int GLOBAL_HOT_KEY_3 = 3;
		final int GLOBAL_HOT_KEY_4 = 4;
		final int GLOBAL_HOT_KEY_5 = 5;
		final int GLOBAL_HOT_KEY_6 = 6;
		final int GLOBAL_HOT_KEY_7 = 7;
		final int GLOBAL_HOT_KEY_8 = 8;
		final int GLOBAL_HOT_KEY_9 = 9;
		// ��һ����ע���ȼ�����һ�����������Լ��ı�ʶ���ڶ���������ʾ��ϼ������û����Ϊ0������������Ϊ�������Ҫ�ȼ�(120��F9�ļ�ֵ)
		JIntellitype.getInstance().registerHotKey(GLOBAL_HOT_KEY_1, 0, 112);// F1
		JIntellitype.getInstance().registerHotKey(GLOBAL_HOT_KEY_2, 0, 113);// F2
		JIntellitype.getInstance().registerHotKey(GLOBAL_HOT_KEY_3, 0, 114);// F3
		JIntellitype.getInstance().registerHotKey(GLOBAL_HOT_KEY_4, 0, 115);// F4
		JIntellitype.getInstance().registerHotKey(GLOBAL_HOT_KEY_5, 0, 116);// F5
		JIntellitype.getInstance().registerHotKey(GLOBAL_HOT_KEY_6, 0, 117);// F6
		JIntellitype.getInstance().registerHotKey(GLOBAL_HOT_KEY_7, 0, 118);// F7
		JIntellitype.getInstance().registerHotKey(GLOBAL_HOT_KEY_8, JIntellitype.MOD_SHIFT + JIntellitype.MOD_CONTROL,
				(int) 'X');// ALT+SHIFT+X
		JIntellitype.getInstance().registerHotKey(GLOBAL_HOT_KEY_9, JIntellitype.MOD_SHIFT + JIntellitype.MOD_CONTROL,
				(int) 'Z');// ALT+SHIFT+Z
		// ����ȼ�������
		// �ڶ���������ȼ�������
		JIntellitype.getInstance().addHotKeyListener(new HotkeyListener() {
			public void onHotKey(int markCode) {
				switch (markCode) {
				case GLOBAL_HOT_KEY_1:
					// IdeaFrame.storeIdea();
					new IdeaInputFrame().setVisible(true);
					break;
				case GLOBAL_HOT_KEY_3:
					Notifier.iswatching = false;
					Watcher.iswatching = false;
					setTitle("ѧϰ���� V1.0" + "(�����ػ�)");
					showToast("֪ͨ", "��������ͣ���", MessageType.INFO);
					break;
				case GLOBAL_HOT_KEY_4:
					Notifier.iswatching = true;
					Watcher.iswatching = true;
					setTitle("ѧϰ���� V1.0" + "(�����ػ�)");
					showToast("֪ͨ", "������ָ����", MessageType.INFO);
					break;
				case GLOBAL_HOT_KEY_2:
					MemorandumFrame memorandumFrame = MemorandumFrame.getInstance();
					memorandumFrame.setVisible(true);
					break;
				case GLOBAL_HOT_KEY_5:
					showToast("��ʾ", "ʹ��CTRL+SHIFT+Z�ȼ����������¼ƻ���Ŀ", MessageType.INFO);
					try {
						Runtime.getRuntime()
								.exec(("explorer " + filePath + "\\plan\\" + Utils.getTime().substring(0, 11) + ".txt")
										.replace('/', '\\'));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					break;
				case GLOBAL_HOT_KEY_6:
					try {
						Runtime.getRuntime().exec(("explorer " + filePath + "\\conf\\conf.txt").replace('/', '\\'));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					break;
				case GLOBAL_HOT_KEY_7:
					MindFrame.storeMind();

					break;
				case GLOBAL_HOT_KEY_8:
					IdeaInfoFrame ideaInfo = IdeaInfoFrame.getInstance(IdeaFrame.currentmind);
					ideaInfo.updateView(IdeaFrame.currentmind);
					if (ideaInfo != null) {
						ideaInfo.setAlwaysOnTop(true);
						ideaInfo.setVisible(true);
					}
					break;
				case GLOBAL_HOT_KEY_9:

					PlanInputFrame planInput = new PlanInputFrame();
					planInput.setVisible(true);
					planInput.setAlwaysOnTop(true);
				}
			}
		});

	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(3000);// 5����
			} catch (InterruptedException e) {
			}
			ArrayList<String> proverbs = Utils
					.readTxtFileIntoStringArrList(MainGuardFrame.filePath + "\\proverb\\proverb.txt");
			MainGuardFrame.trayIcon.setToolTip(proverbs.get(((int) (Math.random() * proverbs.size()))));

		}

	}

}
