package com.tsunderebug.mazestuff.mazes.tri;

import com.tsunderebug.mazestuff.Cell;
import com.tsunderebug.mazestuff.cells.TriangularCell;
import com.tsunderebug.mazestuff.utils.Dijkstra;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RectangularTriangularCellMaze implements TriangularCellMaze {

	protected final int w;
	protected final int h;
	protected final TriangularCell[][] cells;

	public RectangularTriangularCellMaze(int w, int h) {
		this.w = w * 2 - 1;
		this.h = h;
		this.cells = new TriangularCell[this.w][h];
		for(int x = 0; x < this.w; x++) {
			for(int y = 0; y < h; y++) {
				this.cells[x][y] = new TriangularCell(this, x, y);
			}
		}
	}

	@Override
	public TriangularCell position(int x, int y) {
		if (x >= 0 && y >= 0 && x < w && y < h) {
			return cells[x][y];
		}
		return null;
	}

	@Override
	public Set<TriangularCell> cells() {
		return new HashSet<>(Arrays.stream(this.cells).flatMap(Arrays::stream).collect(Collectors.toList()));
	}

	@Override
	public TriangularCell startCell() {
		return this.cells[0][0];
	}

	public BufferedImage toImage(int linethickness, int cellsize, int unused, boolean color, Color bright) {
		double th = (Math.sqrt(3) / 2 * cellsize);

		BufferedImage c = new BufferedImage((w / 2 + 1) * (cellsize + linethickness) + linethickness, (int) (h * (th + linethickness) + linethickness), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) c.getGraphics();
		g.setBackground(Color.white);
		g.fillRect(0, 0, c.getWidth(), c.getHeight());
		g.setColor(Color.black);
		g.setStroke(new BasicStroke(linethickness));
		Map<TriangularCell, Integer> d = Dijkstra.forMaze(this);
		int m = d.values().stream().max(Integer::compareTo).get();
		for(int x = 0; x < w; x++) {
			for(int y = 0; y < h; y++) {
				TriangularCell cell = cells[x][y];
				if(cell != null) {
					double x1 = (cell.getX() / 2) * (cellsize + linethickness);
					double x2 = (cell.getX() / 2) * (cellsize + linethickness) + (cellsize + linethickness) / 2;
					double x3 = ((cell.getX() / 2) + 1) * (cellsize + linethickness);
					double y1 = y * (th + linethickness);
					double y2 = (y + 1) * (th + linethickness);
					if(y % 2 == 1) {
						x1 -= cellsize / 2;
						x2 -= cellsize / 2;
						x3 -= cellsize / 2;
					}
					Path2D p = new Path2D.Double();
					if(cell.isUpwards()) {
						if(cell.getX() % 2 == 1) {
							x1 += cellsize;
							x2 += cellsize;
							x3 += cellsize;
						}
						p.moveTo(x3 + linethickness, y2);
						p.lineTo(x1 - linethickness, y2);
						p.lineTo(x2, y1);
						p.lineTo(x3 + linethickness, y2);
					} else {
						x1 += cellsize / 2;
						x2 += cellsize / 2;
						x3 += cellsize / 2;
						p.moveTo(x3 + linethickness, y1);
						p.lineTo(x1 - linethickness, y1);
						p.lineTo(x2, y2);
						p.lineTo(x3 + linethickness, y1);
					}
					Color newc = Color.white;
					if (color) {
						try {
							int l = d.get(cell);
							int newr = (int) (bright.getRed() * ((double) (m - l) / (double) m));
							int newg = (int) (bright.getGreen() * ((double) (m - l) / (double) m));
							int newb = (int) (bright.getBlue() * ((double) (m - l) / (double) m));
							newc = new Color(newr, newg, newb);
						} catch (NullPointerException e) {
							System.out.println("Cell at " + x + ", " + y + " is unreachable!");
							newc = Color.red;
						}
					}
					g.setColor(newc);
					g.fill(p);
					g.setColor(Color.black);
					if (cell.isUpwards()) {
						if (cell.south() == null || !cell.connections().contains(cell.south())) {
							g.draw(new Line2D.Double(x1, y2, x3, y2));
						}
						if (cell.northWest() == null || !cell.connections().contains(cell.northWest())) {
							g.draw(new Line2D.Double(x1, y2, x2, y1));
						}
						if (cell.northEast() == null || !cell.connections().contains(cell.northEast())) {
							g.draw(new Line2D.Double(x2, y1, x3, y2));
						}
					} else {
						if (cell.north() == null || !cell.connections().contains(cell.north())) {
							g.draw(new Line2D.Double(x1, y1, x3, y1));
						}
						if (cell.southWest() == null || !cell.connections().contains(cell.southWest())) {
							g.draw(new Line2D.Double(x1, y1, x2, y2));
						}
						if (cell.southEast() == null || !cell.connections().contains(cell.southEast())) {
							g.draw(new Line2D.Double(x2, y2, x3, y1));
						}
					}
				}
			}
		}
		return c;
	}

}
