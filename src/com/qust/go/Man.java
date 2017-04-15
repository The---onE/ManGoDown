package com.qust.go;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Man {
	public Image mImage;
	public int x = StartGame.WIDTH / 2;
	public int y = 160;

	public int downState = NORMAL; // 下落状态
	public int moveState = NORMAL; // 左右移动状态

	public static final int LEFT = 1;
	public static final int RIGHT = 2;
	public static final int DOWN = 3;
	public static final int NORMAL = 0;

	public float vy = 0; // 当前掉落速度
	public static final float GRAVITY = 0.5f; // 重力加速度
	public static final int MOVE_SPEED = 10; // 左右移动速度
	
	public float life = 12; // 当前生命值
	public static float PER_LIFE = 0.08f; // 掉血状态每次损失的血量
	public static float UP_LIFE = 0.02f; // 回血状态每次恢复的血量

	public final static String[] LEFT_RES = { "gameImages\\manleft1.png", "gameImages\\manleft2.png",
			"gameImages\\manleft3.png" };
	public final static String[] RIGHT_RES = { "gameImages\\manright1.png", "gameImages\\manright2.png",
			"gameImages\\manright3.png" };
	public final static String[] DOWN_RES = { "gameImages\\mandown1.png", "gameImages\\mandown2.png",
			"gameImages\\mandown3.png" };
	public final static String[] NOR_RES = { "gameImages\\mandown3.png" };
	public static Image[] left = new Image[LEFT_RES.length];
	public static Image[] right = new Image[RIGHT_RES.length];
	public static Image[] down = new Image[DOWN_RES.length];
	public static Image[] normal = new Image[NOR_RES.length];
	public int frame = 0;

	public Man() {
		try {
			for (int i = 0; i < LEFT_RES.length; ++i) {
				left[i] = new ImageIcon(LEFT_RES[i]).getImage();
			}
			for (int i = 0; i < RIGHT_RES.length; ++i) {
				right[i] = new ImageIcon(RIGHT_RES[i]).getImage();
			}
			for (int i = 0; i < DOWN_RES.length; ++i) {
				down[i] = new ImageIcon(DOWN_RES[i]).getImage();
			}
			for (int i = 0; i < NOR_RES.length; ++i) {
				normal[i] = new ImageIcon(NOR_RES[i]).getImage();
			}
			mImage = normal[0];
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 帧动画
	 */
	public void frameAnime() {
		frame++;
		int index = frame / 8;
		if (downState == DOWN) {
			mImage = down[index % down.length];
		} else {
			switch (moveState) {
			case LEFT:
				mImage = left[index % left.length];
				break;
			case RIGHT:
				mImage = right[index % right.length];
				break;
			case NORMAL:
				mImage = normal[index % normal.length];
			default:
				break;
			}
		}
	}

	/**
	 * 左右移动
	 */
	public void move() {
		switch (moveState) {
		case LEFT:
			x -= MOVE_SPEED;
			if (x + mImage.getWidth(null) < 0) {
				x = 600;
			}
			break;
		case RIGHT:
			x += MOVE_SPEED;
			if (x > 600) {
				x = -mImage.getWidth(null);
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 下落
	 */
	public void down() {
		if (downState == DOWN) {
			vy -= GRAVITY;
			y -= vy;
		}
	}
	
	/**
	 * 损失生命
	 */
	public void loseLife() {
		life -= PER_LIFE;
		if (life <= 0) {
			life = 0;
		}
	}
	
	/**
	 * 恢复生命
	 */
	public void upLife() {
		life += UP_LIFE;
		if (life >= 12) {
			life = 12;
		}
	}
	
	/**
	 * 落到板子上恢复
	 */
	public void resume() {
		vy = 0;
		downState = NORMAL;
	}
}
