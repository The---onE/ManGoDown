package com.qust.go;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Board {
	public Image boardImage, board, stab_board;
	public int width;
	public int height;
	public int x;
	public int y;
	//记录板子类型
	public int type = 0;
	public boolean status = false; // 是否已加分
	
	public static final int WIDTH = 160;
	public static final int STEP = 4;
	
	public static final int NORMAL = 0;
	public static final int STAB = 1;
	
	public Board(int a, int b, int type) {
		stab_board = new ImageIcon("gameImages\\stab_board.png").getImage();
		board = new ImageIcon("gameImages\\board.png").getImage();
		if (type == NORMAL) {
			boardImage = board;
		}else if (type == STAB) {
			boardImage = stab_board;
		}
		x = a;
		y = b;
		this.type = type;
	}

	public void step() {
		y -= STEP;
	}
}
