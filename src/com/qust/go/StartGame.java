package com.qust.go;

import javax.swing.JFrame;

public class StartGame {
	
	public static final int WIDTH = 480;
	public static final int HEIGHT = 600;

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		//设置宽高
		frame.setSize(WIDTH, HEIGHT);
		//设置默认的关闭操作
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//设置位置和大小
		//frame.setBounds(500, 300, 440, 670);
		//默认居中
		frame.setLocationRelativeTo(null);
		//创建BirdGame对象
		Game game = new Game();
		//在窗体中添加面板
		frame.add(game);
		//设置可见
		frame.setVisible(true);
		game.action();
	}

}
