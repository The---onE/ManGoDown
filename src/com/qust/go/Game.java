package com.qust.go;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * 继承Jpanel(面板)
 * 
 * @author Hey
 *
 */
@SuppressWarnings("serial")
public class Game extends JPanel {
	// 声明图片
	private Image bgImage, overImage;
	private int score = 0;

	private Man man;
	private int count = 0;
	private List<Board> boards = new LinkedList<>();

	public final static String[] LIFE_RES = { "gameImages\\blood0.png", "gameImages\\blood1.png",
			"gameImages\\blood2.png", "gameImages\\blood3.png", "gameImages\\blood4.png", "gameImages\\blood5.png",
			"gameImages\\blood6.png", "gameImages\\blood7.png", "gameImages\\blood8.png", "gameImages\\blood9.png",
			"gameImages\\blood10.png", "gameImages\\blood11.png", "gameImages\\blood12.png", };
	public static Image[] life = new Image[LIFE_RES.length];

	private Random random = new Random();

	// 声明游戏状态的变量
	private int state = 0;
	// 游戏开始
	private static final int START = 1;
	// 游戏运行
	private static final int RUNNING = 2;
	// 游戏结束
	private static final int GAMEOVER = 3;

	private static final int TYPES_COUNT = 3;
	private int index = TYPES_COUNT;
	private int[] types = new int[TYPES_COUNT];

	// 构造方法：修饰符+类名+参数
	public Game() {
		// setBackground(Color.gray);
		state = START;

		bgImage = new ImageIcon("gameImages\\bg.png").getImage();
		overImage = new ImageIcon("gameImages\\gameover.png").getImage();
		for (int i = 0; i < LIFE_RES.length; ++i) {
			life[i] = new ImageIcon(LIFE_RES[i]).getImage();
		}
		man = new Man();
		Board board = new Board(man.x - 50, man.y + man.mImage.getHeight(null), 0);
		boards.add(board);
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		// 在面板上画图片；1.要画的资源;2、3.起始点xy坐标;4、5宽高.
		// 6.当资源发生改变时要通知的对象
		switch (state) {
		case START:
			g.drawImage(new ImageIcon("images\\启动页.jpg").getImage(), 0, 0, getWidth(), getHeight(), null);
			g.drawImage(new ImageIcon("images\\LOGO.png").getImage(), 50, 360, 150, 160, null);
			break;
		case RUNNING:
			g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), null);
			g.drawImage(man.mImage, man.x, man.y, null);
			g.drawImage(life[(int) man.life], 100, 100, null);
			for (Board board : boards) {
				g.drawImage(board.boardImage, board.x, board.y, Board.WIDTH, board.boardImage.getHeight(null), null);
			}
			break;
		case GAMEOVER:
			g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), null);
			g.drawImage(overImage, 0, 0, getWidth(), getHeight(), null);
			try {

			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
		setScore(g);
	}

	/**
	 * 用来执行动作
	 */
	public void action() {
		// 键盘监听
		addKeyListener(new MyKeyListener());
		// 获取焦点
		setFocusable(true);
		requestFocus();

		while (true) {
			switch (state) {
			case START:
				break;
			case RUNNING:
				// 保存移动前的状态
				int mx = man.x;
				int my = man.y;

				// 人物动作处理
				man.frameAnime();
				man.move();
				man.down();
				// man.loseLife();

				// 板子处理
				count++;
				if (count >= 30) {
					count = 0;
					index++;
					if (index >= TYPES_COUNT) {
						index = 0;
						for (int i = 0; i < TYPES_COUNT; ++i) {
							types[i] = Board.NORMAL;
						}
						int i = random.nextInt(TYPES_COUNT);
						types[i] = Board.STAB;
					}
					Board board = new Board(random.nextInt(StartGame.WIDTH - Board.WIDTH), StartGame.HEIGHT,
							types[index]);
					boards.add(board);
				}
				for (Board board : boards) {
					board.step();
				}
				for (Board board : boards) {
					if (board.y + board.boardImage.getHeight(null) < 0) {
						boards.remove(board);
						break;
					}
				}

				// 逻辑判断
				if (man.y > getHeight() || man.y + man.mImage.getHeight(null) < 0) {
					state = GAMEOVER;
				}
				boolean flag = false; // 是否在板子上
				for (Board board : boards) {
					// 板子移动前的位置
					int bx = board.x;
					int by = board.y + Board.STEP;
					// 移动前是否在范围内
					boolean beforeFlag = mx + man.mImage.getWidth(null) >= bx && mx <= bx + Board.WIDTH;
					// 移动后是否在范围内
					boolean afterFlag = man.x + man.mImage.getWidth(null) >= board.x && man.x <= board.x + Board.WIDTH;
					if (beforeFlag || afterFlag) {
						// 人在板子的竖直范围内
						if (my <= by && man.y + man.mImage.getHeight(null) >= board.y) {
							// 人落在板子上
							man.y = board.y - man.mImage.getHeight(null);
							if (board.type == Board.STAB) {
								man.loseLife();
							} else {
								man.upLife();
							}
							man.resume();
							flag = true;
						}
					}
					if (man.y > board.y) {
						// 人已越过板子
						if (!board.status) {
							score++;
							board.status = true;
						}
					}
				}
				if (!flag) {
					man.downState = Man.DOWN;
				}
				if (man.life < 1) {
					state = GAMEOVER;
				}
				break;
			default:
				break;
			}
			// 重新执行paint方法
			repaint();
			// 调用当前线程的休眠方法
			try {
				Thread.sleep(1000 / 60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void setScore(Graphics g) {
		// 设置字体
		Font font = new Font(Font.SERIF, Font.BOLD, 40);
		g.setFont(font);
		// 添加阴影
		g.setColor(Color.GRAY);
		g.drawString("Score:" + score, 37, 57);
		// 绘制文字
		g.setColor(Color.WHITE);
		g.drawString("Score:" + score, 40, 60);
	}

	class MyKeyListener extends KeyAdapter {

		@Override
		public void keyReleased(KeyEvent e) {
			super.keyReleased(e);
			// 松开
			switch (state) {
			case START:
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					state = RUNNING;
				}
				break;
			case RUNNING:
				man.moveState = Man.NORMAL;
				break;
			case GAMEOVER:
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					state = START;
					score = 0;
					man = new Man();
					// 清空上升板列表
					boards.clear();
					Board board = new Board(man.x - 50, man.y + man.mImage.getHeight(null), 0);
					boards.add(board);
				}
				break;
			}
		}

		@Override
		public void keyPressed(KeyEvent e) {
			super.keyPressed(e);
			// 按下
			switch (state) {
			case START:
				break;
			case RUNNING:
				switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					// ←
					man.moveState = Man.LEFT;
					break;
				case KeyEvent.VK_RIGHT:
					// →
					man.moveState = Man.RIGHT;
					break;
				default:
					break;
				}
				break;
			case GAMEOVER:
				break;
			}
		}

	}
}
