/**
 * BrickGenerator class(abstract)
 * 1. draw method that takes ArrayList of rectangles and generates grid and bricks
 */
package application;

import java.util.ArrayList;
import javafx.scene.shape.Rectangle;

public abstract class BrickGenerator {
	//draw method
	public abstract void draw(ArrayList<Rectangle> bricks);
}
