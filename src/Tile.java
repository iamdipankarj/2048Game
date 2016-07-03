import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class Tile {
	
	public int value;
	
	public void addValue(int v) {
		value = v;
	}
	
	private static int offset(int arg) {
		return arg * (Game2048.TILES_MARGIN + Game2048.TILE_SIZE) + Game2048.TILES_MARGIN;
	}

	public Tile() {
		this(0);
	}

	public Tile(int num) {
		value = num;
	}

	public boolean isEmpty() {
		return value == 0;
	}

	public Color getForeground() {
		return value < 16 ? new Color(0x776e65) :	new Color(0xf9f6f2);
	}

	public Color getBackground() {
		switch (value) {
		case 2:	return new Color(0xeee4da);
		case 4:	return new Color(0xede0c8);
		case 8:	return new Color(0xf2b179);
		case 16:	 return new Color(0xf59563);
		case 32:	 return new Color(0xf67c5f);
		case 64:	 return new Color(0xf65e3b);
		case 128:	return new Color(0xedcf72);
		case 256:	return new Color(0xedcc61);
		case 512:	return new Color(0xedc850);
		case 1024: return new Color(0xedc53f);
		case 2048: return new Color(0xedc22e);
		}
		return new Color(0xcdc1b4);
	}
	
	public void draw(Graphics g2, int x, int y) {
		Graphics2D g = ((Graphics2D) g2);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
		int xOffset = offset(x);
		int yOffset = offset(y);
		g.setColor(getBackground());
		g.fillRect(xOffset, yOffset, Game2048.TILE_SIZE, Game2048.TILE_SIZE);
		g.setColor(getForeground());
		final int size = value < 100 ? 36 : value < 1000 ? 32 : 24;
		final Font font = new Font(Game2048.FONT_NAME, Font.BOLD, size);
		g.setFont(font);

		String s = String.valueOf(value);
		final FontMetrics fm = g.getFontMetrics(font);
		final int w = fm.stringWidth(s);
		final int h = -(int) fm.getLineMetrics(s, g).getBaselineOffsets()[2];

		if (value != 0)
			g.drawString(s, xOffset + (Game2048.TILE_SIZE - w) / 2, yOffset + Game2048.TILE_SIZE - (Game2048.TILE_SIZE - h) / 2 - 2);
	}

}


