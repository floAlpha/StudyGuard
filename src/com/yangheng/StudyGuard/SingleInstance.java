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
			// 创建socket，连接22222端口
		} catch (Exception e) {
		}
		try {
			ServerSocket server = new ServerSocket(55661);// 创建socket,在22222端口监听
			server.accept(); // 等待连接
			server.close(); // 有连接到来，也就是说有新的实例
			System.exit(0); // 这个实例退出
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
