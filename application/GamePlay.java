/**
 * GamePlay Class
 * 1. implements initializable for initializing variables, serializable for serializing data and ICollision for checking collisions of objects
 * 2. Objects in the Game Scene
 * 3. Connect methods with different scene objects
 * 4. Necessary methods
 * 5. Necessary variables
 */
package application;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.robot.Robot;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GamePlay implements Initializable, Serializable, ICollision {
	
	/**
	 * Serializable Version
	 */
	private static final long serialVersionUID = 1L;
	
	@FXML
    private AnchorPane GameScene;	//GameScene anchor pane, four borders, one paddle, one ball and two Vertical Boxes
    @FXML
    private VBox GameOverVBox;	//GameOver Vertical Box, visible only when game over, contains two buttons and one label
    	@FXML
    	private Label GameOverLabel;	//game over label that shows "Game Over"
    	@FXML
    	private Label GameOverPoints;	//game over points label that shows the points earned
	    @FXML
	    private Button start_button;	//start button that decides to start over again
	    @FXML
	    private Button return_button;	//return button that decides to return to home scene
	@FXML
	private VBox GamePausedVBox;	//GamePaused Vertical Box, visible only when game being paused by player
	    @FXML
	    private Button continue_button;	//continue button that resumes the game
	    @FXML
	    private Button return_button1;	//different return button that decides to return to home scene, with saved game data
	    
	    @FXML
	    private Circle ball;	//ball 2D circle, moves according to the collision
	    @FXML
	    private Rectangle paddle;	//paddle 2D rectangle, moves along with cursor
	    @FXML
	    private Rectangle top_border;	//top border bounces the ball back
	    @FXML
	    private Rectangle left_border;	//left border bounces the ball back
	    @FXML
	    private Rectangle right_border;	//right border bounces the ball back
	    @FXML
	    private Rectangle bottom_border;	//bottom border shows game over when ball collide with it
	    
	private Robot robot = new Robot();	//robot that captures mouse movement without requiring scene instance
	
	private Image bricksImage = new Image("C:\\Users\\bobby\\eclipse-workspace\\BreakOut (7).zip_expanded\\BreakOutTest\\application\\Image&CSS\\Brick.png");	//brick image from local file
	
	private final static int DEFAULT_WIDTH = 800;		//default width of the application
	private final static int DEFAULT_HEIGHT = 600;		//default height of the application
	private final static int thirteen = 1280;		//width 1280
	private final static int eleven = 1067;	//width 1067
	private final static int eight = 800;	//width 800
	private final static int nine = 900;	//height 900
	private final static int six = 600;	//height 600
	private static int current_window_width = getDefaultWidth();	//current window width variable
	private static int current_window_height = getDefaultHeight();	//current window height variable
	private static int previous_window_width = getDefaultWidth();	//previous window width variable
	private static int previous_window_height = getDefaultHeight();	//previous window height variable
	
	public static boolean fullScreen = false;		//check if full screen is on or off, default to off
	public static boolean previousGame = false;	//check if there is previous game saved
	public static boolean loadGame = false;	//check if load/continue button is pressed or not
	public static boolean brickResize = false;	//check if brickResizing is needed or not
	
    private ArrayList<Rectangle> bricks = new ArrayList<>();	//ArrayList full of Rectangles
    private static int numberBricks = 0;	//counts the number of bricks
    private int current_points = 0;	//counts current point
    private double deltaX = 1;	//ball X movement direction
    private double deltaY = 2.25;	//ball Y movement direction

    //Define the free-movement of paddle and ball, checks if all the bricks were destroyed
    private Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
        	movePaddle();	//move the paddle along with cursor
            checkCollision(paddle);	//check collision of the paddle
            
            ball.setLayoutX(ball.getLayoutX() + deltaX);	//ball X movement
            ball.setLayoutY(ball.getLayoutY() + deltaY);	//ball Y movement

            //check if ArrayList bricks not empty
            if(!bricks.isEmpty()){
            	//removes any brick from ArrayList when it collides with the ball
                bricks.removeIf(brick -> checkCollisionBricks(brick));
            } else {
            	//all bricks got destroyed
                timeline.stop();	//time line will stop
				ball.setVisible(false);
				GameOverPoints.setText(String.valueOf(current_points));
				GameOverVBox.setVisible(true);
            }
            
            //set the borders with window size adjustments
            top_border.setWidth(current_window_width);
            bottom_border.setWidth(current_window_width);
            bottom_border.setLayoutY(current_window_height);
            left_border.setHeight(current_window_height);
            right_border.setHeight(current_window_height);
            right_border.setLayoutX(current_window_width);
            
            //set the paddle with window size adjustments
            paddle.setLayoutY(current_window_height - 100);
            
            //check collisions of the borders
            checkCollision(top_border);
            checkCollision(left_border);
            checkCollision(right_border);
            checkCollision(bottom_border);

            //detects the game pause key
            GUIcontrolls.scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
    			@Override
    			public void handle(KeyEvent event) {
    				switch(event.getCode()) {
    				//if "P" is pressed
    				case P:
    					timeline.pause();
    					GamePausedVBox.setVisible(true);
    				//default case
    				default:
    					break;
    				}
    			}
            });
        }
    }));

    //initialize method, initializing variables
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    	Image ballImage = new Image("C:\\Users\\bobby\\eclipse-workspace\\BreakOut (7).zip_expanded\\BreakOutTest\\application\\Image&CSS\\Ball.png");		//ball image
    	Image paddleImage = new Image("C:\\Users\\bobby\\eclipse-workspace\\BreakOut (7).zip_expanded\\BreakOutTest\\application\\Image&CSS\\Paddle.png");	//paddle image
    	ball.setFill(new ImagePattern(ballImage));	//fill ball with ball image
    	paddle.setFill(new ImagePattern(paddleImage));	//fill paddle with paddle image
    	
    	//generating bricks depends on new game or previous game
    	if (previousGame == true && loadGame == true) {
			loadBricks();
    	} else {
    		EasyLevel level1 = new EasyLevel(3, 7, current_window_width, current_window_height);
    		level1.draw(bricks);
    		for(Rectangle brick : bricks) {
    			GameScene.getChildren().add(brick);
    		}
    	}
    	
    	//set time line variable loop infinite times
    	timeline.setCycleCount(Animation.INDEFINITE);
    	//starts the time line
    	timeline.play();
    	
    }
    
    //load previous game bricks
    public void loadBricks() {
    	//check if bricks need to be resized
    	if((previous_window_width != current_window_width || previous_window_height != current_window_height) && brickResize == true) {	//resizing required
    		setBrickResize(false);	//brick already resizing
    		try {
				FileInputStream fis = new FileInputStream("Game.dat");	//instantiate file input stream from "Game.dat"
				BufferedInputStream bis = new BufferedInputStream(fis);	//instantiate buffered input stream variable
				ObjectInputStream ois = new ObjectInputStream(bis);	//instantiate object input stream variable
				double resizable = (double)current_window_width/previous_window_width;	//resizing with window sizes
				for(int i = 0; i < numberBricks; i++) {
					double rectX = ois.readDouble() * resizable;	//read rectangle's X position from ois
					double rectY = ois.readDouble() * resizable;	//read rectangle's Y position from ois
					double rectW = ois.readDouble() * resizable;	//read rectangle's width from ois
					double rectH = ois.readDouble() * resizable;	//read rectangle's height from ois
					Rectangle current = new Rectangle(rectX, rectY, rectW, rectH);	//current rectangle variable with previous four double value as parameters
					current.setFill(new ImagePattern(bricksImage));	//fill current rectangle with brick image
					GameScene.getChildren().add(current);	//show bricks onto the GameScene
	                bricks.add(current);	//adding bricks into ArrayList bricks
				}
				ois.close();	//close input stream files
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	} else {	//no need resizing
    		try {
				FileInputStream fis = new FileInputStream("Game.dat");	//instantiate file input stream from "Game.dat"
				BufferedInputStream bis = new BufferedInputStream(fis);	//instantiate buffered input stream variable
				ObjectInputStream ois = new ObjectInputStream(bis);	//instantiate object input stream variable
				for(int i = 0; i < numberBricks; i++) {
					Rectangle current = new Rectangle(ois.readDouble(), ois.readDouble(), ois.readDouble(), ois.readDouble() );	//current rectangle variable with previous four double value as parameters
					current.setFill(new ImagePattern(bricksImage));	//fill current rectangle with brick image
					GameScene.getChildren().add(current);	//show bricks onto the GameScene
	                bricks.add(current);	//adding bricks into ArrayList bricks
				}
				ois.close();	//close input stream files
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }
    
    //check collision of bricks
    public boolean checkCollisionBricks(Rectangle brick){
    	//check if ball collides with a brick
        if(ball.getBoundsInParent().intersects(brick.getBoundsInParent())){
            boolean rightBorder = ball.getLayoutX() >= ((brick.getX() + brick.getWidth()) - ball.getRadius());	//true if right border of the brick is collided
            boolean leftBorder = ball.getLayoutX() <= (brick.getX() + ball.getRadius());	//true if left border of the brick is collided
            boolean bottomBorder = ball.getLayoutY() >= ((brick.getY() + brick.getHeight()) - ball.getRadius());	//true if bottom border of the brick is collided
            boolean topBorder = ball.getLayoutY() <= (brick.getY() + ball.getRadius());	//true if top border of the brick is collided
            
            //check which side border is collided based on previous four booleans
            if (rightBorder || leftBorder) {
                deltaX *= -1;	//change ball's X movement direction
            }
            if (bottomBorder || topBorder) {
                deltaY *= -1;	//change ball's Y movement direction
            }
            current_points++;
            GameScene.getChildren().remove(brick);	//remove the brick if collides with ball
            
            return true;	//return true of collision happens
        }
        return false;	//return false by default
    }

    //movement of the paddle
    public void movePaddle(){
        Bounds bounds = GameScene.localToScreen(GameScene.getBoundsInLocal());	//set the boundary of the paddle move area to the bound of the local application screen
        double sceneXPos = bounds.getMinX();	//minimum paddle move area
        double xPos = robot.getMouseX();	//set paddle moves along with robot, and robot captures movement of the mouse
        double paddleWidth = paddle.getWidth();	//get paddle width
        
        //check if paddle is within local application screen
        if(xPos >= sceneXPos + (paddleWidth / 2) && xPos <= (sceneXPos + GameScene.getWidth()) - (paddleWidth / 2)){	//paddle within range
            paddle.setLayoutX(xPos - sceneXPos - (paddleWidth/2));
        } else if (xPos < sceneXPos + (paddleWidth / 2)){	//paddle move outside of the left screen
            paddle.setLayoutX(0);
        } else if (xPos > (sceneXPos + GameScene.getWidth()) - (paddleWidth / 2)){	//paddle move outside of the right screen
            paddle.setLayoutX(GameScene.getWidth() - paddleWidth);
        }
    }
    
    //check collision method from ICollision interface
	@Override
	public void checkCollision(Rectangle rectangle) {
		//check if ball collides with a border or the paddle
		if(ball.getBoundsInParent().intersects(rectangle.getBoundsInParent())){
			if (rectangle == bottom_border) {	//if ball collides with bottom border, game over
				timeline.stop();	//time line stops
				bricks.forEach(brick -> GameScene.getChildren().remove(brick));	//remove all the bricks from scene
				bricks.clear();	//remove all bricks from ArrayList
				deltaX = 1;	//default ball X direction movement
				deltaY = 2.25;	//default ball Y direction movement
				ball.setLayoutX(current_window_width / 2);
				ball.setLayoutY(current_window_height / 2);
				ball.setVisible(false);	//make ball invisible
				GameOverPoints.setText(String.valueOf(current_points));	//set points
				GameOverVBox.setVisible(true);	//show the fake "menu"
			} else if (rectangle == paddle) {	//else if ball collides with paddle
				//decides which side of the paddle the ball collided
				boolean rightBorder = ball.getLayoutX() >= ((rectangle.getLayoutX() + rectangle.getWidth()) - ball.getRadius());
				boolean leftBorder = ball.getLayoutX() <= (rectangle.getLayoutX() + ball.getRadius());
				boolean bottomBorder = ball.getLayoutY() >= ((rectangle.getLayoutY() + rectangle.getHeight()) - ball.getRadius());
				boolean topBorder = ball.getLayoutY() <= (rectangle.getLayoutY() + ball.getRadius());

				//if right or left border of the paddle
				if (rightBorder || leftBorder) {
					deltaX *= 1;	//changes the ball X movement direction
				}
				if (topBorder) {
					deltaY *= -1;	//changes the ball Y movement direction
				}
				if(bottomBorder) {	//if bottom border of the paddle is touched, game over
					timeline.stop();	//time line stops
					bricks.forEach(brick -> GameScene.getChildren().remove(brick));	//remove all the bricks from scene
					bricks.clear();	//remove all bricks from ArrayList
					//default ball settings
					deltaX = 1;
					deltaY = 2.25;
					ball.setLayoutX(400);
					ball.setLayoutY(400);
					ball.setVisible(false);	//ball invisible
					GameOverPoints.setText(String.valueOf(current_points));	//set points
					GameOverVBox.setVisible(true);	//show the fake "menu"
				}
			} else {	//else top, right and left border is being collided
				//decides which side of the border is collided
				boolean rightBorder = ball.getLayoutX() >= ((rectangle.getLayoutX() + rectangle.getWidth()) - ball.getRadius());
				boolean leftBorder = ball.getLayoutX() <= (rectangle.getLayoutX() + ball.getRadius());
				boolean bottomBorder = ball.getLayoutY() >= ((rectangle.getLayoutY() + rectangle.getHeight()) - ball.getRadius());
				boolean topBorder = ball.getLayoutY() <= (rectangle.getLayoutY() + ball.getRadius());
				
				//reverse the ball X direction movement
				if (rightBorder || leftBorder) {
					deltaX *= -1;
				}
				//reverse the ball Y direction movement
				if (bottomBorder || topBorder) {
					deltaY *= -1;
				}
			}
		}
	}
	
	//start button is being pressed on either GameOver fake "menu"
	public void startGame(ActionEvent event) throws IOException {	//called when the start button is clicked and the game scene will be loaded
		current_points = 0;	//new game starts with 0 points
		setPreviousGame(false);	//no previous game
		setLoadGame(false);	//no loading previous game
		Parent root = FXMLLoader.load(getClass().getResource("GameScene.fxml"));//load the game scene from "GameScene.fxml"
		GUIcontrolls.scene = new Scene(root);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        //check if full screen is being chosen after previous settings
        if (getFullScreen()) {
        	stage.setFullScreen(false);
        	stage.setScene(GUIcontrolls.scene);
        	stage.setFullScreen(true);
		} else {
			stage.setScene(GUIcontrolls.scene);
		}
        stage.show();
	}
	
	//continue button is being pressed on the GamePaused fake "menu"
	public void continueGame(ActionEvent event) {
		GamePausedVBox.setVisible(false);	//hides the fake "menu"
		timeline.play();	//time line resumes
	}
	
	//return button is being pressed on the GamePaused and GameOver fake "menu"
	public void returnToHome(ActionEvent event) throws IOException {	//called when return button is clicked
		Parent root = FXMLLoader.load(getClass().getResource("HomeScene.fxml"));	//load home scene from "HomeScene.fxml"
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		GUIcontrolls.scene = new Scene(root);
		//Auto save on return to home decision
		FileOutputStream fos = new FileOutputStream("Game.dat");
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		//writes every brick's four parameters into "Game.dat"
		for(Rectangle brick : bricks) {
			oos.writeDouble(brick.getX());	//write brick's X position
			oos.writeDouble(brick.getY());	//write brick's Y position
			oos.writeDouble(brick.getWidth());	//write brick's width
			oos.writeDouble(brick.getHeight());	//write brick's height
			numberBricks++;	//number of bricks being saved
		}
		oos.close();	//close Output Stream files
		setPreviousGame(true);
		//retains full screen if full screen radio button is selected
		if (getFullScreen()) {
			stage.setFullScreen(false);
			stage.setScene(GUIcontrolls.scene);
			stage.setFullScreen(true);
		} else {
			stage.setScene(GUIcontrolls.scene);
		}
		stage.show();
	}

	/**
	 * @param current_window_width the current_window_width to set
	 */
	public static void setCurrent_window_width(int current_window_width) {
		GamePlay.current_window_width = current_window_width;
	}
	
	/**
	 * @return the current_window_width
	 */
	public static int getCurrent_window_width() {
		return current_window_width;
	}

	/**
	 * @param current_window_height the current_window_height to set
	 */
	public static void setCurrent_window_height(int current_window_height) {
		GamePlay.current_window_height = current_window_height;
	}
	
	/**
	 * @return the current_window_height
	 */
	public static int getCurrent_window_height() {
		return current_window_height;
	}

	/**
	 * @param previous_window_width the previous_window_width to set
	 */
	public static void setPrevious_window_width(int previous_window_width) {
		GamePlay.previous_window_width = previous_window_width;
	}

	/**
	 * @param previous_window_height the previous_window_height to set
	 */
	public static void setPrevious_window_height(int previous_window_height) {
		GamePlay.previous_window_height = previous_window_height;
	}

	/**
	 * @param loadGame the loadGame to set
	 */
	public static void setLoadGame(boolean loadGame) {
		GamePlay.loadGame = loadGame;
	}

	/**
	 * @param previousGame the previousGame to set
	 */
	public static void setPreviousGame(boolean previousGame) {
		GamePlay.previousGame = previousGame;
	}
	
	/**
	 * @return the previousGame
	 */
	public static boolean getPreviousGame() {
		return GamePlay.previousGame;
	}

	/**
	 * @param brickResize the brickResize to set
	 */
	public static void setBrickResize(boolean brickResize) {
		GamePlay.brickResize = brickResize;
	}

	/**
	 * @return the fullScreen
	 */
	public static boolean getFullScreen() {
		return GamePlay.fullScreen;
	}

	/**
	 * @param fullScreen the fullScreen to set
	 */
	public static void setFullScreen(boolean fullScreen) {
		GamePlay.fullScreen = fullScreen;
	}

	/**
	 * @return the defaultWidth
	 */
	public static int getDefaultWidth() {
		return DEFAULT_WIDTH;
	}

	/**
	 * @return the defaultHeight
	 */
	public static int getDefaultHeight() {
		return DEFAULT_HEIGHT;
	}

	/**
	 * @return the thirteen
	 */
	public static int getThirteen() {
		return thirteen;
	}

	/**
	 * @return the eleven
	 */
	public static int getEleven() {
		return eleven;
	}

	/**
	 * @return the eight
	 */
	public static int getEight() {
		return eight;
	}

	/**
	 * @return the nine
	 */
	public static int getNine() {
		return nine;
	}

	/**
	 * @return the six
	 */
	public static int getSix() {
		return six;
	}
	
}
