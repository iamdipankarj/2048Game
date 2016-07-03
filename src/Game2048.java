/**
 * @author Dipankar Jana
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Game2048 extends JPanel {
	private static final long serialVersionUID = 1L;
	public static final Color BACKGROUND = new Color(0xbbada0);
	public static final String FONT_NAME = "Arial";
	public static final int TILE_SIZE = 64;
	public static final int TILES_MARGIN = 16;

	private Tile[] tiles;
	boolean isWinning = false;
	boolean isLossing = false;
	int highScore = 0;

	/**
	 * Constructor for initializing the key listeners
	 */
	public Game2048() {
		setFocusable(true);
		addKeyListener(new KeyAdapter() {
		
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					resetGame();
				}
				if (!isMovePossible()) {
					isLossing = true;
				}
	
				if (!isWinning && !isLossing) {
					switch (e.getKeyCode()) {
					case KeyEvent.VK_LEFT:
						left();
						break;
					case KeyEvent.VK_RIGHT:
						right();
						break;
					case KeyEvent.VK_DOWN:
						down();
						break;
					case KeyEvent.VK_UP:
						up();
						break;
					}
				}
	
				if (!isWinning && !isMovePossible()) {
					isLossing = true;
				}
	
				repaint();
			}
		});
		resetGame();
	}

	public void resetGame() {
		highScore = 0;
		isWinning = false;
		isLossing = false;
		tiles = new Tile[4 * 4];
		for (int i = 0; i < tiles.length; i++) {
			tiles[i] = new Tile();
		}
		addTile();
		addTile();
	}

	public void left() {
		boolean needTile = false;
		for (int i = 0; i < 4; i++) {
			Tile[] line = new Tile[4];
			for (int j = 0; j < 4; j++) {
				line[j] = getTileFromPosition(j, i);
			}
			
			Tile[] merged = addLine(movePlayer(line));
			setLine(i, merged);
			if (!needTile && !compareTiles(line, merged)) {
				needTile = true;
			}
		}

		if (needTile) {
			addTile();
		}
	}

	public void right() {
		tiles = rotate(180);
		left();
		tiles = rotate(180);
	}

	public void up() {
		tiles = rotate(270);
		left();
		tiles = rotate(90);
	}

	public void down() {
		tiles = rotate(90);
		left();
		tiles = rotate(270);
	}

	private Tile getTileFromPosition(int x, int y) {
		return tiles[x + y * 4];
	}

	private void addTile() {
		List<Tile> list = getFreeSpace();
		if (!getFreeSpace().isEmpty()) {
			int index = (int) (Math.random() * list.size()) % list.size();
			Tile tile = list.get(index);
			tile.addValue(Math.random() < 0.9 ? 2 : 4);
		}
	}

	private List<Tile> getFreeSpace() {
		final List<Tile> list = new ArrayList<Tile>(16);
		for (Tile t : tiles) {
			if (t.isEmpty()) {
			list.add(t);
			}
		}
		return list;
	}

	private boolean isFull() {
		return getFreeSpace().size() == 0;
	}

	public boolean isMovePossible() {
		if (!isFull()) return true;
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				Tile t = getTileFromPosition(x, y);
				if ((x < 3 && t.value == getTileFromPosition(x + 1, y).value)
					|| ((y < 3) && t.value == getTileFromPosition(x, y + 1).value)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean compareTiles(Tile[] l1, Tile[] l2) {
		if (l1 == l2) return true;
		else if (l1.length != l2.length) return false;
		for (int i = 0; i < l1.length; i++) {
			if (l1[i].value != l2[i].value) return false;
		}
		return true;
	}

	private Tile[] rotate(int angle) {
		Tile[] newTiles = new Tile[4 * 4];
		int offsetX = 3, offsetY = 3;
		if (angle == 90) {
			offsetY = 0;
		} else if (angle == 270) {
			offsetX = 0;
		}

		double rad = Math.toRadians(angle);
		int cos = (int) Math.cos(rad);
		int sin = (int) Math.sin(rad);
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				int newX = (x * cos) - (y * sin) + offsetX;
				int newY = (x * sin) + (y * cos) + offsetY;
				newTiles[(newX) + (newY) * 4] = getTileFromPosition(x, y);
			}
		}
		return newTiles;
	}

	private Tile[] movePlayer(Tile[] oldLine) {
		LinkedList<Tile> l = new LinkedList<Tile>();
		for (int i = 0; i < 4; i++) {
			if (!oldLine[i].isEmpty())
			l.addLast(oldLine[i]);
		}
		if (l.size() == 0) {
			return oldLine;
		} else {
			Tile[] newLine = new Tile[4];
			checkSizeLimit(l, 4);
			for (int i = 0; i < 4; i++) newLine[i] = l.removeFirst();
			return newLine;
		}
	}

	private Tile[] addLine(Tile[] oldLine) {
		LinkedList<Tile> list = new LinkedList<Tile>();
		for (int i = 0; i < 4 && !oldLine[i].isEmpty(); i++) {
			int num = oldLine[i].value;
			if (i < 3 && oldLine[i].value == oldLine[i + 1].value) {
				num *= 2;
				highScore += num;
				int ourTarget = 2048;
				if (num == ourTarget) {
					isWinning = true;
				}
				i++;
			}
			list.add(new Tile(num));
		}
		if (list.size() == 0) {
			return oldLine;
		} else {
			checkSizeLimit(list, 4);
			return list.toArray(new Tile[4]);
		}
	}

	public static void checkSizeLimit(java.util.List<Tile> l, int s) {
		while (l.size() != s) {
			l.add(new Tile());
		}
	}

	public void setLine(int index, Tile[] re) {
		System.arraycopy(re, 0, tiles, index * 4, 4);
	}

	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(BACKGROUND);
		g.fillRect(0, 0, this.getSize().width, this.getSize().height);
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				tiles[x + y * 4].draw(g, x, y);
				checkGame(g);
			}
		}
	}

	public void checkGame(Graphics g2) {
		Graphics2D g = ((Graphics2D) g2);
		if (isWinning || isLossing) {
			g.setColor(new Color(255, 255, 255, 30));
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(new Color(68, 140, 215));
			g.setFont(new Font(FONT_NAME, Font.BOLD, 48));
			if (isWinning) {
				g.drawString("You Won!", 68, 150);
			}
			if (isLossing) {
				g.drawString("Game over!", 50, 130);
			}
			if (isWinning || isLossing) {
				g.setFont(new Font(FONT_NAME, Font.PLAIN, 16));
				g.setColor(new Color(128, 128, 128, 128));
				g.drawString("Press RETURN to play again", 80, getHeight() - 40);
			}
		}
		g.setColor(new Color(255, 255, 255, 255));
		g.setFont(new Font(FONT_NAME, Font.ITALIC, 18));
		g.drawString("Score: " + highScore, 10, 365);
	}

	public static void main(String[] args) {
		JFrame game = new JFrame();
		game.setTitle("2048 Game");
		game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		game.setSize(340, 400);
		game.setResizable(false);

		game.add(new Game2048());

		game.setLocationRelativeTo(null);
		game.setVisible(true);
	}
}