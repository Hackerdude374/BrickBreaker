/**
 * EasyLevel class
 * 1. child class of BrickGenerator
 * 2. local variables
 * 3. Constructor
 * 4. draw method from parent class 
 */
package application;

import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class EasyLevel extends BrickGenerator {
	
	//Local variable
	private int grid[][];	//grid variable that is a 2D integer Array for the grid
	private int brickWidth;	//brick width variable
	private int brickHeight;	//brick height variable
	private Image bricksImage = new Image("C:\\Users\\bobby\\eclipse-workspace\\BreakOut (7).zip_expanded\\BreakOutTest\\application\\Image&CSS\\Brick.png");	//brick image variable
	private int current_window_width;
	private int current_window_height;
	
	//Constructor that takes in number of rows and number of columns for the grid
	public EasyLevel(int row, int col, int current_window_width, int current_window_height) {
		grid = new int[row][col];
		//generating the grid
		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid[0].length; j++) {
				grid[i][j] = 1;
			}
		}
		//get window size
		this.current_window_width = current_window_width;
		this.current_window_height = current_window_height;
		//setup brick width and height
		brickWidth = this.current_window_width / col;
		brickHeight = (this.current_window_height / 4) / row;
	}
	
	//draw method that draw rectangles into a ArrayList, will be implemented in GamePlay class
	@Override
	public void draw(ArrayList<Rectangle> bricks) {
		//generating bricks based on the grid
		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid[0].length; j++) {
				if (grid[i][j] > 0) {
					Rectangle current = new Rectangle(j * brickWidth, i * brickHeight, brickWidth - 5, brickHeight - 5);	//current rectangle variable that determines the x position, y position, width and height
					current.setFill(new ImagePattern(bricksImage));	//fill current rectangle with image
					bricks.add(current);	//add current rectangle into a ArrayList
				}
			}
		}
	}
}
