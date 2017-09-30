package com.tsunderebug.mazegen;

import com.tsunderebug.mazestuff.DrawableMaze;
import com.tsunderebug.mazestuff.algorithms.RecursiveBacktracker;
import com.tsunderebug.mazestuff.mazes.MaskedRectangularSquareCellMaze;
import com.tsunderebug.mazestuff.mazes.RectangularSquareCellMaze;
import com.tsunderebug.mazestuff.mazes.hex.RectangularHexagonalCellMaze;
import com.tsunderebug.mazestuff.mazes.tri.TriangularCellMaze;
import com.tsunderebug.mazestuff.mazes.tri.TriangularTriangularCellMaze;
import com.tsunderebug.mazestuff.utils.Mask;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		DrawableMaze m = new RectangularHexagonalCellMaze(10, 10);
		new RecursiveBacktracker().apply(m);
		ImageIO.write(m.toImage(10, 50, true, new Color(0x7289DA)), "png", new File("hex.png"));
	}

}
