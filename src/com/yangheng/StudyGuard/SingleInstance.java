package com.yangheng.StudyGuard;

import java.net.ServerSocket;
import java.net.Socket;

public class SingleInstance extends Thread {

	@SuppressWarnings("resource")
	@Override
	public void run() {
		try {
			@SuppressWarnings("unused")
			Socket sock = new Socket("127.0.0.1", 55661);
			// ����socket������22222�˿�
		} catch (Exception e) {
		}
		try {
			ServerSocket server = new ServerSocket(55661);// ����socket,��22222�˿ڼ���
			server.accept(); // �ȴ�����
			server.close(); // �����ӵ�����Ҳ����˵���µ�ʵ��
			System.exit(0); // ���ʵ���˳�
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
