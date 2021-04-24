import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Pong extends JPanel {
	public static int level = 1;
	public static int topScore = 0;
	public static int bottomScore = 0;
	public static JFrame f = new JFrame();
	public static JButton decreaseLevel = new JButton();
	public static JButton increaseLevel = new JButton();
	public static JButton pauseButton = new JButton();
	public static JButton speedUpButton = new JButton();
	public static JLabel topbar = new JLabel(
			"Computer: "
					+ Pong.topScore
					+ "          Player: "
					+ Pong.bottomScore
					+ "                                                             Level: "
					+ Pong.level);
	public static boolean pause = false;
	public static JButton takeOverButton = new JButton();
	public static boolean takeover = false;
	static int speedUp = 0;

	public static void main(String[] a) {
		mainGUI();
	}

	public static void mainGUI() {
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem newGame = new JMenuItem("New Game");
		JMenuItem quit = new JMenuItem("Quit");
		JMenuItem saveGame = new JMenuItem("Save Game");
		JMenuItem loadGame = new JMenuItem("Load Game");
		fileMenu.add(newGame);
		fileMenu.add(saveGame);
		fileMenu.add(loadGame);
		fileMenu.add(quit);
		menuBar.add(fileMenu);
		f.setJMenuBar(menuBar);

		newGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				resetAll();
			}
		});

		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				f.dispose();
			}
		});

		saveGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					saveGame("game.txt", "" + level);
				} catch (IOException e) {
					System.out.println("Error Saving");
				}
			}
		});

		loadGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				loadGame("game.txt");
				Pong.topbar
						.setText("Computer: "
								+ Pong.topScore
								+ "          Player: "
								+ Pong.bottomScore
								+ "                                                             Level: "
								+ Pong.level);
			}
		});

		f.setBackground(Color.white);
		f.setSize(500, 540);
		f.setTitle("Pong");
		topbar.setBounds(0, 0, 500, 25);
		decreaseLevel.setBounds(0, 400, 200, 100);
		increaseLevel.setBounds(200, 400, 200, 100);
		pauseButton.setBounds(400, 25, 100, 200);
		pauseButton.setText("Pause");
		pauseButton.setBackground(Color.darkGray);
		pauseButton.setOpaque(true);
		takeOverButton.setBounds(400, 200, 100, 200);
		takeOverButton.setText("Take Over");
		takeOverButton.setBackground(Color.darkGray);
		takeOverButton.setOpaque(true);
		speedUpButton.setBounds(400, 400, 100, 100);
		speedUpButton.setText("Speed Up");
		speedUpButton.setBackground(Color.darkGray);
		speedUpButton.setOpaque(true);
		decreaseLevel.setBackground(Color.darkGray);
		decreaseLevel.setText("Level--");
		decreaseLevel.setOpaque(true);
		increaseLevel.setBackground(Color.darkGray);
		increaseLevel.setText("Level++");
		increaseLevel.setOpaque(true);
		f.add(topbar);
		f.add(pauseButton);
		f.add(takeOverButton);
		f.add(decreaseLevel);
		f.add(increaseLevel);
		f.add(speedUpButton);
		topbar.setBackground(Color.CYAN);
		topbar.setOpaque(true);
		f.add(new Move());
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}

	public static void resetAll() {
		level = 1;
		topScore = 0;
		bottomScore = 0;
		Pong.topbar
				.setText("Computer: "
						+ Pong.topScore
						+ "          Player: "
						+ Pong.bottomScore
						+ "                                                             Level: "
						+ Pong.level);

	}

	public static void saveGame(String fileName, String text)
			throws IOException {
		System.out.println("Saving");
		File game = new File(fileName);
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(
				fileName, false)));
		out.write(text);
		out.close();
	}

	public static void loadGame(String fileName) {
		Scanner sc = new Scanner(fileName);
		Pong.level = sc.nextInt();
		sc.close();
	}

}

@SuppressWarnings("serial")
class Move extends JComponent implements KeyListener{

	static Graphics2D gg;
	static Graphics2D ff;
	static int ballxplace = 100;
	static int ballyplace = 100;
	static boolean ballright = true;
	static boolean ballup = false;
	static int toprectxplace = 157;
	static int bottomrectxplace = 157;
	static boolean toprectright = true;
	static boolean bottomrectright = false;
	static int block = 0;
	static boolean allowLevelChange = true;
	static double velx = 0;

	public Move() {
		addKeyListener(this);
		setFocusable(true);
		Thread animationThread = new Thread(new Runnable() {
			public void run() {
				while (true) {
					repaint();
					try {
						if (Pong.level + Pong.speedUp > 12) {
							Thread.sleep(1);
						} else {
							Thread.sleep(5 - (Pong.level + Pong.speedUp / 3));
						}
					} catch (Exception ex) {
					}
				}
			}
		});

		Pong.pauseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (Pong.pause) {
					Pong.pause = false;
				} else {
					Pong.pause = true;
				}

			}
		});

		Pong.takeOverButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (Pong.takeover) {
					Pong.takeover = false;
				} else {
					Pong.takeover = true;
				}

			}
		});

		Pong.speedUpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Pong.speedUp++;

			}
		});

		animationThread.start();
	}

	public void paint(Graphics g) {
		gg = (Graphics2D) g;

		ff = (Graphics2D) g;
		Graphics2D ss = (Graphics2D) g;

		Random gen = new Random();

		if (Pong.bottomScore == 5) {
			Pong.topScore = 0;
			Pong.bottomScore = 0;
			Pong.level++;
			Pong.topbar
					.setText("Computer: "
							+ Pong.topScore
							+ "          Player: "
							+ Pong.bottomScore
							+ "                                                             Level: "
							+ Pong.level);
		} else if (Pong.topScore == 5) {
			Pong.f.dispose();
			JFrame frame = new JFrame("Game Over.");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(250, 100);
			frame.setLocationRelativeTo(null);
			Container pane = frame.getContentPane();
			pane.setLayout(new GridLayout(1, 1));
			JLabel loser = new JLabel(" You lose! You made it to level "
					+ Pong.level + "!");
			loser.setBackground(Color.cyan);
			loser.setOpaque(true);
			pane.add(loser);
			frame.setVisible(true);
		}

		if (block != 2 + Pong.level) {
			if (toprectxplace > ballxplace - 50) {
				toprectright = false;

			} else if (toprectxplace < ballxplace - 50) {
				toprectright = true;
			}

			if (toprectright) {
				toprectxplace += 2;
			} else {
				toprectxplace -= 2;
			}
		} else {
			if (toprectxplace > 299) {
				toprectright = false;

			} else if (toprectxplace < 1) {
				toprectright = true;
			}

			if (toprectright) {
				toprectxplace += 2;
			} else {
				toprectxplace -= 2;
			}
		}

		gg.fillRect(toprectxplace, 30, 100, 10);

		Pong.decreaseLevel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (allowLevelChange) {
					if (Pong.level != 1) {
						System.out.println("Level Down");
						Pong.level--;
						allowLevelChange = false;
						Pong.topbar
								.setText("Computer: "
										+ Pong.topScore
										+ "          Player: "
										+ Pong.bottomScore
										+ "                                                             Level: "
										+ Pong.level);
					}

				}
			}
		});

		Pong.increaseLevel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (allowLevelChange) {
					Pong.level++;
					allowLevelChange = false;
					Pong.topbar
							.setText("Computer: "
									+ Pong.topScore
									+ "          Player: "
									+ Pong.bottomScore
									+ "                                                             Level: "
									+ Pong.level);

				}
			}
		});

		if (Pong.takeover) {
			int temp = 0;
			if (Pong.level > 16) {
				temp = 1;
			} else {
				temp = (5 - (Pong.level / 4));
			}
			if (bottomrectxplace > ballxplace - 50) {
				bottomrectright = false;

			} else if (bottomrectxplace < ballxplace - 50) {
				bottomrectright = true;
			}

			if (bottomrectright) {
				bottomrectxplace += 2;
			} else {
				bottomrectxplace -= 2;
			}
		}
		
		bottomrectxplace += velx;

		ff.fillRect(bottomrectxplace, 350, 100, 10);

		if (!Pong.pause) {

			if (ballxplace >= 390) {
				ballright = false;

			} else if (ballxplace <= 0) {
				ballright = true;
			}

			if (ballright) {
				ballxplace += 2;
			} else {
				ballxplace -= 2;
			}

			if (ballyplace == 350
					&& (bottomrectxplace + 101 > ballxplace && bottomrectxplace - 1 < ballxplace)) {
				ballup = false;
				allowLevelChange = true;
				Pong.topbar
				.setText("Computer: "
						+ Pong.topScore
						+ "          Player: "
						+ Pong.bottomScore
						+ "                                                             Level: "
						+ Pong.level);
				block = gen.nextInt(3 + Pong.level);
			} else if (ballyplace == 30
					&& (toprectxplace + 101 > ballxplace && toprectxplace - 1 < ballxplace)) {
				ballup = true;
				allowLevelChange = true;
				
				Pong.topbar
				.setText("Computer: "
						+ Pong.topScore
						+ "          Player: "
						+ Pong.bottomScore
						+ "                                                             Level: "
						+ Pong.level);

			}

			if (ballyplace >= 368) {
				ballup = false;
				allowLevelChange = true;
				block = gen.nextInt(3 + Pong.level);
				Pong.topScore++;
				Pong.topbar
						.setText("Computer: "
								+ Pong.topScore
								+ "          Player: "
								+ Pong.bottomScore
								+ "                                                             Level: "
								+ Pong.level);
			} else if (ballyplace <= 0) {
				ballup = true;
				allowLevelChange = true;
				Pong.bottomScore++;
				Pong.topbar
						.setText("Computer: "
								+ Pong.topScore
								+ "          Player: "
								+ Pong.bottomScore
								+ "                                                             Level: "
								+ Pong.level);
			}

			if (ballup) {
				ballyplace += 2;
			} else {
				ballyplace -= 2;
			}
		}

		ss.fillOval(ballxplace, ballyplace, 10, 10);

	}

	public static void left() {
		if (Move.bottomrectxplace > 0) {
			velx = -3;
		}else{
			velx = 0;
		}
	}

	public static void right() {
		if (Move.bottomrectxplace < 300) {
			velx = 3.5;
		}else{
			velx = 0;
		}
	}

	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			if (Move.bottomrectxplace > 0) {
				Move.left();
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {

			if (Move.bottomrectxplace < 300) {
				Move.right();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT
				|| e.getKeyCode() == KeyEvent.VK_LEFT) {
			velx = 0;
		}
	}
}
