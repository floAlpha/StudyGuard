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
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.FontUIResource;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;
import com.yangheng.StudyGuard.PlanNotifier;
import com.yangheng.StudyGuard.SingleInstance;
import com.yangheng.StudyGuard.Utils.IOUtils;
import com.yangheng.StudyGuard.Utils.Utils;

/**
 * @author chuan
 *
 */
public class MainFrame extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public static String filePath;
	public static IOUtils ioUtils;
	static IdeaFloatFrame ideaFloatFrame = IdeaFloatFrame.getInstance();
	static IdeaInfoFrame ideaInfoFrame;
	TipFrame tipFrame = TipFrame.getInstance();
	public static SystemTray systemTray;// 系统托盘
	public static TrayIcon trayIcon;// 托盘图标

	PlanInfoFrame planInfoFrame;
	String realPath = "./";

	public static String getAppPath() {
		// 获取jar文件当前执行路径
		String apppath = MainFrame.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		String realPath = "./";
		try {
			realPath = java.net.URLDecoder.decode(apppath, "utf-8");
			realPath = (realPath.substring(0, realPath.substring(0, realPath.length() - 1).lastIndexOf('/')) + "/");
			realPath = realPath.substring(1, realPath.length());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return realPath;
	}

	/**
	 * Launch the application.
	 */

	public static void showToast(String title, String message, MessageType messageType) {

		trayIcon.displayMessage("速记助手V1.1", message, messageType);

	}
	
	private static void initGlobalFont(){

	    FontUIResource fontUIResource = new FontUIResource(new Font("黑体",Font.PLAIN, 12));

	    for (@SuppressWarnings("rawtypes")
		Enumeration keys = UIManager.getDefaults().keys(); keys.hasMoreElements();) {

	        Object key = keys.nextElement();

	        Object value= UIManager.get(key);

	        if (value instanceof FontUIResource) {

	            UIManager.put(key, fontUIResource);

	        }  

	    }

	}

	public static void main(String[] args) {

		MainFrame mainframe = MainFrame.getInstance();
		new Thread(mainframe).start();
	}

	private static MainFrame instance = null;

	// 实现阻止程序多次启动
	public static MainFrame getInstance() {
		
		initGlobalFont();
	
		MainFrame.filePath = getAppPath() + "/Data/";
		try {
			systemTray = SystemTray.getSystemTray();
			Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
			trayIcon = new TrayIcon(image, "正在运行\n使用CTRL+SHIFT+Z快捷键开关速记悬浮窗");
			trayIcon.setImageAutoSize(true);
			systemTray.add(trayIcon);

		} catch (AWTException e1) {

			e1.printStackTrace();
		}
		SingleInstance singleInstance = new SingleInstance();
		singleInstance.start();
		Utils.loadConfig();
		if (instance == null) {
			synchronized (MainFrame.class) {
				if (instance == null) {

					instance = new MainFrame();
					MainFrame.showToast("速记助手v1.1", "软件正在后台运行", MessageType.INFO);
					try {
						UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					} catch (Exception e) {
						e.printStackTrace();
					}
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

	private MainFrame() {

		trayIcon.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2)// 点击托盘窗口再现
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
				dispose();// 窗口最小化时dispose该窗口
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				dispose();// 窗口最小化时dispose该窗口
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

		setTitle("速记助手  V1.1");
		Toolkit tool = getToolkit(); // 得到一个Toolkit对象
		Image image = tool.getImage(realPath + "src/icon.png");
		setIconImage(image);

		setFont(new Font("黑体", Font.BOLD, 16));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setResizable(false);
		setBounds(100, 100, 489, 255);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
		Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
		int screenWidth = screenSize.width / 2; // 获取屏幕的宽
		int screenHeight = screenSize.height / 2; // 获取屏幕的高

		int height = this.getHeight();
		int width = this.getWidth();
		setLocation(screenWidth - width / 2, screenHeight - height / 2);

		JButton pausewatch = new JButton("\u65B0\u5EFA\u4EFB\u52A1(F3)");
		pausewatch.setFont(new Font("楷体", Font.BOLD, 18));
		pausewatch.setBounds(35, 33, 173, 37);
		contentPane.add(pausewatch);

		JButton satrtwatch = new JButton("\u5DE5\u4F5C\u76EE\u5F55(F6)");

		satrtwatch.setFont(new Font("楷体", Font.BOLD, 18));
		satrtwatch.setBounds(277, 80, 173, 37);
		contentPane.add(satrtwatch);

		JButton openplanfile = new JButton("\u6253\u5F00\u4EFB\u52A1(F4)");
		openplanfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				planInfoFrame.setVisible(!planInfoFrame.isVisible());
			}
		});

		openplanfile.setFont(new Font("楷体", Font.BOLD, 18));
		openplanfile.setBounds(277, 32, 173, 38);
		contentPane.add(openplanfile);

		JSeparator separator = new JSeparator();
		separator.setBounds(-2, 144, 485, 2);
		contentPane.add(separator);

		JButton takeidea = new JButton("\u65B0\u5EFAIDEA (F1)");
		takeidea.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new IdeaInputFrame().setVisible(true);
			}
		});
		takeidea.setForeground(Color.BLUE);
		takeidea.setFont(new Font("楷体", Font.BOLD, 16));
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
		takeMemorandumFrame.setFont(new Font("楷体", Font.BOLD, 18));
		takeMemorandumFrame.setBounds(277, 167, 173, 39);
		contentPane.add(takeMemorandumFrame);

		pausewatch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				PlanInputFrame planInputFrame = new PlanInputFrame();
				planInputFrame.setVisible(true);
				planInputFrame.setAlwaysOnTop(true);
			}
		});

		satrtwatch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Runtime runtime = Runtime.getRuntime();
				try {
					runtime.exec(("explorer " + filePath).replace('/', '\\'));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		filePath = realPath + "Data";

		ioUtils = new IOUtils(filePath);
		ioUtils.start();

		JButton btnf = new JButton("\u914D\u7F6E\u6587\u4EF6(F5)");
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
		btnf.setFont(new Font("楷体", Font.BOLD, 18));
		btnf.setBounds(35, 80, 173, 38);
		contentPane.add(btnf);

		
		
		new Thread(IdeaFrame.getInstance()).start();
		new Thread(new MemorandumFrame()).start();
		new Thread(new TipFrame()).start();	
		TipFrame tipFrame= new TipFrame();
		tipFrame.setVisible(true);
		tipFrame.setAlwaysOnTop(true);
		new Thread(tipFrame).start();
	
		new Thread(new PlanNotifier()).start();
		
		planInfoFrame = PlanInfoFrame.getInstance();
		planInfoFrame.setVisible(false);
		new Thread(planInfoFrame).start();
		
		ideaFloatFrame.setAlwaysOnTop(true);
		ideaFloatFrame.setVisible(true);

		try {
			JIntellitype.setLibraryLocation(realPath + "\\JIntellitype64.dll");
		} catch (Exception e) {

			JOptionPane.showConfirmDialog(null, "组件丢失！");
			dispose();
			JIntellitype.setLibraryLocation(realPath + "\\JIntellitype.dll");
		}
		// 热键标识
		final int GLOBAL_HOT_KEY_1 = 1;
		final int GLOBAL_HOT_KEY_2 = 2;
		final int GLOBAL_HOT_KEY_3 = 3;
		final int GLOBAL_HOT_KEY_4 = 4;
		final int GLOBAL_HOT_KEY_5 = 5;
		final int GLOBAL_HOT_KEY_6 = 6;
		final int GLOBAL_HOT_KEY_7 = 7;
		final int GLOBAL_HOT_KEY_8 = 8;
		final int GLOBAL_HOT_KEY_9 = 9;
		// 第一步：注册热键，第一个参数我们自己的标识，第二个参数表示组合键，如果没有则为0，第三个参数为定义的主要热键(120是F9的键值)
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
		// 添加热键监听器
		// 第二步：添加热键监听器
		JIntellitype.getInstance().addHotKeyListener(new HotkeyListener() {
			public void onHotKey(int markCode) {
				switch (markCode) {
				case GLOBAL_HOT_KEY_1:
					new IdeaInputFrame().setVisible(true);
					break;
				case GLOBAL_HOT_KEY_2:
					MemorandumFrame memorandumFrame = MemorandumFrame.getInstance();
					memorandumFrame.setVisible(true);
					break;
				case GLOBAL_HOT_KEY_3:
					PlanInputFrame planInput = new PlanInputFrame();
					planInput.setVisible(true);
					planInput.setAlwaysOnTop(true);
					break;
				case GLOBAL_HOT_KEY_4:
					planInfoFrame.setVisible(!planInfoFrame.isVisible());
					break;
				case GLOBAL_HOT_KEY_5:
					try {
						Runtime.getRuntime().exec(("explorer " + filePath + "\\conf\\conf.txt").replace('/', '\\'));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					break;
				case GLOBAL_HOT_KEY_6:
					Runtime runtime = Runtime.getRuntime();
					try {
						runtime.exec(("explorer " + filePath).replace('/', '\\'));
					} catch (IOException e1) {
						e1.printStackTrace();
					}

					break;
				case GLOBAL_HOT_KEY_7:
					MindFrame.storeMind();
					break;
				case GLOBAL_HOT_KEY_8:

					ideaInfoFrame = IdeaInfoFrame.getInstance(IdeaFrame.currentmind);
					ideaInfoFrame.updateView(IdeaFrame.currentmind);
					if (ideaInfoFrame != null) {
						ideaInfoFrame.setAlwaysOnTop(true);
						ideaInfoFrame.setVisible(true);
					}
					break;
				case GLOBAL_HOT_KEY_9:
					ideaFloatFrame.setVisible(!ideaFloatFrame.isVisible());
					tipFrame.setVisible(!tipFrame.isVisible());
				}
			}
		});
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(300000);// 5分钟
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			ArrayList<String> proverbs = ioUtils.getProverblist();
			MainFrame.trayIcon.setToolTip(proverbs.get((int) (Math.random() * proverbs.size())));
		}
	}

}
